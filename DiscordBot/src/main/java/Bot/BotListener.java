package Bot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;

public class BotListener extends ListenerAdapter {
    private BotModel botModel;

    // variables for onMessageReceived
    private Guild guild;
    private User author;
    private Message message;
    private String content;
    private MessageChannel channel;

    public BotListener(BotModel botModel) {
        this.botModel = botModel;
    }

    private boolean isPrideBotAdmin(User user) {
        guild.loadMembers(); // TODO: check is needed
        List<Member> admins = guild.getMembersWithRoles(guild.getRolesByName("PrideBotAdmin", false));
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
        if (!botModel.playerExists(author.getId())) {
            botModel.addNewPlayer(author.getId());
        }
        if (content.startsWith("p>")) {
            Command command = new Command(author, content.substring(2));
            Response response = botModel.process(command, isPrideBotAdmin(author));
            sendResponse(channel, response);
        }
    }

    private void sendResponse(MessageChannel channel, Response response) {
        if (response.TARGET_UUID != null) {
            channel.sendMessage("<@" + response.TARGET_UUID + "> \n" + response.MESSAGES[0]).queue();
        }
        for (int i = 1; i < response.MESSAGES.length; i++) {
            channel.sendMessage(response.MESSAGES[i]).queue();
        }
    }
}
