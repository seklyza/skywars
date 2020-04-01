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
        if(!sender.isOp()) {
            sender.sendMessage("§cGame> §7You are not a moderator! Ask the server owner for more details.");
            return true;
        }
        if (args.length < 1) return false;

        String type = args[0];

        if (type.equalsIgnoreCase("start")) {
            if (args.length == 1) {
                announceStart();
                game.startCountdown();
                return true;
            } else if (args.length == 2) {
                announceStart();
                game.startCountdown(Integer.parseInt(args[1]));
                return true;
            }
        } else if (type.equalsIgnoreCase("stop") && args.length == 1) {
            announceStop();
            game.stopGame();
            return true;
        }

        return false;
    }

    private void announceStart() {
        plugin.getServer().broadcastMessage("§9Game> §7The game has been started manually by a moderator!");
    }

    private void announceStop() {
        plugin.getServer().broadcastMessage("§9Game> §7The game has been stopped manually by a moderator!");
    }
}
