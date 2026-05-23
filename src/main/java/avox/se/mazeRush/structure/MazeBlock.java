package avox.se.mazeRush.structure;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.structure.Structure;
import org.joml.Vector2d;

import java.util.Objects;

public final class MazeBlock {
    public Vector2d coordinate;
    public MazeBlockType mazeBlockType;

    public MazeBlock(Vector2d coordinate, MazeBlockType mazeBlockType) {
        this.coordinate = coordinate;
        this.mazeBlockType = mazeBlockType;
    }

    public record MazeBlockType(Structure structure, float chance, boolean spawnable) {}
}