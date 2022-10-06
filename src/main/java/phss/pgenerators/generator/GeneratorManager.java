package phss.pgenerators.generator;

import com.bgsoftware.superiorskyblock.api.island.Island;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import phss.pgenerators.PGenerators;
import phss.pgenerators.data.GeneratorRepository;
import phss.pgenerators.generator.model.Generator;
import phss.pgenerators.generator.model.GeneratorLevel;
import phss.pgenerators.generator.model.impl.GeneratorImpl;
import phss.pgenerators.generator.model.impl.GeneratorItemImpl;
import phss.pgenerators.generator.model.impl.GeneratorLevelImpl;
import phss.pgenerators.generator.model.impl.GeneratorTypeImpl;

import java.util.*;
import java.util.stream.Collectors;

public class GeneratorManager {

    final private PGenerators plugin;
    final private GeneratorRepository repository;

    public GeneratorManager(PGenerators plugin, GeneratorRepository repository) {
        this.plugin = plugin;
        this.repository = repository;
    }

    public List<Generator> getGenerators() {
        return new ArrayList<>(repository.generators.values());
    }

    public Generator createGenerator(Island placedIsland, Player placer, Block block, String key) {
        return createGenerator(placedIsland, placer, block, key, 1);
    }

    public Generator createGenerator(Island placedIsland, Player placer, Block block, String key, int level) {
        Generator model = new GeneratorImpl(key, block.getLocation(), placedIsland.getUniqueId(), placer.getUniqueId(), null, null);
        model.setCurrentLevel(new GeneratorLevelImpl(plugin, model, level));
        model.setType(new GeneratorTypeImpl(plugin, model));

        saveGenerator(model);
        return model;
    }

    public void saveGenerator(Generator generator) {
        repository.save(generator);
    }

    public void deleteGenerator(Generator generator) {
        repository.delete(generator);
    }

    /**
     * @code [Integer.MAX_VALUE] is used to the max level
     *
     * @param generator the desired generator to get the next level
     * @return the generator next level or max level if it's already on the max one
     */
    public GeneratorLevel getNextOrMaxLevel(Generator generator) {
        Set<String> levels = Objects.requireNonNull(plugin.config.get().getConfigurationSection("Generators." + generator.getKey() + ".levels")).getKeys(false);
        int maxLevel = Integer.parseInt(Collections.max(levels));

        if (generator.getCurrentLevel().getLevel() == maxLevel) return new GeneratorLevelImpl(plugin, generator, Integer.MAX_VALUE);
        else return new GeneratorLevelImpl(plugin, generator, generator.getCurrentLevel().getLevel() + 1);
    }

    /**
     * @param generator the desired generator to get the next level
     * @return the generator next level or null if it's already on the max one
     */
    public GeneratorLevel getNextLevel(Generator generator) {
        Set<String> levels = Objects.requireNonNull(plugin.config.get().getConfigurationSection("Generators." + generator.getKey() + ".levels")).getKeys(false);
        int maxLevel = Integer.parseInt(Collections.max(levels));

        if (generator.getCurrentLevel().getLevel() == maxLevel) return null;
        else return new GeneratorLevelImpl(plugin, generator, generator.getCurrentLevel().getLevel() + 1);
    }

    public Optional<ItemStack> getGeneratorItemStackByKey(String key) {
        return getGeneratorItemStackByKey(key, 1);
    }

    public Optional<ItemStack> getGeneratorItemStackByKey(String key, int level) {
        ItemStack item = new GeneratorItemImpl(plugin, key, level).build();

        if (item == null) return Optional.empty();
        else return Optional.of(item);
    }

    public Optional<Generator> findGeneratorAt(Location location) {
        return repository.generators.values().stream().filter(it -> compareLocations(it.getLocation(), location)).findFirst();
    }

    private boolean compareLocations(Location one, Location two) {
        return (one.getX() == two.getX()) && (one.getY() == two.getY()) && (one.getZ() == two.getZ());
    }

}