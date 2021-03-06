package dev.foodcans.enhancedping.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.foodcans.enhancedping.EnhancedPing;
import dev.foodcans.enhancedping.settings.Config;
import dev.foodcans.pluginutils.Callback;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonStorage implements IStorage
{
    private final File showingFile;
    private final Gson gson = new Gson();

    public JsonStorage()
    {
        showingFile = new File(EnhancedPing.getInstance().getDataFolder().getAbsolutePath(), "showing.json");
        if (!showingFile.exists())
        {
            try
            {
                showingFile.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void fetchShowing(UUID uuid, Callback<Boolean> callback)
    {
        Bukkit.getScheduler().runTaskAsynchronously(EnhancedPing.getInstance(), () ->
        {
            boolean showing = loadShowing(uuid);
            Bukkit.getScheduler().runTask(EnhancedPing.getInstance(), () -> callback.call(showing));
        });
    }

    @Override
    public void setShowing(UUID uuid, boolean showing)
    {
        Bukkit.getScheduler()
                .runTaskAsynchronously(EnhancedPing.getInstance(), () -> saveShowing(uuid, showing));
    }

    @Override
    public boolean loadShowing(UUID uuid)
    {
        Path path = Paths.get(showingFile.getAbsolutePath());
        try (Reader reader = Files.newBufferedReader(path))
        {
            Type mapType = new TypeToken<Map<UUID, Boolean>>(){}.getType();
            Map<UUID, Boolean> map = gson.fromJson(reader, mapType);
            if (map.containsKey(uuid))
            {
                return map.get(uuid);
            }
            return Config.SHOW_PING_BAR_DEFAULT;
        } catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void saveShowing(UUID uuid, boolean showing)
    {
        Path path = Paths.get(showingFile.getAbsolutePath());
        try (Writer writer = Files.newBufferedWriter(path))
        {
            Map<UUID, Boolean> map = getAllData();
            map.put(uuid, showing);
            gson.toJson(map, writer);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Map<UUID, Boolean> getAllData()
    {
        Map<UUID, Boolean> map = null;
        Path path = Paths.get(showingFile.getAbsolutePath());
        try (Reader reader = Files.newBufferedReader(path))
        {
            Type mapType = new TypeToken<Map<UUID, Boolean>>(){}.getType();
            map = gson.fromJson(reader, mapType);
            if (map == null)
            {
                map = new  HashMap<>();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public void deleteStorage()
    {
        showingFile.delete();
    }
}
