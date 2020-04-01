package com.seklyza.skywars.tasks;

import com.seklyza.skywars.Main;
import com.seklyza.skywars.game.Game;
import com.seklyza.skywars.utils.sidebarutils.IngameSidebar;
import com.seklyza.skywars.utils.sidebarutils.SidebarUtils;

public class TimeElapsedTask implements Runnable {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Game game = plugin.getGame();

    private int elapsedSeconds = 0;

    @Override
    public void run() {
        SidebarUtils.renderAll(plugin, IngameSidebar.of(elapsedSeconds, game.getAlivePlayers().size()).build());

        elapsedSeconds++;
    }
}
