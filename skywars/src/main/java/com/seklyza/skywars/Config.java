package com.seklyza.skywars;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import static com.seklyza.skywars.utils.LocationUtils.parseLocations;

public class Config {
    private Main plugin = Main.getPlugin(Main.class);
    private FileConfiguration config = plugin.getConfig();

    public final int MIN_PLAYERS = config.getInt("min_players");
    public final int MAX_PLAYERS = config.getInt("max_players");
    public final int TIME_BEFORE_START = config.getInt("time_before_start");

    public Location[] getSpawnPoints(World world) {
        return parseLocations(world, config.getStringList("spawn_points"));
    }

    public Config() {
        config.options().copyDefaults(true);
        plugin.saveConfig();
    }
}
