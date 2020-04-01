package com.seklyza.skywars.listeners;

import com.seklyza.skywars.Main;
import com.seklyza.skywars.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Game game = plugin.getGame();

    @EventHandler
    public void onPlayerJoin(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Player player = event.getPlayer();

        game.onPlayerQuit(player);
    }
}
