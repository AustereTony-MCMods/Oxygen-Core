package austeretony.oxygen_core.client.currency;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.client.api.ClientReference;
import net.minecraft.util.ResourceLocation;

public abstract class AbstractCurrencyProperties implements CurrencyProperties {

    private String name;

    private ResourceLocation icon;

    private int 
    width = 8, 
    height = 8, 
    xOffset, yOffset;

    public AbstractCurrencyProperties(String name, ResourceLocation icon, int width, int height, int xOffset, int yOffset) {
        this.name = name;
        this.icon = icon;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public String getLocalizedName() {
        return ClientReference.localize(this.name);
    }

    @Override
    public void setIcon(ResourceLocation icon) {
        this.icon = icon;
    }

    @Override
    public ResourceLocation getIcon() {
        return this.icon;
    }

    @Override
    public int getIconWidth() {
        return this.width;
    }

    @Override
    public int getIconHeight() {
        return this.height;
    }

    @Override
    public int getXOffset() {
        return this.xOffset;
    }

    @Override
    public int getYOffset() {
        return this.yOffset;
    }

    @Override
    public void deserialize(JsonObject propertiesObject) {
        this.name = propertiesObject.get("name").getAsString();

        this.width = propertiesObject.get("icon_width").getAsInt();
        this.height = propertiesObject.get("icon_height").getAsInt();
        this.xOffset = propertiesObject.get("icon_x_offset").getAsInt();
        this.yOffset = propertiesObject.get("icon_y_offset").getAsInt();
    }

    @Override
    public JsonObject serialize() {
        JsonObject propertiesObject = new JsonObject();
        propertiesObject.add("name", new JsonPrimitive(this.name));

        propertiesObject.add("icon_width", new JsonPrimitive(this.width));
        propertiesObject.add("icon_height", new JsonPrimitive(this.height));
        propertiesObject.add("icon_x_offset", new JsonPrimitive(this.xOffset));
        propertiesObject.add("icon_y_offset", new JsonPrimitive(this.yOffset));
        return propertiesObject;
    }
}
