package PrideBot.Bot;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BotListener extends ListenerAdapter {
    private BotModel botModel;
    private HashMap<String, ArrayList<String>> prideBotAdmins;
    private ArrayList<String> guildsDetected;

    // variables for onMessageReceived
    private Guild guild;
    private User author;
    private Message message;
    private String content;
    private MessageChannel channel;


    public BotListener(BotModel botModel) {
        this.botModel = botModel;
        this.prideBotAdmins = new HashMap<>();
        this.guildsDetected = new ArrayList<>();
    }

    private void updatePrideBotAdmins() {
        guild.loadMembers(); // load the members of the guild into the cache
        prideBotAdmins.put(guild.getId(), new ArrayList<>());
        List<Role> roles = guild.getRolesByName("pride_dm", false);
        if (roles.size() == 0) { // guild does not have pride_dm role
            return;
        }
        List<Member> admins = guild.getMembersWithRoles(roles);
        for (Member member : admins) {
            prideBotAdmins.get(guild.getId()).add(member.getId());
        }
    }

    private void updateNames(Guild guild) {
        List<Member> members = guild.getMembers();
        for (Member member : members) {
            if (member.getUser().isBot()) {
                continue;
            }
            if (!botModel.doesPlayerExist(member.getId())) {
                botModel.addNewPlayer(member.getId());
            }
            botModel.updateName(member.getId(), member.getUser().getName());
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        super.onGuildMemberJoin(event);
        if (event.getUser().isBot()) {
            return;
        }
        botModel.addNewPlayer(event.getUser().getId());
        botModel.updateName(event.getUser().getId(), event.getUser().getName());
    }

    @Override
    public void onUserUpdateName(UserUpdateNameEvent event) {
        super.onUserUpdateName(event);
        if (event.getUser().isBot()) {
            return;
        }
        botModel.updateName(event.getUser().getId(), event.getNewName());
    }

    @Override
    public void onGuildMemberRoleAdd(GuildMemberRoleAddEvent event) {
        super.onGuildMemberRoleAdd(event);
        System.out.println("Member Role Change Detected");
        guild = event.getGuild();
        updatePrideBotAdmins();
    }

    @Override
    public void onGuildMemberRoleRemove(GuildMemberRoleRemoveEvent event) {
        super.onGuildMemberRoleRemove(event);
        System.out.println("Member Role Change Detected");
        guild = event.getGuild();
        updatePrideBotAdmins();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        guild = event.getGuild();
        author = event.getAuthor();
        message = event.getMessage();
        content = message.getContentRaw();
        channel = event.getChannel();

        if (!guildsDetected.contains(guild.getId())) {
            updatePrideBotAdmins();
            updateNames(guild);
            guildsDetected.add(guild.getId());
        }

        if (author.isBot()) {
            return;
        }
        if (!botModel.doesPlayerExist(author.getId())) {
            botModel.addNewPlayer(author.getId());
        }
        if (content.startsWith("p>")) {
            Command command = new Command(author, content.substring(2));
            boolean isPrideBotAdmin = prideBotAdmins.get(guild.getId()).contains(author.getId());
            Response response = botModel.process(command, guild, isPrideBotAdmin);
            sendResponse(channel, response);
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
