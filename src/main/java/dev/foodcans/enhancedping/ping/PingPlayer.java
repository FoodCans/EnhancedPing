package dev.foodcans.enhancedping.ping;

import java.util.UUID;

public class PingPlayer
{
    private final UUID uuid;
    private final Pings pings;

    public PingPlayer(UUID uuid)
    {
        this.uuid = uuid;
        this.pings = new Pings();
    }

    public UUID getUuid()
    {
        return uuid;
    }

    public Pings getPings()
    {
        return pings;
    }
}
