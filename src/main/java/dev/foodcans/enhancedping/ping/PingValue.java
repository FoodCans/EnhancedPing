package dev.foodcans.enhancedping.ping;

import dev.foodcans.enhancedping.settings.Config;
import org.bukkit.ChatColor;

public enum PingValue
{
    EXCELLENT(ChatColor.DARK_GREEN),
    GOOD(ChatColor.GREEN),
    OKAY(ChatColor.YELLOW),
    BAD(ChatColor.GOLD),
    VERY_BAD(ChatColor.RED),
    HORRIBLE(ChatColor.DARK_RED);

    private ChatColor color;

    PingValue(ChatColor color)
    {
        this.color = color;
    }

    public static PingValue ofPing(long ping)
    {
        if (ping <= Config.PING_VALUES_EXCELLENT)
        {
            return EXCELLENT;
        } else if (ping <= Config.PING_VALUES_GOOD)
        {
            return GOOD;
        } else if (ping <= Config.PING_VALUES_OKAY)
        {
            return OKAY;
        } else if (ping <= Config.PING_VALUES_BAD)
        {
            return BAD;
        } else if (ping <= Config.PING_VALUES_VERY_BAD)
        {
            return VERY_BAD;
        } else
        {
            return HORRIBLE;
        }
    }

    public ChatColor getColor()
    {
        return color;
    }
}
