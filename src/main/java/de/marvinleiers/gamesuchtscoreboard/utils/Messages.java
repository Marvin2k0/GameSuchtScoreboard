package de.marvinleiers.gamesuchtscoreboard.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Messages
{
    private static FileConfiguration config;
    private static JavaPlugin plugin;

    public static String get(String path, boolean prefix)
    {
        return prefix ? get(path) : ChatColor.translateAlternateColorCodes('&', config.getString(path));
    }

    public static String get(String path)
    {
        return ChatColor.translateAlternateColorCodes('&', path.equalsIgnoreCase("prefix") ? config.getString("prefix") : config.getString("prefix") + " " + config.getString(path));
    }

    private static void addDefaults()
    {
        config.options().copyDefaults(true);

        config.addDefault("title", "&6&lGameSucht");
        config.addDefault("line-1", "&7%playername%");
        config.addDefault("line-2", "&6Playtime");
        config.addDefault("line-3", "&7%playtime%");
        config.addDefault("line-4", "&6game-sucht.tk");
        config.addDefault("placeholder", "&f");

        plugin.saveConfig();
    }

    public static void setUp(JavaPlugin plugin)
    {
        Messages.plugin = plugin;

        config = plugin.getConfig();

        addDefaults();
    }
}
