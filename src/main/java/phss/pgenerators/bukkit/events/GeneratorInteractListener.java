package phss.pgenerators.bukkit.events;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import com.bgsoftware.superiorskyblock.api.island.IslandPrivilege;
import com.bgsoftware.superiorskyblock.api.wrappers.SuperiorPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import phss.pgenerators.PGenerators;
import phss.pgenerators.bukkit.view.UpgradeMenuView;

import java.util.HashMap;
import java.util.Map;

public class GeneratorInteractListener implements Listener {

    final private PGenerators plugin;

    public GeneratorInteractListener(PGenerators plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            plugin.generatorManager.findGeneratorAt(event.getClickedBlock().getLocation()).ifPresent(generator -> {
                Island island = SuperiorSkyblockAPI.getIslandAt(generator.getLocation());
                SuperiorPlayer superiorPlayer = SuperiorSkyblockAPI.getPlayer(event.getPlayer());
                if (island == null || !island.hasPermission(superiorPlayer, IslandPrivilege.getByName("BUILD"))) {
                    event.getPlayer().sendMessage(plugin.messagesConfig.getMessage("notHasPermissionToOpen"));
                    return;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("generator", generator);

                plugin.upgradeView.open(UpgradeMenuView.class, event.getPlayer(), data);
            });
        }
    }

}