package com.seklyza.skywars.listeners;

import com.seklyza.skywars.Config;
import com.seklyza.skywars.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Config config = plugin.getConfigVariables();
    private final int MAX_PLAYERS = config.MAX_PLAYERS;

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();
        int size = plugin.getServer().getOnlinePlayers().size();

        plugin.getServer().broadcastMessage(String.format("§9Left> §e%s §7left the game. §c(%s/%s)", player.getName(), size - 1, MAX_PLAYERS));
    }
}
