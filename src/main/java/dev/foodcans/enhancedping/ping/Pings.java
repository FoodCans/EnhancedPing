package dev.foodcans.enhancedping.ping;

import dev.foodcans.enhancedping.settings.Config;

import java.util.ArrayList;
import java.util.List;

public class Pings
{
    private final List<Ping> pings;

    public Pings()
    {
        this.pings = new ArrayList<>();
    }

    public void addPing(long timeSent)
    {
        pings.add(new Ping(timeSent));
        if (pings.size() > Config.MAX_PINGS)
        {
            pings.remove(0);
        }
    }

    public void clearPings()
    {
        pings.clear();
    }

    public boolean isEmpty()
    {
        return pings.isEmpty();
    }

    public boolean hasReceivedLastPing()
    {
        return !pings.isEmpty() && pings.get(pings.size() - 1).hasReceived();
    }

    public void updateLastPing()
    {
        if (!pings.isEmpty())
        {
            Ping ping = pings.get(pings.size() - 1);
            ping.received();
        }
    }

    public long getPing()
    {
        if (pings.isEmpty())
        {
            return -1;
        }
        long size = 0;
        long total = 0;
        for (Ping ping : pings)
        {
            if (ping.hasReceived())
            {
                size++;
                total += ping.getPing();
            }
        }
        return total / size;
    }

    private static class Ping
    {
        private final Long timeSent;
        private Long timeReceived;

        public Ping(long timeSent)
        {
            this.timeSent = timeSent;
        }

        public boolean hasReceived()
        {
            return timeReceived != null;
        }

        public void received()
        {
            if (timeReceived == null)
            {
                timeReceived = System.currentTimeMillis();
            }
        }

        public long getPing()
        {
            if (hasReceived())
            {
                return timeReceived - timeSent;
            }
            return -1;
        }
    }
}
