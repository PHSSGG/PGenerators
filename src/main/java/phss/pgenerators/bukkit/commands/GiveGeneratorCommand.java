package phss.pgenerators.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import phss.pgenerators.PGenerators;
import phss.pgenerators.config.providers.MessagesConfig;

import java.util.Optional;
import java.util.Set;

public class GiveGeneratorCommand implements CommandExecutor {

    final private PGenerators plugin;
    final private MessagesConfig messagesConfig;

    public GiveGeneratorCommand(PGenerators plugin) {
        this.plugin = plugin;
        this.messagesConfig = plugin.messagesConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("godlygens.admin")) sender.sendMessage(messagesConfig.getMessage("notHasPermission"));
        else {
            if (args.length < 2) {
                sender.sendMessage(messagesConfig.getMessage("giveGeneratorArgsEmpty"));
                return true;
            }

            Player target = Bukkit.getPlayer(args[1]);

            if (target == null) {
                sender.sendMessage(messagesConfig.getMessage("playerNotFound"));
                return true;
            }
            if (!plugin.generatorManager.getGeneratorItemStackByKey(args[0]).isPresent()) {
                sender.sendMessage(messagesConfig.getMessage("generatorNotFound"));
                return true;
            }

            int amount = 1;
            if (args.length == 3) {
                try {
                    amount = Integer.parseInt(args[2]);
                } catch (NumberFormatException ignored) {
                    sender.sendMessage(messagesConfig.getMessage("onlyNumbers"));
                    return true;
                }
            }

            int level = 1;
            if (args.length == 4) {
                try {
                    level = Integer.parseInt(args[3]);

                    Set<String> generatorLevels = plugin.config.get().getConfigurationSection("Generators." + args[0] + ".levels").getKeys(false);
                    if (!generatorLevels.contains("" + level)) {
                        sender.sendMessage(messagesConfig.getMessage("levelNotFound"));
                        return true;
                    }
                } catch (NumberFormatException ignored) {
                    sender.sendMessage(messagesConfig.getMessage("onlyNumbers"));
                    return true;
                }
            }

            Optional<ItemStack> generatorItemStack = plugin.generatorManager.getGeneratorItemStackByKey(args[0], level);
            ItemStack item = generatorItemStack.get();
            item.setAmount(amount);

            target.getInventory().addItem(item);

            target.sendMessage(messagesConfig.getMessage("generatorReceived").replace("%sender%", sender.getName()).replace("%type%", args[0]).replace("%amount%", "" + amount).replace("%level%", "" + level));
            sender.sendMessage(messagesConfig.getMessage("generatorSent").replace("%target%", target.getName()).replace("%type%", args[0]).replace("%amount%", "" + amount).replace("%level%", "" + level));
        }
        return true;
    }

}