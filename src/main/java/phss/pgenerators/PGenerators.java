package phss.pgenerators;

import me.saiintbrisson.minecraft.ViewFrame;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;
import phss.pgenerators.bukkit.commands.GiveGeneratorCommand;
import phss.pgenerators.bukkit.events.*;
import phss.pgenerators.bukkit.view.*;
import phss.pgenerators.config.Config;
import phss.pgenerators.config.providers.MessagesConfig;
import phss.pgenerators.data.*;
import phss.pgenerators.generator.GeneratorManager;
import phss.pgenerators.hooks.VaultHook;
import phss.pgenerators.tasks.GeneratorTask;

import java.io.IOException;

public class PGenerators extends JavaPlugin {

    public NamespacedKey generatorKey = new NamespacedKey(this, "generator");

    private GeneratorTask generatorTask;
    private final GeneratorDataSource generatorDataSource = new GeneratorDataSource(this);
    private GeneratorRepository generatorRepository;
    public GeneratorManager generatorManager;

    public Config config = new Config(this, "config");
    public Config lang = new Config(this, "lang");
    public Config data = new Config(this, "data");

    public MessagesConfig messagesConfig = new MessagesConfig(this);

    public VaultHook vaultHook = new VaultHook(this);

    public ViewFrame upgradeView;
    public ViewFrame confirmDestroyView;
    public ViewFrame confirmUpgradeView;

    @Override
    public void onEnable() {
        loadFiles();
        vaultHook.register();

        upgradeView = ViewFrame.of(this, new UpgradeMenuView(this)).register();
        confirmDestroyView = ViewFrame.of(this, new DestroyConfirmMenuView(this)).register();
        confirmUpgradeView = ViewFrame.of(this, new UpgradeConfirmMenuView(this)).register();

        generatorRepository = new GeneratorRepository(generatorDataSource);
        generatorManager = new GeneratorManager(this, generatorRepository);

        generatorTask = new GeneratorTask(this);
        generatorTask.start();

        getCommand("generator").setExecutor(new GiveGeneratorCommand(this));
        getServer().getPluginManager().registerEvents(new GeneratorEditListener(this), this);
        getServer().getPluginManager().registerEvents(new GeneratorInteractListener(this), this);
    }

    private void loadFiles() {
        try {
            config.load();
            lang.load();
            data.load();
        } catch (IOException | InvalidConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }

}