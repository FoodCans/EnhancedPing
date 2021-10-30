package dev.foodcans.enhancedping.storage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import dev.foodcans.enhancedping.EnhancedPing;
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
import java.util.ArrayList;
import java.util.List;
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
    public Boolean loadShowing(UUID uuid)
    {
        Path path = Paths.get(showingFile.getAbsolutePath());
        try (Reader reader = Files.newBufferedReader(path))
        {
            Type uuidListType = new TypeToken<ArrayList<UUID>>(){}.getType();
            List<UUID> list = gson.fromJson(reader, uuidListType);
            return list.contains(uuid);
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
            List<UUID> list = getAllData();
            if (showing && !list.contains(uuid))
            {
                list.add(uuid);
            } else
            {
                list.remove(uuid);
            }
            gson.toJson(list, writer);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public List<UUID> getAllData()
    {
        List<UUID> list = new ArrayList<>();
        Path path = Paths.get(showingFile.getAbsolutePath());
        try (Reader reader = Files.newBufferedReader(path))
        {
            Type uuidListType = new TypeToken<ArrayList<UUID>>(){}.getType();
            List<UUID> loaded = gson.fromJson(reader, uuidListType);
            if (loaded == null)
            {
                 loaded = new ArrayList<>();
            }
            list.addAll(loaded);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void deleteStorage()
    {
        showingFile.delete();
    }
}
