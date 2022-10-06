package phss.pgenerators.bukkit.events;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataType;
import phss.pgenerators.PGenerators;

public class GeneratorEditListener implements Listener {

    final private PGenerators plugin;

    public GeneratorEditListener(PGenerators plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().hasItemMeta() && event.getItemInHand().getItemMeta().getPersistentDataContainer().has(plugin.generatorKey, PersistentDataType.STRING)) {
            String[] input = event.getItemInHand().getItemMeta().getPersistentDataContainer().get(plugin.generatorKey, PersistentDataType.STRING).split(":");
            String key = input[0];
            int level = Integer.parseInt(input[1]);
            Island island = SuperiorSkyblockAPI.getIslandAt(event.getBlockPlaced().getLocation());
            if (island == null) return;

            if (plugin.generatorManager.getGenerators().stream().filter(generator -> generator.getKey().equals(key) && generator.getPlacedIslandUUID().equals(island.getUniqueId())).count() >= plugin.config.get().getInt("Generators." + key + ".limitPerIsland")) {
                event.getPlayer().sendMessage(plugin.messagesConfig.getMessage("placeLimit"));
                event.setCancelled(true);
                return;
            }

            plugin.generatorManager.createGenerator(island, event.getPlayer(), event.getBlockPlaced(), key, level);
            event.getPlayer().sendMessage(plugin.messagesConfig.getMessage("placeSuccess"));

            if (plugin.config.get().getBoolean("Config.log_breaking_and_placing")) {
                plugin.getLogger().info("GENERATOR: Generator placed at " + event.getBlockPlaced().getLocation().toString() + " by " + event.getPlayer().getName());
                Bukkit.getOnlinePlayers().stream().filter(it -> it.isOp() || it.hasPermission("gens.notify")).forEach(it -> {
                    it.sendMessage("GENERATOR: Generator placed at " + event.getBlockPlaced().getLocation().toString() + " by " + event.getPlayer().getName());
                });
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        plugin.generatorManager.findGeneratorAt(event.getBlock().getLocation()).ifPresent(generator -> {
            plugin.generatorManager.deleteGenerator(generator);
            event.getPlayer().getInventory().addItem(generator.getType().getItem().build());

            event.getBlock().setType(Material.AIR);

            event.getPlayer().sendMessage(plugin.messagesConfig.getMessage("generatorDestroyed"));
        });
    }

}