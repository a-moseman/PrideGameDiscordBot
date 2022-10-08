package Game;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;

public class PersistenceManager {
    private final String PATH;
    private GameModel gameModel;

    protected PersistenceManager(String path, GameModel gameModel) {
        this.PATH = path;
        this.gameModel = gameModel;
    }

    protected void load() {
        File file = new File(PATH);
        if (file.exists()) {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root;
            try {
                root = mapper.readValue(file, JsonNode.class);
            }
            catch (Exception e) {
                e.printStackTrace();
                return;
            }
            JsonNode playersNode = root.get("players");
            Player player;
            JsonNode playerNode;
            String uuid;
            long lastCollectionTime;
            JsonNode statsNode;
            long pride;
            long shame;
            long ego;
            long guilt;
            long honor;
            long dishonor;
            PlayerStats stats;
            for (int i = 0; i < playersNode.size(); i++) {
                playerNode = playersNode.get(i);
                uuid = playerNode.get("uuid").asText();
                lastCollectionTime = playerNode.get("last_collection_time").asLong();
                statsNode = playerNode.get("stats");
                pride = statsNode.get("pride").asLong();
                shame = statsNode.get("shame").asLong();
                ego = statsNode.get("ego").asLong();
                guilt = statsNode.get("guilt").asLong();
                honor = statsNode.get("honor").asLong();
                dishonor = statsNode.get("dishonor").asLong();
                stats = new PlayerStats(pride, ego, honor, shame, guilt, dishonor);
                player = new Player(uuid, stats, lastCollectionTime);
                gameModel.addPlayer(uuid, player);
            }
        }
    }

    protected void save() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode root = mapper.createObjectNode();
        root.set("players", mapper.convertValue(gameModel.buildPlayerJsonNode(mapper), JsonNode.class));
        try {
            mapper.writeValue(new File(PATH), root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
