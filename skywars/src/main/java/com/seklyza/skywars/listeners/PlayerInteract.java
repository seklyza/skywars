package com.seklyza.skywars.listeners;

import com.seklyza.skywars.Main;
import com.seklyza.skywars.game.Game;
import com.seklyza.skywars.game.GameState;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.loot.LootTable;

import java.util.ArrayList;
import java.util.List;

public class PlayerInteract implements Listener {
    private Main plugin = Main.getPlugin(Main.class);
    private Game game = plugin.getGame();
    private Server server = plugin.getServer();

    private List<Chest> chests = new ArrayList<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (game.getGameState() == GameState.INGAME &&
                event.getAction() == Action.RIGHT_CLICK_BLOCK &&
                block != null &&
                block.getType() == Material.CHEST) {
            Chest chest = (Chest) block.getState();
            if(chests.contains(chest)) return;

            LootTable lootTable = server.getLootTable(new NamespacedKey(plugin, "chest"));
            chests.add(chest);
            chest.setLootTable(lootTable);
            chest.update();
        }
    }

}
