package avox.se.mazeRush.commands;

import avox.se.mazeRush.WorldManager;
import avox.se.mazeRush.game.Game;
import avox.se.mazeRush.generator.MazeGenerator;
import avox.se.mazeRush.generator.MazePlacer;
import avox.se.mazeRush.structure.MazeBlock;
import avox.se.mazeRush.structure.MazeMap;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Random;

import static avox.se.mazeRush.structure.StructureLoader.mazeMaps;

public class TestCommand {
    public LiteralCommandNode<CommandSourceStack> build(Plugin plugin) {
        return Commands.literal("test")
            .then(Commands.literal("check")
                .executes(ctx -> {
                    Player sender = (Player) ctx.getSource().getSender();

                    sender.sendMessage("Hello World!");

                    return 0;
                }))
            .then(Commands.literal("start")
                .executes(ctx -> {
                    Player sender = (Player) ctx.getSource().getSender();

                    MazeMap mazeMap = mazeMaps.getFirst();

                    World world = WorldManager.createWorld();

                    boolean[][][] maze = MazeGenerator.generate(12, 12, 0.12f, new Random());
                    MazeBlock[][] blocks = new MazePlacer(mazeMap, 100).place(world, maze);

                    Game.INSTANCE = new Game(plugin, mazeMap, world, blocks, List.of(sender));

                    return 0;
                }))

            .build();
    }

    public void register(Commands commands, Plugin plugin) {
        commands.register(build(plugin), "Test command", List.of());
    }
}
