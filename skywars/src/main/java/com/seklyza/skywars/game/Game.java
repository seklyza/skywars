package com.seklyza.skywars.game;

import com.seklyza.skywars.Config;
import com.seklyza.skywars.Main;
import com.seklyza.skywars.tasks.StartCountdownTask;
import com.seklyza.skywars.tasks.TimeElapsedTask;
import com.seklyza.skywars.utils.sidebarutils.SidebarUtils;
import com.seklyza.skywars.utils.sidebarutils.WaitingSidebar;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Game {
    private final Main plugin = Main.getPlugin(Main.class);
    private final ScoreboardManager scoreboardManager = plugin.getServer().getScoreboardManager();
    private final Config config = plugin.getConfigVariables();
    private final World world;
    private final BukkitScheduler scheduler = plugin.getServer().getScheduler();
    private final Location[] SPAWN_POINTS;
    private final boolean AUTO_START = config.AUTO_START;
    private final int MIN_PLAYERS = config.MIN_PLAYERS;
    private final int MAX_PLAYERS = config.MAX_PLAYERS;
    private final int TIME_BEFORE_START = config.TIME_BEFORE_START;

    private GameState gameState = GameState.WAITING;
    private int currentIndex = 0;
    private Map<UUID, Player> alivePlayers = new HashMap<>();
    private Map<UUID, Integer> kills = new HashMap<>();
    private BukkitTask startCountdownTask;

    public Map<UUID, Player> getAlivePlayers() {
        return alivePlayers;
    }

    public Map<UUID, Integer> getKills() {
        return kills;
    }

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

        if (size >= MAX_PLAYERS || gameState == GameState.INGAME) {
            player.kickPlayer("§cThe game has already started! Try again later!");

            return;
        }

        Location spawnPoint = SPAWN_POINTS[currentIndex++ % MAX_PLAYERS];
        player.teleport(spawnPoint);
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            for (Player onlinePlayer : plugin.getServer().getOnlinePlayers()) {
                onlinePlayer.showPlayer(plugin, player);
                player.showPlayer(plugin, onlinePlayer);
            }
        }, 10);
        plugin.getServer().broadcastMessage(String.format("§9Join> §e%s §7joined the game. §a(%s/%s)", player.getName(), size, MAX_PLAYERS));
        Scoreboard scoreboard = scoreboardManager.getNewScoreboard();
        player.setScoreboard(scoreboard);
        Objective sidebar = scoreboard.registerNewObjective(player.getName(), "dummy", player.getName());
        sidebar.setDisplaySlot(DisplaySlot.SIDEBAR);
        sidebar.setDisplayName("§6§lSKYWARS");

        if (gameState == GameState.WAITING) {
            SidebarUtils.renderAll(plugin, WaitingSidebar.of(size).build());
        }

        if (size < MIN_PLAYERS || !AUTO_START)
            return; // We won't start the game here.

        startCountdown();
    }

    public void onPlayerQuit(Player player) {
        String message = "§9Left> §e%s §7left the game. §c(%s/%s)";

        if (gameState == GameState.STARTING || gameState == GameState.WAITING) {
            int size = plugin.getServer().getOnlinePlayers().size() - 1;
            plugin.getServer().broadcastMessage(String.format(message, player.getName(), size, MAX_PLAYERS));
            if (gameState == GameState.WAITING) {
                SidebarUtils.renderAll(plugin, WaitingSidebar.of(size).build());
            } else if (size < MIN_PLAYERS) {
                stopGame(null);
            }
        } else if (gameState == GameState.INGAME) {
            alivePlayers.remove(player.getUniqueId());
            int size = alivePlayers.size();

            if (size <= 1) {
                alivePlayers.forEach((uuid, winner) -> {
                    stopGame(winner, true);
                });
                return;
            }

            if (alivePlayers.containsKey(player.getUniqueId())) {
                plugin.getServer().broadcastMessage(String.format(message, player.getName(), size - 1, MAX_PLAYERS));
            }
        }
    }

    public void startCountdown() {
        startCountdown(TIME_BEFORE_START);
    }

    public void startCountdown(int interval) {
        if (gameState != GameState.WAITING) return;
        gameState = GameState.STARTING;

        StartCountdownTask startCountdownTask = new StartCountdownTask(interval);
        this.startCountdownTask = scheduler.runTaskTimer(plugin, startCountdownTask, 0, 20);
        startCountdownTask.setTaskId(this.startCountdownTask.getTaskId());
    }

    public void startGame() {
        stopTask(startCountdownTask);
        world.setGameRule(GameRule.FALL_DAMAGE, false);
        gameState = GameState.INGAME;
        plugin.getServer().broadcastMessage("§9Game> §7The game has started!!!");

        for (Player player : plugin.getServer().getOnlinePlayers()) {
            player.setGameMode(GameMode.SURVIVAL);
            alivePlayers.put(player.getUniqueId(), player);
            kills.put(player.getUniqueId(), 0);
        }

        // Break the glass in every loaded chunk.
        for (Chunk chunk : world.getLoadedChunks()) {
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

        scheduler.runTaskLater(plugin, () -> {
            world.setGameRule(GameRule.FALL_DAMAGE, true);
            world.setDifficulty(Difficulty.NORMAL);
        }, 20 * 2);
        TimeElapsedTask timeElapsedTask = new TimeElapsedTask();
        scheduler.runTaskTimer(plugin, timeElapsedTask, 0, 20);
    }

    public void stopGame(Player winner) {
        stopGame(winner, false);
    }


    public void stopGame(Player winner, boolean wait) {
        if (winner == null) {
            plugin.getServer().broadcastMessage("§9Game> §7The game has been stopped.");
        } else {
            plugin.getServer().broadcastMessage(String.format("§4§lGAME OVER!!! §a%s §cwon with §a%s §ckills!", winner.getName(), kills.get(winner.getUniqueId())));
        }

        if (wait) {
            plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
                plugin.getServer().reload();
            }, 20 * 2);
        } else plugin.getServer().reload();
    }

    private void stopTask(BukkitTask task) {
        if (task != null && scheduler.isCurrentlyRunning(task.getTaskId())) {
            scheduler.cancelTask(task.getTaskId());
        }
    }

    public void unloadMap() {
        plugin.getServer().unloadWorld(world, false);
    }
}
