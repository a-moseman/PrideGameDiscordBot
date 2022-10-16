package PrideBot.Concepts.Experimentation;

public class Testing {
    public static void main(String[] args) {
        int pride = 0;
        int ego = 0;
        int[] daysInEgo = new int[100];
        for (int day = 0; day < 100; day++) {
            pride += 1 + ego;
            while (pride >= (ego + 1) * 10) {
                ego++;
                pride -= ego * 10;
            }
            daysInEgo[ego]++;
            System.out.println(pride+","+ego);
        }
        for (int i = 0; i < daysInEgo.length; i++) {
            System.out.print(daysInEgo[i] + ", ");
        }
        System.out.println();

        pride = 0;
        ego = 0;
        int days = 0;
        while (ego < 10) {
            pride += 1 + ego;
            while (pride >= (ego + 1) * 10) {
                ego++;
                pride -= ego * 10;
            }
            days++;
        }
        System.out.println("Days to get 1 honor: " + days);

    }
}
