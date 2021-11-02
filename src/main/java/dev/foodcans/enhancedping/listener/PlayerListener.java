package dev.foodcans.enhancedping.listener;

import dev.foodcans.enhancedping.ping.PingManager;
import dev.foodcans.enhancedping.settings.Config;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

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
        UUID uuid = event.getPlayer().getUniqueId();
        pingManager.addPingPlayer(uuid);
        pingManager.getStorage().fetchShowing(uuid, showing -> pingManager.setShowing(uuid, showing));
    }
}
