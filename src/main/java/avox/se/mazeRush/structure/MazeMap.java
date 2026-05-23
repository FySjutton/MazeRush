package avox.se.mazeRush.structure;

import org.bukkit.structure.Structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MazeMap {
    public String name;
    public String creator;
    public int roomSize;
    public int mapSize;
    public float loopChance;

    public final HashMap<BlockType, ArrayList<MazeBlock.MazeBlockType>> blocks = new HashMap<>();
    public final Structure spawn;

    public MazeMap(String name, String creator, int roomSize, int mapSize, float loopChance, Structure spawn) {
        this.name = name;
        this.creator = creator;
        this.roomSize = roomSize;
        this.mapSize = mapSize;
        this.loopChance = loopChance;
        this.spawn = spawn;
    }

    public MazeBlock.MazeBlockType getBlock(BlockType type) {
        ArrayList<MazeBlock.MazeBlockType> blockList = blocks.get(type);
        Random rand = new Random();

        double total = blockList.stream().mapToDouble(MazeBlock.MazeBlockType::chance).sum();
        double roll = rand.nextDouble() * total;
        double cumulative = 0;

        for (MazeBlock.MazeBlockType item : blockList) {
            cumulative += item.chance();
            if (roll < cumulative) return item;
        }

        return blockList.getLast();
    }

    public enum BlockType {
        WAY_4,
        WAY_3,
        STRAIGHT,
        CORNER,
        DEADEND
    }
}
