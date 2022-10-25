package PrideBot.Concepts.ForVersion2;

public class PrideCollectionStats {
    protected final long PRIDE_PER_COLLECTION;
    protected final double COLLECTION_CRIT_PROBABILITY;

    protected PrideCollectionStats() {
        // for Jackson
        this.PRIDE_PER_COLLECTION = 0;
        this.COLLECTION_CRIT_PROBABILITY = 0;
    }

    protected PrideCollectionStats(long pridePerCollection) {
        this.PRIDE_PER_COLLECTION = pridePerCollection;
        this.COLLECTION_CRIT_PROBABILITY = 0;
    }

    protected PrideCollectionStats(long pridePerCollection, double collectionCritProbability) {
        this.PRIDE_PER_COLLECTION = pridePerCollection;
        this.COLLECTION_CRIT_PROBABILITY = collectionCritProbability;
    }

    protected PrideCollectionStats add(PrideCollectionStats other) {
        return new PrideCollectionStats(
                PRIDE_PER_COLLECTION + other.PRIDE_PER_COLLECTION,
                COLLECTION_CRIT_PROBABILITY + other.COLLECTION_CRIT_PROBABILITY
        );
    }

    @Override
    public PrideCollectionStats clone() {
        return new PrideCollectionStats(PRIDE_PER_COLLECTION, COLLECTION_CRIT_PROBABILITY);
    }

    @Override
    public String toString() {
        return "Stats:" +
                "\n\tPride Per Collection: " + PRIDE_PER_COLLECTION +
                "\n\tCollection Crit Probability: " + COLLECTION_CRIT_PROBABILITY;
    }
}
