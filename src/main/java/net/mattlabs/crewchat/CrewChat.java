package net.mattlabs.crewchat;

import co.aikar.commands.PaperCommandManager;
import github.scarsz.discordsrv.DiscordSRV;
import github.scarsz.discordsrv.dependencies.jda.api.requests.GatewayIntent;
import github.scarsz.discordsrv.dependencies.jda.api.utils.cache.CacheFlag;
import io.leangen.geantyref.TypeToken;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.format.TextColor;
import net.mattlabs.crewchat.commands.*;
import net.mattlabs.crewchat.listeners.ChatListener;
import net.mattlabs.crewchat.listeners.DiscordSRVListener;
import net.mattlabs.crewchat.listeners.JoinListener;
import net.mattlabs.crewchat.listeners.QuitListener;
import net.mattlabs.crewchat.messaging.Messages;
import net.mattlabs.crewchat.util.*;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;

public class CrewChat extends JavaPlugin{

    private static CrewChat instance;

    private ChannelManager channelManager;
    private PlayerManager playerManager;
    private MsgManager msgManager;
    private ConfigurateManager configurateManager;
    private PaperCommandManager paperCommandManager;

    private ChatSender chatSender;
    private MeSender meSender;

    private static Chat chat = null;
    private BukkitAudiences platform;
    private Messages messages;
    private Config config;
    private DiscordSRVListener discordSRVListener;

    private String version;
    private boolean discordSRVEnabled;

    public void onEnable() {
        instance = this;

        // Determine version
        version = Bukkit.getVersion();
        int start = version.indexOf("MC: ") + 4;
        int end = version.length() - 1;
        version = version.substring(start, end);

        if (Versions.versionCompare("1.8.0", version) >= 0) {
            getLogger().severe("You are running MC " + version + ". This plugin requires MC 1.8.0 or higher, disabling plugin...");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Register ACF
        paperCommandManager = new PaperCommandManager(this);

        // Vault Check
        if (!hasVault()) {
            this.getLogger().severe("Disabled due to no Vault dependency found!");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Vault Setup
        if (!setupChat()) {
            this.getLogger().severe("Disabled due to Vault Chat error!");
            this.getLogger().severe("Is there a permissions plugin installed?");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (!setupPermissions()) {
            this.getLogger().severe("Disabled due to Vault Permissions error!");
            this.getLogger().severe("Is there a permissions plugin installed?");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Configuration Section
        getLogger().info(LocalDateTime.now().toString());
        configurateManager = new ConfigurateManager();

        configurateManager.add("config.conf", TypeToken.get(Config.class), new Config(), Config::new,
                opts -> opts.serializers(build -> build.register(TextColor.class, TextColorSerializer.INSTANCE)));
        configurateManager.add("playerdata.conf", TypeToken.get(PlayerData.class), new PlayerData(), PlayerData::new,
                opts -> opts.serializers(build -> build.register(LocalDateTime.class, LocalDateTimeSerializer.INSTANCE)));
        configurateManager.add("messages.conf", TypeToken.get(Messages.class), new Messages(), Messages::new);

        configurateManager.saveDefaults("config.conf");
        configurateManager.saveDefaults("playerdata.conf");
        configurateManager.saveDefaults("messages.conf");

        configurateManager.load("config.conf");
        configurateManager.load("playerdata.conf");
        configurateManager.load("messages.conf");

        configurateManager.save("config.conf");
        configurateManager.save("playerdata.conf");
        configurateManager.save("messages.conf");

        config = configurateManager.get("config.conf");

        // Load Messages
        messages = configurateManager.get("messages.conf");
        getLogger().info("Messages on load: " + messages.toString());

        // DiscordSRV Check
        if (!hasDiscordSRV()) {
            this.getLogger().info("DiscordSRV disabled or not detected, disabling integration.");
            discordSRVEnabled = false;
        }
        else {
            this.getLogger().info("DiscordSRV detected, enabling integration.");
            discordSRVEnabled = true;
            DiscordSRV.api.requireIntent(GatewayIntent.GUILD_PRESENCES);
            DiscordSRV.api.requireCacheFlag(CacheFlag.ACTIVITY);
        }

        // Register Audience (Messages)
        platform = BukkitAudiences.create(this);

        // Load Channels
        channelManager = new ChannelManager();
        channelManager.loadChannels();

        // Load Players
        playerManager = new PlayerManager();
        playerManager.loadPlayers();
        playerManager.loadOnlinePlayers();

        // Load Private Messages
        msgManager = new MsgManager();

        // Load Senders
        chatSender = new ChatSender();
        meSender = new MeSender();

        // Register Listeners
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);

        // Register DiscordSRV Listener
        discordSRVListener = new DiscordSRVListener();
        if (discordSRVEnabled) DiscordSRV.api.subscribe(discordSRVListener);

        // ACF Registrations
        // Register Command Contexts
        paperCommandManager.getCommandContexts().registerContext(Channel.class, Channel.getContextResolver());
        // Register Commands
        paperCommandManager.registerCommand(new CrewChatCommand());
        paperCommandManager.registerCommand(new ChatCommand());
        paperCommandManager.registerCommand(new MeCommand());
        paperCommandManager.registerCommand(new MsgCommand());
        paperCommandManager.registerCommand(new ReplyCommand());

        // bStats
        new Metrics(this, 5799);

        this.getLogger().info("CrewChat loaded - By mattboy9921");
    }

    public void onDisable() {
    }

    public void reload() {
        getLogger().info("Reloading CrewChat...");
        configurateManager.reload();
        config = configurateManager.get("config.conf");
        messages = configurateManager.get("messages.conf");
        getLogger().info("Configuration reloaded.");
        channelManager.reloadChannels();
        getLogger().info("Channels reloaded.");
        playerManager.reloadPlayers();
        getLogger().info("Players reloaded.");
    }

    public ConfigurateManager getConfigurateManager() {
        return configurateManager;
    }

    public static CrewChat getInstance() {
        return instance;
    }

    public ChannelManager getChannelManager() {
        return channelManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public MsgManager getMsgManager() {
        return msgManager;
    }

    public ChatSender getChatSender() {
        return chatSender;
    }

    public MeSender getMeSender() {
        return meSender;
    }

    public boolean getDiscordSRVEnabled() {
        return discordSRVEnabled;
    }

    public static Chat getChat() {
        return chat;
    }

    public PaperCommandManager getPaperCommandManager() {
        return paperCommandManager;
    }

    public BukkitAudiences getPlatform() {
        return platform;
    }

    public String getVersion() {
        return version;
    }

    public Messages getMessages() {
        return messages;
    }

    public Config getConfigCC() {
        return config;
    }

    // Vault Helper Methods

    private boolean hasVault() {
        return getServer().getPluginManager().getPlugin("Vault") != null;
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp != null) {
            chat = rsp.getProvider();
            return true;
        }
        else return false;
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) {
            rsp.getProvider();
            return true;
        }
        else return false;
    }

    // DiscordSRV Helper Methods

    private boolean hasDiscordSRV() {
        return getServer().getPluginManager().getPlugin("DiscordSRV") != null && getConfigCC().isEnableDiscordSRV();
    }

    public void setDiscordConfigError() {
        this.getLogger().info("DiscordSRV config invalid, disabling integration.");
        DiscordSRV.api.unsubscribe(discordSRVListener);
        discordSRVEnabled = false;
    }
}
