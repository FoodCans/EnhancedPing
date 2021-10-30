package dev.foodcans.enhancedping.storage;

import dev.foodcans.pluginutils.Callback;

import java.util.List;
import java.util.UUID;

public interface IStorage
{
    void fetchShowing(UUID uuid, Callback<Boolean> callback);

    void setShowing(UUID uuid, boolean showing);

    Boolean loadShowing(UUID uuid);

    void saveShowing(UUID uuid, boolean showing);

    List<UUID> getAllData();

    void deleteStorage();
}
