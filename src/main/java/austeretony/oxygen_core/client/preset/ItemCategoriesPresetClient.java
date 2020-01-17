package austeretony.oxygen_core.client.preset;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.JsonUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;

public class ItemCategoriesPresetClient extends AbstractPresetClient {

    public static final ItemCategory COMMON_CATEGORY = new ItemCategory("oxygen_core.category.common");

    static {
        COMMON_CATEGORY.subCategories.add(new ItemSubCategoryCommon("oxygen_core.category.common"));
    }

    private final List<ItemCategory> categories = new ArrayList<>(5);

    private boolean verified;

    public boolean isVerified() {
        return this.verified;
    }

    public List<ItemCategory> getCategories() {
        return this.categories;
    }

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
    public boolean load(String folder) {
        String pathStr = folder + "/item_categories.json";
        Path path = Paths.get(pathStr);     
        if (Files.exists(path)) {
            this.categories.clear();
            try {      
                this.categories.add(COMMON_CATEGORY);
                for (JsonElement categoryEntry : JsonUtils.getExternalJsonData(pathStr).getAsJsonArray())
                    this.categories.add(ItemCategory.deserialize(categoryEntry.getAsJsonObject()));
                this.sortCategories();
                this.verified = true;
                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
            }       
        }
        return false;
    }

    private void sortCategories() {
        Collections.sort(this.categories, (c1, c2)->(c1.localizedName().compareTo(c2.localizedName())));
    }

    @Override
    public boolean saveToFolder(String folder) {
        String pathStr = folder + "/item_categories.json";
        Path path = Paths.get(pathStr);
        if (!Files.exists(path)) {
            try {                   
                Files.createDirectories(path.getParent());              
            } catch (IOException exception) {               
                exception.printStackTrace();
            }
        }
        try {
            JsonArray config = new JsonArray();
            for (ItemCategory category : this.categories)
                config.add(category.serialize());  
            JsonUtils.createExternalJsonFile(pathStr, config);
            return true;
        } catch (IOException exception) {      
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean reloadAfterSave() {
        return true;
    }

    @Override
    public void reset() {
        this.categories.clear();
    }

    @Override
    public void readFromBuf(ByteBuf buffer) {
        int amount = buffer.readByte();
        for (int i = 0; i < amount; i++)
            this.categories.add(ItemCategory.read(buffer));
    }

    public static class ItemCategory {

        public final String name;

        protected final List<ItemSubCategory> subCategories = new ArrayList<>(5);

        public ItemCategory(String name) {
            this.name = name;
        }

        public List<ItemSubCategory> getSubCategories() {
            return this.subCategories;
        }

        public boolean isValid(ItemSubCategory subCategory, ResourceLocation registryName) {
            if (!this.subCategories.contains(subCategory))
                return false;
            return subCategory.isValid(registryName);
        }

        public String localizedName() {
            return ClientReference.localize(this.name);
        }

        private void sortSubCategories() {
            Collections.sort(this.subCategories, (c1, c2)->(c1.localizedName().compareTo(c2.localizedName())));
        }

        protected static ItemCategory deserialize(JsonObject jsonObject) {
            ItemCategory category = new ItemCategory(jsonObject.get("name").getAsString());
            ItemSubCategory commonSubCategory = new ItemSubCategory("oxygen_core.category.common");
            category.subCategories.add(commonSubCategory);
            ItemSubCategory subCategory;
            for (JsonElement subCategoryEntry : jsonObject.get("sub_categories").getAsJsonArray()) {
                category.subCategories.add(subCategory = ItemSubCategory.deserialize(subCategoryEntry.getAsJsonObject()));
                commonSubCategory.registryNames.addAll(subCategory.registryNames);
            }
            category.sortSubCategories();
            return category;
        }

        protected JsonObject serialize() {
            JsonObject categoryEntry = new JsonObject();
            categoryEntry.add("name", new JsonPrimitive(this.name));
            JsonArray subCategoryEntries = new JsonArray();
            for (ItemSubCategory subCategory : this.subCategories)
                subCategoryEntries.add(subCategory.serialize());
            categoryEntry.add("sub_categories", subCategoryEntries);
            return categoryEntry;
        }

        protected static ItemCategory read(ByteBuf buffer) {
            ItemCategory category = new ItemCategory(ByteBufUtils.readString(buffer));
            int amount = buffer.readByte();
            for (int i = 0; i < amount; i++)
                category.subCategories.add(ItemSubCategory.read(buffer));
            return category;
        }
    }

    public static class ItemSubCategory {

        public final String name;

        protected final Set<ResourceLocation> registryNames = new HashSet<>();

        public ItemSubCategory(String name) {
            this.name = name;
        }

        public boolean isValid(ResourceLocation registryName) {
            return this.registryNames.contains(registryName);
        }

        public String localizedName() {
            return ClientReference.localize(this.name);
        }

        protected static ItemSubCategory deserialize(JsonObject jsonObject) {
            ItemSubCategory subCategory = new ItemSubCategory(jsonObject.get("name").getAsString());
            for (JsonElement itemEntry : jsonObject.get("items").getAsJsonArray())
                subCategory.registryNames.add(new ResourceLocation(itemEntry.getAsString()));
            return subCategory;
        }

        protected JsonObject serialize() {
            JsonObject subCategoryEntry = new JsonObject();
            subCategoryEntry.add("name", new JsonPrimitive(this.name));
            JsonArray itemEntries = new JsonArray();
            for (ResourceLocation registryName : this.registryNames)
                itemEntries.add(new JsonPrimitive(registryName.toString()));
            subCategoryEntry.add("items", itemEntries);
            return subCategoryEntry;
        }

        protected static ItemSubCategory read(ByteBuf buffer) {
            ItemSubCategory subCategory = new ItemSubCategory(ByteBufUtils.readString(buffer));
            int amount = buffer.readShort();
            for (int i = 0; i < amount; i++)
                subCategory.registryNames.add(new ResourceLocation(ByteBufUtils.readString(buffer)));
            return subCategory;
        }
    }

    public static class ItemSubCategoryCommon extends ItemSubCategory {

        public ItemSubCategoryCommon(String name) {
            super(name);
        }

        public boolean isValid(ResourceLocation registryName) {
            return true;
        }
    }
}
