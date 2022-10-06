package phss.pgenerators.data;

import org.bukkit.Location;
import phss.pgenerators.PGenerators;
import phss.pgenerators.generator.model.Generator;

import java.util.ArrayList;
import java.util.HashMap;

public class GeneratorDataSource {

    final private PGenerators plugin;

    public GeneratorDataSource(PGenerators plugin) {
        this.plugin = plugin;
    }

    public HashMap<Location, Generator> loadGenerators() {
        HashMap<Location, Generator> generators = new HashMap<>();

        plugin.data.get().getStringList("generators").forEach(selected -> {
            Generator model = Generator.deserialize(plugin, selected);
            generators.put(model.getLocation(), model);
        });

        return generators;
    }

    public void save(Generator generator) {
        ArrayList<String> list = new ArrayList<>(plugin.data.get().getStringList("generators"));
        list.add(generator.serialize());

        plugin.data.get().set("generators", list);
        plugin.data.save();
    }

    public void delete(Generator generator) {
        ArrayList<String> list = new ArrayList<>(plugin.data.get().getStringList("generators"));
        list.remove(generator.serialize());

        plugin.data.get().set("generators", list);
        plugin.data.save();
    }

}