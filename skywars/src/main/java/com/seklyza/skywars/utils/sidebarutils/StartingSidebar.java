package com.seklyza.skywars.utils.sidebarutils;

import com.seklyza.skywars.Config;
import com.seklyza.skywars.Main;
import org.bukkit.scoreboard.Objective;

import java.util.Map;

public class StartingSidebar {
    private final Main plugin = Main.getPlugin(Main.class);
    private final Config config = plugin.getConfigVariables();

    private final int size;
    private int MAX_PLAYERS = config.MAX_PLAYERS;
    private final int seconds;

    private StartingSidebar(int size, int seconds) {
        this.size = size;
        this.seconds = seconds;
    }

    public static StartingSidebar of(int size, int seconds) {
        return new StartingSidebar(size, seconds);
    }

    public Map<Integer, String> build() {
        return LineManager.builder()
                .newLine()
                .add("Players: §a%s/%s", size, MAX_PLAYERS)
                .newLine()
                .add("Starting in §a%s §rseconds", seconds)
                .newLine()
                .add("§ewww.seklyza.com")
                .build();
    }
}
