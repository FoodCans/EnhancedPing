package dev.foodcans.enhancedping.hook;

import dev.foodcans.enhancedping.PingAPI;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class PlaceholderAPIHook extends PlaceholderExpansion
{
    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params)
    {
        return Long.toString(PingAPI.getPing(player));
    }

    @Override
    public @NotNull String getIdentifier()
    {
        return "eping";
    }

    @Override
    public @NotNull String getAuthor()
    {
        return "FoodCans";
    }

    @Override
    public @NotNull String getVersion()
    {
        return "1.0.0";
    }

    @Override
    public boolean persist()
    {
        return true;
    }
}
