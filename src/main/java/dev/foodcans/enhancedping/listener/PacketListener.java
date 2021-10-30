package dev.foodcans.enhancedping.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import dev.foodcans.enhancedping.EnhancedPing;
import dev.foodcans.enhancedping.ping.PingManager;
import dev.foodcans.enhancedping.ping.PingPlayer;
import dev.foodcans.enhancedping.ping.Pings;
import dev.foodcans.enhancedping.settings.Config;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PacketListener extends PacketAdapter
{
    private static final Random RANDOM = new Random();

    private PingManager pingManager;
    private ProtocolManager protocolManager;
    private Set<Long> sentPacketIds;
    private BukkitTask pingingTask;

    public PacketListener(EnhancedPing plugin, PingManager pingManager, ProtocolManager protocolManager)
    {
        super(plugin, ListenerPriority.HIGHEST, PacketType.Play.Client.KEEP_ALIVE);
        this.pingManager = pingManager;
        this.protocolManager = protocolManager;
        this.sentPacketIds = new HashSet<>();
    }

    @Override
    public void onPacketReceiving(PacketEvent event)
    {
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();
        long id = packet.getLongs().read(0);

        if (sentPacketIds.remove(id))
        {
            PingPlayer pingPlayer = pingManager.getPingPlayer(player.getUniqueId());
            if (pingManager.cancelTimeoutTask(pingPlayer))
            {
                Pings pings = pingPlayer.getPings();
                pings.updateLastPing();
            }
            event.setCancelled(true);
        }
    }

    public void start()
    {
        pingingTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> Bukkit.getOnlinePlayers().forEach(player ->
        {
            PingPlayer pingPlayer = pingManager.getPingPlayer(player.getUniqueId());
            Pings pings = pingPlayer.getPings();
            if (pings.isEmpty() || pings.hasReceivedLastPing())
            {
                // If the player has no pings we want to start sending them. Or if their last ping has been received then we want to send a new one
                PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.KEEP_ALIVE);
                long packetId = RANDOM.nextLong();
                while (sentPacketIds.contains(packetId))
                {
                    packetId = RANDOM.nextLong();
                }
                sentPacketIds.add(packetId);
                packet.getLongs().write(0, packetId);
                try
                {
                    protocolManager.sendServerPacket(player, packet);
                } catch (InvocationTargetException e)
                {
                    e.printStackTrace();
                }
                pings.addPing(System.currentTimeMillis());
                pingManager.startTimeoutTask(pingPlayer);
            }
        }), 0L, Config.PING_RATE);
    }

    public void stop()
    {
        pingingTask.cancel();
        pingingTask = null;
    }
}
