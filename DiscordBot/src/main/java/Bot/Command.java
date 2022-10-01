package Bot;

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
        String term = "";
        for (i = 0; i < raw.length(); i++) {
            ch = raw.charAt(i);
            if (ch == '"') {
                if (inQuotes) {
                    terms.add(term);
                    term = "";
                }
                else {
                    inQuotes = true;
                }
            }
            else {
                if (inQuotes) {
                    term += ch;
                }
                else {
                    if (ch == ' ') {
                        terms.add(term);
                        term = "";
                    }
                    else {
                        term += ch;
                    }
                }
            }
        }
        terms.add(term);
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
}
