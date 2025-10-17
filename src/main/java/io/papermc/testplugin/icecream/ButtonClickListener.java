package io.papermc.testplugin.icecream;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class ButtonClickListener implements Listener {

    private final IceCreamMachine machine;
    private final Plugin plugin;

    public ButtonClickListener(IceCreamMachine machine, Plugin plugin) {
        this.machine = machine;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block block = event.getClickedBlock();
        Player player = event.getPlayer();


        if (block == null || !(block.getBlockData() instanceof Switch)) return;
        if (block.getLocation().equals(machine.getPuzzleButton().getLocation()) && !machine.getStatus()) {
            int[] response = machine.checkPuzzle();
            Component message = getResultsComponent(response);

            if (response[0] == 5) {
                machine.fix();
            }


            player.sendMessage(message);

            player.sendMessage(Component.text("You clicked the button!"));
        }

    }

    private static @NotNull Component getResultsComponent(int[] response) {
        int numCorrect = response[0];
        int numIncorrect = response[1];
        String correct = numCorrect + " correct";
        String incorrect = numIncorrect + " incorrect";

        Component message = Component.text("Results: ", NamedTextColor.GRAY)
                .append(Component.text(numCorrect + " correct")
                        .color(NamedTextColor.GREEN)
                        .decorate(TextDecoration.BOLD))
                .append(Component.text(" | ", NamedTextColor.DARK_GRAY))
                .append(Component.text(numIncorrect + " incorrect")
                        .color(NamedTextColor.RED)
                        .decorate(TextDecoration.BOLD));
        return message;
    }
}
