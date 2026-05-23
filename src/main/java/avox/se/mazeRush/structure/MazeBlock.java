package avox.se.mazeRush.structure;

import org.bukkit.structure.Structure;

public record MazeBlock(Structure structure, float chance, boolean spawnable) {}