package PrideBot.Concepts.ForVersion2;

import java.util.Random;

public class Artifact implements Comparable<Artifact> {
    private static final Random RANDOM = new Random();
    private String name;
    private PrideCollectionStats prideCollectionStats;
    private int grade;

    protected Artifact(String name, PrideCollectionStats prideCollectionStats, int grade) {
        this.name = name;
        this.prideCollectionStats = prideCollectionStats;
        this.grade = grade;
    }

    protected String getName() {
        return name;
    }

    protected PrideCollectionStats getStats() {
        return prideCollectionStats;
    }

    protected int getGrade() {
        return grade;
    }

    private static int rollForGrade() {
        double r = RANDOM.nextDouble();
        if (r < Parameters.ARTIFACT_GRADE_FIVE_PROBABILITY) {
            return 5;
        }
        if (r < Parameters.ARTIFACT_GRADE_FOUR_PROBABILITY) {
            return 4;
        }
        if (r < Parameters.ARTIFACT_GRADE_THREE_PROBABILITY) {
            return 3;
        }
        if (r < Parameters.ARTIFACT_GRADE_TWO_PROBABILITY) {
            return 2;
        }
        return 1; // grade 1 is guaranteed
    }

    private static Artifact getArtifact(int grade) {
        switch (RANDOM.nextInt(2)) {
            case 0:
                return buildNamelessItemOne(grade);
            case 1:
                return buildNamelessItemTwo(grade);
        }
        return null;
    }

    protected static Artifact searchForArtifact() {
        return RANDOM.nextDouble() < Parameters.ARTIFACT_DROP_PROBABILITY ? getArtifact(rollForGrade()) : null;
    }

    private static Artifact buildNamelessItemOne(int grade) {
        return new Artifact("Nameless One", new PrideCollectionStats(1 * grade, 0), grade);
    }

    private static Artifact buildNamelessItemTwo(int grade) {
        return new Artifact("Nameless Two", new PrideCollectionStats(0, 0.01 * grade), grade);
    }

    @Override
    public int compareTo(Artifact artifact) {
        int nameComparison = name.compareTo(artifact.getName());
        int gradeComparison = grade - artifact.getGrade();
        if (gradeComparison == 0) {
            return nameComparison;
        }
        return gradeComparison;
    }

    @Override
    public String toString() {
        return name + " (Grade " + grade + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Artifact) {
            Artifact other = (Artifact) obj;
            return name.equals(other.getName()) && grade == other.getGrade();
        }
        return super.equals(obj);
    }
}
