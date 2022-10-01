package Game;

import Game.Combat.CombatSimulator;
import Game.Combat.Dice;
import Game.Combat.ItemMerchant;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameInterface {
    private GameModel gameModel;

    public GameInterface() {
        this.gameModel = new GameModel();
    }

    public void load(String path) {
        // TODO: implement
        // load game data, etc.
        if (!(new File(path).exists())) {
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            gameModel = mapper.readValue(Paths.get(path).toFile(), GameModel.class);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save(String path) {
        try {
            File file = new File(path);
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(file, gameModel);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String simulateCombat(String attackerUUID, String defenderUUID) {
        long result = CombatSimulator.simulate(gameModel.getPlayer(attackerUUID), gameModel.getPlayer(defenderUUID));
        long prideToExchange = Math.min(getPride(defenderUUID), Math.abs(result));
        addPride(defenderUUID, -prideToExchange);
        addPride(attackerUUID, prideToExchange);
        return (result > 0 ? "Attacker" : "Defender") + "wins!\n" + prideToExchange + " pride was exchanged.";
    }

    public String searchForArtifact(String uuid) {
        addPride(uuid, -1);
        Artifact artifact = Artifact.searchForArtifact();
        if (artifact == null) {
            return "You did not find any artifacts.\nYou have lost 1 pride.";
        }
        addArtifact(uuid, artifact);
        return "You have found the artifact: " + artifact + ".\nYou have lost 1 pride.";
    }

    public String getPlayerArtifacts(String uuid) {
        List<Artifact> sortedArtifacts = gameModel.getPlayer(uuid).getSortedArtifacts();
        if (sortedArtifacts.size() == 0) {
            return "You have no artifacts.";
        }
        StringBuilder sb = new StringBuilder();
        int duplicates = 0;
        Artifact currentArtifact = sortedArtifacts.get(0);
        Artifact artifact;
        for (int i = 1; i < sortedArtifacts.size(); i++) {
            artifact = sortedArtifacts.get(i);
            if (currentArtifact.equals(artifact)) {
                duplicates++;
            }
            else {
                sb.append(currentArtifact).append("x").append(duplicates+1).append('\n');
                currentArtifact = artifact;
            }
        }
        sb.append(currentArtifact).append("x").append(duplicates+1).append('\n');
        return sb.toString();
    }

    public String buyDie(String uuid, String die, String type) {
        long cost = 0;
        Dice dice = null;
        switch (die.toUpperCase(Locale.ROOT)) {
            case "D2":
                if (getPride(uuid) >= ItemMerchant.D2_PRIDE_COST) {
                    cost = ItemMerchant.D2_PRIDE_COST;
                    dice = Dice.D2;
                }
                break;
            case "D4":
                if (getPride(uuid) >= ItemMerchant.D4_PRIDE_COST) {
                    cost = ItemMerchant.D4_PRIDE_COST;
                    dice = Dice.D4;
                }
                break;
            case "D6":
                if (getPride(uuid) >= ItemMerchant.D6_PRIDE_COST) {
                    cost = ItemMerchant.D6_PRIDE_COST;
                    dice = Dice.D6;
                }
                break;
            case "D8":
                if (getPride(uuid) >= ItemMerchant.D8_PRIDE_COST) {
                    cost = ItemMerchant.D8_PRIDE_COST;
                    dice = Dice.D8;
                }
                break;
            case "D10":
                if (getPride(uuid) >= ItemMerchant.D10_PRIDE_COST) {
                    cost = ItemMerchant.D10_PRIDE_COST;
                    dice = Dice.D10;
                }
                break;
            case "D12":
                if (getPride(uuid) >= ItemMerchant.D12_PRIDE_COST) {
                    cost = ItemMerchant.D12_PRIDE_COST;
                    dice = Dice.D12;
                }
                break;
            case "D20":
                if (getPride(uuid) >= ItemMerchant.D20_PRIDE_COST) {
                    cost = ItemMerchant.D20_PRIDE_COST;
                    dice = Dice.D20;
                }
                break;
        }
        if (dice == null) {
            return "Not enough pride.";
        }
        switch (type.toUpperCase(Locale.ROOT)) {
            case "ATTACK":
                gameModel.getPlayer(uuid).getCombatStats().addAttackDice(dice);
                break;
            case "DEFENSE":
                gameModel.getPlayer(uuid).getCombatStats().addDefenseDice(dice);
                break;
            default:
                return "Invalid type of die.";
        }
        addPride(uuid, -cost);
        return "You bought a " + die.toUpperCase(Locale.ROOT) + " for " + cost + " pride.";
    }

    public String levelUpPrestige(String uuid) {
        if (gameModel.getPlayer(uuid).isMaxPrestige()) {
            return "You are at max prestige.";
        }
        if (gameModel.getPlayer(uuid).levelUpPrestige()) {
            return "You have leveled up your prestige.";
        }
        return "You do not have enough ego to prestige.";
    }

    private void addAttackDie(String uuid, Dice dice) {
        gameModel.getPlayer(uuid).getCombatStats().addAttackDice(dice);
    }

    private void addDefenseDie(String uuid, Dice dice) {
        gameModel.getPlayer(uuid).getCombatStats().addDefenseDice(dice);
    }

    public void addNewPlayer(String uuid) {
        gameModel.addPlayer(uuid, new Player(System.currentTimeMillis(), Parameters.PLAYER_STARTING_PRIDE, Parameters.PLAYER_STARTING_EGO, Parameters.PLAYER_STARTING_PRESTIGE, new ArrayList<>(), null));
    }

    public boolean playerExists(String uuid) {
        return gameModel.playerExists(uuid);
    }

    public void addPride(String uuid, long pride) {
        gameModel.getPlayer(uuid).addPride(pride);
    }

    public boolean collectPride(String uuid) {
        return gameModel.getPlayer(uuid).collectPride();
    }

    public boolean levelUpEgo(String uuid, int levels) {
        return gameModel.getPlayer(uuid).levelUpEgo(levels);
    }

    public long getPride(String uuid) {
        return gameModel.getPlayer(uuid).getPride();
    }

    public long getEgo(String uuid) {
        return gameModel.getPlayer(uuid).getEgo();
    }

    public int getPrestige(String uuid) {
        return gameModel.getPlayer(uuid).getPrestige();
    }

    public void addArtifact(String uuid, Artifact artifact) {
        gameModel.getPlayer(uuid).addArtifact(artifact);
    }

    public boolean hasCombatModule(String uuid) {
        return gameModel.getPlayer(uuid).hasCombatModule();
    }

    public long prideForNextEgo(String uuid) {
        return Conversions.egoToPride(getEgo(uuid) + 1);
    }

    public long egoForNextPrestige(String uuid) {
        return Conversions.prestigeToEgo(getPrestige(uuid) + 1);
    }
}
