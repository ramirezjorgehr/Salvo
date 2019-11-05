package com.codeoftheweb.salvo.Models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Salvo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native") //ANOTACION QUE GENERA VALOR AUTOMATICO AL ID
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ElementCollection
    @Column(name = "locations")
    private List<String> salvoLocations = new ArrayList<>();

    private int turn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "gamePlayer_id")
    private GamePlayer gamePlayer;

    public Salvo() {
    }

    public Salvo(GamePlayer gamePlayer, List<String> locations, int turn) {
        this.gamePlayer = gamePlayer;
        this.salvoLocations = locations;
        this.turn = turn;
    }

    public Long getId() {
        return id;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public int getTurn() {
        return turn;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public void setSalvoLocations(List<String> locations) {
        this.salvoLocations = locations;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }


    public Map<String, Object> makeSalvoDTO() {
        Map<String, Object> dto = new LinkedHashMap<>();
        dto.put("turn", this.turn);
        dto.put("player", gamePlayer.getPlayer().getId());
        dto.put("locations", this.getSalvoLocations().stream().collect(Collectors.toList()));
        return dto;
    }


    public Map<String, Object> makeSelfOpponent(GamePlayer self, GamePlayer opponent) {

        Salvo salvo = opponent.getSalvoes().stream().filter(salvo1 -> salvo1.getTurn() == this.getTurn()).findFirst().orElse(null);//aa
        List<Ship> ships = self.getShips().stream().collect(Collectors.toList());

        List<String> hitLocations = new ArrayList<>();

        int missed = 0;
        int carrierHits = 0;
        int battleshipHits = 0;
        int submarineHits = 0;
        int patrolboatHits = 0;
        int destroyerHits = 0;

        int carrier = 0;
        int battleship = 0;
        int submarine = 0;
        int patrolboat = 0;
        int destroyer = 0;

        if (salvo != null && ships != null) {

            for (int j = 0; j < salvo.getSalvoLocations().size(); j++) {
                for (int i = 0; i < ships.size(); i++) {

                    if (ships.get(i).getLocations().contains(salvo.getSalvoLocations().get(j))) {

                        hitLocations.add(salvo.getSalvoLocations().get(j));


                        switch (ships.get(i).getType().toLowerCase()) {

                            case "carrier":
                                carrierHits++;
                                carrier = this.hitsAcumulator(self, opponent, ships.get(i));
                                break;
                            case "battleship":
                                battleshipHits++;
                                battleship = salvo.hitsAcumulator(self, opponent, ships.get(i));
                                ;
                                break;
                            case "submarine":
                                submarineHits++;
                                submarine = salvo.hitsAcumulator(self, opponent, ships.get(i));
                                break;
                            case "patrolboat":
                                patrolboatHits++;
                                patrolboat = salvo.hitsAcumulator(self, opponent, ships.get(i));
                                break;
                            case "destroyer":
                                destroyerHits++;
                                destroyer = salvo.hitsAcumulator(self, opponent, ships.get(i));
                                break;
                        }
                    }
                }
            }

            if (hitLocations.size() <= salvo.getSalvoLocations().size()) {

                missed = salvo.getSalvoLocations().size() - hitLocations.size();

            }
        }

        Map<String, Object> damages = new LinkedHashMap<>();

        damages.put("carrierHits", carrierHits);
        damages.put("battleshipHits", battleshipHits);
        damages.put("submarineHits", submarineHits);
        damages.put("destroyerHits", destroyerHits);
        damages.put("patrolboatHits", patrolboatHits);

        damages.put("carrier", carrier);
        damages.put("battleship", battleship);
        damages.put("submarine", submarine);
        damages.put("destroyer", destroyer);
        damages.put("patrolboat", patrolboat);

        Map<String, Object> hits = new LinkedHashMap<>();

        hits.put("turn", this.getTurn());
        hits.put("hitLocations", hitLocations);
        hits.put("damages", damages);
        hits.put("missed", missed);


        return hits;


    }

    public int hitsAcumulator(GamePlayer self, GamePlayer opponent, Ship ship) {
        int acumulator = 0;
        String type = ship.getType();

        List<Salvo> salvos = opponent.getSalvoes().stream().filter(salvo1 -> salvo1.getTurn() <= this.getTurn()).collect(Collectors.toList());
        List<Ship> ships = self.getShips().stream().collect(Collectors.toList());
        List<String> allsalvos = new ArrayList<>();

        for (int i = 0; i < salvos.size(); i++) {
            for (int j = 0; j < salvos.get(i).getSalvoLocations().size(); j++) {
                allsalvos.add(salvos.get(i).getSalvoLocations().get(j));
            }
        }

        for (int i = 0; i < ships.size(); i++) {
            for (int j = 0; j < allsalvos.size(); j++) {
                if (ships.get(i).getType().equals(type) && ships.get(i).getLocations().contains(allsalvos.get(j))) {
                    acumulator++;

                }
            }
        }
        return acumulator;
    }
}
