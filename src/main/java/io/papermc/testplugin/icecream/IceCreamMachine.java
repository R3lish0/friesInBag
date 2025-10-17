package io.papermc.testplugin.icecream;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class IceCreamMachine {
    private final Location location;
    private ArmorStand titleStand;
    private ArmorStand infoStand;
    private UUID id;
    private int status = 1;
    private double odds;
    private boolean[] puzzle = {false, false, false, false, false};

    private Block puzzleButton;
    private final Map<Location, IceCreamLever> levers = new HashMap<>();



    public IceCreamMachine(World world, double x, double y, double z) {
        this.location = new Location(world, x, y, z);
        this.odds = 5;
        createIceCreamMachine(this.location);
    }




    private void createIceCreamMachine(Location location) {
        for(int i = 0; i <= 4; i++) {
            Block lever = location.getBlock();
            World world = lever.getWorld();
            String flavor = switch (i) {
                case 0 -> "Vanilla";
                case 1 -> "Chocolate";
                case 2 -> "Strawberry";
                case 3 -> "Lemon";
                case 4 -> "Cherry";
                default -> "";
            };

            if (i == 2)
            {
                Location buttonLocation = lever.getLocation().clone().subtract(-2, 2, 0);
                Block button = buttonLocation.getBlock();
                Bukkit.broadcast(Component.text("Block at " + buttonLocation + ": is a " + button.getType()));

                if(button.getType() == Material.STONE_BUTTON)
                {
                    puzzleButton = button;
                }
            }

            if (lever.getType() == Material.LEVER) {
                IceCreamLever iceCreamLever = new IceCreamLever(world, lever.getX()+i, lever.getY(), lever.getZ(), flavor, "R: 5");
                levers.put(iceCreamLever.getLocation(), iceCreamLever);
            }


        }
    }

    public void setStatus(boolean status) {
        this.status = status ? 1 : 0;
    }

    public Block getPuzzleButton() {
        return puzzleButton;
    }

    public int[] checkPuzzle() {
        int i = 0;
        int numCorrect = 0;
        int numIncorrect = 0;
        for (IceCreamLever lever : levers.values())
        {
            if (puzzle[i] == lever.isFlipped())
                numCorrect++;
            else
                numIncorrect++;
            i++;
        }
        return new int[]{numCorrect, numIncorrect};
    }

    public IceCreamLever getLever(Location location) {
        return levers.get(location);
    }


    public void generatePuzzle() {
        Random random = new Random();
        for(int i = 0; i <= 4; i++) {
            puzzle[i] = random.nextBoolean();
        }
    }

    public boolean rollOdds() {
        Random random = new Random();
        int num = random.nextInt(100)+1;
        if (num <= odds)
        {
            generatePuzzle();
            this.status = 0;
            return true;
        }
        return false;
    }


    public void raiseOdds() {
        odds += 5;
    }

    public boolean getStatus() {
        return status != 0;
    }

    public void resetOdds() {
        odds = 5;
    }

    public void fix(){
        this.status = 1;
        resetOdds();
        for(IceCreamLever lever : levers.values())
        {
            lever.reset();
        }

    }

    public UUID getId() {
        return id;
    }

    public void remove() {
        if (titleStand != null && !titleStand.isDead()) titleStand.remove();
        if (infoStand != null && !infoStand.isDead()) infoStand.remove();
    }

    public Location getLocation() {
        return location;
    }
}
