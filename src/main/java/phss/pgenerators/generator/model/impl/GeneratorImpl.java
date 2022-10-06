package phss.pgenerators.generator.model.impl;

import phss.pgenerators.generator.model.Generator;
import phss.pgenerators.generator.model.GeneratorLevel;
import phss.pgenerators.generator.model.GeneratorType;
import org.bukkit.Location;

import java.util.UUID;

public class GeneratorImpl implements Generator {

    String key;

    Location location;
    UUID placedIslandUUID;
    UUID placerUUID;
    GeneratorLevel currentLevel;
    GeneratorType type;

    public GeneratorImpl(String key, Location location, UUID placedIslandUUID, UUID placerUUID, GeneratorLevel currentLevel, GeneratorType type) {
        this.key = key;
        this.location = location;
        this.placedIslandUUID = placedIslandUUID;
        this.placerUUID = placerUUID;
        this.currentLevel = currentLevel;
        this.type = type;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public UUID getPlacedIslandUUID() {
        return this.placedIslandUUID;
    }

    @Override
    public UUID getPlacerUUID() {
        return this.placerUUID;
    }

    @Override
    public GeneratorLevel getCurrentLevel() {
        return this.currentLevel;
    }

    @Override
    public void setCurrentLevel(GeneratorLevel level) {
        this.currentLevel = level;
    }

    @Override
    public GeneratorType getType() {
        return this.type;
    }

    @Override
    public void setType(GeneratorType type) {
        this.type = type;
    }

}
