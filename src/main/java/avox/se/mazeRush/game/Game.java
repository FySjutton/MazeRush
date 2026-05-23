package avox.se.mazeRush.game;

import avox.se.mazeRush.structure.MazeBlock;
import avox.se.mazeRush.structure.MazeMap;
import org.bukkit.Location;

import java.util.ArrayList;

public class Game {
    private Location origin;
    private MazeMap map;
    private MazeBlock[][] blockList;

    public Game(Location origin, MazeMap map, MazeBlock[][] blockList) {
        this.origin = origin;
        this.map = map;
        this.blockList = blockList;
    }
}
