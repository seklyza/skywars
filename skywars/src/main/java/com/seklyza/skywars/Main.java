package com.seklyza.skywars;

import com.seklyza.skywars.listeners.PlayerJoin;
import com.seklyza.skywars.listeners.PlayerQuit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private Config config;

    public Config getConfigVariables() {
        return config;
    }

    @Override
    public void onEnable() {
        config = new Config();
        registerEvents();
        getLogger().info("Skywars has been enabled.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Skywars has been disabled.");
    }

    private void registerEvents() {
        PluginManager pm = getServer().getPluginManager();

        pm.registerEvents(new PlayerJoin(), this);
        pm.registerEvents(new PlayerQuit(), this);
    }
}
