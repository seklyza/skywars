package com.seklyza.skywars.tasks;

import com.seklyza.skywars.Main;
import com.seklyza.skywars.game.Game;
import com.seklyza.skywars.utils.sidebarutils.SidebarUtils;
import com.seklyza.skywars.utils.sidebarutils.StartingSidebar;

public class StartCountdownTask implements Runnable {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Game game = plugin.getGame();

    private int taskId;
    private int remainingSeconds;

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public StartCountdownTask(int remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    @Override
    public void run() {
        if (remainingSeconds == 0) {
            plugin.getServer().getScheduler().cancelTask(taskId);
            game.startGame();

            return;
        }

        // For example: 30, 25, 20, 15, 10, 5, 4, 3, 2, 1
        if ((remainingSeconds % 5 == 0 || remainingSeconds < 5)) {
            plugin.getServer().broadcastMessage(String.format("§9Skywars> §7Starting game in §e%s §7second%s!", remainingSeconds, remainingSeconds == 1 ? "" : "s"));
        }

        int size = plugin.getServer().getOnlinePlayers().size();
        SidebarUtils.renderAll(plugin, StartingSidebar.of(size, remainingSeconds).build());

        remainingSeconds--;
    }
}
