package com.seklyza.skywars.listeners;

import com.seklyza.skywars.Config;
import com.seklyza.skywars.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Config config = plugin.getConfigVariables();
    private final Location[] SPAWN_POINTS = config.SPAWN_POINTS;
    private final int MAX_PLAYERS = config.MAX_PLAYERS;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        int size = plugin.getServer().getOnlinePlayers().size();

        if(size >= MAX_PLAYERS) {
            player.kickPlayer("§cGame is full!");

            return;
        }

        Location spawnPoint = SPAWN_POINTS[size];
        player.teleport(spawnPoint);
        plugin.getServer().broadcastMessage(String.format("§9Join> §a%s §7joined the game. §a(%s/%s)", player.getName(), size, MAX_PLAYERS));
    }
}
