package dev.foodcans.enhancedping.ping;

import dev.foodcans.enhancedping.EnhancedPing;
import dev.foodcans.enhancedping.PingAPI;
import dev.foodcans.enhancedping.settings.Config;
import dev.foodcans.enhancedping.storage.IStorage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class PingManager
{
    private final EnhancedPing plugin;
    private final IStorage storage;

    private Map<UUID, PingPlayer> pingPlayerMap;
    private Map<UUID, BukkitTask> taskMap;
    private Set<UUID> showing; // TODO NEed to rework this completely.
    private BukkitTask showPingTask;

    public PingManager(EnhancedPing plugin, IStorage storage)
    {
        this.plugin = plugin;
        this.storage = storage;
        this.pingPlayerMap = new HashMap<>();
        this.taskMap = new HashMap<>();
        this.showing = new HashSet<>();
    }

    public void addPingPlayer(UUID uuid)
    {
        pingPlayerMap.remove(uuid);
        pingPlayerMap.put(uuid, new PingPlayer(uuid));
    }

    public void startTimeoutTask(PingPlayer pingPlayer)
    {
        cancelTimeoutTask(pingPlayer);
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () ->
        {
            pingPlayer.getPings().updateLastPing();
            taskMap.remove(pingPlayer.getUuid());
        }, Config.PING_REQUEST_TIMEOUT);
        taskMap.put(pingPlayer.getUuid(), task);
    }

    public boolean cancelTimeoutTask(PingPlayer pingPlayer)
    {
        if (taskMap.containsKey(pingPlayer.getUuid()))
        {
            taskMap.get(pingPlayer.getUuid()).cancel();
            return true;
        }
        return false;
    }

    public void startShowPingTask()
    {
        showPingTask = Bukkit.getScheduler().runTaskTimer(plugin, () ->
        {
            for (PingPlayer pingPlayer : pingPlayerMap.values())
            {
                if (isShowing(pingPlayer.getUuid()))
                {
                    Player player = Bukkit.getPlayer(pingPlayer.getUuid());
                    if (player == null)
                    {
                        continue;
                    }

                    // TODO Format ping bar
                    long ping = PingAPI.getPing(player);
                    PingValue pingValue = PingValue.ofPing(ping);
                    TextComponent component = new TextComponent(pingPlayer.getPings().getPingBar().build());
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                }
            }
        }, 0L, Config.PING_BAR_RATE);
    }

    public void stopShowPingTask()
    {
        if (showPingTask != null)
        {
            showPingTask.cancel();
            showPingTask = null;
        }
    }

    public boolean isShowing(UUID uuid)
    {
        return showing.contains(uuid);
    }

    public void setShowing(UUID uuid, boolean showing)
    {
        if (showing)
        {
            this.showing.add(uuid);
        } else
        {
            this.showing.remove(uuid);
        }
        storage.setShowing(uuid, showing);
    }

    public PingPlayer getPingPlayer(UUID uuid)
    {
        return pingPlayerMap.get(uuid);
    }
}
