package phss.pgenerators.tasks;

import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import phss.pgenerators.PGenerators;

public class GeneratorTask extends BukkitRunnable {

    final private PGenerators plugin;

    public GeneratorTask(PGenerators plugin) {
        this.plugin = plugin;
    }

    public void start() {
        runTaskTimer(plugin, 180L, plugin.config.get().getInt("Config.interval") * 20L);
    }

    @Override
    public void run() {
        plugin.generatorManager.getGenerators().forEach(generator -> {
            if (generator.getLocation().getWorld() == null || generator.getLocation().getBlock().getType().equals(Material.AIR)) {
                plugin.generatorManager.deleteGenerator(generator);
            } else {
                generator.getType().generate();
            }
        });
    }

}

