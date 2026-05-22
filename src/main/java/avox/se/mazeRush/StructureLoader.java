package avox.se.mazeRush;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class StructureLoader {
    public static final Map<String, Structure> structures = new HashMap<>();
    private final Plugin plugin;

    public StructureLoader(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {
        File folder = new File(plugin.getDataFolder(), "structures");
        folder.mkdirs();

        StructureManager manager = Bukkit.getServer().getStructureManager();

        for (File file : folder.listFiles((file) -> file.getName().endsWith(".nbt"))) {
            String name = file.getName().replace(".nbt", "");
            try {
                Structure structure = manager.loadStructure(file);
                structures.put(name, structure);
                plugin.getLogger().info("Loaded structure: " + name);
            } catch (IOException e) {
                plugin.getLogger().severe("Failed to load structure: " + name);
            }
        }
    }
}