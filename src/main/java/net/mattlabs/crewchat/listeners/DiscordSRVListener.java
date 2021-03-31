package net.mattlabs.crewchat.listeners;

import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.api.Subscribe;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePostProcessEvent;
import github.scarsz.discordsrv.api.events.DiscordGuildMessagePreProcessEvent;
import github.scarsz.discordsrv.util.DiscordUtil;
import net.mattlabs.crewchat.CrewChat;
import net.mattlabs.crewchat.util.ChatSender;

public class DiscordSRVListener {

    private final ChatSender chatSender = CrewChat.getInstance().getChatSender();

    @Subscribe
    public void discordMessageReceivedPre(DiscordGuildMessagePreProcessEvent event) {
        CrewChat.getInstance().getLogger().info("Discord pre: " + event.getMessage());
        CrewChat.getInstance().getLogger().info("Discord pre: " + event.getMessage().getContentDisplay());
        CrewChat.getInstance().getLogger().info("Discord pre: " + event.getAuthor().getName());
        CrewChat.getInstance().getLogger().info("Discord pre: " + event.getMember().getEffectiveName());
        CrewChat.getInstance().getLogger().info("Discord pre: #" + Integer.toHexString(event.getMember().getColor().getRGB()).substring(2));
        CrewChat.getInstance().getLogger().info("Discord pre: " + event.getChannel().getName());
        CrewChat.getInstance().getLogger().info("Discord pre: " + event.getChannel().getId());
        CrewChat.getInstance().getLogger().info("Discord pre: " + DiscordSRV.getPlugin().getDestinationGameChannelNameForTextChannel(event.getChannel()));
        chatSender.sendDiscordMessage(event.getMember(), event.getChannel(), event.getMessage().getContentDisplay());
    }

    @Subscribe
    public void discordMessageReceivedPost(DiscordGuildMessagePostProcessEvent event) {
        CrewChat.getInstance().getLogger().info("Discord post: " + event.getMessage());
        event.setCancelled(true);
    }
}
