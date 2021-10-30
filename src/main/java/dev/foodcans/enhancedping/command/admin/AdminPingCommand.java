package dev.foodcans.enhancedping.command.admin;

import dev.foodcans.enhancedping.PingAPI;
import dev.foodcans.enhancedping.ping.PingValue;
import dev.foodcans.enhancedping.settings.lang.Lang;
import dev.foodcans.pluginutils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;

public class AdminPingCommand extends SubCommand
{
    public AdminPingCommand()
    {
        super("ping", "enhancedping.command.admin.ping", Collections.singletonList("<player>"));
    }

    @Override
    public void onCommand(CommandSender sender, String... args)
    {
        String playerName = args[0];
        Player player = Bukkit.getPlayer(playerName);
        if (player == null)
        {
            Lang.PLAYER_NOT_FOUND.sendMessage(sender, playerName);
            return;
        }

        long ping = PingAPI.getPing(player);
        PingValue pingValue = PingValue.ofPing(ping);
        Lang.PING_FORMAT_ADMIN.sendMessage(sender, player.getName(),
                pingValue.getHexColor() + Long.toString(ping));
    }

    @Override
    public int getMinArgs()
    {
        return 1;
    }

    @Override
    public int getMaxArgs()
    {
        return 1;
    }

    @Override
    public boolean allowConsoleSender()
    {
        return true;
    }
}
