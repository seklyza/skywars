package com.seklyza.skywars.listeners;

import com.destroystokyo.paper.Title;
import com.seklyza.skywars.Main;
import com.seklyza.skywars.game.Game;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;

public class PlayerDeath implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Game game = plugin.getGame();

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Location location = player.getLocation();

        if (!game.getAlivePlayers().containsKey(player.getUniqueId())) return;
        String message = String.format("§cGame> §e%s §7was killed!", player.getName());
        if (player.getKiller() != null) {
            game.getKills().put(player.getKiller().getUniqueId(), game.getKills().get(player.getKiller().getUniqueId()) + 1);
            message = String.format("§cGame> §e%s §7was killed by §e%s§7!", player.getName(), player.getKiller().getName());
        }

        plugin.getServer().broadcastMessage(message);
        player.sendTitle(Title.builder().title("§c§lGAME OVER!").subtitle("You were killed!").fadeOut(2).build());
        if (location.getY() < 100) {
            location = new Location(location.getWorld(), location.getX(), 120, location.getZ());
        }
        player.setMetadata("respawnLocation", new FixedMetadataValue(plugin, location));
    }
}
