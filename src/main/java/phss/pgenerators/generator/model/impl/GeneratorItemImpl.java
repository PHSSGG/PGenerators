package phss.pgenerators.generator.model.impl;

import phss.pgenerators.PGenerators;
import phss.pgenerators.generator.model.Generator;
import phss.pgenerators.generator.model.GeneratorItem;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import phss.pgenerators.utils.ItemBuilder;
import phss.pgenerators.utils.MessageUtils;

import java.util.List;

public class GeneratorItemImpl implements GeneratorItem {

    final private PGenerators plugin;
    final private String generatorKey;

    String name;
    List<String> lore;
    String id;
    int data;
    int level = 1;

    public GeneratorItemImpl(PGenerators plugin, String generatorKey) {
        this.plugin = plugin;
        this.generatorKey = generatorKey;

        FileConfiguration config = plugin.config.get();
        String path = "Generators." + generatorKey;

        this.name = MessageUtils.replaceColor(config.getString(path + ".name"));
        this.lore = MessageUtils.replaceColor(MessageUtils.replace(config.getStringList(path + ".lore"), "%level%", level));
        this.id = config.getString(path + ".id");
        this.data = config.getInt(path + ".data");
    }

    public GeneratorItemImpl(PGenerators plugin, String generatorKey, int level) {
        this.plugin = plugin;
        this.generatorKey = generatorKey;
        this.level = level;

        FileConfiguration config = plugin.config.get();
        String path = "Generators." + generatorKey;

        this.name = MessageUtils.replaceColor(config.getString(path + ".name"));
        this.lore = MessageUtils.replaceColor(MessageUtils.replace(config.getStringList(path + ".lore"), "%level%", level));
        this.id = config.getString(path + ".id");
        this.data = config.getInt(path + ".data");
    }

    public GeneratorItemImpl(PGenerators plugin, Generator generator) {
        this.plugin = plugin;
        this.generatorKey = generator.getKey();
        this.level = generator.getCurrentLevel().getLevel();

        FileConfiguration config = plugin.config.get();
        String path = "Generators." + generatorKey;

        this.name = MessageUtils.replaceColor(MessageUtils.replaceGeneratorInfo(config.getString(path + ".name"), generator, config, plugin));
        this.lore = MessageUtils.replaceColor(MessageUtils.replaceGeneratorInfo(config.getStringList(path + ".lore"), generator, config, plugin));
        this.id = config.getString(path + ".id");
        this.data = config.getInt(path + ".data");
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public ItemStack build() {
        ItemStack item = null;
        try {
            item = new ItemBuilder(Material.getMaterial(id))
                    .setDurability(data)
                    .setName(name)
                    .setLore(lore)
                    .build();

            ItemMeta meta = item.getItemMeta();
            meta.getPersistentDataContainer().set(plugin.generatorKey, PersistentDataType.STRING, generatorKey + ":" + level);
            item.setItemMeta(meta);
        } catch (Exception ignored) {}

        return item;
    }

}
