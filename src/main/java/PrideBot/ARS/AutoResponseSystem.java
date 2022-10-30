package PrideBot.ARS;

import PrideBot.Bot.Response;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.HashMap;

public class AutoResponseSystem {
    private final static double TWO_DAY_GREET_PROBABILITY = 0.10;
    private final static double WEEK_GREET_PROBABILITY = 0.5;

    private ResponseGenerator greeter;

    private HashMap<String, Long> playersLastSeenTimes;

    public AutoResponseSystem() {
        this.greeter = new Greeter();
        this.playersLastSeenTimes = new HashMap<>();
    }

    public Response process(Guild guild, MessageChannel channel, User author, Message message) {
        Response response = null;
        if (playersLastSeenTimes.containsKey(author.getId())) {
            if (millisToDays(playersLastSeenTimes.get(author.getId())) >= 7) { // if it has been a week since last seen
                if (Util.getDouble() < WEEK_GREET_PROBABILITY) {
                    response = buildResponse(greeter.generate(guild, channel, author, message));
                }
            }
            if (millisToDays(playersLastSeenTimes.get(author.getId())) >= 2) { // if it has been two days since last seen
                if (Util.getDouble() < TWO_DAY_GREET_PROBABILITY) {
                    response =  buildResponse(greeter.generate(guild, channel, author, message));
                }
            }
        }
        playersLastSeenTimes.put(author.getId(), System.currentTimeMillis());
        return response;
    }

    private Response buildResponse(String content) {
        return new Response(content);
    }

    private double millisToDays(long ms) {
        return (double) ms / 1000 / 60/ 60 / 24;
    }
}
