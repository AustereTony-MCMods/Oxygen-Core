package austeretony.oxygen_core.client.currency;

import com.google.gson.JsonObject;

import net.minecraft.util.ResourceLocation;

public interface CurrencyProperties {

    int getIndex();

    String getLocalizedName();

    void setIcon(ResourceLocation icon);

    ResourceLocation getIcon();

    int getIconWidth();

    int getIconHeight();

    int getXOffset();

    int getYOffset();

    void deserialize(JsonObject propertiesObject);

    JsonObject serialize();
}
