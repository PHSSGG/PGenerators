package phss.pgenerators.bukkit.view;

import me.saiintbrisson.minecraft.View;
import me.saiintbrisson.minecraft.ViewContext;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import phss.pgenerators.PGenerators;
import phss.pgenerators.bukkit.view.schema.ItemSchema;
import phss.pgenerators.generator.model.Generator;
import phss.pgenerators.generator.model.GeneratorLevel;
import phss.pgenerators.utils.MessageUtils;

import java.util.HashMap;
import java.util.Map;

public class UpgradeMenuView extends View {

    final private PGenerators plugin;

    public UpgradeMenuView(PGenerators plugin) {
        super(plugin.config.get().getInt("Menus.upgradeMenu.rows"), MessageUtils.replaceColor(plugin.config.get().getString("Menus.upgradeMenu.name")));
        setCancelOnClick(true);

        this.plugin = plugin;

        FileConfiguration config = plugin.config.get();
        String path = "Menus.upgradeMenu";

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
        String path = "Menus.upgradeMenu";

        Player player = context.getPlayer();
        Generator generator = context.get("generator");

        for (String item : config.getConfigurationSection(path + ".items").getKeys(false)) {
            ItemSchema schema = ItemSchema.createItemSchemaByConfig(path + ".items." + item, config);
            schema.name = MessageUtils.replaceGeneratorInfo(schema.name, generator, config, plugin);
            schema.lore = MessageUtils.replaceGeneratorInfo(schema.lore, generator, config, plugin);

            context.slot(schema.slot, schema.buildItem()).onClick(handler -> {
                switch(item.toLowerCase()) {
                    case "generator":
                        break;
                    case "upgrade":
                        GeneratorLevel nextLevel = plugin.generatorManager.getNextLevel(generator);
                        if (nextLevel != null) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("generator", generator);
                            data.put("nextLevel", nextLevel);

                            if (plugin.vaultHook.hasEnoughMoney(player, nextLevel.getAmount())) {
                                context.closeUninterruptedly();
                                plugin.confirmUpgradeView.open(UpgradeConfirmMenuView.class, player, data);
                            } else {
                                context.close();
                                player.sendMessage(plugin.messagesConfig.getMessage("notEnoughMoney"));
                            }
                        } else player.sendMessage(plugin.messagesConfig.getMessage("maxLevel"));
                        break;
                    case "destroy":
                        Map<String, Object> data = new HashMap<>();
                        data.put("generator", generator);

                        context.closeUninterruptedly();
                        plugin.confirmDestroyView.open(DestroyConfirmMenuView.class, player, data);
                        break;
                }
            });
        }
    }

}