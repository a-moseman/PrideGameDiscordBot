package Bot;

import Game.GameInterface;

public class BotModel {
    private static final Response ERR_NOT_ADMIN = new Response("ERROR", "Not admin.");
    private static final Response ERR_INVALID_COMMAND = new Response("ERROR",  "Invalid command.");
    private static final Response ERR_TOO_MANY_ARGS = new Response("ERROR",  "Too many arguments.");
    private static final Response ERR_MISSING_ARGS = new Response("ERROR", "Missing arguments.");
    private static final Response ERR_INVALID_ARG = new Response("ERROR",  "Invalid argument.");
    private static final Response ERR_PLAYER_DNE = new Response("ERROR",  "Player does not exist.");
    private static final Response ERR_INSUFFICIENT_PRIDE = new Response("ERROR", "Insufficient pride.");
    private static final Response RESPONSE_BLESSING = new Response("MESSAGE", "A blessing has occurred.");
    private static final Response RESPONSE_EGO_LEVEL_UP = new Response("MESSAGE", "Your ego has leveled up.");

    private static final String COMMANDS_MESSAGE_BLESS = "p>bless <target> <amount> - Grants the target player the given amount of pride. The target argument must be a mention and the amount argument must an integer. Only users with the PrideAdminRole can use this.";
    private static final String COMMANDS_MESSAGE_COLLECT = "p>collect - Grants you your daily pride.";
    private static final String COMMANDS_MESSAGE_LEVELUP = "p>levelup <levels> - Levels up your ego at the cost of some pride. The optional levels argument is the amount of times you want to level up your ego.";
    private static final String COMMANDS_MESSAGE_PRESTIGE = "p>prestige - Levels up your prestige at the cost of some ego.";
    private static final String COMMANDS_MESSAGE_SEARCH = "p>search - Pay 1 pride to search for an artifact.";
    private static final String COMMANDS_MESSAGE_STATS = "p>stats <user> - Provides your personal stats. With the optional user argument, a mention, you get the user's stats.";
    private static final String COMMANDS_MESSAGE_ARTIFACTS = "p>artifacts <user> - Provides a list of your artifacts. With the optional user argument, a mention, you get a list of the user's artifacts.";
    private static final String COMMANDS_MESSAGE_INFO = "p>info - Provides information on how aspects of the game work.";
    private static final String COMMANDS_MESSAGE_COMMANDS = "p>commands - Provides the list of commands.";
    private static final String COMMANDS_MESSAGE_ATTACK = "p>attack <user> - Initiates combat between you, the attacker, and another user, the defender and provides the result.";
    private static final String COMMANDS_MESSAGE_BUY = "p>buy <die> <type> - Buy a die at the cost of some pride. The die argument should be formatted as Dn (e.g. D2, D4, etc.). Only standard dice are implemented. The type argument should be ATTACK or DEFENSE for the corresponding type of dice. This commands arguments are not case-sensitive.";
    private GameInterface gameInterface;
    private String savePath;

    public BotModel() {
        this.gameInterface = new GameInterface();
    }

    public void initialize(String path) {
        savePath = path;
        gameInterface.load(path);
    }

    public void save() {
        gameInterface.save(savePath);
    }

    public boolean playerExists(String uuid) {
        return gameInterface.playerExists(uuid);
    }

    public void addNewPlayer(String uuid) {
        save(); // save on successful adding of new player
        gameInterface.addNewPlayer(uuid);
    }

    public Response process(Command command, boolean isAdmin) {
        // TODO: implement
        switch (command.getTerm(0)) {
            case "bless":
                if (!isAdmin) {
                    return ERR_NOT_ADMIN;
                }
                return bless(command);
            case "collect":
                return collect(command);
            case "levelup":
                return levelup(command);
            case "search":
                return search(command);
            case "stats":
                return stats(command);
            case "artifacts":
                return artifacts(command);
            case "attack":
                return attack(command);
            case "buy":
                return buy(command);
            case "prestige":
                return prestige(command);
            case "info":
                return info(command);
            case "commands":
                return commands(command);
            default:
                return ERR_INVALID_COMMAND;
        }
    }

    private Response bless(Command command) {
        // TODO: add ability to roll for bless
        if (command.getSize() < 3) {
            return ERR_MISSING_ARGS;
        }
        if (command.getSize() > 3) {
            return ERR_TOO_MANY_ARGS;
        }
        String targetUUID = extractUUIDFromMention(command.getTerm(1));
        if (!gameInterface.playerExists(targetUUID)) {
            return ERR_PLAYER_DNE;
        }
        int pride;
        try {
            pride = Integer.parseInt(command.getTerm(2));
        }
        catch (Exception e) {
            return ERR_INVALID_ARG;
        }
        gameInterface.addPride(targetUUID, pride);
        save(); // save on successful bless
        return RESPONSE_BLESSING;
    }

    private Response collect(Command command) {
        if (command.getSize() > 1) {
            return ERR_TOO_MANY_ARGS;
        }
        if (gameInterface.collectPride(command.getAuthor().getId())) {
            save(); // save on successful collect
            return new Response("MESSAGE", "You have collected your pride.");
        }
        else {
            return new Response("MESSAGE", "You cannot collected your pride.");
        }
    }

    private Response levelup(Command command) {
        if (command.getSize() > 2) {
            return ERR_TOO_MANY_ARGS;
        }
        if (command.getSize() == 2) {
            int levels;
            try {
                levels = Integer.parseInt(command.getTerm(1));
                if (levels <= 0) {
                    throw new Exception();
                }
            }
            catch (Exception e) {
                return ERR_INVALID_ARG;
            }
            if (gameInterface.levelUpEgo(command.getAuthor().getId(), levels)) {
                save(); // save on successful level up
                return RESPONSE_EGO_LEVEL_UP;
            }
            else {
                return ERR_INSUFFICIENT_PRIDE;
            }
        }
        else {
            if (gameInterface.levelUpEgo(command.getAuthor().getId(), 1)) {
                save(); // save on successful level up
                return RESPONSE_EGO_LEVEL_UP;
            }
            else {
                return ERR_INSUFFICIENT_PRIDE;
            }
        }
    }

    private Response search(Command command) {
        if (command.getSize() > 1) {
            return ERR_TOO_MANY_ARGS;
        }
        if (gameInterface.getPride(command.getAuthor().getId()) < 1) {
            return ERR_INSUFFICIENT_PRIDE;
        }
        String result = gameInterface.searchForArtifact(command.getAuthor().getId());
        save();
        return new Response("MESSAGE", result);
    }

    private Response stats(Command command) {
        // TODO: update provided stats
        // TODO: add ability to get another user's stats
        if (command.getSize() > 1) {
            return ERR_TOO_MANY_ARGS;
        }
        return new Response("MESSAGE",
                command.getAuthor().getName() +
                        "\nPride: " + gameInterface.getPride(command.getAuthor().getId()) +
                        "\nEgo: " + gameInterface.getEgo(command.getAuthor().getId()) +
                        "\nPrestige: " + gameInterface.getPrestige(command.getAuthor().getId()) +
                        "\nCost of next ego level: " + gameInterface.prideForNextEgo(command.getAuthor().getId()) + " pride" +
                        "\nCost of next prestige: " + gameInterface.egoForNextPrestige(command.getAuthor().getId()) + " ego" +
                "\"");
    }

    private Response artifacts(Command command) {
        // TODO: add ability to get list of another user's artifacts
        if (command.getSize() > 1) {
            return ERR_TOO_MANY_ARGS;
        }
        String result = gameInterface.getPlayerArtifacts(command.getAuthor().getId());
        return new Response("MESSAGE", result);
    }

    private Response attack(Command command) {
        if (!gameInterface.hasCombatModule(command.getAuthor().getId())) {
            return new Response("ERROR", "You do not have the combat module.");
        }
        String targetUUID = extractUUIDFromMention(command.getTerm(1));
        if (!gameInterface.playerExists(targetUUID)) {
            return ERR_PLAYER_DNE;
        }
        if (!gameInterface.hasCombatModule(targetUUID)) {
            return new Response("ERROR", "Target player does not have combat module.");
        }
        String result = gameInterface.simulateCombat(command.getAuthor().getId(), targetUUID);
        return new Response("MESSAGE", result);
    }

    private Response buy(Command command) {
        if (!gameInterface.hasCombatModule(command.getAuthor().getId())) {
            return new Response("ERROR", "You do not have the combat module.");
        }
        String result = gameInterface.buyDie(command.getAuthor().getId(), command.getTerm(1), command.getTerm(2));
        return new Response("MESSAGE", result);
    }

    private Response prestige(Command command) {
        if (command.getSize() > 1) {
            return ERR_TOO_MANY_ARGS;
        }
        String result = gameInterface.levelUpPrestige(command.getAuthor().getId());
        return new Response("MESSAGE", result);
    }

    private Response info(Command command) {
        // TODO: add all of the game info
        return new Response("MESSAGE",
                "Info: " +
                        "\n\tPride - The atomic value of the game." +
                        "\n\tEgo - Can be traded for using pride. One ego costs ((current_ego + 1) * 10) pride." +
                        "\n\tDaily Pride - The amount of pride you gain from the collect command, once per day. It is equal to ego + c, where c is buffs from your artifacts. Crits only double c and not ego + c." +
                        "\n\tArtifact - A rare object you have found with mystical powers. Grants buffs to daily pride collection." +
                        "\n\tPrestige - Prestige grants access to new features. It costs all of your ego and a minimum amount of ego to level up."
        );
    }

    private Response commands(Command command) {
        return new Response("MESSAGE",
                "Commands: " +
                        "\n\t" + COMMANDS_MESSAGE_STATS +
                        "\n\t" + COMMANDS_MESSAGE_BLESS +
                        "\n\t" + COMMANDS_MESSAGE_COLLECT +
                        "\n\t" + COMMANDS_MESSAGE_LEVELUP +
                        "\n\t" + COMMANDS_MESSAGE_PRESTIGE +
                        "\n\t" + COMMANDS_MESSAGE_SEARCH +
                        "\n\t" + COMMANDS_MESSAGE_ARTIFACTS +
                        "\n\t" + COMMANDS_MESSAGE_ATTACK +
                        "\n\t" + COMMANDS_MESSAGE_BUY +
                        "\n\t" + COMMANDS_MESSAGE_INFO +
                        "\n\t" + COMMANDS_MESSAGE_COMMANDS
        );
    }

    private String extractUUIDFromMention(String mention) {
        return mention.substring(2, mention.length() - 1);
    }
}