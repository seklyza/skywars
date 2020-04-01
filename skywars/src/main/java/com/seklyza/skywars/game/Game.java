package com.seklyza.skywars.game;

import com.seklyza.skywars.Config;
import com.seklyza.skywars.Main;
import com.seklyza.skywars.tasks.StartCountdownTask;
import com.seklyza.skywars.utils.sidebarutils.StartingSidebar;
import com.seklyza.skywars.utils.sidebarutils.WaitingSidebar;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Game {
    private final Main plugin = Main.getPlugin(Main.class);
    private final ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
    private final Config config = plugin.getConfigVariables();
    private final Location[] SPAWN_POINTS = config.SPAWN_POINTS;
    private final int MIN_PLAYERS = config.MIN_PLAYERS;
    private final int MAX_PLAYERS = config.MAX_PLAYERS;
    private final int TIME_BEFORE_START = config.TIME_BEFORE_START;

    private GameState gameState = GameState.WAITING;
    private BukkitTask startCountdownTask = null;

    public Game() {
        plugin.getLogger().info("New game?? :)");
    }

    public void onPlayerJoin(Player player) {
        int size = plugin.getServer().getOnlinePlayers().size();

        if (size >= MAX_PLAYERS) {
            player.kickPlayer("§cGame is full!");

            return;
        }

        Location spawnPoint = SPAWN_POINTS[size];
        player.teleport(spawnPoint);
        plugin.getServer().broadcastMessage(String.format("§9Join> §e%s §7joined the game. §a(%s/%s)", player.getName(), size, MAX_PLAYERS));
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        player.setScoreboard(scoreboard);
        Objective sidebar = scoreboard.registerNewObjective(player.getName(), "dummy", player.getName());
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName("§6§lSKYWARS");

        WaitingSidebar.of(size).render(sidebar);

        if (!(size >= MIN_PLAYERS && gameState == GameState.WAITING)) return;

        gameState = GameState.STARTING;

        plugin.getServer().broadcastMessage(String.format("§9Skywars> §7Starting game in §e%s §7seconds!", TIME_BEFORE_START));
        StartingSidebar.of(size, TIME_BEFORE_START).render(sidebar);
        startCountdownTask = plugin.getServer().getScheduler().runTaskTimer(plugin, new StartCountdownTask(TIME_BEFORE_START, sidebar), 0, 20);
    }

    public void onPlayerQuit(Player player) {
        int size = plugin.getServer().getOnlinePlayers().size();

        plugin.getServer().broadcastMessage(String.format("§9Left> §e%s §7left the game. §c(%s/%s)", player.getName(), size - 1, MAX_PLAYERS));
    }
}
