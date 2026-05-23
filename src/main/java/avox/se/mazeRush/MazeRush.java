package avox.se.mazeRush;

import avox.se.mazeRush.commands.TestCommand;
import avox.se.mazeRush.structure.StructureLoader;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MazeRush extends JavaPlugin {

    @Override
    public void onEnable() {
        new File(getDataFolder(), "maps").mkdirs();

        new StructureLoader(this).loadAll();
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, commands -> new TestCommand().register(commands.registrar()));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
