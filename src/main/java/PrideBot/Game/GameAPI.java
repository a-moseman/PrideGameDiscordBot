package PrideBot.Game;

import PrideBot.Game.Responses.BuyFailResult;
import PrideBot.Game.Responses.BuyResult;
import PrideBot.Game.Responses.BuySuccessResult;
import PrideBot.Game.Responses.Currency;

/**
 * Finished for version 1.
 * The API of the game.
 * The only thing accessible outside of the PrideBot.Game package.
 */
public class GameAPI {
    private final String SAVE_DATA_PATH;
    private GameModel gameModel;
    private PersistenceManager persistenceManager;

    public GameAPI(String saveDataPath) {
        this.SAVE_DATA_PATH = saveDataPath;
        this.gameModel = new GameModel();
        this.persistenceManager = new PersistenceManager(saveDataPath, gameModel);
        persistenceManager.load();
    }

    /**
     * Save the game.
     */
    public void save() {
        persistenceManager.save();
    }

    /**
     * Blesses the player with a given amount of pride.
     *
     * @param uuid   The UUID of the player.
     * @param amount The amount of pride to give the player.
     */
    public void bless(String uuid, long amount) {
        gameModel.getPlayer(uuid).getStats().addPride(amount);
    }

    /**
     * Curses the player with a given amount of shame.
     *
     * @param uuid   The UUID of the player.
     * @param amount The amount of shame to give the player.
     */
    public void curse(String uuid, long amount) {
        gameModel.getPlayer(uuid).getStats().addShame(amount);
    }

    /**
     * Collect the player's daily pride or shame.
     *
     * @param uuid The UUID of the player.
     * @return boolean Whether or not they can collect at the moment.
     */
    public boolean collect(String uuid) {
        return gameModel.getPlayer(uuid).collect();
    }

    /**
     * Exchange some of a player's pride for 1 ego.
     *
     * @param uuid The UUID of the player.
     * @return boolean Whether or not they can afford it.
     */
    public BuyResult buyEgo(String uuid) {
        long cost = gameModel.getPlayer(uuid).getStats().costOfNextEgo();
        if (gameModel.getPlayer(uuid).getStats().buyEgo()) {
            return new BuySuccessResult(
                    "player_name",
                    "ego",
                    Currency.PRIDE,
                    cost
            );
        }
        return new BuyFailResult(
                "player_name",
                "ego",
                Currency.PRIDE,
                cost - getPride(uuid)
        );
    }

    /**
     * Exchange some of a player's shame for 1 guilt.
     *
     * @param uuid The UUID of the player.
     * @return boolean Whether or not they can afford it.
     */
    public BuyResult buyGuilt(String uuid) {
        //return gameModel.getPlayer(uuid).getStats().buyGuilt();
        long cost = gameModel.getPlayer(uuid).getStats().costOfNextGuilt();
        if (gameModel.getPlayer(uuid).getStats().buyGuilt()) {
            return new BuySuccessResult(
                    "player_name",
                    "ego",
                    Currency.SHAME,
                    cost
            );
        }
        return new BuyFailResult(
                "player_name",
                "ego",
                Currency.SHAME,
                cost - getShame(uuid)
        );
    }

    /**
     * Exchange all of a player's ego for 1 honor.
     *
     * @param uuid The UUID of the player.
     * @return boolean Whether or not they can afford it.
     */
    public BuyResult buyHonor(String uuid) {
        long cost = gameModel.getPlayer(uuid).getStats().costOfNextHonor();
        if (gameModel.getPlayer(uuid).getStats().buyHonor()) {
            gameModel.getPlayer(uuid).retroactivelyAddModules();
            return new BuySuccessResult(
                    "player_name",
                    "honor",
                    Currency.EGO,
                    getEgo(uuid)
            );
        }
        return new BuyFailResult(
                "player_name",
                "honor",
                Currency.EGO,
                cost - getEgo(uuid)
        );
    }

    /**
     * Exchange all of a player's guilt for 1 dishonor.
     * @param uuid The UUID of the player.
     * @return boolean Whether or not they can afford it.
     */
    public BuyResult buyDishonor(String uuid) {
        long cost = gameModel.getPlayer(uuid).getStats().costOfNextDishonor();
        if (gameModel.getPlayer(uuid).getStats().buyDishonor()) {
            gameModel.getPlayer(uuid).retroactivelyAddModules();
            return new BuySuccessResult(
                    "player_name",
                    "dishonor",
                    Currency.GUILT,
                    getGuilt(uuid)
            );
        }
        return new BuyFailResult(
                "player_name",
                "dishonor",
                Currency.GUILT,
                cost - getGuilt(uuid)
        );
    }

    /**
     * Exchange 2 of a player's pride for a chance to get a spell.
     * @param uuid The UUID of the player.
     * @return boolean Whether or not they can afford it or they don't have the module.
     */
    public BuyResult buySpell(String uuid) {
        if (gameModel.getPlayer(uuid).buySpell()) {
            return new BuySuccessResult(
                    "player_name",
                    "spell",
                    Currency.NONE,
                    0
            );
        }
        return new BuyFailResult(
                "player_name",
                "spell",
                Currency.NONE,
                0
        );
    }

    /**
     * Add a new player to the game.
     * @param uuid The UUID of the new player.
     */
    public void addNewPlayer(String uuid) {
        gameModel.addNewPlayer(uuid);
    }

    /**
     * Cast the given spell of the player.
     * @param uuid The UUID of the player.
     * @param index The index of the spell.
     * @return boolean
     */
    public boolean castSpell(String uuid, int index) {
        return gameModel.getPlayer(uuid).getSpellBookModule().castSpell(index);
    }

    //___GETTERS___\\

    /**
     * Get the list of a players spells.
     * @param uuid The UUID of the player.
     * @return String
     */
    public String getSpellList(String uuid) {
        return gameModel.getPlayer(uuid).getSpellBookModule().getSpellList();
    }

    /**
     * Determine if a player has the spell book module.
     * @param uuid The UUID of the player.
     * @return boolean
     */
    public boolean hasSpellBookModule(String uuid) {
        return gameModel.getPlayer(uuid).hasSpellBookModule();
    }

    /**
     * Determine if a player exists.
     * @param uuid The UUID of the player.
     * @return boolean
     */
    public boolean doesPlayerExist(String uuid) {
        return gameModel.playerExists(uuid);
    }

    /**
     * Get how many days until the player can collect next.
     *
     * @param uuid The UUID of the player.
     * @return double
     */
    public double getDaysUntilNextCollection(String uuid) {
        return gameModel.getPlayer(uuid).daysUntilNextCollection();
    }

    /**
     * Get how much pride the player has.
     *
     * @param uuid The UUID of the player.
     * @return long
     */
    public long getPride(String uuid) {
        return gameModel.getPlayer(uuid).getStats().getPride();
    }

    /**
     * Get how much shame the player has.
     *
     * @param uuid The UUID of the player.
     * @return long
     */
    public long getShame(String uuid) {
        return gameModel.getPlayer(uuid).getStats().getShame();
    }

    /**
     * Get how much ego the player has.
     *
     * @param uuid The UUID of the player.
     * @return long
     */
    public long getEgo(String uuid) {
        return gameModel.getPlayer(uuid).getStats().getEgo();
    }

    /**
     * Get how much guilt the player has.
     *
     * @param uuid The UUID of the player.
     * @return long
     */
    public long getGuilt(String uuid) {
        return gameModel.getPlayer(uuid).getStats().getGuilt();
    }

    /**
     * Get how much honor the player has.
     *
     * @param uuid The UUID of the player.
     * @return long
     */
    public long getHonor(String uuid) {
        return gameModel.getPlayer(uuid).getStats().getHonor();
    }

    /**
     * Get how much dishonor the player has.
     *
     * @param uuid The UUID of the player.
     * @return long
     */
    public long getDishonor(String uuid) {
        return gameModel.getPlayer(uuid).getStats().getDishonor();
    }
}
