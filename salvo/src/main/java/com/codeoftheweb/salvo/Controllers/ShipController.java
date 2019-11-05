package com.codeoftheweb.salvo.Controllers;

import com.codeoftheweb.salvo.Models.GamePlayer;
import com.codeoftheweb.salvo.Models.Player;
import com.codeoftheweb.salvo.Models.Ship;
import com.codeoftheweb.salvo.Repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.Repositories.GameRepository;
import com.codeoftheweb.salvo.Repositories.PlayerRepository;
import com.codeoftheweb.salvo.Repositories.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class ShipController {

    @Autowired
    private GameRepository repoGames;
    @Autowired
    private GamePlayerRepository gamePlayerRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private ShipRepository shipRepository;


    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/ships", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addShips(Authentication authentication, @PathVariable long gamePlayerId, @RequestBody List<Ship> listShip) {

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        Player player = playerRepository.findByUserName(authentication.getName());
        if (isGuest(authentication) || gamePlayer == null || !(gamePlayer.getPlayer().equals(player))) {
            return new ResponseEntity<>(makeMap("Error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }
        if (gamePlayer.getShips().size() != 0) {
            return new ResponseEntity<>(makeMap("error", "Ya tiene barcos"), HttpStatus.FORBIDDEN);
        }

        listShip.forEach(ship2 -> ship2.setGamePlayer(gamePlayer));
        listShip.forEach(ship2 -> shipRepository.save(ship2));

        return new ResponseEntity<>(makeMap("OK", "barcos agregadoss"), (HttpStatus.CREATED));

    }

}
