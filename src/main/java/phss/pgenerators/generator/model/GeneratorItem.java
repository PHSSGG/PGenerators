package phss.pgenerators.generator.model;

import org.bukkit.inventory.ItemStack;

public interface GeneratorItem {

    void setLevel(int level);
    ItemStack build();

}
