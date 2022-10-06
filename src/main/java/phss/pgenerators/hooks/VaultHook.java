package phss.pgenerators.hooks;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import phss.pgenerators.PGenerators;

public class VaultHook {

    public Economy economy;
    private final PGenerators plugin;

    public VaultHook(PGenerators plugin) {
        this.plugin = plugin;
    }

    public void register() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            plugin.getLogger().severe("Disabled due to no Vault dependency found!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        RegisteredServiceProvider<Economy> economyServiceProvider = plugin.getServer().getServicesManager().getRegistration(Economy.class);

        assert economyServiceProvider != null;
        economy = economyServiceProvider.getProvider();
    }

    public boolean hasEnoughMoney(Player player, int amount) {
        return economy.getBalance(player) >= amount;
    }

    public void withdrawMoney(Player player, int amount) {
        economy.withdrawPlayer(player, amount);
    }

}