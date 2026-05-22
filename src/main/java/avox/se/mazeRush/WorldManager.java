package avox.se.mazeRush;

import org.bukkit.*;
import org.bukkit.generator.ChunkGenerator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Random;
import java.util.UUID;

public class WorldManager {
    public static class EmptyChunkGenerator extends ChunkGenerator {
        @Override
        public Location getFixedSpawnLocation(@NotNull World world, @NotNull Random random) {
            return new Location(world, 0, 64, 0);
        }
    }

    public static World createWorld() {
        String instanceId = "maze_rush_" + UUID.randomUUID();

        World world = new WorldCreator(instanceId)
            .generator(new EmptyChunkGenerator())
            .environment(World.Environment.NORMAL)
            .createWorld();

        world.setAutoSave(false);
        world.setSpawnFlags(false, false);

        return world;
    }

    public static void unloadAndDeleteArenaWorld(String instanceId) {
        World world = Bukkit.getWorld(instanceId);
        if (world != null) {
            Bukkit.unloadWorld(world, false);
        }

        File dir = new File(Bukkit.getWorldContainer(), instanceId);
        deleteDirectory(dir.toPath());
    }

    public static void cleanOldArenaWorlds() {
        File worldContainer = Bukkit.getWorldContainer();
        File[] files = worldContainer.listFiles();

        if (files == null) return;

        for (File file : files) {
            if (file.isDirectory() && file.getName().startsWith("t2r_game_")) {
                deleteDirectory(file.toPath());
            }
        }
    }

    private static void deleteDirectory(Path path) {
        if (!Files.exists(path)) return;
        try {
            Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .forEach(p -> {
                    try {
                        Files.delete(p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
