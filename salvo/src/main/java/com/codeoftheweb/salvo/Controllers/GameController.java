package com.codeoftheweb.salvo.Controllers;

import com.codeoftheweb.salvo.Models.Game;
import com.codeoftheweb.salvo.Models.GamePlayer;
import com.codeoftheweb.salvo.Models.Player;
import com.codeoftheweb.salvo.Repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.Repositories.GameRepository;
import com.codeoftheweb.salvo.Repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class GameController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    private Date date = new Date();

    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Object> addGame(
            Authentication authentication) {
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No estas auutorizado"), HttpStatus.UNAUTHORIZED);
        }
        Player player = playerRepository.findByUserName(authentication.getName());
        if (player == null) {
            return new ResponseEntity<>(makeMap("error", "Missing Data"), HttpStatus.FORBIDDEN);
        }
        Game game = gameRepository.save(new Game());
        GamePlayer gamePlayer = gamePlayerRepository.save(new GamePlayer(date, game, player));
        return new ResponseEntity<>(makeMap("gpid", gamePlayer.getId()), HttpStatus.CREATED);
    }

    @RequestMapping(path = "/game/{gameId}/players", method = RequestMethod.POST)
    public ResponseEntity<Object> joinGame(Authentication authentication, @PathVariable long gameId) {
        Player playerLogin = playerRepository.findByUserName(authentication.getName());
        Game game = gameRepository.findById(gameId).get();
        if (isGuest(authentication)) {
            return new ResponseEntity<>(makeMap("error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if (game == null) {
            return new ResponseEntity<>(makeMap("error", "No existe el juego"), HttpStatus.FORBIDDEN);
        }
        if (game.getGamePlayers().size() > 1) {
            return new ResponseEntity<>(makeMap("error", "El juego esta lleno"), HttpStatus.FORBIDDEN);
        }

        GamePlayer gamePlayer1 = gamePlayerRepository.save(new GamePlayer(date, game, playerLogin));
        return new ResponseEntity<>(makeMap("gpid", gamePlayer1.getId()), HttpStatus.CREATED);

    }

}
