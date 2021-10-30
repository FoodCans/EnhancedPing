package dev.foodcans.enhancedping;

import dev.foodcans.enhancedping.ping.PingManager;
import org.bukkit.entity.Player;

public class PingAPI
{
    private static PingManager pingManager;

    static void init(PingManager pingManager1)
    {
        pingManager = pingManager1;
    }

    public static long getPing(Player player)
    {
        return pingManager.getPingPlayer(player.getUniqueId()).getPings().getPing();
    }

    public static boolean isShowingPingBar(Player player)
    {
        return pingManager.isShowing(player.getUniqueId());
    }

    public static void setShowingPingBar(Player player, boolean showing)
    {
        pingManager.setShowing(player.getUniqueId(), showing);
    }
}
