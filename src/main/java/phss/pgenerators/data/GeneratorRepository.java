package phss.pgenerators.data;

import org.bukkit.Location;
import phss.pgenerators.generator.model.Generator;

import java.util.HashMap;

public class GeneratorRepository {

    public HashMap<Location, Generator> generators;

    final private GeneratorDataSource dataSource;

    public GeneratorRepository(GeneratorDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public HashMap<Location, Generator> loadGenerators() {
        generators = dataSource.loadGenerators();
        return generators;
    }

    public void save(Generator generator) {
        generators.put(generator.getLocation(), generator);
        dataSource.save(generator);
    }

    public void delete(Generator generator) {
        generators.remove(generator.getLocation());
        dataSource.delete(generator);
    }

}