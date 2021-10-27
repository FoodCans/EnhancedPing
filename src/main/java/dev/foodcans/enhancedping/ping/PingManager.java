package dev.foodcans.enhancedping.ping;

import dev.foodcans.enhancedping.EnhancedPing;
import dev.foodcans.enhancedping.PingAPI;
import dev.foodcans.enhancedping.settings.Config;
import dev.foodcans.enhancedping.settings.lang.Lang;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static dev.foodcans.pluginutils.PluginUtils.Strings.translateColor;

public class PingManager
{
    private final EnhancedPing plugin;

    private Map<UUID, PingPlayer> pingPlayerMap;
    private Map<UUID, BukkitTask> taskMap;
    private BukkitTask highPingTask;

    public PingManager(EnhancedPing plugin)
    {
        this.plugin = plugin;
        this.pingPlayerMap = new HashMap<>();
        this.taskMap = new HashMap<>();
    }

    public void addPingPlayer(UUID uuid)
    {
        pingPlayerMap.putIfAbsent(uuid, new PingPlayer(uuid));
    }

    public void startTimeoutTask(PingPlayer pingPlayer)
    {
        cancelTimeoutTask(pingPlayer);
        BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, () -> pingPlayer.getPings().updateLastPing(),
                Config.PING_REQUEST_TIMEOUT);
        taskMap.put(pingPlayer.getUuid(), task);
    }

    public void cancelTimeoutTask(PingPlayer pingPlayer)
    {
        if (taskMap.containsKey(pingPlayer.getUuid()))
        {
            taskMap.get(pingPlayer.getUuid()).cancel();
        }
    }

    public void startHighPingTask()
    {
        highPingTask = Bukkit.getScheduler().runTaskTimer(plugin, () ->
        {
            for (PingPlayer pingPlayer : pingPlayerMap.values())
            {
                if (pingPlayer.getPings().getPing() > Config.HIGH_PING_THRESHOLD)
                {
                    Player player = Bukkit.getPlayer(pingPlayer.getUuid());
                    if (player == null)
                    {
                        continue;
                    }
                    long ping = PingAPI.getPing(player);
                    PingValue pingValue = PingValue.ofPing(ping);
                    TextComponent component =
                            new TextComponent(
                                    translateColor(Lang.HIGH_PING_FORMAT.getMessage(pingValue + Long.toString(ping))));
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
                }
            }
        }, 0L, Config.HIGH_PING_RATE);
    }

    public void stopHighPingTask()
    {
        if (highPingTask != null)
        {
            highPingTask.cancel();
            highPingTask = null;
        }
    }

    public PingPlayer getPingPlayer(UUID uuid)
    {
        return pingPlayerMap.get(uuid);
    }
}
