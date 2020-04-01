package com.seklyza.skywars.utils.sidebarutils;

import java.util.Map;

public class IngameSidebar {
    private final int elapsedSeconds;
    private final int alivePlayers;

    private IngameSidebar(int elapsedSeconds, int alivePlayers) {
        this.elapsedSeconds = elapsedSeconds;
        this.alivePlayers = alivePlayers;
    }

    public static IngameSidebar of(int elapsedSeconds, int alivePlayers) {
        return new IngameSidebar(elapsedSeconds, alivePlayers);
    }

    public Map<Integer, String> build() {
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        String minutesString = minutes < 10 ? "0" + minutes : Integer.toString(minutes);
        String secondsString = seconds < 10 ? "0" + seconds : Integer.toString(seconds);

            return LineManager.builder()
                    .newLine()
                    .add("Players left: §a%s", alivePlayers)
                    .newLine()
                    .add("Time elapsed: §a%s:%s§r", minutesString, secondsString)
                    .newLine()
                    .add("§ewww.seklyza.com")
                    .build();
    }
}
