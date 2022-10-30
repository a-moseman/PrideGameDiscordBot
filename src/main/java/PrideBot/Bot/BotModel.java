package PrideBot.Bot;

import PrideBot.DND.DaFoin;
import PrideBot.DND.Fish;
import PrideBot.Game.GameAPI;
import PrideBot.Game.Results.BuyFailResult;
import PrideBot.Game.Results.BuyResult;
import PrideBot.Game.Results.BuySuccessResult;
import net.dv8tion.jda.api.entities.Guild;

import java.util.Locale;

public class BotModel {
    private static final Response ERR_INVALID_COMMAND = new Response("Invalid command.");
    private static final Response ERR_TOO_MANY_ARGS = new Response("Too many arguments.");
    private static final Response ERR_MISSING_ARGS = new Response("Missing arguments.");
    private static final Response ERR_INVALID_ARG = new Response("Invalid argument.");
    private static final Response ERR_PLAYER_DNE = new Response("Player does not exist.");
    private static final Response ERR_MISSING_SPELL_BOOK_MODULE = new Response("Missing spell book module.");
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
    private static final String COMMANDS_MESSAGE_DESCRIBE = "p>describe <args> - Describes the given thing. WIP.";
    private static final String COMMANDS_MESSAGE_DAFOIN = "p>dafoin - Flips DA FOIN.";

    private GameAPI api;
    private final long START_TIME;

    private final int AUTO_SAVE_RATE_IN_MINUTES = 45;
    private long lastSaveTime;

    private final int RANKS_TO_SHOW = 10;

    public BotModel(String savePath) {
        this.START_TIME = System.currentTimeMillis();
        this.lastSaveTime = System.currentTimeMillis();
        this.api = new GameAPI(savePath);
    }

    public void updateName(String uuid, String name) {
        api.setName(uuid, name);
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

    public Response process(Command command, Guild guild, boolean isAdmin) {
        String c = Aliaser.translate(command.getTerm(0).toUpperCase(Locale.ROOT));
        switch (c) {
            case "BLESS":
                if (!isAdmin) {
                    return new Response(command.getAuthor().getName() + ", you do not have the pride_dm role.");
                }
                return bless(command, guild);
            case "CURSE":
                if (!isAdmin) {
                    return new Response(command.getAuthor().getName() + ", you do not have the pride_dm role.");
                }
                return curse(command, guild);
            case "COLLECT":
                return collect(command);
            case "BUY":
                return buy(command);
            case "STATS":
                return stats(command, guild);
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
            case "DESCRIBE":
                return describe(command);
            case "DAFOIN":
                return flipDaFoin(command);
            case "RANKS":
                return ranks(command);
            default:
                return ERR_INVALID_COMMAND;
        }
    }

    private Response ranks(Command command) {
        return new Response(
                api.getTopNPlayers(RANKS_TO_SHOW)
        );
    }

    private Response flipDaFoin(Command command) {
        Fish fish = DaFoin.flip();
        return new Response(
                command.getAuthor().getName() + " has flipped DA FOIN!" +
                "\nA " + fish.NAME + " of direness " + fish.DIRENESS + " has spawned."
        );
    }

    private static final Response PRIDE_DESCRIPTION = new Response("Pride:" +
            "\n\tOne of two atomic values in the game. Used as a currency."
    );
    private static final Response SHAME_DESCRIPTION = new Response("Shame:" +
            "\n\tOne of two atomic values of the game. Used as a currency."
    );
    private static final Response AFFINITY_DESCRIPTION = new Response("Affinity:" +
            "\n\tYou can either have affinity for Pride or Shame." +
            "\n\tAffinity is determined by comparing in order: honor and dishonor, ego and guilt, pride and shame." +
            "\n\tYour affinity determines what you when you collect."
    );
    private static final Response COLLECTION_DESCRIPTION = new Response("Collection:" +
            "\n\tThe action of collecting your daily pride or shame, based on your affinity." +
            "\n\tThe amount you collect is 1 + x + b, where x is your ego or guilt and b is your bountifulness." +
            "\n\tEgo is used for x if you're collecting pride, guilt is used for x is you're collecting shame." +
            "\n\tOn a crit, the amount you collect is 2 * (1 + x + b)"
    );
    private static final Response EGO_DESCRIPTION = new Response("Ego:" +
            "\n\tEgo can be bought with pride. It costs (e + 1) * 7 pride, where e is your current ego." +
            "\n\tThe more ego you have, the more pride you gain when you collect." +
            "\n\tEgo is also used to pay for honor."
    );
    private static final Response GUILT_DESCRIPTION = new Response("Ego:" +
            "\n\tGuilt can be bought with shame. It costs (g + 1) * 7 shame, where g is your current guilt." +
            "\n\tThe more guilt you have, the more guilt you gain when you collect." +
            "\n\tGuilt is also used to pay for dishonor."
    );
    private static final Response HONOR_DESCRIPTION = new Response("Honor: " +
            "\n\tHonor is used to determine your level, which grants access to features in the game." +
            "\n\tIt costs ego to buy. Specifically, you need (h + 1) * 10 ego to buy 1 honor, where h is your current honor." +
            "\n\tHowever, do note that you spend all of your ego when you buy honor, the cost is just the minimum requirement."
    );
    private static final Response DISHONOR_DESCRIPTION = new Response("Honor: " +
            "\n\tDishonor is used to determine your level, which grants access to features in the game." +
            "\n\tIt costs guilt to buy. Specifically, you need (d + 1) * 10 guilt to buy 1 dishonor, where d is your current dishonor." +
            "\n\tHowever, do note that you spend all of your guilt when you buy dishonor, the cost is just the minimum requirement."
    );
    private static final Response LEVEL_DESCRIPTION = new Response("Level: " +
            "\n\tYour level determines your access to game features." +
            "\n\tIt is equal to your honor or dishonor, whatever is greater." +
            "\n\tLevel 0 grants access to the base game." +
            "\n\tLevel 1 grants access to your spell book and the spell system." +
            "\n\tThere are no more levels beyond this, although you can attain them." +
            "\n\tYou will gain access to features retroactively when they are implemented, if you already have the needed level."
    );
    private static final Response SPELLBOOK_DESCRIPTION = new Response("Spellbook:" +
            "\n\tAt level 1, you gain access to your spellbook." +
            "\n\tYour spellbook grants you the ability to store spells, cast spells, and try to buy spells." +
            "\n\tSpells grant various effects that benefit the amount of pride or shame you gain when you next collect." +
            "\n\tThe effects are:" +
            "\n\t\tBountifulness - A flat bonus added to the amount of pride or shame you collect." +
            "\n\t\tCrit Chance - A probability to double your pride or shame collected." +
            "\n\t\tPride Favored - A guarantee that you will collect pride." +
            "\n\t\tShame Favored - A guarantee that you will collect shame."
    );
    private static final Response SPELLS_DESCRIPTION = new Response("Spells:" +
            "\n\tSpells can be bought using the buy command." +
            "\n\tHowever, what you get is random, assuming you even get one." +
            "\n\tOn the bright side, they are cheap, only costing one pride or shame--what ever you have most of." +
            "\n\tYou can cast these spells using the cast command and providing the index of the spell in your spellbook (use p>spells to get a list of your spells and their indices)." +
            "\n\tWhen cast, they will grant buffs to your next collection." +
            "\n\tSpells have a number of uses and you will lose the spell once those uses reach zero." +
            "\n\tEach spell can also only be cast once per day." +
            "\n\tHowever, the spell effects do stack, with the exception of pride favored and shame favored which override." +
            "\n\tTo get information on a spell you have, use p>describe spell <index>, where index is the index of the spell in your spellbook."
    );
    private static final Response DAFOIN_DESCRIPTION = new Response("DA FOIN:" +
            "\n\t\"It just generates a fish, and fucking murders it\" - A slightly drunk Andrew."
    );


    private Response describe(Command command) {
        if (command.getSize() == 1) {
            return new Response("Missing thing to describe.");
        }
        switch (command.getTerm(1).toUpperCase(Locale.ROOT)) {
            case "SPELL": // describe a given spell in the player's spell book based on index
                return describeSpell(command);
            case "PRIDE":
                return PRIDE_DESCRIPTION;
            case "SHAME":
                return SHAME_DESCRIPTION;
            case "AFFINITY":
                return AFFINITY_DESCRIPTION;
            case "COLLECT":
            case "COLLECTION":
            case "COLLECTING":
                return COLLECTION_DESCRIPTION;
            case "EGO":
                return EGO_DESCRIPTION;
            case "GUILT":
                return GUILT_DESCRIPTION;
            case "HONOR":
                return HONOR_DESCRIPTION;
            case "DISHONOR":
                return DISHONOR_DESCRIPTION;
            case "LEVEL":
            case "LEVELS":
                return LEVEL_DESCRIPTION;
            case "SPELLBOOK":
                return SPELLBOOK_DESCRIPTION;
            case "SPELLS":
                return SPELLS_DESCRIPTION;
            case "DAFOIN":
                return DAFOIN_DESCRIPTION;
            default:
                return new Response("I do not have a description for that.");
        }
    }

    private Response describeSpell(Command command) {
        if (command.getSize() < 3) { // the player is NOT trying to get info on a specific spell they possess.
            return SPELLS_DESCRIPTION;
        }
        int spellIndex;
        try {
            spellIndex = Integer.parseInt(command.getTerm(2));
        }
        catch (Exception e) {
            return ERR_INVALID_ARG;
        }
        String spellDescription;
        try {
            spellDescription = api.getSpellDescription(command.getAuthor().getId(), spellIndex);
        }
        catch (Exception e) {
            return ERR_INVALID_ARG;
        }
        return new Response(spellDescription);
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
        String spellName = api.getSpellName(command.getAuthor().getId(), spellIndex) + " lv. " + api.getSpellLevel(command.getAuthor().getId(), spellIndex);
        if (api.castSpell(command.getAuthor().getId(), spellIndex)) {
            return new Response(spellName + " cast successfully.");
        }
        return new Response("Failed to cast " + spellName + ".");
    }

    private Response spells(Command command) {
        if (command.getSize() > 2) {
            return ERR_TOO_MANY_ARGS;
        }
        if (!api.hasSpellBookModule(command.getAuthor().getId())) {
            return ERR_MISSING_SPELL_BOOK_MODULE;
        }
        return new Response(
                command.getAuthor().getName() + "'s Spells: \n" +
                api.getSpellList(command.getAuthor().getId()));
    }

    private Response bless(Command command, Guild guild) {
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
            return new Response("Amount must be a positive integer.");
        }
        api.bless(targetUUID, pride);
        return new Response(guild.getMemberById(targetUUID).getEffectiveName() + ", you have been blessed for " + pride + " pride.");
    }

    private Response curse(Command command, Guild guild) {
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
            return new Response("Amount must be a positive integer.");
        }
        api.curse(targetUUID, shame);
        return new Response(guild.getMemberById(targetUUID).getEffectiveName() + ", you have been cursed for " + shame + " shame.");
    }

    private Response collect(Command command) {
        if (command.getSize() > 1) {
            return ERR_TOO_MANY_ARGS;
        }
        if (api.collect(command.getAuthor().getId())) {
            return new Response(command.getAuthor().getName() + ", you have completed your daily collection.");
        }
        else {
            double t = ((double) ((int) (api.getDaysUntilNextCollection(command.getAuthor().getId()) * 1000))) / 1000;
            t += 0.005;
            t = ((double)(int)(t * 100)) / 100;

            return new Response(command.getAuthor().getName() + ", you cannot collect at the moment. Try again in " + t + " days.");
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
            return new Response(username + ", you cannot afford to buy any ego.\nYou need " + buyFailResult.MISSING_CURRENCY + " more pride to buy 1 ego.");
        }
        return new Response(username + ", you have bought " + i + " ego for " + spentPride + " pride.");
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
            return new Response(username + ", you can not afford to buy guilt.\n You need " + buyFailResult.MISSING_CURRENCY + " more shame to buy 1 guilt.");
        }
        return new Response(username + ", you have bought " + i + " guilt for " + spentShame + " shame.");
    }

    private Response buyHonor(String uuid, String username) {
        BuyResult buyResult = api.buyHonor(uuid);
        if (buyResult instanceof BuySuccessResult) {
            return new Response(username + ", you bought 1 honor for all of your ego.");
        }
        BuyFailResult buyFailResult = (BuyFailResult) buyResult;
        return new Response(username + ", you can not afford to buy honor.\nYou need " + buyFailResult.MISSING_CURRENCY + " more ego to buy 1 honor.");
    }

    private Response buyDishonor(String uuid, String username) {
        BuyResult buyResult = api.buyDishonor(uuid);
        if (buyResult instanceof BuySuccessResult) {
            return new Response(username + ", you bought 1 dishonor for all of your guilt.");
        }
        BuyFailResult buyFailResult = (BuyFailResult) buyResult;
        return new Response(username + ", you can not afford to buy dishonor.\nYou need " + buyFailResult.MISSING_CURRENCY + " more guilt to buy 1 dishonor.");
    }

    private Response buySpell(String uuid, String username) {
        BuyResult buyResult = api.buySpell(uuid);
        if (buyResult instanceof BuySuccessResult) {
            BuySuccessResult buySuccessResult = (BuySuccessResult) buyResult;
            return new Response(username + ", you bought 1 spell for " + buySuccessResult.SPENT_AMOUNT + " pride/shame.");
        }
        return new Response(username + ", you failed to buy a spell.");
    }

    private Response stats(Command command, Guild guild) {
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
        String name = guild.getMemberById(uuid).getEffectiveName();
        return new Response(name +  "'s Stats:" +
                "\n\tPride: " + api.getPride(uuid) +
                "\n\tShame: " + api.getShame(uuid) +
                "\n\tEgo: " + api.getEgo(uuid) + " (" + api.getPrideToNextEgo(uuid) + " pride to next)" +
                "\n\tGuilt: " + api.getGuilt(uuid) + " (" + api.getShameToNextGuilt(uuid) + " shame to next)" +
                "\n\tHonor: " + api.getHonor(uuid) + " (" + api.getEgoToNextHonor(uuid) + " ego to next)" +
                "\n\tDishonor: " + api.getDishonor(uuid) + " (" + api.getGuiltToNextDishonor(uuid) + " guilt to next)"
        );
    }

    private Response gameinfo(Command command) {
        return new Response("PrideBot.Game Info:" +
                "\n\tPride and Shame:" +
                "\n\t\tThe two atomic values of the game." +
                "\n\t\tEffectively a currency." +
                "\n\tEgo and Guilt:" +
                "\n\t\tEach can be bought with pride and shame respectively." +
                "\n\t\tCost is equal to 1 more than the current amount times 7 (e.g. cost_of_next_ego = (ego + 1) * 7)." +
                "\n\t\tEach increase their respective collection amounts by their value (e.g. pride_per_collection = 1 + ego)" +
                "\n\tHonor and Dishonor:" +
                "\n\t\tAs you gain levels of these, you gain access to various game features." +
                "\n\t\tSpecifically, your feature access level is equal to the greatest of the two values." +
                "\n\t\tThe first level gives you access to the spell book module."
        );
    }

    private Response botinfo(Command command) {
        return new Response("PrideBot.Bot Info:" +
                "\n\tUptime: " + getUptime() + " days" +
                "\n\tVersion: v2.2" +
                "\n\tDeveloper: Glyphical" +
                "\n\tGitHub: https://github.com/a-moseman/PrideGameDiscordBot"
        );
    }

    private double getUptime() {
        return (double) (System.currentTimeMillis() - START_TIME) / 1000 / 60 / 60 / 24;
    }

    private Response commands(Command command) {
        return new Response("Commands:" +
                "\n\t" + COMMANDS_MESSAGE_BLESS +
                "\n\t" + COMMANDS_MESSAGE_CURSE +
                "\n\t" + COMMANDS_MESSAGE_COLLECT +
                "\n\t" + COMMANDS_MESSAGE_BUY +
                "\n\t" + COMMANDS_MESSAGE_STATS +
                "\n\t" + COMMANDS_MESSAGE_SPELLS +
                "\n\t" + COMMANDS_MESSAGE_CAST +
                "\n\t" + COMMANDS_MESSAGE_DESCRIBE +
                "\n\t" + COMMANDS_MESSAGE_GAMEINFO +
                "\n\t" + COMMANDS_MESSAGE_BOTINFO +
                "\n\t" + COMMANDS_MESSAGE_COMMANDS +
                "\n\t" + COMMANDS_MESSAGE_HELP +
                "\n\t" + COMMANDS_MESSAGE_DAFOIN
        );
    }

    private Response help(Command command) {
        return new Response("Help:" +
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