package dev.foodcans.enhancedping.ping;

import dev.foodcans.enhancedping.settings.Config;
import net.md_5.bungee.api.ChatColor;

public enum PingValue
{
    EXCELLENT("#00FF00", '\uFE58'),
    GREAT("#4BFF00", '\u208B'),
    GOOD("#A5FF00", '\u23BC'),
    OKAY("#FFFF00", '\u2212'),
    BAD("#FFB400", '\u23BB'),
    VERY_BAD("#FF5A00", '\u23BA'),
    HORRIBLE("#FF0000", '\u00AF');

    private String hexColor;
    private char unicode;

    PingValue(String hexColor, char unicode)
    {
        this.hexColor = hexColor;
        this.unicode = unicode;
    }

    public static PingValue ofPing(long ping)
    {

        if (ping <= Config.PING_VALUES_EXCELLENT)
        {
            return EXCELLENT;
        } else if (ping <= Config.PING_VALUES_GREAT)
        {
            return GREAT;
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

    public ChatColor getHexColor()
    {
        return ChatColor.of(hexColor);
    }

    public char getUnicode()
    {
        return unicode;
    }
}
