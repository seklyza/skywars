package com.seklyza.skywars.utils.sidebarutils;

import com.seklyza.skywars.Config;
import com.seklyza.skywars.Main;
import org.bukkit.scoreboard.Objective;

import java.util.Map;

public class WaitingSidebar {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Config config = plugin.getConfigVariables();

    private final int size;
    private int MAX_PLAYERS = config.MAX_PLAYERS;

    private WaitingSidebar(int size) {
        this.size = size;
    }

    public static WaitingSidebar of(int size) {
        return new WaitingSidebar(size);
    }

    public void render(Objective sidebar) {
        Map<Integer, String> lines = LineManager.builder()
                .newLine()
                .add("Players: §a%s/%s", size, MAX_PLAYERS)
                .newLine()
                .add("Waiting...")
                .newLine()
                .add("§ewww.seklyza.com")
                .build();

        SidebarUtils.render(sidebar, lines);
    }
}
