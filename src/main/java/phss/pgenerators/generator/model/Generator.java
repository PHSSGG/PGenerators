package phss.pgenerators.generator.model;

import com.bgsoftware.superiorskyblock.api.SuperiorSkyblockAPI;
import com.bgsoftware.superiorskyblock.api.island.Island;
import phss.pgenerators.PGenerators;
import phss.pgenerators.generator.model.impl.GeneratorImpl;
import phss.pgenerators.generator.model.impl.GeneratorLevelImpl;
import phss.pgenerators.generator.model.impl.GeneratorTypeImpl;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;
import java.util.UUID;

public interface Generator {

    String getKey();

    Location getLocation();
    UUID getPlacedIslandUUID();
    UUID getPlacerUUID();

    GeneratorLevel getCurrentLevel();
    void setCurrentLevel(GeneratorLevel level);

    GeneratorType getType();
    void setType(GeneratorType type);

    default String serialize() {
        return getKey() + "," + getLocation().getWorld().getName() + "," + getLocation().getX() + "," + getLocation().getY() + "," + getLocation().getZ() + "," + getPlacedIslandUUID().toString() + "," + getPlacerUUID().toString() + ","+ getCurrentLevel().getLevel();
    }

    static Generator deserialize(PGenerators plugin, String serialized) {
        String[] input = serialized.split(",");

        Island island = SuperiorSkyblockAPI.getIslandByUUID(UUID.fromString(input[5]));
        World world = Bukkit.getWorld(input[1]);
        Optional<Chunk> chunk = island.getAllChunks().stream().filter(it -> it.getWorld() != null).findFirst();
        if (world == null && chunk.isPresent()) world = chunk.get().getWorld();

        Generator model = new GeneratorImpl(input[0], new Location(world, Double.parseDouble(input[2]), Double.parseDouble(input[3]), Double.parseDouble(input[4])), UUID.fromString(input[5]), UUID.fromString(input[6]), null, null);
        model.setCurrentLevel(new GeneratorLevelImpl(plugin, model, Integer.parseInt(input[7])));
        model.setType(new GeneratorTypeImpl(plugin, model));

        return model;
    }

}
