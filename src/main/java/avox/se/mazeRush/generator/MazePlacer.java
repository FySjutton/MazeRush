package avox.se.mazeRush.generator;

import avox.se.mazeRush.structure.MazeBlock;
import avox.se.mazeRush.structure.MazeMap;
import org.bukkit.Location;
import org.bukkit.Rotation;
import org.bukkit.World;
import org.bukkit.block.structure.Mirror;
import org.bukkit.block.structure.StructureRotation;
import org.bukkit.structure.Structure;
import org.joml.Vector2d;

import java.util.Map;
import java.util.Random;

public class MazePlacer {
    private final MazeMap mazeMap;
    private final int yLevel;

    public MazePlacer(MazeMap mazeMap, int yLevel) {
        this.mazeMap = mazeMap;
        this.yLevel = yLevel;
    }

    public MazeBlock[][] place(World world, boolean[][][] walls) {
        int width = walls.length;
        int height = walls[0].length;
        int[] center = MazeGenerator.getCenterCell(width, height);
        int cx = center[0], cz = center[1];

        MazeBlock[][] blocks = new MazeBlock[width][height];
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                if (Math.abs(x - cx) <= 1 && Math.abs(z - cz) <= 1) continue;

                boolean n = walls[x][z][0];
                boolean e = walls[x][z][1];
                boolean s = walls[x][z][2];
                boolean w = walls[x][z][3];

                RoomType room = resolveRoom(n, e, s, w);
                MazeBlock.MazeBlockType block = mazeMap.getBlock(room.type);
                blocks[x][z] = new MazeBlock(new Vector2d(x, z), block);
                Structure structure = block.structure();

                Location loc = getLocation(world, x, z, room.rotation);
                structure.place(loc, true, room.rotation, Mirror.NONE, 0, 1.0f, new Random());
            }
        }

        int[] coords = MazeGenerator.getCenterCell(width, height);
        mazeMap.spawn.place(new Location(world, (coords[0] - 1) * mazeMap.roomSize, 100, (coords[1] - 1) * mazeMap.roomSize), true, StructureRotation.NONE, Mirror.NONE, 0, 1.0f, new Random());
        return blocks;
    }

    private RoomType resolveRoom(boolean n, boolean e, boolean s, boolean w) {
        int open = (n?1:0) + (e?1:0) + (s?1:0) + (w?1:0);

        return switch (open) {
            case 3 -> resolve3Way(n, e, s, w);
            case 2 -> resolve2Way(n, e, s, w);
            case 1 -> resolve1Way(n, e, s, w);
            default -> new RoomType(MazeMap.BlockType.WAY_4, StructureRotation.NONE);
        };
    }

    // 3_way.nbt -> NSE (N+S+E open, W closed)
    private RoomType resolve3Way(boolean n, boolean e, boolean s, boolean w) {
        if (!w) return new RoomType(MazeMap.BlockType.WAY_3, StructureRotation.NONE); // NSE
        if (!n) return new RoomType(MazeMap.BlockType.WAY_3, StructureRotation.CLOCKWISE_90); // SEW
        if (!e) return new RoomType(MazeMap.BlockType.WAY_3, StructureRotation.CLOCKWISE_180); // NSW
        else return new RoomType(MazeMap.BlockType.WAY_3, StructureRotation.COUNTERCLOCKWISE_90); // NEW
    }

    // straight.nbt -> NS, corner.nbt at NE
    private RoomType resolve2Way(boolean n, boolean e, boolean s, boolean w) {
        if (n && s) return new RoomType(MazeMap.BlockType.STRAIGHT, StructureRotation.NONE); // NS
        if (e && w) return new RoomType(MazeMap.BlockType.STRAIGHT, StructureRotation.CLOCKWISE_90); // EW
        if (n && e) return new RoomType(MazeMap.BlockType.CORNER, StructureRotation.NONE); // NE
        if (e && s) return new RoomType(MazeMap.BlockType.CORNER, StructureRotation.CLOCKWISE_90); // SE
        if (s && w) return new RoomType(MazeMap.BlockType.CORNER, StructureRotation.CLOCKWISE_180); // SW
        else return new RoomType(MazeMap.BlockType.CORNER, StructureRotation.COUNTERCLOCKWISE_90); // NW
    }

    // deadend.nbt -> N open
    private RoomType resolve1Way(boolean n, boolean e, boolean s, boolean w) {
        if (n) return new RoomType(MazeMap.BlockType.DEADEND, StructureRotation.NONE);
        if (e) return new RoomType(MazeMap.BlockType.DEADEND, StructureRotation.CLOCKWISE_90);
        if (s) return new RoomType(MazeMap.BlockType.DEADEND, StructureRotation.CLOCKWISE_180);
        else return new RoomType(MazeMap.BlockType.DEADEND, StructureRotation.COUNTERCLOCKWISE_90);
    }

    private Location getLocation(World world, int x, int z, StructureRotation rotation) {
        int bx = x * mazeMap.roomSize;
        int bz = z * mazeMap.roomSize;
        int s = mazeMap.roomSize - 1; // 8

        return switch (rotation) {
            case NONE -> new Location(world, bx, yLevel, bz );
            case CLOCKWISE_90 -> new Location(world, bx + s, yLevel, bz );
            case CLOCKWISE_180 -> new Location(world, bx + s, yLevel, bz + s);
            case COUNTERCLOCKWISE_90 -> new Location(world, bx, yLevel, bz + s);
        };
    }

    private record RoomType(MazeMap.BlockType type, StructureRotation rotation) {}
}