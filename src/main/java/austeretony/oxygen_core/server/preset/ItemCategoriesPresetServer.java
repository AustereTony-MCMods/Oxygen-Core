package austeretony.oxygen_core.server.preset;

import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.preset.Preset;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistryNamespaced;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class ItemCategoriesPresetServer implements Preset {

    private long version;
    private final Multimap<String, SubCategory> categoriesMap = HashMultimap.create();

    public Map<String, Collection<SubCategory>> getCategoriesMap() {
        return categoriesMap.asMap();
    }

    @Override
    public int getId() {
        return OxygenMain.PRESET_ITEM_CATEGORIES;
    }

    @Override
    public String getName() {
        return "item_categories";
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public void loadVersion(String worldId) {
        version = PresetsManagerServer.loadOrCreateVersion(getName());
    }

    @Override
    public void load(String worldId) {
        categoriesMap.clear();
        String folder = OxygenCommon.getConfigFolder() + "/data/server/presets/" + getName() + "/categories/";
        File file = new File(folder);
        if (file.exists()) {
            loadFromFolder(file);
        } else {
            defaultData();
            try {
                Files.createDirectories(Paths.get(folder));

                for (Map.Entry<String, Collection<SubCategory>> entry : categoriesMap.asMap().entrySet()) {
                    String category = entry.getKey();
                    List<String> lines = new ArrayList<>();

                    lines.add("# This is a commentary");
                    lines.add("#");
                    lines.add("# This file was generated automatically");
                    lines.add("# It is based on the content of the creative inventory tabs");
                    lines.add("#");
                    lines.add("# File name is category name, value after '>' symbol is subcategory name");

                    entry.getValue()
                            .stream()
                            .sorted(Comparator.comparing(e -> e.name))
                            .forEach(subCategory -> {
                                lines.add("");
                                lines.add(">" + subCategory.name);
                                lines.add("");

                                subCategory.items
                                        .stream()
                                        .sorted(String::compareTo)
                                        .forEach(lines::add);
                            });

                    Files.write(Paths.get(folder + "/" + category + ".txt"), lines, StandardCharsets.UTF_8);
                }
                OxygenMain.logInfo(2, "[Core] Created preset <{}> data file", getName());
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to create <{}> preset data file", getName());
                exception.printStackTrace();
            }
        }
    }

    private void loadFromFolder(File folder) {
        for (File file : folder.listFiles()) {
            if (!file.isDirectory() && file.getName().endsWith(".txt")) {
                loadFromFile(file);
            }
        }
    }

    private void loadFromFile(File file) {
        String category = StringUtils.remove(file.getName(), ".txt");
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            SubCategory subCategory = null;
            for (String l : lines) {
                String line = l.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                if (line.startsWith(">")) {
                    if (subCategory != null) {
                        categoriesMap.put(category, subCategory);
                    }
                    subCategory = new SubCategory(line.substring(1));
                    continue;
                }
                if (subCategory != null) {
                    subCategory.items.add(line);
                }
            }
            if (subCategory != null) {
                categoriesMap.put(category, subCategory);
            }
        } catch (Exception exception) {
            OxygenMain.logError(1, "[Core] Failed to load preset <{}> data: category <{}>", getName(), category);
            exception.printStackTrace();
        }
    }

    private void defaultData() {
        processRegistry("Blocks", Block.REGISTRY, Block::getCreativeTabToDisplayOn);
        processRegistry("Items", Item.REGISTRY, Item::getCreativeTab);
    }

    private <T> void processRegistry(String category, RegistryNamespaced<ResourceLocation, T> registry,
                                     Function<T, CreativeTabs> tabExtractor) {
        Map<CreativeTabs, String> labelsMap = new HashMap<>();
        Field field;
        try { //TODO Used MC 1.12.2 field srg name
            field = CreativeTabs.class.getDeclaredField(MinecraftCommon.isIDE() ? "tabLabel" : "field_78034_o");
            field.setAccessible(true);
        } catch (SecurityException | NoSuchFieldException exception) {
            exception.printStackTrace();
            return;
        }

        Map<String, SubCategory> subCategoriesMap = new HashMap<>();
        for (T entry : registry) {
            CreativeTabs tab = tabExtractor.apply(entry);
            if (tab == null) continue;

            String tabLabel = labelsMap.get(tab);
            if (tabLabel == null) {
                try {
                    tabLabel = (String) field.get(tab);
                } catch (IllegalAccessException exception) {
                    exception.printStackTrace();
                }
                if (tabLabel == null) continue;
                tabLabel = formatTabLabel(tabLabel);
                labelsMap.put(tab, tabLabel);

                SubCategory subCategory = new SubCategory(tabLabel);
                subCategoriesMap.put(tabLabel, subCategory);
                categoriesMap.put(category, subCategory);
            }
            ResourceLocation registryName = registry.getNameForObject(entry);
            if (registryName == null) continue;
            subCategoriesMap.get(tabLabel).items.add(registryName.toString());
        }
    }

    private static String formatTabLabel(String tabLabel) {
        List<String> words = new ArrayList<>(1);
        StringBuilder wordBuilder = new StringBuilder();
        for (char c : tabLabel.toCharArray()) {
            if (wordBuilder.length() == 0)
                c = Character.toUpperCase(c);
            if (Character.isUpperCase(c)) {
                words.add(wordBuilder.toString());
                wordBuilder = new StringBuilder();
            }
            wordBuilder.append(c);
        }
        words.add(wordBuilder.toString());

        String result = StringUtils.join(words, ' ').trim();
        return result.isEmpty() ? tabLabel : result;
    }

    @Override
    public void save() {}

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeLong(version);
        buffer.writeByte(categoriesMap.keySet().size());
        for (Map.Entry<String, Collection<SubCategory>> category : categoriesMap.asMap().entrySet()) {
            ByteBufUtils.writeString(category.getKey(), buffer);
            buffer.writeByte(category.getValue().size());
            for (SubCategory subCategory : category.getValue()) {
                subCategory.write(buffer);
            }
        }
    }

    @Override
    public void read(ByteBuf buffer) {}

    public static class SubCategory {

        public final String name;
        public final Set<String> items = new HashSet<>();

        SubCategory(String name) {
            this.name = name;
        }

        void write(ByteBuf buffer) {
            ByteBufUtils.writeString(name, buffer);
            buffer.writeShort(items.size());
            for (String item : items) {
                ByteBufUtils.writeString(item, buffer);
            }
        }
    }
}
