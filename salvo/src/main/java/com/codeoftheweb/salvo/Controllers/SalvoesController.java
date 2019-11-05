package com.codeoftheweb.salvo.Controllers;

import com.codeoftheweb.salvo.Models.GamePlayer;
import com.codeoftheweb.salvo.Models.Player;
import com.codeoftheweb.salvo.Models.Salvo;
import com.codeoftheweb.salvo.Repositories.GamePlayerRepository;
import com.codeoftheweb.salvo.Repositories.PlayerRepository;
import com.codeoftheweb.salvo.Repositories.SalvoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequestMapping("/api")
@RestController
public class SalvoesController {

    @Autowired
    GamePlayerRepository gamePlayerRepository;

    @Autowired
    PlayerRepository playerRepository;

    @Autowired
    SalvoRepository salvoRepository;


    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

    @RequestMapping(path = "/games/players/{gamePlayerId}/salvoes", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> addSalvos(Authentication authentication, @PathVariable long gamePlayerId, @RequestBody Salvo salvo) {

        GamePlayer gamePlayer = gamePlayerRepository.findById(gamePlayerId).get();
        Player player = playerRepository.findByUserName(authentication.getName());

        if (isGuest(authentication) || gamePlayer == null || !(gamePlayer.getPlayer().equals(player))) {
            return new ResponseEntity<>(makeMap("Error", "No estas autorizado"), HttpStatus.UNAUTHORIZED);
        }

        salvo.setTurn(gamePlayer.getSalvoes().size() + 1);
        salvo.setGamePlayer(gamePlayer);
        salvoRepository.save(salvo);
        return new ResponseEntity<>(makeMap("OK", "salvos agregados"), (HttpStatus.CREATED));

    }

}
