package dev.foodcans.enhancedping;

import dev.foodcans.enhancedping.ping.PingManager;
import org.bukkit.entity.Player;

public class PingAPI
{
    private static PingManager pingManager;

    public static long getPing(Player player)
    {
        return pingManager.getPingPlayer(player.getUniqueId()).getPings().getPing();
    }

    static void init(PingManager pingManager1)
    {
        pingManager = pingManager1;
    }
}
