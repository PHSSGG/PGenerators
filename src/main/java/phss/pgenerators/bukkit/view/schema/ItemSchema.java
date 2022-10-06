package phss.pgenerators.bukkit.view.schema;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import phss.pgenerators.utils.ItemBuilder;
import phss.pgenerators.utils.MessageUtils;

import java.util.List;

public class ItemSchema {

    public String name;
    public List<String> lore;
    public String id;
    public int data;
    public boolean glow;
    public int slot;

    public ItemSchema(String name, List<String> lore, String id, int data, boolean glow) {
        this.name = name;
        this.lore = lore;
        this.id = id;
        this.data = data;
        this.glow = glow;
    }

    public ItemSchema(String name, List<String> lore, String id, int data, boolean glow, int slot) {
        this.name = name;
        this.lore = lore;
        this.id = id;
        this.data = data;
        this.glow = glow;
        this.slot = slot;
    }

    public ItemStack buildItem() {
        ItemStack item = new ItemBuilder(Material.getMaterial(id)).setDurability(data)
                .setName(MessageUtils.replaceColor(name))
                .setLore(MessageUtils.replaceColor(lore))
                .build();

        if (glow) {
            item.addUnsafeEnchantment(Enchantment.WATER_WORKER, 1);

            ItemMeta meta = item.getItemMeta();
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }

        return item;
    }

    public static ItemSchema createItemSchemaByConfig(String path, FileConfiguration config) {
        return new ItemSchema(
                config.getString(path + ".name"),
                config.getStringList(path + ".lore"),
                config.getString(path + ".id"),
                config.getInt(path + ".data"),
                config.getBoolean(path + ".glow", false),
                config.getInt(path + ".slot", 0)
        );
    }

}