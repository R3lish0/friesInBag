package io.papermc.testplugin.icecream;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.block.data.type.Switch;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public class IceCreamLever {
    private final Location location;
    private ArmorStand titleStand;
    private ArmorStand infoStand;
    private String flavor;
    private UUID id;
    private Block lever;
    private boolean isFlipped = false;
    private int count = 5;

    public IceCreamLever(World world, double x, double y, double z, String name, String info) {
        this.location = new Location(world, x, y, z);
        this.flavor = name;
        this.id = UUID.randomUUID();
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


    public void flipLever(Player player, Plugin plugin, boolean status) {
        Block block = location.getBlock();
        if (!(block.getBlockData() instanceof Switch leverData)) return;



        // Update armor stand info line
        if (infoStand != null && !infoStand.isDead() && count > 0 && status) {
            isFlipped = true;
            this.count--;

            String statusText = "R: " + this.count;
            infoStand.customName(Component.text(statusText));

            dispense(player);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                leverData.setPowered(false);
                block.setBlockData(leverData);
                isFlipped = false;
            }, 8L);

        }
        else if(status && count <= 0)
        {
            player.sendMessage("I'm out of ice cream");
        }
        else if(!status)
        {
            isFlipped = !isFlipped;
        }

    }

    public void dispense(Player player)
    {
        player.sendMessage("🍦 Ice cream machine works ... for now");

        Material material = switch (flavor) {
            case "Vanilla" -> Material.WHITE_DYE;
            case "Chocolate" -> Material.BROWN_DYE;
            case "Strawberry" -> Material.PINK_DYE;
            case "Cherry" -> Material.RED_DYE;
            case "Lemon" -> Material.YELLOW_DYE;
            default -> null;
        };

        if (material != null) {
            ItemStack iceCream = new ItemStack(material);
            ItemMeta meta = iceCream.getItemMeta();

            meta.displayName(Component.text(flavor + " Ice Cream")
                    .color(NamedTextColor.GOLD)); // optional color
            iceCream.setItemMeta(meta);

            player.getInventory().addItem(iceCream);
        }
    }

    public void updateInfo(String newInfo) {
        if (infoStand != null && !infoStand.isDead()) {
            infoStand.customName(Component.text(newInfo));
        }
    }

    public String getFlavor() {
        return flavor;
    }

    public UUID getId() {
        return id;
    }

    public void reset()
    {
        Block block = location.getBlock();
        if (!(block.getBlockData() instanceof Switch leverData)) return;
        leverData.setPowered(false);
        block.setBlockData(leverData);
        isFlipped = false;
    }


    public boolean isFlipped() {
        return isFlipped;
    }

    public void remove() {
        if (titleStand != null && !titleStand.isDead()) titleStand.remove();
        if (infoStand != null && !infoStand.isDead()) infoStand.remove();
    }

    public Location getLocation() {
        return location;
    }
}
