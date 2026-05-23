package avox.se.mazeRush.game;

import avox.se.mazeRush.structure.MazeBlock;
import avox.se.mazeRush.structure.MazeMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

public class Game {
    public static Game INSTANCE;
    public Plugin plugin;
    public boolean started = false;
    public HashMap<Player, GamePlayer> players = new HashMap<>();

    private MazeMap map;
    private World world;
    private MazeBlock[][] blockList;

    public Game(Plugin plugin, MazeMap map, World world, MazeBlock[][] blockList, List<Player> players) {
        this.plugin = plugin;
        this.map = map;
        this.world = world;
        this.blockList = blockList;

        for (Player player : players) {
            this.players.put(player, new GamePlayer(player));
        }

        List<Integer> indices = IntStream.range(0, blockList.length * blockList[0].length)
            .filter(i -> blockList[i / blockList[0].length][i % blockList[0].length].spawnable())
            .boxed()
            .toList();

        List<CompletableFuture<Boolean>> futures = players.stream()
            .map(player -> {
                int index = indices.get(new Random().nextInt(indices.size()));
                int x = index / blockList[0].length;
                int z = index % blockList[0].length;
                return player.teleportAsync(new Location(world, x * map.roomSize + (double) map.roomSize / 2, 102, z * map.roomSize + (double) map.roomSize / 2));
            })
            .toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
            .thenRun(() -> Bukkit.getScheduler().runTaskLater(plugin, () -> {
                for (int i = 5; i > 0; i--) {
                    final int count = i;
                    Bukkit.getScheduler().runTaskLater(plugin, () -> Bukkit.getOnlinePlayers().forEach(p -> p.showTitle(Title.title(Component.text(String.valueOf(count)), Component.empty()))), (5 - i) * 20L);
                }

                Bukkit.getScheduler().runTaskLater(plugin, () -> started = true, 5 * 20L);
            }, 5 * 20L));
    }
}
