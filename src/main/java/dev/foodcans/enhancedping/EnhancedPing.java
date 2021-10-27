package dev.foodcans.enhancedping;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.foodcans.enhancedping.command.PingCommand;
import dev.foodcans.enhancedping.command.admin.AdminPingCommand;
import dev.foodcans.enhancedping.command.admin.ReloadCommand;
import dev.foodcans.enhancedping.hook.PlaceholderAPIHook;
import dev.foodcans.enhancedping.listener.PacketListener;
import dev.foodcans.enhancedping.listener.PlayerListener;
import dev.foodcans.enhancedping.ping.PingManager;
import dev.foodcans.enhancedping.settings.Config;
import dev.foodcans.enhancedping.settings.lang.Lang;
import dev.foodcans.enhancedping.settings.lang.LangFile;
import dev.foodcans.pluginutils.command.FailureReason;
import dev.foodcans.pluginutils.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class EnhancedPing extends JavaPlugin
{
    private static EnhancedPing instance;

    private ProtocolManager protocolManager;
    private PacketListener packetListener;
    private LangFile langFile;

    public static EnhancedPing getInstance()
    {
        return instance;
    }

    @Override
    public void onLoad()
    {
        instance = this;
        saveDefaultConfig();
        Config.load(getConfig());
        this.langFile = new LangFile(getDataFolder(), "lang.yml");
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable()
    {
        PingManager pingManager = new PingManager(this);
        setupListeners(pingManager);
        setupCommands(pingManager);
        pingManager.startHighPingTask();

        setupPlaceholder();

        PingAPI.init(pingManager);
    }

    @Override
    public void onDisable()
    {
        this.packetListener.stop();
        this.protocolManager.removePacketListener(this.packetListener);
    }

    private void setupListeners(PingManager pingManager)
    {
        this.packetListener = new PacketListener(this, pingManager, this.protocolManager);
        this.protocolManager.addPacketListener(this.packetListener);
        this.packetListener.start();
        getServer().getPluginManager().registerEvents(new PlayerListener(pingManager), this);
    }

    private void setupCommands(PingManager pingManager)
    {
        getCommand("ping").setExecutor(new PingCommand());
        PluginCommand pingAdminCommand = new PluginCommand((failureReason, sender, replacements) ->
        {
            if (failureReason == FailureReason.NO_ARGS)
            {
                Lang.LIST_COMMANDS.sendMessage(sender);
            } else if (failureReason == FailureReason.COMMAND_NOT_FOUND)
            {
                Lang.COMMAND_NOT_FOUND.sendMessage(sender);
            } else if (failureReason == FailureReason.NO_PERMISSION)
            {
                Lang.NO_PERMISSION_COMMAND.sendMessage(sender, replacements[0]);
            } else if (failureReason == FailureReason.NOT_ENOUGH_ARGS)
            {
                Lang.NOT_ENOUGH_ARGS.sendMessage(sender, replacements[0]);
            } else if (failureReason == FailureReason.TOO_MANY_ARGS)
            {
                Lang.TOO_MANY_ARGS.sendMessage(sender, replacements[0]);
            } else if (failureReason == FailureReason.ONLY_PLAYERS)
            {
                Lang.COMMAND_ONLY_RUN_BY_PLAYERS.sendMessage(sender);
            } else if (failureReason == FailureReason.HELP)
            {
                Lang.HELP.sendMessage(sender, replacements[0]);
            } else if (failureReason == FailureReason.HELP_NONE)
            {
                Lang.HELP_NONE.sendMessage(sender);
            }
        });
        pingAdminCommand.registerSubCommand(new ReloadCommand(pingManager), new AdminPingCommand());
        getCommand("pingadmin").setExecutor(pingAdminCommand);
    }

    private void setupPlaceholder()
    {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null)
        {
            new PlaceholderAPIHook().register();
        }
    }

    public LangFile getLangFile()
    {
        return langFile;
    }
}
