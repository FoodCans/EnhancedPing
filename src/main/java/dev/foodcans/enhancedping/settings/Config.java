package dev.foodcans.enhancedping.settings;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Locale;

public class Config
{
    public static boolean MIGRATE;
    public static StorageType STORAGE_TYPE;

    public static String DB_URL;
    public static String DB_USERNAME;
    public static String DB_PASSWORD;
    public static boolean DB_ALLOW_PUBLIC_KEY_RETRIEVAL;
    public static boolean DB_USE_SSL;

    public static boolean SHOW_PING_BAR_DEFAULT;
    public static long PING_BAR_RATE;

    public static long MAX_PINGS;
    public static long PING_RATE;
    public static long PING_REQUEST_TIMEOUT;

    public static long PING_GRADES_EXCELLENT;
    public static long PING_GRADES_GREAT;
    public static long PING_GRADES_GOOD;
    public static long PING_GRADES_OKAY;
    public static long PING_GRADES_BAD;
    public static long PING_GRADES_VERY_BAD;

    public static void load(FileConfiguration config)
    {
        MIGRATE = config.getBoolean("Migrate");
        STORAGE_TYPE = StorageType.valueOf(config.getString("Storage-Type").toUpperCase(Locale.ROOT));

        ConfigurationSection mySQLSection = config.getConfigurationSection("MySQL");
        DB_URL = mySQLSection.getString("Url");
        DB_USERNAME = mySQLSection.getString("Username");
        DB_PASSWORD = mySQLSection.getString("Password");
        DB_ALLOW_PUBLIC_KEY_RETRIEVAL = mySQLSection.getBoolean("Allow-Public-Key-Retrieval");
        DB_USE_SSL = mySQLSection.getBoolean("Use-SSL");

        SHOW_PING_BAR_DEFAULT = config.getBoolean("Show-Ping-Bar-Default");
        PING_BAR_RATE = config.getLong("Ping-Bar-Rate");

        MAX_PINGS = config.getLong("Max-Pings");
        PING_RATE = config.getLong("Ping-Rate");
        PING_REQUEST_TIMEOUT = config.getLong("Ping-Request-Timeout");

        ConfigurationSection gradesSection = config.getConfigurationSection("Ping-Grades");
        PING_GRADES_EXCELLENT = gradesSection.getLong("Excellent");
        PING_GRADES_GREAT = gradesSection.getLong("Great");
        PING_GRADES_GOOD = gradesSection.getLong("Good");
        PING_GRADES_OKAY = gradesSection.getLong("Okay");
        PING_GRADES_BAD = gradesSection.getLong("Bad");
        PING_GRADES_VERY_BAD = gradesSection.getLong("Very-Bad");
    }

    public enum StorageType
    {
        JSON, MYSQL
    }
}
