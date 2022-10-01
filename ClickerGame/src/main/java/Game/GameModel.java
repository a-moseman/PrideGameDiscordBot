package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

class GameModel {
    private HashMap<String, Player> players;

    public GameModel() {
        this.players = new HashMap<>();
    }

    protected Player getPlayer(String uuid) {
        return players.get(uuid);
    }

    protected void addPlayer(String uuid, Player player) {
        players.put(uuid, player);
    }

    public HashMap<String, Player> getPlayers() {
        return players;
    }

    public boolean playerExists(String uuid) {
        return players.containsKey(uuid);
    }

    public List<Player> getRankedPlayers() {
        List<Player> rankedPlayers = new ArrayList<>(players.values());
        Collections.sort(rankedPlayers);
        return rankedPlayers;
    }
}
