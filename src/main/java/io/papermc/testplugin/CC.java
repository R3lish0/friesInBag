package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class CC implements CommandExecutor {

    private final ExamplePlugin plugin;

    public CC(ExamplePlugin plugin) {
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

        Location playerLocation = player.getLocation();
        Location origin = plugin.getMcDonaldsLocation(player);

        int dx = playerLocation.getBlockX() - origin.getBlockX();
        int dy = playerLocation.getBlockY() - origin.getBlockY();
        int dz = playerLocation.getBlockZ() - origin.getBlockZ();

        player.sendMessage(Component.text(
                "Distance from McDonald's → X: " + dx + " | Y: " + dy + " | Z: " + dz
        ));











        return true;
    }

}
