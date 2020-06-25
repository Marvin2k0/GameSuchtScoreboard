package de.marvinleiers.gamesuchtscoreboard;

import com.sun.deploy.security.SelectableSecurityManager;
import de.marvinleiers.gamesuchtscoreboard.utils.CustomConfig;
import de.marvinleiers.gamesuchtscoreboard.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.HashMap;

public final class GameSuchtScoreboard extends JavaPlugin implements Listener
{
    private static HashMap<Player, String> temp = new HashMap<>();
    private static CustomConfig config;

    @Override
    public void onEnable()
    {
        Messages.setUp(this);
        config = new CustomConfig(this.getDataFolder().getPath() + "/data/scoreboard-data.yml");

        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event)
    {
        Player player = event.getPlayer();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective sidebar;

        if (!config.isSet(player.getUniqueId() + ".playtime"))
        {
            if (player.hasPlayedBefore())
                config.set(player.getUniqueId() + ".playtime", player.getFirstPlayed());
            else
                config.set(player.getUniqueId() + ".playtime", System.currentTimeMillis());
        }

        long longTime = System.currentTimeMillis() - config.getLong(player.getUniqueId() + ".playtime");
        int hours = (int) (longTime / 1000 / 60 / 60 % 24);
        int minutes = (int) (longTime / 1000 / 60 % 60);

        String playtime = (hours == 0 ? "" : hours + "h ") + minutes + "m ";

        try
        {
            sidebar = scoreboard.registerNewObjective((player.getName() + player.getUniqueId()).substring(0, 15), "", Messages.get("title", false));
        }
        catch (IllegalArgumentException e)
        {
            sidebar = scoreboard.getObjective((player.getName() + player.getUniqueId()).substring(0, 15));
        }

        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.getScore("§f" + Messages.get("placeholder", false)).setScore(6);
        sidebar.getScore(Messages.get("line-1", false).replace("%playername%", player.getDisplayName())).setScore(5);
        sidebar.getScore("§f§f" + Messages.get("placeholder", false)).setScore(4);
        sidebar.getScore(Messages.get("line-2", false)).setScore(3);
        sidebar.getScore(Messages.get("line-3", false).replace("%playtime%", playtime + "")).setScore(2);
        sidebar.getScore("§f§f§f" + Messages.get("placeholder", false)).setScore(1);
        sidebar.getScore(Messages.get("line-4", false)).setScore(0);

        temp.put(player, playtime);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                player.setScoreboard(scoreboard);
            }
        }.runTaskLater(this, 1);

        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                if (!player.isOnline())
                {
                    this.cancel();
                    return;
                }

                update(player);
            }
        }.runTaskTimer(this, 0, 20);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event)
    {
        Player player = event.getPlayer();
        player.getScoreboard().getObjective((player.getName() + player.getUniqueId()).substring(0, 15)).unregister();

        temp.remove(player);
    }

    private void update(Player player)
    {
        try
        {
            long longTime = System.currentTimeMillis() - config.getLong(player.getUniqueId() + ".playtime");
            int hours = (int) (longTime / 1000 / 60 / 60 % 24);
            int minutes = (int) (longTime / 1000 / 60 % 60);

            String playtime = (hours == 0 ? "" : hours + "h ") + minutes + "m ";
            String oldPlaytime = temp.get(player);

            Objective objective = player.getScoreboard().getObjective((player.getName() + player.getUniqueId()).substring(0, 15));

            player.getScoreboard().resetScores(Messages.get("line-3", false).replace("%playtime%", oldPlaytime));
            objective.getScore(Messages.get("line-3", false).replace("%playtime%", playtime + "")).setScore(2);

            temp.put(player, playtime);
        }
        catch (NullPointerException ignored)
        {
        }
    }
}
