package com.seklyza.skywars.utils;

import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

public class SidebarUtils {
    public static void render(Objective sidebar, int size, int MAX_PLAYERS) {
        Map<Integer, String> lines = LineManager.builder()
                .newLine()
                .add("Players: §a%s/%s", size, MAX_PLAYERS)
                .newLine()
                .add("Waiting...")
                .newLine()
                .add("§ewww.seklyza.com")
                .build();

        Scoreboard scoreboard = sidebar.getScoreboard();
        lines.forEach((i, v) -> {
            assert scoreboard != null;
            scoreboard.resetScores(v);
            sidebar.getScore(v).setScore(i);
        });
    }
}

