package Bot;

import Game.GameAPI;

import java.util.Locale;

public class BotModel {
    private static final Response ERR_NOT_ADMIN = new Response("ERROR", "Not admin.");
    private static final Response ERR_INVALID_COMMAND = new Response("ERROR",  "Invalid command.");
    private static final Response ERR_TOO_MANY_ARGS = new Response("ERROR",  "Too many arguments.");
    private static final Response ERR_MISSING_ARGS = new Response("ERROR", "Missing arguments.");
    private static final Response ERR_INVALID_ARG = new Response("ERROR",  "Invalid argument.");
    private static final Response ERR_PLAYER_DNE = new Response("ERROR",  "Player does not exist.");
    private static final Response RESPONSE_BLESSING = new Response("MESSAGE", "A blessing has occurred.");
    private static final Response RESPONSE_CURSING = new Response("MESSAGE", "A cursing has occurred.");
    private static final String COMMANDS_MESSAGE_BLESS = "p>bless <target> <amount> - Grants the target player the given amount of pride. The target argument must be a mention and the amount argument must be a positive integer. (pride_dm only)";
    private static final String COMMANDS_MESSAGE_CURSE = "p>curse <target> <amount> - Grants the target player the given amount of shame. The target argument must be a mention and the amount argument must be a positive integer. (pride_dm only)";
    private static final String COMMANDS_MESSAGE_COLLECT = "p>collect - Collect your daily pride or shame. If you currently have more pride, you will collect pride, and vice versa.";
    private static final String COMMANDS_MESSAGE_STATS = "p>stats <target> - Provides your personal stats. With the optional target argument, a mention, you get the target player's stats.";
    private static final String COMMANDS_MESSAGE_GAMEINFO = "p>gameinfo - Provides information on how aspects of the game work.";
    private static final String COMMANDS_MESSAGE_BOTINFO = "p>botinfo - Provides information on the bot.";
    private static final String COMMANDS_MESSAGE_COMMANDS = "p>commands - Provides the list of commands.";
    private static final String COMMANDS_MESSAGE_BUY = "p>buy <type> <amount> - Purchase one of the given type {ego, guilt, honor, dishonor}. The optional amount argument can be used for some of the types to buy more than one in a single command invocation.";
    private static final String COMMANDS_MESSAGE_HELP = "p>help - Provides some helpful starting information.";

    private GameAPI api;
    private final long START_TIME;

    private int AUTO_SAVE_RATE_IN_MINUTES = 45;
    private long lastSaveTime;

    public BotModel(String savePath) {
        this.START_TIME = System.currentTimeMillis();
        this.lastSaveTime = System.currentTimeMillis();
        this.api = new GameAPI(savePath);
    }

    public void save() {
        if ((double) (System.currentTimeMillis() - lastSaveTime) / 1000 / 60 >= AUTO_SAVE_RATE_IN_MINUTES) {
            api.save();
            lastSaveTime = System.currentTimeMillis();
            System.out.println("SAVED"); //TODO: DEBUG
        }
    }

    public boolean doesPlayerExist(String uuid) {
        return api.doesPlayerExist(uuid);
    }

    public void addNewPlayer(String uuid) {
        api.addNewPlayer(uuid);
    }

    public Response process(Command command, boolean isAdmin) {
        switch (command.getTerm(0).toUpperCase(Locale.ROOT)) {
            case "BLESS":
                if (!isAdmin) {
                    return ERR_NOT_ADMIN;
                }
                return bless(command);
            case "CURSE":
                if (!isAdmin) {
                    return ERR_NOT_ADMIN;
                }
                return curse(command);
            case "COLLECT":
                return collect(command);
            case "BUY":
                return buy(command);
            case "STATS":
                return stats(command);
            case "GAMEINFO":
                return gameinfo(command);
            case "BOTINFO" :
                return botinfo(command);
            case "COMMANDS":
                return commands(command);
            case "HELP":
                return help(command);
            default:
                return ERR_INVALID_COMMAND;
        }
    }

    private Response bless(Command command) {
        // TODO: add ability to roll for amount
        if (command.getSize() < 3) {
            return ERR_MISSING_ARGS;
        }
        if (command.getSize() > 3) {
            return ERR_TOO_MANY_ARGS;
        }
        String targetUUID = extractUUIDFromMention(command.getTerm(1));
        if (!api.doesPlayerExist(targetUUID)) {
            return ERR_PLAYER_DNE;
        }
        int pride;
        try {
            pride = Integer.parseInt(command.getTerm(2));
        }
        catch (Exception e) {
            return ERR_INVALID_ARG;
        }
        if (pride < 0) {
            return new Response("MESSAGE", "Amount must be a positive integer.");
        }
        api.bless(targetUUID, pride);
        return RESPONSE_BLESSING;
    }

    private Response curse(Command command) {
        // TODO: add ability to roll for amount
        if (command.getSize() < 3) {
            return ERR_MISSING_ARGS;
        }
        if (command.getSize() > 3) {
            return ERR_TOO_MANY_ARGS;
        }
        String targetUUID = extractUUIDFromMention(command.getTerm(1));
        if (!api.doesPlayerExist(targetUUID)) {
            return ERR_PLAYER_DNE;
        }
        int shame;
        try {
            shame = Integer.parseInt(command.getTerm(2));
        }
        catch (Exception e) {
            return ERR_INVALID_ARG;
        }
        if (shame < 0) {
            return new Response("MESSAGE", "Amount must be a positive integer.");
        }
        api.curse(targetUUID, shame);
        return RESPONSE_CURSING;
    }

    private Response collect(Command command) {
        if (command.getSize() > 1) {
            return ERR_TOO_MANY_ARGS;
        }
        if (api.collect(command.getAuthor().getId())) {
            return new Response("MESSAGE", "You have completed your daily collection.");
        }
        else {
            return new Response("MESSAGE", "You cannot collect at the moment. Try again later.");
        }
    }

    private Response buy(Command command) {
        if (command.getSize() < 2) {
            return ERR_MISSING_ARGS;
        }
        if (command.getSize() > 3) {
            return ERR_TOO_MANY_ARGS;
        }
        switch (command.getTerm(1).toUpperCase(Locale.ROOT)) {
            case "EGO":
                return buyEgo(command.getAuthor().getId(), 1);
            case "GUILT":
                return buyGuilt(command.getAuthor().getId(), 1);
            case "HONOR":
                if (command.getSize() > 2) {
                    return ERR_TOO_MANY_ARGS;
                }
                return buyHonor(command.getAuthor().getId());
            case "DISHONOR":
                if (command.getSize() > 2) {
                    return ERR_TOO_MANY_ARGS;
                }
                return buyDishonor(command.getAuthor().getId());
            default:
                return ERR_INVALID_ARG;
        }
    }

    private Response buyEgo(String uuid, int amount) {
        int i = 0;
        while (i < amount && api.buyEgo(uuid)) {
            i++;
        }
        if (i == 0) {
            return new Response("MESSAGE", "You can not afford to buy ego");
        }
        return new Response("MESSAGE", "You have bought " + i + " ego");
    }

    private Response buyGuilt(String uuid, int amount) {
        int i = 0;
        while (i < amount && api.buyGuilt(uuid)) {
            i++;
        }
        if (i == 0) {
            return new Response("MESSAGE", "You can not afford to buy guilt");
        }
        return new Response("MESSAGE", "You have bought " + i + " guilt");
    }

    private Response buyHonor(String uuid) {
        if (api.buyHonor(uuid)) {
            return new Response("MESSAGE", "You bought 1 honor");
        }
        return new Response("MESSAGE", "You can not afford to buy honor");
    }

    private Response buyDishonor(String uuid) {
        if (api.buyDishonor(uuid)) {
            return new Response("MESSAGE", "You bought 1 dishonor");
        }
        return new Response("MESSAGE", "You can not afford to buy dishonor");
    }

    private Response stats(Command command) {
        if (command.getSize() > 2) {
            return ERR_TOO_MANY_ARGS;
        }
        String uuid;
        if (command.getSize() == 2) {
            uuid = extractUUIDFromMention(command.getTerm(1));
        }
        else {
            uuid = command.getAuthor().getId();
        }
        return new Response("MESSAGE", "Stats:" +
                "\n\tPride: " + api.getPride(uuid) +
                "\n\tShame: " + api.getShame(uuid) +
                "\n\tEgo: " + api.getEgo(uuid) +
                "\n\tGuilt: " + api.getGuilt(uuid) +
                "\n\tHonor: " + api.getHonor(uuid) +
                "\n\tDishonor: " + api.getDishonor(uuid)
        );
    }

    private Response gameinfo(Command command) {
        return new Response("MESSAGE", "Game Info:" +
                "\n\tPride and Shame:" +
                "\n\t\tThe two atomic values of the game." +
                "\n\t\tEffectively a currency." +
                "\n\tEgo and Guilt:" +
                "\n\t\tEach can be bought with pride and shame respectively." +
                "\n\t\tCost is equal to 1 more than the current amount times 10 (e.g. cost_of_next_ego = (ego + 1) * 10)." +
                "\n\t\tEach increase their respective collection amounts by their value (e.g. pride_per_collection = 1 + ego)" +
                "\n\tHonor and Dishonor:" +
                "\n\t\t[CURRENTLY DO NOTHING]" +
                "\n\t\tAs you gain levels of these, you gain access to various game features." +
                "\n\t\tSpecifically, your feature access level is equal to the greatest of the two values."
        );
    }

    private Response botinfo(Command command) {
        return new Response("MESSAGE", "Bot Info:" +
                "\n\tUptime: " + getUptime() + " days" +
                "\n\tVersion: v1.0" +
                "\n\tDeveloper: Glyphical" +
                "\n\tGitHub: https://github.com/a-moseman/PrideGameDiscordBot"
        );
    }

    private double getUptime() {
        return (double) (System.currentTimeMillis() - START_TIME) / 1000 / 60 / 60 / 24;
    }

    private Response commands(Command command) {
        return new Response("MESSAGE", "Commands:" +
                "\n\t" + COMMANDS_MESSAGE_BLESS +
                "\n\t" + COMMANDS_MESSAGE_CURSE +
                "\n\t" + COMMANDS_MESSAGE_COLLECT +
                "\n\t" + COMMANDS_MESSAGE_BUY +
                "\n\t" + COMMANDS_MESSAGE_STATS +
                "\n\t" + COMMANDS_MESSAGE_GAMEINFO +
                "\n\t" + COMMANDS_MESSAGE_BOTINFO +
                "\n\t" + COMMANDS_MESSAGE_COMMANDS +
                "\n\t" + COMMANDS_MESSAGE_HELP
        );
    }

    private Response help(Command command) {
        return new Response("MESSAGE", "Help:" +
                "\n\tAll commands use the p> prefix. This prefix is case-sensitive." +
                "\n\tCommand names and arguments are not case-sensitive." +
                "\n\tUse p>commands to get the list of commands." +
                "\n\tThe p>bless and p>curse commands can only be used by Discord users with a role name pride_dm. The only requirement for the role is the name." +
                "\n\tIf the bot does not respond to a command, it means an exception was thrown. This means I have an issue in my code, so feel free to let me know so I can fix it."
        );
    }

    private String extractUUIDFromMention(String mention) {
        return mention.substring(2, mention.length() - 1);
    }
}