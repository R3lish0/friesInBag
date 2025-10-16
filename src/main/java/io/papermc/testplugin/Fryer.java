package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

public class Fryer {
    private final Location location;
    private ArmorStand titleStand;
    private ArmorStand infoStand;

    public Fryer(World world, double x, double y, double z, String name, String info) {
        this.location = new Location(world, x, y, z);

        createFryer(name, info);
    }



    private void createFryer(String name, String info) {
        World world = location.getWorld();

        // Always align to the block center
        Location base = new Location(
                world,
                location.getBlockX() + 0.5,
                location.getBlockY(),
                location.getBlockZ() + 0.5
        );

        // Spawn name (top line)
        titleStand = (ArmorStand) world.spawnEntity(
                base.clone().add(0, 1.8, 0),
                EntityType.ARMOR_STAND
        );
        titleStand.customName(Component.text(name));
        titleStand.setCustomNameVisible(true);
        titleStand.setInvisible(true);
        titleStand.setMarker(true);
        titleStand.setGravity(false);

        // Spawn info (bottom line)
        infoStand = (ArmorStand) world.spawnEntity(
                base.clone().add(0, 1.5, 0),
                EntityType.ARMOR_STAND
        );
        infoStand.customName(Component.text(info));
        infoStand.setCustomNameVisible(true);
        infoStand.setInvisible(true);
        infoStand.setMarker(true);
        infoStand.setGravity(false);
    }


    public void updateInfo(String newInfo) {
        if (infoStand != null && !infoStand.isDead()) {
            infoStand.customName(Component.text(newInfo));
        }
    }

    public void remove() {
        if (titleStand != null && !titleStand.isDead()) titleStand.remove();
        if (infoStand != null && !infoStand.isDead()) infoStand.remove();
    }

    public Location getLocation() {
        return location;
    }
}
