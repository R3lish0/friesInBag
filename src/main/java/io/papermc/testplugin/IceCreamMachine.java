package io.papermc.testplugin;

import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class IceCreamMachine {
    private final Location location;
    private ArmorStand titleStand;
    private ArmorStand infoStand;
    private UUID id;

    private final Map<Location, IceCreamLever> levers = new HashMap<>();



    public IceCreamMachine(World world, double x, double y, double z) {
        this.location = new Location(world, x, y, z);

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

            if (lever.getType() == Material.LEVER) {
                IceCreamLever iceCreamLever = new IceCreamLever(world, lever.getX()+i, lever.getY(), lever.getZ(), flavor, "R: 5");
                levers.put(iceCreamLever.getLocation(), iceCreamLever);
            }


        }
    }


    public IceCreamLever getLever(Location location) {
        return levers.get(location);
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
