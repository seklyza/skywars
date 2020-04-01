package com.seklyza.skywars;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import static com.seklyza.skywars.utils.LocationUtils.parseLocations;

public class Config {
    private Main plugin = Main.getPlugin(Main.class);
    private FileConfiguration config = plugin.getConfig();

    public int MIN_PLAYERS = config.getInt("min_players");
    public int MAX_PLAYERS = config.getInt("max_players");
    public Location[] SPAWN_POINTS = parseLocations(plugin.getServer().getWorld("world"), config.getStringList("spawn_points"));

    public Config() {
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
}
