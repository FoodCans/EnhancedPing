package dev.foodcans.enhancedping.ping;

import com.google.common.collect.EvictingQueue;

import java.util.Arrays;

public class PingBar
{
    private EvictingQueue<PingValue> pingValues;

    public PingBar()
    {
        PingValue[] pingValues = new PingValue[7];
        Arrays.fill(pingValues, PingValue.EXCELLENT);
        this.pingValues = EvictingQueue.create(7);
        this.pingValues.addAll(Arrays.asList(pingValues));
    }

    public void addPing(long ping)
    {
        pingValues.add(PingValue.ofPing(ping));
    }

    public String build()
    {
        StringBuilder builder = new StringBuilder();
        for (PingValue pingValue : pingValues)
        {
            builder.append(pingValue.getHexColor()).append(pingValue.getUnicode());
        }
        return builder.toString();
    }
}
