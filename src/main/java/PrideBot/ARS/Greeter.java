package PrideBot.ARS;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.Calendar;

public class Greeter implements ResponseGenerator {
    private final static int MORNING_START = 5;
    private final static int MORNING_END = 12;
    private final static int AFTERNOON_START = 12;
    private final static int AFTERNOON_END = 18;
    private final static int EVENING_START = 18;
    private final static int EVENING_END = 21;
    private final static double PROBABILITY_OF_TIME_BASED_GREET = 0.5;

    private final static String[] GENERIC_GREETING_PREFIXES = {
            "Hi ",
            "Hello ",
            "Long time no see ",
            "How's it going ",
            "Howdy "
    };
    private final static String[] MORNING_GREETING_PREFIXES = {
            "Mornin' ",
            "Morning ",
            "Good morning ",
            "Good mornin' ",
    };
    private final static String[] AFTERNOON_GREETING_PREFIXES = {
            "Good afternoon "
    };
    private final static String[] EVENING_GREETING_PREFIXES = {
            "Good evening "
    };
    private final static String[] GREETING_AFFIXES = {
            "!",
            " ^w^",
            "."
    };

    private Calendar calendar;

    public Greeter() {
        this.calendar = Calendar.getInstance();
    }

    private String buildGenericGreet(String username) {
        return
                Util.getRandom(GENERIC_GREETING_PREFIXES) +
                username +
                Util.getRandom(GREETING_AFFIXES);
    }

    private String buildMorningGreet(String username) {
        return
                Util.getRandom(MORNING_GREETING_PREFIXES) +
                username +
                Util.getRandom(GREETING_AFFIXES);
    }

    private String buildAfternoonGreet(String username) {
        return
                Util.getRandom(AFTERNOON_GREETING_PREFIXES) +
                username +
                Util.getRandom(GREETING_AFFIXES);
    }

    private String buildEveningGreet(String username) {
        return
                Util.getRandom(EVENING_GREETING_PREFIXES) +
                        username +
                        Util.getRandom(GREETING_AFFIXES);
    }

    @Override
    public String generate(Guild guild, MessageChannel channel, User author, Message message) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (Util.RANDOM.nextDouble() < PROBABILITY_OF_TIME_BASED_GREET) {
            if (hour > MORNING_START && hour < MORNING_END) {
                return buildMorningGreet(author.getName());
            }
            else if (hour > AFTERNOON_START && hour < AFTERNOON_END) {
                return buildAfternoonGreet(author.getName());
            }
            else if (hour > EVENING_START && hour < EVENING_END) {
                return buildEveningGreet(author.getName());
            }
        }
        return buildGenericGreet(author.getName());
    }
}
