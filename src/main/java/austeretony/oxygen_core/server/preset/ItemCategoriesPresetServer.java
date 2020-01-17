package austeretony.oxygen_core.server.preset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.JsonUtils;
import io.netty.buffer.ByteBuf;

public class ItemCategoriesPresetServer extends AbstractPresetServer {

    private final List<ItemCategory> categories = new ArrayList<>(5);

    @Override
    public int getId() {
        return OxygenMain.ITEM_CATEGORIES_PRESET_ID;
    }

    @Override
    public String getDirectory() {
        return "core/item categories";
    }

    @Override
    public String getName() {
        return "item_categories";
    }

    @Override
    public boolean loadFromFolder(String folder) {
        String pathStr = folder + "/item_categories.json";
        Path path = Paths.get(pathStr);   
        this.categories.clear();
        if (Files.exists(path)) {
            try {      
                for (JsonElement categoryEntry : JsonUtils.getExternalJsonData(pathStr).getAsJsonArray())
                    this.categories.add(ItemCategory.deserialize(categoryEntry.getAsJsonObject()));
                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
            }       
        } else {
            try {
                JsonArray config = JsonUtils.getInternalJsonData("assets/oxygen_core/presets/item_categories.json").getAsJsonArray();
                for (JsonElement categoryEntry : config)
                    this.categories.add(ItemCategory.deserialize(categoryEntry.getAsJsonObject()));
                JsonUtils.createExternalJsonFile(pathStr, config);
                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void writeToBuf(ByteBuf buffer) {
        buffer.writeByte(this.categories.size());
        for (ItemCategory category : this.categories)
            category.write(buffer);
    }

    public static class ItemCategory {

        public final String name;

        private final List<ItemSubCategory> subCategories = new ArrayList<>(5);

        public ItemCategory(String name) {
            this.name = name;
        }

        public List<ItemSubCategory> getSubCategories() {
            return this.subCategories;
        }

        protected static ItemCategory deserialize(JsonObject jsonObject) {
            ItemCategory category = new ItemCategory(jsonObject.get("name").getAsString());
            for (JsonElement subCategoryEntry : jsonObject.get("sub_categories").getAsJsonArray())
                category.subCategories.add(ItemSubCategory.deserialize(subCategoryEntry.getAsJsonObject()));
            return category;
        }

        protected void write(ByteBuf buffer) {
            ByteBufUtils.writeString(this.name, buffer);
            buffer.writeByte(this.subCategories.size());
            for (ItemSubCategory subCategory : this.subCategories)
                subCategory.write(buffer);
        }
    }

    public static class ItemSubCategory {

        public final String name;

        private final Set<String> registryNames = new HashSet<>();

        public ItemSubCategory(String name) {
            this.name = name;
        }

        protected static ItemSubCategory deserialize(JsonObject jsonObject) {
            ItemSubCategory subCategory = new ItemSubCategory(jsonObject.get("name").getAsString());
            for (JsonElement itemEntry : jsonObject.get("items").getAsJsonArray())
                subCategory.registryNames.add(itemEntry.getAsString());
            return subCategory;
        }

        protected void write(ByteBuf buffer) {
            ByteBufUtils.writeString(this.name, buffer);
            buffer.writeShort(this.registryNames.size());
            for (String registryName : this.registryNames)
                ByteBufUtils.writeString(registryName, buffer);
        }
    }
}
