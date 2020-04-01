package com.seklyza.skywars.commands;

import com.seklyza.skywars.Main;
import com.seklyza.skywars.game.Game;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GameCommand implements CommandExecutor {
    public static final String NAME = "game";

    private final Main plugin = Main.getPlugin(Main.class);
    private final Game game = plugin.getGame();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) return false;

        String type = args[0];

        if (type.equalsIgnoreCase("start")) {
            if (args.length == 1) {
                game.startCountdown();
                return true;
            } else if (args.length == 2) {
                game.startCountdown(Integer.parseInt(args[1]));
                return true;
            } else return false;
        } else if (type.equalsIgnoreCase("stop") && args.length == 1) {
            // TODO stop game
        }

        return false;
    }
}
