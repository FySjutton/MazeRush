package avox.se.mazeRush.structure;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.structure.Structure;
import org.bukkit.structure.StructureManager;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;

public class StructureLoader {
    public static final ArrayList<MazeMap> mazeMaps = new ArrayList<>();
    private final Plugin plugin;

    public StructureLoader(Plugin plugin) {
        this.plugin = plugin;
    }

    public void loadAll() {
        File folder = new File(plugin.getDataFolder(), "maps");

        StructureManager manager = Bukkit.getServer().getStructureManager();

        File[] maps = folder.listFiles(File::isDirectory);
        if (maps == null) return;
        for (File mapFile : maps) {
            File[] dataFile = mapFile.listFiles(f -> f.getName().equals("data.json"));
            if (dataFile == null || dataFile.length == 0) continue;
            System.out.println(dataFile.length + " " + dataFile[0].getName());

            MazeMap map;
            JsonObject data;
            try {
                Reader reader = new FileReader(dataFile[0]);
                data = new Gson().fromJson(reader, JsonObject.class);

                map = new MazeMap(
                    data.get("map").getAsString(),
                    data.get("creator").getAsString(),
                    data.get("room_size").getAsInt(),
                    data.get("map_size").getAsInt(),
                    data.get("loop_chance").getAsFloat()
                );

                JsonObject files = data.get("files").getAsJsonObject();
                String mapName = mapFile.getName();
                if (!(
                    getMaps(manager, map, mapName, files, "4_way", MazeMap.BlockType.WAY_4) &&
                    getMaps(manager, map, mapName, files, "3_way", MazeMap.BlockType.WAY_3) &&
                    getMaps(manager, map, mapName, files, "straight", MazeMap.BlockType.STRAIGHT) &&
                    getMaps(manager, map, mapName, files, "corner", MazeMap.BlockType.CORNER) &&
                    getMaps(manager, map, mapName, files, "deadend", MazeMap.BlockType.DEADEND))
                ) {
                    plugin.getLogger().severe("Failed loading sub-blocks!");
                    continue;
                }
                mazeMaps.add(map);
            } catch (Exception e) {
                plugin.getLogger().severe("Error loading maze map!");
                e.printStackTrace();
            }
        }
    }

    private boolean getMaps(StructureManager manager, MazeMap map, String mapName, JsonObject files, String folderName, MazeMap.BlockType type) {
        try {
            JsonArray list = files.get(folderName).getAsJsonArray();

            for (JsonElement element : list) {
                JsonObject block = element.getAsJsonObject();

                File file = new File(plugin.getDataFolder(), "maps/" + mapName + "/" + folderName + "/" + block.get("name").getAsString());
                System.out.println(file.getAbsolutePath());
                if (!file.exists()) {
                    return false;
                }

                Structure structure = manager.loadStructure(file);
                map.blocks.computeIfAbsent(type, _ -> new ArrayList<>()).add(new MazeBlock(structure, block.get("chance").getAsFloat()));
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}