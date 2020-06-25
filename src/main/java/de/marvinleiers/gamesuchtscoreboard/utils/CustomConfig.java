package de.marvinleiers.gamesuchtscoreboard.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class CustomConfig
{
    private static Plugin plugin;

    private File file;
    private FileConfiguration config;

    public CustomConfig(String path)
    {
        file = new File(path);
        config = YamlConfiguration.loadConfiguration(file);
        saveConfig();
    }

    public void addDefault(String path, Object obj)
    {
        if (!config.isSet(path))
            config.set(path, obj);

        System.out.println("added " + obj.toString() + " to " + path);
        saveConfig();
    }

    public void set(String path, Object obj)
    {
        config.set(path, obj);
        saveConfig();
    }

    public boolean isSet(String path)
    {
        return config.isSet(path);
    }

    public void setLocation(String path, Location loc)
    {
        config.set(path + ".world", loc.getWorld().getName());
        config.set(path + ".x", loc.getX());
        config.set(path + ".y", loc.getY());
        config.set(path + ".z", loc.getZ());
        config.set(path + ".yaw", loc.getYaw());
        config.set(path + ".pitch", loc.getPitch());

        saveConfig();
    }

    public Location getLocation(String path)
    {
        return new Location(Bukkit.getWorld(config.getString(path + ".world")), config.getDouble(path + ".x"),
                config.getDouble(path + ".y"), config.getDouble(path + ".z"),
                (float) config.getDouble(path + ".yaw"), (float) config.getDouble(path + ".pitch"));
    }

    public long getLong(String path)
    {
        return config.getLong(path);
    }

    public int getInt(String path)
    {
        return config.getInt(path);
    }

    public String getString(String path)
    {
        return config.getString(path);
    }

    public double getDouble(String path)
    {
        return config.getDouble(path);
    }

    public ConfigurationSection getSection(String path)
    {
        return config.getConfigurationSection(path);
    }

    public FileConfiguration getConfig()
    {
        return config;
    }

    public void saveConfig()
    {
        try
        {
            config.save(file);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void setUp(Plugin plugin)
    {
        CustomConfig.plugin = plugin;
    }
}
