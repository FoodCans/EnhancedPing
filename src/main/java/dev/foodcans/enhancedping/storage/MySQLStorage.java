package dev.foodcans.enhancedping.storage;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.foodcans.enhancedping.EnhancedPing;
import dev.foodcans.enhancedping.settings.Config;
import dev.foodcans.pluginutils.Callback;
import org.bukkit.Bukkit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MySQLStorage implements IStorage
{
    private final HikariDataSource dataSource;

    public MySQLStorage()
    {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl("jdbc:mysql://" + Config.DB_URL);
        hikariConfig.setUsername(Config.DB_USERNAME);
        hikariConfig.setPassword(Config.DB_PASSWORD);
        hikariConfig.addDataSourceProperty("cachePrepStmts", "true");
        hikariConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        hikariConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        hikariConfig.addDataSourceProperty("useServerPrepStmts", "true");
        hikariConfig.addDataSourceProperty("allowPublicKeyRetrieval",
                Config.DB_ALLOW_PUBLIC_KEY_RETRIEVAL);
        hikariConfig.addDataSourceProperty("useSSL", Config.DB_USE_SSL);
        this.dataSource = new HikariDataSource(hikariConfig);

        this.setup();
    }

    private void setup()
    {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(Queries.CREATE_TABLE);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
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
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(Queries.GET);
            statement.setString(1, uuid.toString());
            ResultSet result = statement.executeQuery();
            if (result.next())
            {
                return result.getBoolean("showing");
            }
            return Config.SHOW_PING_BAR_DEFAULT;
        } catch (SQLException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void saveShowing(UUID uuid, boolean showing)
    {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(Queries.INSERT);
            statement.setString(1, uuid.toString());
            statement.setBoolean(2, showing);
            statement.setBoolean(3, showing);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public Map<UUID, Boolean> getAllData()
    {
        Map<UUID, Boolean> map = new HashMap<>();
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(Queries.GET_ALL);
            ResultSet result = statement.executeQuery();
            while (result.next())
            {
                map.put(UUID.fromString(result.getString("uuid")), result.getBoolean("showing"));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
        return map;
    }

    @Override
    public void deleteStorage()
    {
        try (Connection connection = getConnection())
        {
            PreparedStatement statement = connection.prepareStatement(Queries.DROP_TABLE);
            statement.executeUpdate();
        } catch (SQLException e)
        {
            e.printStackTrace();
        }
    }

    private Connection getConnection() throws SQLException
    {
        return dataSource.getConnection();
    }

    private static class Queries
    {
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS enhancedping(uuid CHAR(36) NOT NULL," +
                "showing TINYINT NOT NULL,PRIMARY KEY (uuid))";
        public static final String DROP_TABLE = "DROP TABLE IF EXISTS enhancedping";
        public static final String INSERT = "INSERT INTO enhancedping (uuid,showing) VALUES(?,?) ON DUPLICATE KEY " +
                "UPDATE showing=?";
        public static final String GET = "SELECT showing FROM enhancedping WHERE uuid=?";
        public static final String GET_ALL = "SELECT * FROM enhancedping?";
    }
}
