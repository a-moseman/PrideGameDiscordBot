package PrideBot.Game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Finished for version 1.
 */
public class GameModel {
    private HashMap<String, Player> players;

    protected GameModel() {
        this.players = new HashMap<>();
    }

    protected Player getPlayer(String uuid) {
        return players.get(uuid);
    }

    protected void addPlayer(String uuid, Player player) {
        if (players.containsKey(uuid)) { // DO NOT REMOVE: THIS PROTECTS PLAYER DATA FROM BEING OVERWRITTEN IN CASE OF BUG
            return;
        }
        players.put(uuid, player);
    }

    protected void addNewPlayer(String uuid) {
        if (players.containsKey(uuid)) { // DO NOT REMOVE: THIS PROTECTS PLAYER DATA FROM BEING OVERWRITTEN IN CASE OF BUG
            return;
        }
        players.put(uuid, new Player(uuid));
    }

    protected boolean playerExists(String uuid) {
        return players.containsKey(uuid);
    }

    protected List<Player> getPlayers() {
        return new ArrayList<>(players.values());
    }

    protected List<Player> getRankedPlayers() {
        List<Player> players = getPlayers();
        Collections.sort(players);
        return players;
    }

    protected List<ObjectNode> buildPlayerJsonNode(ObjectMapper mapper) {
        List<ObjectNode> playersList = new ArrayList<>();
        for (Player player : players.values()) {
            playersList.add(player.buildJsonNode(mapper));
        }
        return playersList;
    }

    protected void setName(String uuid, String name) {
        if (!players.containsKey(uuid)) {
            System.out.println(name + " NOT IN GAME"); // DEBUG
            return;
        }
        getPlayer(uuid).setName(name);
    }
}
