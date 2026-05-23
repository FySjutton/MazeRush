package avox.se.mazeRush.events;

import avox.se.mazeRush.game.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class MovementListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        event.setCancelled(true);

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (Game.INSTANCE == null) return;
        if (!Game.INSTANCE.started && event.hasChangedPosition()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (Game.INSTANCE == null) return;
        if (!Game.INSTANCE.started) {
            event.setCancelled(true);
        }
    }
}
