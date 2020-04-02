package com.seklyza.skywars.utils.sidebarutils;

import java.util.Map;

public class IngameSidebar {
    private final int elapsedSeconds;
    private final int alivePlayers;
    private final int kills;

    private IngameSidebar(int elapsedSeconds, int alivePlayers, int kills) {
        this.elapsedSeconds = elapsedSeconds;
        this.alivePlayers = alivePlayers;
        this.kills = kills;
    }

    public static IngameSidebar of(int elapsedSeconds, int alivePlayers, int kills) {
        return new IngameSidebar(elapsedSeconds, alivePlayers, kills);
    }

    public Map<Integer, String> build() {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        String minutesString = minutes < 10 ? "0" + minutes : Integer.toString(minutes);
        String secondsString = seconds < 10 ? "0" + seconds : Integer.toString(seconds);

            return LineManager.builder()
                    .newLine()
                    .add("Players left: §a%s§r", alivePlayers)
                    .newLine()
                    .add("Time elapsed: §a%s:%s§r", minutesString, secondsString)
                    .newLine()
                    .add("Kills: §a%s§r", kills)
                    .newLine()
                    .add("§ewww.seklyza.com")
                    .build();
    }
}
