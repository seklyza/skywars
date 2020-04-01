package com.seklyza.skywars.game;

import com.seklyza.skywars.Config;
import com.seklyza.skywars.Main;
import com.seklyza.skywars.tasks.StartCountdownTask;
import com.seklyza.skywars.utils.sidebarutils.SidebarUtils;
import com.seklyza.skywars.utils.sidebarutils.WaitingSidebar;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

public class Game {
    private final Main plugin = Main.getPlugin(Main.class);
    private final ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
    private final Config config = plugin.getConfigVariables();
    private final World world;
    private final Location[] SPAWN_POINTS;
    private final int MIN_PLAYERS = config.MIN_PLAYERS;
    private final int MAX_PLAYERS = config.MAX_PLAYERS;
    private final boolean AUTO_START = config.AUTO_START;
    private final int TIME_BEFORE_START = config.TIME_BEFORE_START;

    private GameState gameState = GameState.WAITING;

    public Game() {
        world = plugin.getServer().createWorld(new WorldCreator("map"));
        assert world != null;
        world.setAutoSave(false);
        world.setDifficulty(Difficulty.PEACEFUL);
        SPAWN_POINTS = config.getSpawnPoints(world);
    }

    public void onPlayerJoin(Player player) {
        player.setGameMode(GameMode.ADVENTURE);
        player.getInventory().clear();
        for (PotionEffect activePotionEffect : player.getActivePotionEffects()) {
            player.removePotionEffect(activePotionEffect.getType());
        }

        int size = plugin.getServer().getOnlinePlayers().size();

        if (size >= MAX_PLAYERS && (gameState == GameState.WAITING || gameState == GameState.STARTING)) {
            player.kickPlayer("§cThe game has already started! Try again later!");

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

        SidebarUtils.renderAll(plugin, WaitingSidebar.of(size).build());

        if (size < MIN_PLAYERS || gameState != GameState.WAITING || !AUTO_START)
            return; // We won't start the game here.

        startCountdown();
    }

    public void onPlayerQuit(Player player) {
        int size = plugin.getServer().getOnlinePlayers().size();

        plugin.getServer().broadcastMessage(String.format("§9Left> §e%s §7left the game. §c(%s/%s)", player.getName(), size - 1, MAX_PLAYERS));
    }

    public void startCountdown() {
        startCountdown(TIME_BEFORE_START);
    }

    public void startCountdown(int interval) {
        gameState = GameState.STARTING;

        StartCountdownTask startCountdownTask = new StartCountdownTask(interval);
        int startCountdownTaskId = plugin.getServer().getScheduler().runTaskTimer(plugin, startCountdownTask, 0, 20).getTaskId();
        startCountdownTask.setTaskId(startCountdownTaskId);
    }

    public void startGame() {
        gameState = GameState.INGAME;
        plugin.getServer().broadcastMessage("§9Skywars> §7The game has started!!!");


        for (Player player : plugin.getServer().getOnlinePlayers()) {
            // Break the glass
            Chunk chunk = player.getLocation().getChunk();

            int X = chunk.getX() * 16;
            int Z = chunk.getZ() * 16;

            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    for (int y = 100; y < 120; y++) {
                        Block block = chunk.getWorld().getBlockAt(X + x, y, Z + z);
                        if (block.getType() == Material.GLASS) {
                            block.setType(Material.AIR);
                        }
                    }
                }
            }
        }
    }

    public void unloadMap() {
        plugin.getServer().unloadWorld(world, false);
    }
}
