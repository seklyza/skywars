package com.seklyza.skywars.tasks;

import com.seklyza.skywars.Main;
import com.seklyza.skywars.utils.sidebarutils.StartingSidebar;
import org.bukkit.scoreboard.Objective;

public class StartCountdownTask implements Runnable {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Objective sidebar;

    private int remainingSeconds;

    public StartCountdownTask(int remainingSeconds, Objective sidebar) {
        this.remainingSeconds = remainingSeconds;
        this.sidebar = sidebar;
    }

    @Override
    public void run() {
        int size = plugin.getServer().getOnlinePlayers().size();
        StartingSidebar.of(size, remainingSeconds).render(sidebar);

        remainingSeconds--;
    }
}
