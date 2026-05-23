package avox.se.mazeRush.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MazeMap {
    public String name;
    public String creator;
    public int roomSize;
    public int mapSize;
    public float loopChance;

    public final HashMap<BlockType, ArrayList<MazeBlock>> blocks = new HashMap<>();

    public MazeMap(String name, String creator, int roomSize, int mapSize, float loopChance) {
        this.name = name;
        this.creator = creator;
        this.roomSize = roomSize;
        this.mapSize = mapSize;
        this.loopChance = loopChance;
    }

    public MazeBlock getBlock(BlockType type) {
        ArrayList<MazeBlock> blockList = blocks.get(type);
        Random rand = new Random();

        double total = blockList.stream().mapToDouble(MazeBlock::chance).sum();
        double roll = rand.nextDouble() * total;
        double cumulative = 0;

        for (MazeBlock item : blockList) {
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
