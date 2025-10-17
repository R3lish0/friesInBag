package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Location;

public class ClockHitListener implements Listener {

    private final JavaPlugin plugin;

    public ClockHitListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClockHit(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        // Only when holding a compass
        if (player.getInventory().getItemInMainHand().getType() != Material.COMPASS)
            return;

        // Only when left-clicking a block
        if (event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        event.setCancelled(true); // stop teleport or default behavior

        Block block = event.getClickedBlock();
        if (block == null)
            return;

        Location blockLoc = block.getLocation();
        Location origin = ((ExamplePlugin) plugin).getMcDonaldsLocation(player);

        // Get relative coordinates
        int dx = blockLoc.getBlockX() - origin.getBlockX();
        int dy = blockLoc.getBlockY() - origin.getBlockY();
        int dz = blockLoc.getBlockZ() - origin.getBlockZ();

        player.sendMessage(Component.text(
                "Block: " + block.getType() + "\n" +
                        "Offset from McDonald's → X: " + dx + " | Y: " + dy + " | Z: " + dz
        ));
    }
}
