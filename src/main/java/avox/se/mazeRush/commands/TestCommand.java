package avox.se.mazeRush.commands;

import avox.se.mazeRush.WorldManager;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class TestCommand {
    public LiteralCommandNode<CommandSourceStack> build() {
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

                    World world = WorldManager.createWorld();
                    sender.teleport(new Location(world, 0, 100, 0));

                    return 0;
                }))

            .build();
    }

    public void register(io.papermc.paper.command.brigadier.Commands commands) {
        commands.register(build(), "Test command", List.of());
    }
}
