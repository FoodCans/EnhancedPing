package dev.foodcans.enhancedping.command;

import dev.foodcans.enhancedping.PingAPI;
import dev.foodcans.enhancedping.ping.PingValue;
import dev.foodcans.enhancedping.settings.lang.Lang;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PingCommand implements CommandExecutor
{
    private static final String PERMISSION = "enhancedping.command.ping";

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
        long ping = PingAPI.getPing(player);
        PingValue pingValue = PingValue.ofPing(ping);
        Lang.PING_FORMAT.sendMessage(player, pingValue.getColor() + Long.toString(ping));
        return true;
    }
}
