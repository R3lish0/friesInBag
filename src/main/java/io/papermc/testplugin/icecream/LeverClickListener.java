package io.papermc.testplugin.icecream;

import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

public class LeverClickListener implements Listener {

    private final IceCreamMachine iceCreamMachine;
    private final Plugin plugin;

    public LeverClickListener(IceCreamMachine machine, Plugin plugin) {
        this.iceCreamMachine = machine;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();
        if (block == null || !(block.getBlockData() instanceof Switch)) return;

        IceCreamLever lever = iceCreamMachine.getLever(block.getLocation());
        if (lever != null) {
            if (iceCreamMachine.getStatus()) {
                if (!iceCreamMachine.rollOdds()) {
                    lever.flipLever(player, plugin, iceCreamMachine.getStatus());
                    iceCreamMachine.raiseOdds();
                }
            }
            else {
                lever.flipLever(player, plugin, iceCreamMachine.getStatus());
            }
        }
    }
}
