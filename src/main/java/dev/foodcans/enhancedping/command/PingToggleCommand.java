package dev.foodcans.enhancedping.command;

import dev.foodcans.enhancedping.PingAPI;
import dev.foodcans.enhancedping.settings.lang.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingToggleCommand implements CommandExecutor
{
    private static final String PERMISSION = "enhancedping.command.pingtoggle";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            Lang.COMMAND_ONLY_RUN_BY_PLAYERS.sendMessage(sender);
            return true;
        }
        Player player = (Player) sender;
        if (!player.hasPermission(PERMISSION))
        {
            Lang.NO_PERMISSION_COMMAND.sendMessage(player, PERMISSION);
            return true;
        }
        boolean toggledShowing = !PingAPI.isShowingPingBar(player);
        PingAPI.setShowingPingBar(player, toggledShowing);
        if (toggledShowing)
        {
            Lang.TOGGLED_SHOWING_ON.sendMessage(player);
        } else
        {
            Lang.TOGGLED_SHOWING_OFF.sendMessage(player);
        }
        return true;
    }
}
