package phss.pgenerators.generator.model.impl;

import phss.pgenerators.PGenerators;
import phss.pgenerators.generator.model.Generator;
import phss.pgenerators.generator.model.GeneratorItem;
import phss.pgenerators.generator.model.GeneratorType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import phss.pgenerators.utils.ItemBuilder;
import phss.pgenerators.utils.MessageUtils;

import java.util.*;

public class GeneratorTypeImpl implements GeneratorType {

    final private PGenerators plugin;
    final private Generator generator;
    final private GeneratorItem item;

    int generateCounter = 1;

    ArrayList<ItemStack> drops = new ArrayList<>();

    public GeneratorTypeImpl(PGenerators plugin, Generator generator) {
        this.plugin = plugin;
        this.generator = generator;

        FileConfiguration config = plugin.config.get();
        Set<String> itemsList = Objects.requireNonNull(config.getConfigurationSection("Generators." + generator.getKey() + ".drops")).getKeys(false);
        itemsList.forEach(selected -> {
            String path = "Generators." + generator.getKey() + ".drops." + selected;

            ItemBuilder itemBuilder = new ItemBuilder(Material.getMaterial(config.getString(path + ".id"))).setDurability(config.getInt(path + ".data"));
            if (config.getBoolean(path + ".customNameAndLore")) {
                String name = MessageUtils.replaceColor(config.getString(path + ".name"));
                List<String> lore = MessageUtils.replaceColor(config.getStringList(path + ".lore"));

                itemBuilder.setName(name);
                itemBuilder.setLore(lore);
            }
            if (!config.getStringList(path + ".enchantments").isEmpty()) {
                config.getStringList(path + ".enchantments").forEach(received -> {
                    String[] input = received.split(":");
                    itemBuilder.addUnsafeEnchantment(Enchantment.getByName(input[0].toUpperCase()), Integer.parseInt(input[1]));
                });
            }
            if (config.getBoolean(path + ".hideEnchantments", false)) itemBuilder.hideEnchantments();

            drops.add(itemBuilder.build());
        });

        this.item = new GeneratorItemImpl(plugin, generator);
        this.generateCounter = generator.getCurrentLevel().getInterval();
    }

    @Override
    public GeneratorItem getItem() {
        return item;
    }

    @Override
    public void generate() {
        generateCounter -= 1;

        if (generateCounter == 0) {
            if (generator.getLocation().getChunk().isLoaded() && Bukkit.getPlayer(generator.getPlacerUUID()) != null) {
                ItemStack item = drops.get(new Random().nextInt(drops.size())).clone();
                item.setAmount(generator.getCurrentLevel().getAmount());
                generator.getLocation().getWorld().dropItem(generator.getLocation(), item);
            }

            generateCounter = generator.getCurrentLevel().getInterval();
        }
    }

}
