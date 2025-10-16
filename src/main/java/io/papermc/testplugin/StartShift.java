package io.papermc.testplugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class StartShift implements CommandExecutor {

    private final ExamplePlugin plugin;

    public StartShift(ExamplePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(
            CommandSender sender,
            Command command,
            String label,
            String[] args
    ) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Only players can use this command!"));
            return true;
        }

        player.sendMessage(Component.text("Building your McDonald's restaurant..."));
        plugin.buildMcDonaldsAndTeleport(player);


        gameLoop(player);



        return true;
    }


    public void gameLoop(Player player) {
        player.sendMessage(Component.text("Buckle up the gameloop just started"));
    }
}
