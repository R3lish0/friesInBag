package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class EndShift implements CommandExecutor {

    private final ExamplePlugin plugin;

    public EndShift(ExamplePlugin plugin) {
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

        boolean demolished = plugin.demolishMcDonalds(player);
        
        if (demolished) {
            player.sendMessage(Component.text("Your shift has ended. McDonald's has been demolished."));
        } else {
            player.sendMessage(Component.text("You don't have an active McDonald's to demolish!"));
        }

        return true;
    }
}
