package PrideBot.Game;

import PrideBot.SpellBook.Spell;
import PrideBot.SpellBook.SpellBookModule;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
            loadPlayers(root);
        }
    }

    private void loadPlayers(JsonNode root) {
        JsonNode playersNode = root.get("players");
        Player player;
        JsonNode playerNode;
        String uuid;
        long lastCollectionTime;
        PlayerStats stats;
        for (int i = 0; i < playersNode.size(); i++) {
            playerNode = playersNode.get(i);
            uuid = playerNode.get("uuid").asText();
            lastCollectionTime = playerNode.get("last_collection_time").asLong();
            stats = loadPlayerStats(playerNode);
            SpellBookModule spellBookModule = loadSpellBookModule(playerNode);
            if (spellBookModule != null) {
                player = new Player(uuid, stats, lastCollectionTime, spellBookModule);
            }
            else {
                player = new Player(uuid, stats, lastCollectionTime);
            }
            gameModel.addPlayer(uuid, player);
        }
    }

    private PlayerStats loadPlayerStats(JsonNode playerNode) {
        JsonNode statsNode = playerNode.get("stats");
        long pride = statsNode.get("pride").asLong();
        long shame = statsNode.get("shame").asLong();
        long ego = statsNode.get("ego").asLong();
        long guilt = statsNode.get("guilt").asLong();
        long honor = statsNode.get("honor").asLong();
        long dishonor = statsNode.get("dishonor").asLong();
        return new PlayerStats(pride, ego, honor, shame, guilt, dishonor);
    }

    private SpellBookModule loadSpellBookModule(JsonNode playerNode) {
        JsonNode spellBookNode = playerNode.get("spell_book");
        if (spellBookNode == null) {
            return null;
        }
        JsonNode spellsNode = spellBookNode.get("spells");
        ArrayList<Spell> spells = new ArrayList<>();
        JsonNode spellNode;
        String spellName;
        int spellLevel;
        for (int j = 0; j < spellsNode.size(); j++) {
            spellNode = spellsNode.get(j);
            spellName = spellNode.get("name").asText();
            spellLevel = spellNode.get("level").asInt();
            spells.add(SpellBookModule.generateSpell(spellName, spellLevel));
        }
        JsonNode currentSpellEffectNode = spellBookNode.get("current_spell_effect");
        double collectionCritChance = currentSpellEffectNode.get("collection_crit_chance").asDouble();
        boolean prideFavored = currentSpellEffectNode.get("pride_favored").asBoolean();
        boolean  shameFavored = currentSpellEffectNode.get("shame_favored").asBoolean();
        long bountifulness = currentSpellEffectNode.get("bountifulness").asLong();
        return new SpellBookModule(spells, collectionCritChance, prideFavored, shameFavored, bountifulness);
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
