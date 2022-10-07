package Game;

import java.util.HashMap;

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
        players.put(uuid, player);
    }

    protected void addNewPlayer(String uuid) {
        players.put(uuid, new Player());
    }

    protected boolean playerExists(String uuid) {
        return players.containsKey(uuid);
    }
}
