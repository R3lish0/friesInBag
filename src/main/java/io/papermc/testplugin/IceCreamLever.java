package io.papermc.testplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.block.data.type.Switch;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class IceCreamLever {
    private final Location location;
    private ArmorStand titleStand;
    private ArmorStand infoStand;
    private String flavor;
    private UUID id;
    private boolean isFlipped;
    private Block lever;
    private int count = 5;

    public IceCreamLever(World world, double x, double y, double z, String name, String info) {
        this.location = new Location(world, x, y, z);
        this.flavor = name;
        this.id = UUID.randomUUID();
        this.isFlipped = false;
        this.lever = location.getBlock();

        createLever(this.flavor, info);
    }

    private void createLever(String name, String info) {
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


    public void flipLever() {
        isFlipped = !isFlipped;

        Block block = location.getBlock();
        if (!(block.getBlockData() instanceof Switch leverData)) return;

        leverData.setPowered(isFlipped);  // flips the lever
        block.setBlockData(leverData);

        // Update armor stand info line
        if (infoStand != null && !infoStand.isDead() && isFlipped) {
            this.count--;
            String statusText = "R: " + this.count;
            infoStand.customName(Component.text(statusText));
        }

        Bukkit.broadcast(Component.text("Lever at " + location.getBlockX() + "," +
                location.getBlockY() + "," + location.getBlockZ() + " is now " + (isFlipped ? "ON" : "OFF")));
    }

    public void updateInfo(String newInfo) {
        if (infoStand != null && !infoStand.isDead()) {
            infoStand.customName(Component.text(newInfo));
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
