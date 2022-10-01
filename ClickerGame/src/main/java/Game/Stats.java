package Game;

public class Stats {
    public final long PRIDE_PER_COLLECTION;
    public final double COLLECTION_CRIT_PROBABILITY;

    public Stats() {
        // for Jackson
        this.PRIDE_PER_COLLECTION = 0;
        this.COLLECTION_CRIT_PROBABILITY = 0;
    }

    public Stats(long pridePerCollection) {
        this.PRIDE_PER_COLLECTION = pridePerCollection;
        this.COLLECTION_CRIT_PROBABILITY = 0;
    }

    public Stats(long pridePerCollection, double collectionCritProbability) {
        this.PRIDE_PER_COLLECTION = pridePerCollection;
        this.COLLECTION_CRIT_PROBABILITY = collectionCritProbability;
    }

    public Stats add(Stats other) {
        return new Stats(
                PRIDE_PER_COLLECTION + other.PRIDE_PER_COLLECTION,
                COLLECTION_CRIT_PROBABILITY + other.COLLECTION_CRIT_PROBABILITY
        );
    }

    @Override
    public Stats clone() {
        return new Stats(PRIDE_PER_COLLECTION, COLLECTION_CRIT_PROBABILITY);
    }

    @Override
    public String toString() {
        return "Stats:" +
                "\n\tPride Per Collection: " + PRIDE_PER_COLLECTION +
                "\n\tCollection Crit Probability: " + COLLECTION_CRIT_PROBABILITY;
    }
}
