package austeretony.oxygen_core.client.preset;

import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;

import java.util.HashSet;
import java.util.Set;

public class ItemsSubCategory {

    private final String name;
    private final Set<String> items = new HashSet();

    ItemsSubCategory(String name) {
        this.name = name;
    }

    public static ItemsSubCategory read(ByteBuf buffer) {
        ItemsSubCategory subCategory = new ItemsSubCategory(ByteBufUtils.readString(buffer));
        int amount = buffer.readShort();
        for (int i = 0; i < amount; i++) {
            subCategory.items.add(ByteBufUtils.readString(buffer));
        }
        return subCategory;
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return MinecraftClient.localize(name);
    }

    public Set<String> getItems() {
        return items;
    }

    public boolean isCommon() {
        return name.equals(ItemCategoriesPresetClient.COMMON.getName());
    }
}
