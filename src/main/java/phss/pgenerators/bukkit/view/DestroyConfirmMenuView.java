package phss.pgenerators.bukkit.view;

import me.saiintbrisson.minecraft.View;
import me.saiintbrisson.minecraft.ViewContext;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.BlockBreakEvent;
import org.jetbrains.annotations.NotNull;
import phss.pgenerators.PGenerators;
import phss.pgenerators.bukkit.view.schema.ItemSchema;
import phss.pgenerators.generator.model.Generator;
import phss.pgenerators.utils.MessageUtils;

public class DestroyConfirmMenuView extends View {

    final private PGenerators plugin;

    public DestroyConfirmMenuView(PGenerators plugin) {
        super(plugin.config.get().getInt("Menus.confirmMenu.destroy.rows"), MessageUtils.replaceColor(plugin.config.get().getString("Menus.confirmMenu.destroy.name")));
        setCancelOnClick(true);

        this.plugin = plugin;

        FileConfiguration config = plugin.config.get();
        String path = "Menus.confirmMenu";

        ConfigurationSection ornamentItems = config.getConfigurationSection(path + ".ornamentItems");
        if (ornamentItems != null) {
            for (String ornament : ornamentItems.getKeys(false)) {
                ItemSchema schema = ItemSchema.createItemSchemaByConfig(path + ".ornamentItems." + ornament, config);
                slot(schema.slot, schema.buildItem());
            }
        }
    }

    @Override
    protected void onRender(@NotNull ViewContext context) {
        FileConfiguration config = plugin.config.get();
        String path = "Menus.confirmMenu.destroy";

        Generator generator = context.get("generator");

        ItemSchema acceptItem = ItemSchema.createItemSchemaByConfig(path + ".acceptItem", config);
        acceptItem.name = MessageUtils.replaceGeneratorInfo(acceptItem.name, generator, config, plugin);
        acceptItem.lore = MessageUtils.replaceGeneratorInfo(acceptItem.lore, generator, config, plugin);

        ItemSchema denyItem = ItemSchema.createItemSchemaByConfig(path + ".denyItem", config);
        denyItem.name = MessageUtils.replaceGeneratorInfo(denyItem.name, generator, config, plugin);
        denyItem.lore = MessageUtils.replaceGeneratorInfo(denyItem.lore, generator, config, plugin);

        context.slot(acceptItem.slot, acceptItem.buildItem()).onClick(handler -> {
            plugin.getServer().getPluginManager().callEvent(new BlockBreakEvent(generator.getLocation().getBlock(), context.getPlayer()));
            context.close();
        });
        context.slot(denyItem.slot, denyItem.buildItem()).onClick(handler -> {
            context.close();
        });
    }

}