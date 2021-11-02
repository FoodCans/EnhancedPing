package dev.foodcans.enhancedping.ping;

import com.google.common.collect.EvictingQueue;

import java.util.Arrays;

public class PingBar
{
    private EvictingQueue<PingGrade> pingGrades;

    public PingBar()
    {
        PingGrade[] pingGrades = new PingGrade[7];
        Arrays.fill(pingGrades, PingGrade.EXCELLENT);
        this.pingGrades = EvictingQueue.create(7);
        this.pingGrades.addAll(Arrays.asList(pingGrades));
    }

    public void addPing(long ping)
    {
        pingGrades.add(PingGrade.ofPing(ping));
    }

    public String build()
    {
        StringBuilder builder = new StringBuilder();
        for (PingGrade pingGrade : pingGrades)
        {
            builder.append(pingGrade.getHexColor()).append(pingGrade.getUnicode());
        }
        return builder.toString();
    }
}
