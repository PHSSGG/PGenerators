package phss.pgenerators.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import phss.pgenerators.PGenerators;
import phss.pgenerators.generator.model.Generator;
import phss.pgenerators.generator.model.GeneratorLevel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageUtils {

    public static String replaceColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static List<String> replaceColor(List<String> message) {
        ArrayList<String> list = new ArrayList<>();
        message.forEach(line -> list.add(replaceColor(line)));

        return list;
    }

    public static List<String> replace(List<String> message, Object one, Object two) {
        ArrayList<String> list = new ArrayList<>();
        message.forEach(line -> list.add(line.replace(one.toString(), two.toString())));

        return list;
    }

    public static List<String> replace(List<String> message, Object[] one, Object[] two) {
        List<Object> listObjectsOne = Arrays.stream(one).collect(Collectors.toList());
        ArrayList<String> list = new ArrayList<>();
        message.forEach(line -> {
            String currentLine = line;
            for (Object selected : listObjectsOne) {
                currentLine = currentLine.replace(selected.toString(), two[listObjectsOne.indexOf(selected)].toString());
            }
            list.add(currentLine);
        });

        return list;
    }

    public static String replaceGeneratorInfo(String string, Generator generator, FileConfiguration config, PGenerators plugin) {
        String path = "Generators." + generator.getKey();
        GeneratorLevel nextLevel = plugin.generatorManager.getNextOrMaxLevel(generator);
        String nextLevelString;
        if (nextLevel.getLevel() == Integer.MAX_VALUE) nextLevelString = "Max";
        else nextLevelString = "" + nextLevel.getLevel();

        return string
                .replace("%name%", MessageUtils.replaceColor(config.getString(path + ".name")))
                .replace("%level%", "" + generator.getCurrentLevel().getLevel())
                .replace("%time%", "" + generator.getCurrentLevel().getInterval())
                .replace("%amount%", "" + generator.getCurrentLevel().getAmount())
                .replace("%next%", nextLevelString)
                .replace("%next_time%", "" + nextLevel.getInterval())
                .replace("%next_amount%", "" + nextLevel.getAmount())
                .replace("%price%", "" + nextLevel.getPrice());
    }

    public static List<String> replaceGeneratorInfo(List<String> list, Generator generator, FileConfiguration config, PGenerators plugin) {
        ArrayList<String> newList = new ArrayList<>();

        String path = "Generators." + generator.getKey();
        GeneratorLevel nextLevel = plugin.generatorManager.getNextOrMaxLevel(generator);
        String nextLevelString;
        if (nextLevel.getLevel() == Integer.MAX_VALUE) nextLevelString = "Max";
        else nextLevelString = "" + nextLevel.getLevel();

        for (String line : list) {
            newList.add(line.replace("%name%", MessageUtils.replaceColor(config.getString(path + ".name")))
                    .replace("%level%", "" + generator.getCurrentLevel().getLevel())
                    .replace("%time%", "" + generator.getCurrentLevel().getInterval())
                    .replace("%amount%", "" + generator.getCurrentLevel().getAmount())
                    .replace("%next%", nextLevelString)
                    .replace("%next_time%", "" + nextLevel.getInterval())
                    .replace("%next_amount%", "" + nextLevel.getAmount())
                    .replace("%price%", "" + nextLevel.getPrice()));
        }

        return newList;
    }

}