package phss.pgenerators.generator.model.impl;

import phss.pgenerators.PGenerators;
import phss.pgenerators.generator.model.Generator;
import phss.pgenerators.generator.model.GeneratorLevel;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class GeneratorLevelImpl implements GeneratorLevel {

    final PGenerators plugin;
    final Generator generator;

    int level;
    int interval;
    int amount;
    int price;

    public GeneratorLevelImpl(PGenerators plugin, Generator generator, int level) {
        FileConfiguration config = plugin.config.get();

        this.plugin = plugin;
        this.generator = generator;
        this.level = level;
        this.interval = config.getInt("Generators." + generator.getKey() + ".levels." + level + ".interval");
        this.amount = config.getInt("Generators." + generator.getKey() + ".levels." + level + ".amount");
        this.price = config.getInt("Generators." + generator.getKey() + ".levels." + level + ".price");
    }

    @Override
    public Integer getLevel() {
        return level;
    }

    @Override
    public Integer getInterval() {
        return interval;
    }

    @Override
    public Integer getAmount() {
        return amount;
    }

    @Override
    public Integer getPrice() {
        return price;
    }

    @Override
    public void upgrade() {
        FileConfiguration config = plugin.config.get();
        Set<String> levels = Objects.requireNonNull(config.getConfigurationSection("Generators." + generator.getKey() + ".levels")).getKeys(false);
        int maxLevel = Integer.parseInt(Collections.max(levels));

        if (getLevel() < maxLevel) {
            plugin.generatorManager.deleteGenerator(generator); // delete previous generator model from the data

            this.level += 1;
            this.interval = config.getInt("Generators." + generator.getKey() + ".levels." + level + ".interval");
            this.amount = config.getInt("Generators." + generator.getKey() + ".levels." + level + ".amount");

            generator.getType().getItem().setLevel(getLevel());
            plugin.generatorManager.saveGenerator(generator); // save new generator model on the data
        }
    }

}
