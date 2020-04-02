package com.seklyza.skywars.listeners;

import com.seklyza.skywars.Main;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class PlayerRespawn implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location location = (Location) player.getMetadata("respawnLocation").get(0).value();

        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            assert location != null;
            player.teleport(location);
            player.setGameMode(GameMode.SPECTATOR);
            plugin.getGame().onPlayerQuit(player);
        }, 1);
    }
}
