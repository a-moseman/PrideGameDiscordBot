package PrideBot.Bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class PrideBot extends ListenerAdapter {
    private JDA jda;
    private BotModel botModel;
    private BotListener botListener;

    public PrideBot(String token, String savePath) {
        this.botModel = new BotModel(savePath);
        this.botListener = new BotListener(botModel);
        this.jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .build();
        this.jda.addEventListener(botListener);
    }
}
