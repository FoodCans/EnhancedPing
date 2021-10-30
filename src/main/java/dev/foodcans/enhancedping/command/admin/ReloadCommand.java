package dev.foodcans.enhancedping.command.admin;

import dev.foodcans.enhancedping.EnhancedPing;
import dev.foodcans.enhancedping.ping.PingManager;
import dev.foodcans.enhancedping.settings.Config;
import dev.foodcans.enhancedping.settings.lang.Lang;
import dev.foodcans.pluginutils.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.Collections;

public class ReloadCommand extends SubCommand
{
    private final PingManager pingManager;

    public ReloadCommand(PingManager pingManager)
    {
        super("reload", "enhancedping.command.admin.reload", Collections.emptyList());
        this.pingManager = pingManager;
    }

    @Override
    public void onCommand(CommandSender sender, String... args)
    {
        EnhancedPing.getInstance().reloadConfig();
        EnhancedPing.getInstance().getLangFile().reload();
        Config.load(EnhancedPing.getInstance().getConfig());
        pingManager.stopShowPingTask();
        pingManager.startShowPingTask();
        Lang.CONFIG_RELOADED.sendMessage(sender);
    }

    @Override
    public int getMinArgs()
    {
        return 0;
    }

    @Override
    public int getMaxArgs()
    {
        return 0;
    }

    @Override
    public boolean allowConsoleSender()
    {
        return true;
    }
}
