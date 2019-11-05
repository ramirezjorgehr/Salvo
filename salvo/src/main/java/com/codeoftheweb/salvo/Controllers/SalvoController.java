package com.codeoftheweb.salvo.Controllers;

import com.codeoftheweb.salvo.Models.*;
import com.codeoftheweb.salvo.Repositories.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
public class SalvoController {

    @Autowired
    private GameRepository repoGames;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;
    @Autowired
    private ScoreRepository scoreRepository;


    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    public Map<String, Object> makeMapDto(long idPlayerRepository) {

        GamePlayer gamePlayer = gamePlayerRepository.findById(idPlayerRepository).orElse(null);
        GamePlayer self = gamePlayer;

        GamePlayer opponent = gamePlayer.getGame().getGamePlayers().stream().filter(gamePlayer1 -> gamePlayer1.getId() != idPlayerRepository).findFirst().orElse(null);

        Map<String, Object> hits = new LinkedHashMap<>();
        if (opponent == null) {
            hits.put("self", new ArrayList<>());
            hits.put("opponent", new ArrayList<>());
        } else {
            hits.put("self", opponent.getSalvoes().stream().sorted((o1, o2) -> o1.getTurn()-o2.getTurn()).map(salvo -> salvo.makeSelfOpponent(self, opponent)));
            hits.put("opponent", self.getSalvoes().stream().sorted((o1, o2) -> o1.getTurn()-o2.getTurn()).map(salvo -> salvo.makeSelfOpponent(opponent, self)));
        }

        Map<String, Object> dto = new LinkedHashMap<>();

        dto.put("id", gamePlayer.getGame().getId());
        dto.put("created", gamePlayer.getGame().getCreationDate());
        dto.put("gameState", this.gameState(self, opponent));
        dto.put("gamePlayers", gamePlayer.getGame().getGamePlayers().stream().map(gamePlayer1 -> gamePlayer1.makeGamePlayerDTO()).collect(Collectors.toList()));
        dto.put("ships", gamePlayer.getShips().stream().map(gamePlayer1 -> gamePlayer1.makeShipDTO()).collect(Collectors.toList()));
        dto.put("salvoes", gamePlayer.getGame().getGamePlayers().stream().map(gamePlayer1 -> gamePlayer1.getSalvoes()).flatMap(salvos -> salvos.stream().map(salvo1 -> salvo1.makeSalvoDTO())));
        dto.put("hits", hits);

        return dto;
    }


    @RequestMapping("/games")
    public Map<String, Object> getIdGames(Authentication authentication) {

        Map<String, Object> dto = new LinkedHashMap<>();
        if (!isGuest(authentication)) {
            dto.put("player", playerRepository.findByUserName(authentication.getName()).makePlayerDTO());
        } else
            dto.put("player", "Guest");

        dto.put("games", repoGames.findAll().stream().map(game -> game.makeGameDTO(game)).collect(Collectors.toList()));

        return dto;
    }

    @RequestMapping(path = "/game_view/{idn}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> createUser(Authentication authentication, @PathVariable long idn) {

        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("Error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }
        GamePlayer gamePlayer = gamePlayerRepository.findById(idn).get();
        Player player = playerRepository.findByUserName(authentication.getName());


        if (player == null) {
            return new ResponseEntity<>(makeMap("Error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer == null) {
            return new ResponseEntity<>(makeMap("Error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getPlayer().equals(player)) {

            return new ResponseEntity<>(makeMapDto(idn), HttpStatus.OK);
        }
        return new ResponseEntity<>(makeMap("Error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
    }

    public String gameState(GamePlayer self, GamePlayer opponent) {
        String gameState = "";
        if (opponent == null) {
            if (self.getShips().size() == 0) {
                gameState = "PLACESHIPS";
            } else {
                gameState = "WAITINGFOROPP";
            }
        } else if (self.getShips().size() == 0 || opponent.getShips().size() == 0) {
            gameState = "PLACESHIPS";
        } else if ((self.getShips().size() == 5 && opponent.getShips().size() < 5) || (self.getShips().size() < 5 && opponent.getShips().size() == 5) || opponent == null) {
            gameState = "WAITINGFOROPP";
        } else if (opponent.getSalvoes().size() > self.getSalvoes().size()) {
            gameState = "PLAY";
        } else if ((self.getSalvoes().size()) > (opponent.getSalvoes().size())) {
            gameState = "WAIT";

        } else if (opponent.getSalvoes().size() == self.getSalvoes().size()) {
            if (this.winGame(self) && !(this.winGame(opponent))) {
                gameState = "LOSE";
                if (self.getGame().getScores().size() == 0) {
                    scoreRepository.save(new Score(self.getGame(), self.getPlayer(), 0.0, LocalDateTime.now()));
                    scoreRepository.save(new Score(opponent.getGame(), opponent.getPlayer(), 1.0, LocalDateTime.now()));
                }

            } else if (this.winGame(opponent) && !(this.winGame(self))) {
                gameState = "WON";
                if (self.getGame().getScores().size() == 0) {
                    scoreRepository.save(new Score(self.getGame(), self.getPlayer(), 1.0, LocalDateTime.now()));
                    scoreRepository.save(new Score(opponent.getGame(), opponent.getPlayer(), 0.0, LocalDateTime.now()));
                }
            } else if (this.winGame(self) && this.winGame(opponent)) {
                gameState = "TIE";
                if (self.getGame().getScores().size() == 0) {
                    scoreRepository.save(new Score(self.getGame(), self.getPlayer(), 0.5, LocalDateTime.now()));
                    scoreRepository.save(new Score(opponent.getGame(), opponent.getPlayer(), 0.5, LocalDateTime.now()));
                }

            } else {
                gameState = "PLAY";
            }
        }
        return gameState;
    }

    public boolean winGame(GamePlayer self) {
        GamePlayer opponent = self.getGame().getGamePlayers().stream().filter(gamePlayer -> gamePlayer != self).findFirst().orElse(null);
        boolean sunkenShips = false;
        int hits = 0;
        List<Ship> ships = self.getShips().stream().collect(Collectors.toList());
        List<Salvo> salvos = opponent.getSalvoes().stream().collect(Collectors.toList());
        List<String> allsalvos = new ArrayList<>();
        List<String> allShips = new ArrayList<>();

        for (int i = 0; i < ships.size(); i++) {
            for (int j = 0; j < ships.get(i).getLocations().size(); j++) {
                allShips.add(ships.get(i).getLocations().get(j));
            }
        }

        for (int i = 0; i < salvos.size(); i++) {
            for (int j = 0; j < salvos.get(i).getSalvoLocations().size(); j++) {
                allsalvos.add(salvos.get(i).getSalvoLocations().get(j));
            }
        }
        for (int i = 0; i < ships.size(); i++) {
            for (int j = 0; j < allsalvos.size(); j++) {
                if (ships.get(i).getLocations().contains(allsalvos.get(j))) {
                    hits++;
                }
            }
        }
        if (hits == allShips.size()) {

            sunkenShips = true;
        }
        return sunkenShips;
    }

}

