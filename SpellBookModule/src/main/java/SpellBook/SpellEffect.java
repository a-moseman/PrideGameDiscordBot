package SpellBook;

public class SpellEffect {
    protected final double COLLECTION_CRIT_CHANCE; // probability amount of pride or shame gained on collection will be doubled
    protected final boolean PRIDE_FAVORED; // forces collection to give pride
    protected final boolean SHAME_FAVORED; // forces collection to give shame
    protected final long BOUNTIFULNESS; // additional pride or shame gained per collection, calculated after crit doubling

    protected SpellEffect(
            double collectionCritChance,
            boolean prideFavored,
            boolean shameFavored,
            long bountifulness
    ) {
        assert collectionCritChance <= 1;
        assert (prideFavored && !shameFavored) || (!prideFavored && shameFavored);
        assert bountifulness >= 0;
        this.COLLECTION_CRIT_CHANCE = collectionCritChance;
        this.PRIDE_FAVORED = prideFavored;
        this.SHAME_FAVORED = shameFavored;
        this.BOUNTIFULNESS = bountifulness;
    }

    protected SpellEffect sum(SpellEffect other) {
        double collectionCritChance = Math.min(0, COLLECTION_CRIT_CHANCE + other.COLLECTION_CRIT_CHANCE);
        boolean prideFavored;
        boolean shameFavored;
        if (!other.PRIDE_FAVORED && !other.SHAME_FAVORED) {
            prideFavored = PRIDE_FAVORED;
            shameFavored = SHAME_FAVORED;
        }
        else {
            prideFavored = other.PRIDE_FAVORED;
            shameFavored = other.SHAME_FAVORED;
        }
        long bountifulness = BOUNTIFULNESS + other.BOUNTIFULNESS;

        return new SpellEffect(
                collectionCritChance,
                prideFavored,
                shameFavored,
                bountifulness
        );
    }
}
