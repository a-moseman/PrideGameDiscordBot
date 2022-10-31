package PrideBot.Bot;

import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;

public class Command {
    private User author;
    private ArrayList<String> terms;

    public Command(User author, String raw) {
        this.author = author;
        convertRawToTerms(raw);
    }

    private void convertRawToTerms(String raw) {
        // TODO: optimize
        terms = new ArrayList<>();
        int i;
        char ch;
        boolean inQuotes = false;
        StringBuilder term = new StringBuilder();
        char[] arr = raw.toCharArray();
        for (i = 0; i < arr.length; i++) {
            ch = arr[i];
            if (ch == '"') {
                if (inQuotes) {
                    terms.add(term.toString());
                    term = new StringBuilder();
                }
                inQuotes = !inQuotes;
            }
            else {
                if (inQuotes) {
                    term.append(ch);
                }
                else {
                    if (ch == ' ') {
                        if (!term.toString().equals("")) {
                            terms.add(term.toString());
                            term = new StringBuilder();
                        }
                    }
                    else {
                        term.append(ch);
                    }
                }
            }
        }
    }

    public User getAuthor() {
        return author;
    }

    public int getSize() {
        return terms.size();
    }

    public String getTerm(int index) {
        return terms.get(index);
    }

    /**
     * Test command class.
     * Last Runtime = ~50000ns-110000ns
     */
    public static void main(String[] args) {
        long start = System.nanoTime();
        Command c1 = new Command(null, "p>describe \"DA FOIN\"");
        Command c2 = new Command(null, "p>a \"b c\" \"d e\"");
        long end = System.nanoTime();
        boolean test = c1.getTerm(0).equals("p>describe") &&
                c1.getTerm(1).equals("DA FOIN") &&
                c2.getTerm(0).equals("p>a") &&
                c2.getTerm(1).equals("b c") &&
                //c2.getTerm(2).equals("c") &&
                c2.getTerm(2).equals("d e");
        System.out.println("Test: " + (test ? " success" : " failure"));
        System.out.println("Runtime: " + (end - start) + " ns");

        for (int i = 0; i < c1.getSize(); i++) {
            System.out.println("> " + c1.getTerm(i));
        }
        for (int i = 0; i < c2.getSize(); i++) {
            System.out.println("> " + c2.getTerm(i));
        }
    }
}
