package phss.pgenerators.config.providers;

import phss.pgenerators.PGenerators;
import phss.pgenerators.utils.MessageUtils;

import java.util.List;
import java.util.Objects;

public class MessagesConfig {

    final PGenerators plugin;

    public MessagesConfig(PGenerators plugin) {
        this.plugin = plugin;
    }

    public String getMessage(String message) {
        return MessageUtils.replaceColor(Objects.requireNonNull(plugin.lang.get().getString("Messages." + message)));
    }

    public List<String> getMessageList(String message) {
        return MessageUtils.replaceColor(plugin.lang.get().getStringList("Messages." + message));
    }

}