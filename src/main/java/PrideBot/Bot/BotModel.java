package PrideBot.Bot;

import PrideBot.Game.GameAPI;
import PrideBot.Game.Responses.BuyFailResult;
import PrideBot.Game.Responses.BuyResult;
import PrideBot.Game.Responses.BuySuccessResult;

import java.util.Locale;

public class BotModel {
    private static final Response ERR_NOT_ADMIN = new Response("ERROR", "Not admin.");
    private static final Response ERR_INVALID_COMMAND = new Response("ERROR",  "Invalid command.");
    private static final Response ERR_TOO_MANY_ARGS = new Response("ERROR",  "Too many arguments.");
    private static final Response ERR_MISSING_ARGS = new Response("ERROR", "Missing arguments.");
    private static final Response ERR_INVALID_ARG = new Response("ERROR",  "Invalid argument.");
    private static final Response ERR_PLAYER_DNE = new Response("ERROR",  "Player does not exist.");
    private static final Response ERR_MISSING_SPELL_BOOK_MODULE = new Response("ERROR", "Missing spell book module.");
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
    private static final String COMMANDS_MESSAGE_SPELLS = "p>spells - Provides a list of your spells.";
    private static final String COMMANDS_MESSAGE_CAST = "p>cast <index> - Cast the spell denoted by the index. The index of a spell can be found using the p>spells command.";

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
            case "SPELLS":
                return spells(command);
            case "CAST":
                return cast(command);
            default:
                return ERR_INVALID_COMMAND;
        }
    }

    private Response cast(Command command) {
        if (command.getSize() < 2) {
            return ERR_MISSING_ARGS;
        }
        if (command.getSize() > 2) {
            return ERR_TOO_MANY_ARGS;
        }
        if (!api.hasSpellBookModule(command.getAuthor().getId())) {
            return ERR_MISSING_SPELL_BOOK_MODULE;
        }
        int spellIndex;
        try {
            spellIndex = Integer.parseInt(command.getTerm(1));
        }
        catch (Exception e) {
            return ERR_INVALID_ARG;
        }
        if (api.castSpell(command.getAuthor().getId(), spellIndex)) {
            return new Response("MESSAGE", "Spell cast successfully.");
        }
        return new Response("MESSAGE", "Failed to cast spell.");
    }

    private Response spells(Command command) {
        if (command.getSize() > 2) {
            return ERR_TOO_MANY_ARGS;
        }
        if (!api.hasSpellBookModule(command.getAuthor().getId())) {
            return ERR_MISSING_SPELL_BOOK_MODULE;
        }
        return new Response(
                "MESSAGE",
                api.getSpellList(command.getAuthor().getId()));
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
            return new Response("MESSAGE", command.getAuthor().getName() + ", you have completed your daily collection.");
        }
        else {
            double t = ((double) ((int) (api.getDaysUntilNextCollection(command.getAuthor().getId()) * 1000))) / 1000;
            t += 0.005;
            t = ((double)(int)(t * 100)) / 100;

            return new Response("MESSAGE", command.getAuthor().getName() + ", you cannot collect at the moment. Try again in " + t + " days.");
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
                int ego;
                try {
                    ego = Integer.parseInt(command.getTerm(2));
                }
                catch (Exception e) {
                    return ERR_INVALID_ARG;
                }
                return buyEgo(command.getAuthor().getId(), command.getAuthor().getName(), ego);
            case "GUILT":
                int guilt;
                try {
                    guilt = Integer.parseInt(command.getTerm(2));
                }
                catch (Exception e) {
                    return ERR_INVALID_ARG;
                }
                return buyGuilt(command.getAuthor().getId(), command.getAuthor().getName(), guilt);
            case "HONOR":
                if (command.getSize() > 2) {
                    return ERR_TOO_MANY_ARGS;
                }
                return buyHonor(command.getAuthor().getId(), command.getAuthor().getName());
            case "DISHONOR":
                if (command.getSize() > 2) {
                    return ERR_TOO_MANY_ARGS;
                }
                return buyDishonor(command.getAuthor().getId(), command.getAuthor().getName());
            case "SPELL":
                if (command.getSize() > 2) {
                    return ERR_TOO_MANY_ARGS;
                }
                return buySpell(command.getAuthor().getId(), command.getAuthor().getName());
            default:
                return ERR_INVALID_ARG;
        }
    }

    private Response buyEgo(String uuid, String username, int amount) {
        BuyResult buyResult = null;
        long spentPride = 0;
        int i = 0;
        while (i < amount && (buyResult = api.buyEgo(uuid)) instanceof BuySuccessResult) {
            i++;
            spentPride += ((BuySuccessResult) buyResult).SPENT_AMOUNT;
        }
        if (i == 0) {
            BuyFailResult buyFailResult = (BuyFailResult) buyResult;
            return new Response("MESSAGE", username + ", you cannot afford to buy any ego.\nYou need " + buyFailResult.MISSING_CURRENCY + " more pride to buy 1 ego.");
        }
        return new Response("MESSAGE", username + ", you have bought " + i + " ego for " + spentPride + " pride.");
    }

    private Response buyGuilt(String uuid, String username, int amount) {
        BuyResult buyResult = null;
        long spentShame = 0;
        int i = 0;
        while (i < amount && (buyResult = api.buyGuilt(uuid)) instanceof BuySuccessResult) {
            i++;
            spentShame += ((BuySuccessResult) buyResult).SPENT_AMOUNT;
        }
        if (i == 0) {
            BuyFailResult buyFailResult = (BuyFailResult) buyResult;
            return new Response("MESSAGE", username + ", you can not afford to buy guilt.\n You need " + buyFailResult.MISSING_CURRENCY + " more shame to buy 1 guilt.");
        }
        return new Response("MESSAGE", username + ", you have bought " + i + " guilt for " + spentShame + " shame.");
    }

    private Response buyHonor(String uuid, String username) {
        BuyResult buyResult = api.buyHonor(uuid);
        if (buyResult instanceof BuySuccessResult) {
            return new Response("MESSAGE", username + ", you bought 1 honor for all of your ego.");
        }
        BuyFailResult buyFailResult = (BuyFailResult) buyResult;
        return new Response("MESSAGE", username + ", you can not afford to buy honor.\nYou need " + buyFailResult.MISSING_CURRENCY + " more ego to buy 1 honor.");
    }

    private Response buyDishonor(String uuid, String username) {
        BuyResult buyResult = api.buyDishonor(uuid);
        if (buyResult instanceof BuySuccessResult) {
            return new Response("MESSAGE", username + ", you bought 1 dishonor for all of your guilt.");
        }
        BuyFailResult buyFailResult = (BuyFailResult) buyResult;
        return new Response("MESSAGE", username + ", you can not afford to buy dishonor.\nYou need " + buyFailResult.MISSING_CURRENCY + " more guilt to buy 1 dishonor.");
    }

    private Response buySpell(String uuid, String username) {
        BuyResult buyResult = api.buySpell(uuid);
        if (buyResult instanceof BuySuccessResult) {
            BuySuccessResult buySuccessResult = (BuySuccessResult) buyResult;
            return new Response("MESSAGE", username + ", you bought 1 spell for " + buySuccessResult.SPENT_AMOUNT + " pride/shame.");
        }
        return new Response("MESSAGE", username + ", you failed to buy a spell.");
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
        return new Response("MESSAGE", "PrideBot.Game Info:" +
                "\n\tPride and Shame:" +
                "\n\t\tThe two atomic values of the game." +
                "\n\t\tEffectively a currency." +
                "\n\tEgo and Guilt:" +
                "\n\t\tEach can be bought with pride and shame respectively." +
                "\n\t\tCost is equal to 1 more than the current amount times 10 (e.g. cost_of_next_ego = (ego + 1) * 10)." +
                "\n\t\tEach increase their respective collection amounts by their value (e.g. pride_per_collection = 1 + ego)" +
                "\n\tHonor and Dishonor:" +
                "\n\t\tAs you gain levels of these, you gain access to various game features." +
                "\n\t\tSpecifically, your feature access level is equal to the greatest of the two values." +
                "\n\t\tThe first level gives you access to the spell book module."
        );
    }

    private Response botinfo(Command command) {
        return new Response("MESSAGE", "PrideBot.Bot Info:" +
                "\n\tUptime: " + getUptime() + " days" +
                "\n\tVersion: v2.0" +
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
                "\n\t" + COMMANDS_MESSAGE_SPELLS +
                "\n\t" + COMMANDS_MESSAGE_CAST +
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