package austeretony.oxygen_core.client.preset;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.preset.Preset;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class ItemCategoriesPresetClient implements Preset {

    public static final String
            COMMON_CATEGORY_NAME = "...",
            COMMON_SUB_CATEGORY_NAME = "...";
    public static final ItemsSubCategory COMMON = new ItemsSubCategory(COMMON_SUB_CATEGORY_NAME);

    private long version;
    private final Multimap<String, ItemsSubCategory> categoriesMap = HashMultimap.create();

    @Nonnull
    public Map<String, Collection<ItemsSubCategory>> getCategoriesMap() {
        return categoriesMap.asMap();
    }

    @Nonnull
    public List<String> getSortedCategories() {
        return categoriesMap.keySet()
                .stream()
                .sorted()
                .collect(Collectors.toList());
    }

    @Nonnull
    public List<ItemsSubCategory> getSortedSubCategories(String categoryName) {
        return categoriesMap.get(categoryName)
                .stream()
                .sorted(Comparator.comparing(ItemsSubCategory::getName))
                .collect(Collectors.toList());
    }

    public boolean isValidForCategory(String categoryName, String subCategoryName, ResourceLocation registryName) {
        String registryNameStr = registryName.toString();
        Collection<ItemsSubCategory> subCategories = categoriesMap.get(categoryName);
        for (ItemsSubCategory subCategory : subCategories) {
            if ((subCategoryName.equals(COMMON_SUB_CATEGORY_NAME) || subCategory.getName().equals(subCategoryName))
                    && subCategory.getItems().contains(registryNameStr)) {
                return true;
            }
        }
        return categoryName.equals(COMMON_CATEGORY_NAME);
    }

    public boolean isValidForCategory(String categoryName, String subCategoryName, Item item) {
        ResourceLocation registryName = item.getRegistryName();
        if (registryName == null) return false;
        return isValidForCategory(categoryName, subCategoryName, registryName);
    }

    public boolean isValidForCategory(String categoryName, String subCategoryName, ItemStack itemStack) {
        return isValidForCategory(categoryName, subCategoryName, itemStack.getItem());
    }

    public boolean isCommonCategory(String name) {
        return name.equals(COMMON_CATEGORY_NAME);
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
        version = PresetsManagerClient.loadVersion(getName(), worldId);
    }

    @Override
    public void load(String worldId) {
        categoriesMap.clear();
        String folder = OxygenCommon.getConfigFolder() + "/data/client/worlds/" + worldId + "/presets/"
                + getName() + "/categories/";
        File file = new File(folder);
        if (file.exists()) {
            loadFromFolder(file);
        }
    }

    private void loadFromFolder(File folder) {
        File[] files = folder.listFiles();
        if (files == null) return;
        for (File file : files) {
            if (!file.isDirectory() && file.getName().endsWith(".txt")) {
                loadFromFile(file);
            }
        }
    }

    private void loadFromFile(File file) {
        String category = StringUtils.remove(file.getName(), ".txt");
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            ItemsSubCategory subCategory = null;
            for (String l : lines) {
                String line = l.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith(">")) {
                    if (subCategory != null) {
                        categoriesMap.put(category, subCategory);
                    }

                    subCategory = new ItemsSubCategory(line.substring(1).trim());
                    continue;
                }

                if (subCategory != null) {
                    subCategory.getItems().add(line);
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

    @Override
    public void save() {
        saveVersion();
        saveData();
    }

    private void saveVersion() {
        PresetsManagerClient.saveVersion(getName(), version);
    }

    private void saveData() {
        String folderStr = OxygenCommon.getConfigFolder() + "/data/client/worlds/" + OxygenClient.getWorldId() + "/presets/"
                + getName() + "/categories/";
        try {
            Files.createDirectories(Paths.get(folderStr));

            for (Map.Entry<String, Collection<ItemsSubCategory>> entry : categoriesMap.asMap().entrySet()) {
                String category = entry.getKey();
                List<String> lines = new ArrayList<>();

                entry.getValue()
                        .stream()
                        .sorted(Comparator.comparing(ItemsSubCategory::getName))
                        .forEach(subCategory -> {
                            lines.add("");
                            lines.add(">" + subCategory.getName());
                            lines.add("");

                            subCategory.getItems()
                                    .stream()
                                    .sorted(String::compareTo)
                                    .forEach(lines::add);
                        });

                Files.write(Paths.get(folderStr + "/" + category + ".txt"), lines, StandardCharsets.UTF_8);
            }
            OxygenMain.logInfo(2, "[Core] Created preset <{}> data file", getName());
        } catch (IOException exception) {
            OxygenMain.logError(1, "[Core] Failed to create <{}> preset data file", getName());
            exception.printStackTrace();
        }
    }

    @Override
    public void write(ByteBuf buffer) {}

    @Override
    public void read(ByteBuf buffer) {
        categoriesMap.clear();
        version = buffer.readLong();
        int categoriesAmount = buffer.readByte();
        for (int i = 0; i < categoriesAmount; i++) {
            String category = ByteBufUtils.readString(buffer);
            int subCategoriesAmount = buffer.readByte();
            for (int j = 0; j < subCategoriesAmount; j++) {
                ItemsSubCategory subCategory = ItemsSubCategory.read(buffer);
                categoriesMap.put(category, subCategory);
            }
        }
    }
}
