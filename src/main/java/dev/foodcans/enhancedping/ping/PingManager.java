package dev.foodcans.enhancedping.ping;

import dev.foodcans.enhancedping.EnhancedPing;
import dev.foodcans.enhancedping.PingAPI;
import dev.foodcans.enhancedping.settings.Config;
import dev.foodcans.enhancedping.settings.lang.Lang;
import dev.foodcans.enhancedping.storage.IStorage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PingManager
{
    private final EnhancedPing plugin;
    private final IStorage storage;

    private Map<UUID, PingPlayer> pingPlayerMap;
    private Map<UUID, BukkitTask> taskMap;
    private Map<UUID, Boolean> showingMap;
    private BukkitTask showPingTask;

    public PingManager(EnhancedPing plugin, IStorage storage)
    {
        this.plugin = plugin;
        this.storage = storage;
        this.pingPlayerMap = new HashMap<>();
        this.taskMap = new HashMap<>();
        this.showingMap = new HashMap<>();
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

                    long ping = PingAPI.getPing(player);
                    PingGrade pingGrade = PingGrade.ofPing(ping);
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(
                            Lang.PING_BAR_FORMAT.getMessage(pingGrade.getHexColor() + Long.toString(ping),
                                    pingPlayer.getPings().getPingBar().build())));

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
        return showingMap.getOrDefault(uuid, false);
    }

    public void setShowing(UUID uuid, boolean showing)
    {
        this.showingMap.put(uuid, showing);
        storage.setShowing(uuid, showing);
    }

    public IStorage getStorage()
    {
        return storage;
    }

    public PingPlayer getPingPlayer(UUID uuid)
    {
        return pingPlayerMap.get(uuid);
    }
}
