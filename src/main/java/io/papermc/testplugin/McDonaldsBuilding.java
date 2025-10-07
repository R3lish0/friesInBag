package io.papermc.testplugin;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getLogger;


public class McDonaldsBuilding {

    /**
     * Loads and places a McDonald's schematic at the specified location
     * @param baseLocation The base location where the structure will be built
     * @param schematicFile The schematic file to load
     * @return The size of the structure (for demolishing later), or null if failed
     */
    public static BlockVector3 build(Location baseLocation, File schematicFile) {
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(baseLocation.getWorld());
        BlockVector3 position = BlockVector3.at(
                baseLocation.getBlockX(),
                baseLocation.getBlockY(),
                baseLocation.getBlockZ()
        );

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            // Detect the format of the schematic
            ClipboardFormat format = ClipboardFormats.findByFile(schematicFile);
            if (format == null) {
                throw new IOException("Unknown schematic format for file: " + schematicFile.getName());
            }

            // Load the schematic
            try (ClipboardReader reader = format.getReader(new FileInputStream(schematicFile))) {
                Clipboard clipboard = reader.read();

                // Paste the schematic at the location
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(position)
                        .ignoreAirBlocks(false)
                        .build();

                Operations.complete(operation);

                // Return the dimensions for later demolition
                return clipboard.getDimensions();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Overloaded method that uses default schematic name
     * @param baseLocation The base location where the structure will be built
     * @return The size of the structure, or null if failed
     */
    public static BlockVector3 build(Location baseLocation) {
        // Look for schematic in plugin's schematics folder
        File schematicFile = new File("plugins/WorldEdit/schematics/mcdonalds.schem");
        
        // Also try .schematic format (older WorldEdit format)
        if (!schematicFile.exists()) {
            schematicFile = new File("plugins/WorldEdit/schematics/mcdonalds.schematic");
        }

        if (!schematicFile.exists()) {
            System.err.println("McDonald's schematic not found! Expected at: " + schematicFile.getAbsolutePath());
            System.err.println("Please save your McDonald's structure using WorldEdit:");
            System.err.println("  1. Select the structure with //wand");
            System.err.println("  2. Copy it with //copy");
            System.err.println("  3. Save it with //schem save mcdonalds");
            return null;
        }

        return build(baseLocation, schematicFile);
    }

    /**
     * Removes/demolishes the McDonald's structure at the specified location
     * @param baseLocation The base location where the structure was built
     * @param dimensions The dimensions of the structure to remove
     */
    public static void demolish(Location baseLocation, BlockVector3 dimensions) {
        if (dimensions == null) {
            // Fallback to default size if dimensions not available
            dimensions = BlockVector3.at(15, 11, 15);
        }

        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(baseLocation.getWorld());
        int x = baseLocation.getBlockX();
        int y = baseLocation.getBlockY();
        int z = baseLocation.getBlockZ();
        getLogger().info("X: "+ String.valueOf(x)+ "Y: "+ String.valueOf(y)+ "Z: "+ String.valueOf(z));

        try (EditSession editSession = WorldEdit.getInstance().newEditSession(weWorld)) {
            // Clear the structure area based on its dimensions
            CuboidRegion region = new CuboidRegion(
                BlockVector3.at(x, y, z),
                BlockVector3.at(x + dimensions.x()+3,y - dimensions.y(),z - dimensions.z()-3)
            );
            editSession.setBlocks(region, BlockTypes.AIR.getDefaultState());

            Operation op = editSession.commit();
            if(op != null) {
                getLogger().info("Committing edit session...");
                Operations.complete(op);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets a random location in the world, 50 blocks above the highest block
     * @param world The world to place the structure in
     * @param centerX Center X coordinate
     * @param centerZ Center Z coordinate
     * @param radius Radius to search for random location
     * @return Location 50 blocks above the highest block
     */
    public static Location getRandomLocationAboveGround(World world, int centerX, int centerZ, int radius) {
        // Generate random offset within radius
        int randomX = centerX + (int) (Math.random() * radius * 2) - radius;
        int randomZ = centerZ + (int) (Math.random() * radius * 2) - radius;

        // Get the highest block at this location
        int highestY = world.getHighestBlockYAt(randomX, randomZ);

        // Return location 50 blocks above
        return new Location(world, randomX, highestY + 50, randomZ);
    }
}
