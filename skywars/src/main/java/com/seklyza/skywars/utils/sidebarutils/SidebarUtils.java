package com.seklyza.skywars.utils.sidebarutils;

import com.seklyza.skywars.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Map;

public class SidebarUtils {
    public static void render(Objective sidebar, Map<Integer, String> lines) {
        Scoreboard scoreboard = sidebar.getScoreboard();
        assert scoreboard != null;
        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        lines.forEach((i, v) -> {
            sidebar.getScore(v).setScore(i);
        });
    }

    public static void renderAll(Main plugin, Map<Integer, String> lines) {
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            Objective sidebar = player.getScoreboard().getObjective(player.getName());

            assert sidebar != null;
            render(sidebar, lines);
        }
    }
}

