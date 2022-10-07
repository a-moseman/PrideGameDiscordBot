package Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

    protected HashMap<String, Player> getPlayers() {
        return players;
    }

    protected boolean playerExists(String uuid) {
        return players.containsKey(uuid);
    }

    protected List<Player> getRankedPlayers() {
        List<Player> rankedPlayers = new ArrayList<>(players.values());
        Collections.sort(rankedPlayers);
        return rankedPlayers;
    }

    protected long simulateCombat(String attackerUUID, String defenderUUID) {
        Player attacker = players.get(attackerUUID);
        Player defender = players.get(defenderUUID);
        long result = CombatSimulator.simulate(attacker, defender);
        long prideToExchange = Math.min(defender.getPride(), Math.abs(result));
        attacker.addPride(prideToExchange);
        defender.addPride(-prideToExchange);
        return result;
    }
}
