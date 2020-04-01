package com.seklyza.skywars;

import com.seklyza.skywars.commands.GameCommand;
import com.seklyza.skywars.game.Game;
import com.seklyza.skywars.listeners.PlayerJoin;
import com.seklyza.skywars.listeners.PlayerQuit;
import org.bukkit.Location;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

public class Main extends JavaPlugin {
    // Fields
    private Config config;

    // State
    private Game game;

    public Config getConfigVariables() {
        return config;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void onEnable() {
        config = new Config();
        game = new Game();
        for (Player player : getServer().getOnlinePlayers()) {
            game.onPlayerJoin(player);
        }
        registerCommands();
        registerEvents();
        getLogger().info("Skywars has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Skywars has been disabled.");
        Location location = Objects.requireNonNull(getServer().getWorld("world")).getSpawnLocation();
        for (Player player : getServer().getOnlinePlayers()) {
            player.teleport(location);
        }
        game.unloadMap();
    }

    private void registerCommands() {
        registerCommand(GameCommand.class);
    }

    private void registerCommand(Class<? extends CommandExecutor> command) {
        try {
            String name = (String) command.getField("NAME").get(null);

            Objects.requireNonNull(getCommand(name)).setExecutor((CommandExecutor) command.getConstructor().newInstance());
        } catch (IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerQuit(), this);
    }
}
