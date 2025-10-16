package io.papermc.testplugin;

import com.sk89q.worldedit.math.BlockVector3;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ExamplePlugin extends JavaPlugin implements Listener {
    
    // Track each player's McDonald's location and dimensions
    private final Map<UUID, Location> mcDonaldsLocations = new HashMap<>();
    private final Map<UUID, BlockVector3> mcDonaldsDimensions = new HashMap<>();
    private final Map<UUID, McDonaldsBuilding> mcDonaldsObjects = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        this.getCommand("startshift").setExecutor(new StartShift(this));
        this.getCommand("endshift").setExecutor(new EndShift(this));
        this.getCommand("cc").setExecutor(new CC(this));
        getServer().getPluginManager().registerEvents(new CompassHitListener(this), this);


        getLogger().info("Hello, World!");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));
    }




    public Location getMcDonaldsLocation(Player player) {
        return mcDonaldsLocations.get(player.getUniqueId());
    }

    /**
     * Builds a McDonald's and teleports the player to it
     * @param player The player to teleport
     */
    public void buildMcDonaldsAndTeleport(Player player) {





        McDonaldsBuilding mcDonalds = new McDonaldsBuilding(player);
        BlockVector3 dimensions = mcDonalds.getDimensions();
        Location buildLocation = mcDonalds.getLocation();

        // Store the location and dimensions for this player
        mcDonaldsLocations.put(player.getUniqueId(), buildLocation);
        mcDonaldsDimensions.put(player.getUniqueId(), dimensions);
        mcDonaldsObjects.put(player.getUniqueId(), mcDonalds);
        getServer().getPluginManager().registerEvents(new LeverClickListener(mcDonalds.getIceCreamMachine()), this);


        // Teleport player to the entrance (center of structure, ground level)
        getLogger().info("Teleporting player to McDonald's entrance...");
        getLogger().info("X: "+ String.valueOf(buildLocation.getBlockX())+ "Y: "+ String.valueOf(buildLocation.getBlockY())+ "Z: "+ String.valueOf(buildLocation.getBlockZ()));
        Location teleportLoc = buildLocation.clone().subtract(-dimensions.x()/2.0-8, 2, dimensions.z()-6);
        teleportLoc.setYaw(teleportLoc.getYaw() + 90);
        player.teleport(teleportLoc);

        player.sendMessage(Component.text("Welcome to McDonald's! Your shift begins now."));
        getLogger().info("McDonald's built at: " + buildLocation.getBlockX() + ", " + buildLocation.getBlockY() + ", " + buildLocation.getBlockZ());
    }

    /**
     * Demolishes the player's McDonald's structure
     * @param player The player whose McDonald's should be demolished
     * @return true if a McDonald's was found and demolished, false otherwise
     */
    public boolean demolishMcDonalds(Player player) {
        Location buildLocation = mcDonaldsLocations.get(player.getUniqueId());
        BlockVector3 dimensions = mcDonaldsDimensions.get(player.getUniqueId());

        if (buildLocation == null) {
            return false;
        }

        // Demolish the structure
        McDonaldsBuilding.demolish(buildLocation, dimensions);

        // Remove from tracking
        mcDonaldsLocations.remove(player.getUniqueId());
        mcDonaldsDimensions.remove(player.getUniqueId());

        getLogger().info("McDonald's demolished at: " + buildLocation.getBlockX() + ", " + buildLocation.getBlockY() + ", " + buildLocation.getBlockZ());
        return true;
    }
}
