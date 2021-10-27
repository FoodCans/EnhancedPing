package dev.foodcans.enhancedping.listener;

import dev.foodcans.enhancedping.ping.PingManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerListener implements Listener
{
    private PingManager pingManager;

    public PlayerListener(PingManager pingManager)
    {
        this.pingManager = pingManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
        pingManager.addPingPlayer(event.getPlayer().getUniqueId());
    }
}
