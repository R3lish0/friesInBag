package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class LeverClickListener implements Listener {

    private final IceCreamMachine iceCreamMachine;

    public LeverClickListener(IceCreamMachine machine) {
        this.iceCreamMachine = machine;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        if (block == null || !(block.getBlockData() instanceof Switch)) return;

        IceCreamLever lever = iceCreamMachine.getLever(block.getLocation());
        if (lever != null) {
            lever.flipLever();
        }
    }
}
