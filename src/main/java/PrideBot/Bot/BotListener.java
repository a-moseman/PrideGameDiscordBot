package PrideBot.Bot;

import PrideBot.ARS.AutoResponseSystem;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class BotListener extends ListenerAdapter {
    private AutoResponseSystem ars;

    private BotModel botModel;

    // variables for onMessageReceived
    private Guild guild;
    private User author;
    private Message message;
    private String content;
    private MessageChannel channel;

    public BotListener(BotModel botModel) {
        this.botModel = botModel;
        this.ars = new AutoResponseSystem();
    }

    private boolean isPrideBotAdmin(User user) {
        guild.loadMembers(); // load the members of the guild into the cache
        List<Role> roles = guild.getRolesByName("pride_dm", false);
        if (roles.size() == 0) { // guild does not have pride_dm role
            return false;
        }
        List<Member> admins = guild.getMembersWithRoles(roles);
        for (Member member : admins) {
            if (user.getId().equals(member.getId())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        guild = event.getGuild();
        author = event.getAuthor();
        message = event.getMessage();
        content = message.getContentRaw();
        channel = event.getChannel();
        if (author.isBot()) {
            return;
        }
        if (!botModel.doesPlayerExist(author.getId())) {
            botModel.addNewPlayer(author.getId());
        }
        if (content.startsWith("p>")) {
            Command command = new Command(author, content.substring(2));
            Response response = botModel.process(command, guild, isPrideBotAdmin(author));
            sendResponse(channel, response);
        }
        Response arsResponse = ars.process(guild, channel, author, message);
        if (arsResponse != null) {
            sendResponse(channel, arsResponse);
        }
    }

    private void sendResponse(MessageChannel channel, Response response) {
        if (response.TARGET_UUID != null) {
            channel.sendMessage("<@" + response.TARGET_UUID + "> \n" + response.MESSAGES[0]).queue();
        }
        for (int i = 0; i < response.MESSAGES.length; i++) {
            channel.sendMessage(response.MESSAGES[i]).queue();
        }
    }

    @Override
    public void onGenericEvent(@NotNull GenericEvent event) {
        botModel.save();
    }
}
