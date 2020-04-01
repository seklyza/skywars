package com.seklyza.skywars.listeners;

import com.seklyza.skywars.Main;
import com.seklyza.skywars.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Game game = plugin.getGame();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();

        game.onPlayerJoin(player);
    }
}
