package dev.foodcans.enhancedping.storage;

import dev.foodcans.pluginutils.Callback;

import java.util.Map;
import java.util.UUID;

public interface IStorage
{
    void fetchShowing(UUID uuid, Callback<Boolean> callback);

    void setShowing(UUID uuid, boolean showing);

    boolean loadShowing(UUID uuid);

    void saveShowing(UUID uuid, boolean showing);

    Map<UUID, Boolean> getAllData();

    void deleteStorage();
}
