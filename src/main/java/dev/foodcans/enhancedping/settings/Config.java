package dev.foodcans.enhancedping.settings;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class Config
{
    public static boolean SHOW_HIGH_PING;
    public static long HIGH_PING_RATE;
    public static long HIGH_PING_THRESHOLD;

    public static long MAX_PINGS;
    public static long PING_RATE;
    public static long PING_REQUEST_TIMEOUT;

    public static long PING_VALUES_EXCELLENT;
    public static long PING_VALUES_GOOD;
    public static long PING_VALUES_OKAY;
    public static long PING_VALUES_BAD;
    public static long PING_VALUES_VERY_BAD;

    public static void load(FileConfiguration config)
    {
        SHOW_HIGH_PING = config.getBoolean("Show-High-Ping");
        HIGH_PING_RATE = config.getLong("High-Ping-Rate");
        HIGH_PING_THRESHOLD = config.getLong("High-Ping-Threshold");

        MAX_PINGS = config.getLong("Max-Pings");
        PING_RATE = config.getLong("Ping-Rate");
        PING_REQUEST_TIMEOUT = config.getLong("Ping-Request-Timeout");

        ConfigurationSection valuesSection = config.createSection("Ping-Values");
        PING_VALUES_EXCELLENT = valuesSection.getLong("Excellent");
        PING_VALUES_GOOD = valuesSection.getLong("Good");
        PING_VALUES_OKAY = valuesSection.getLong("Okay");
        PING_VALUES_BAD = valuesSection.getLong("Bad");
        PING_VALUES_VERY_BAD = valuesSection.getLong("Very-Bad");
    }
}
