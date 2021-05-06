package austeretony.oxygen_core.client.preset;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.util.ClientUtils;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.FileUtils;
import com.google.gson.JsonObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class CurrencyProperties {

    private final int index;
    private final String name;
    private final byte[] iconRaw;
    private final int iconWidth, iconHeight, iconXOffset, iconYOffset;

    private ResourceLocation iconTexture;

    CurrencyProperties(int index, String name, byte[] iconRaw, int iconWidth, int iconHeight, int iconXOffset,
                       int iconYOffset) {
        this.index = index;
        this.name = name;
        this.iconRaw = iconRaw;
        this.iconWidth = iconWidth;
        this.iconHeight = iconHeight;
        this.iconXOffset = iconXOffset;
        this.iconYOffset = iconYOffset;
    }

    public int getCurrencyIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getLocalizedName() {
        return MinecraftClient.localize(name);
    }

    public int getIconWidth() {
        return iconWidth;
    }

    public int getIconHeight() {
        return iconHeight;
    }

    public int getIconXOffset() {
        return iconXOffset;
    }

    public int getIconYOffset() {
        return iconYOffset;
    }

    public byte[] getIconRaw() {
        return iconRaw;
    }

    @Nonnull
    public ResourceLocation getIconTexture() {
        if (iconTexture == null) {
            iconTexture = ClientUtils.getTextureLocationFromBytes(iconRaw);
        }
        return iconTexture;
    }

    static CurrencyProperties read(ByteBuf buffer) {
        byte[] iconRaw = new byte[buffer.readInt()];
        buffer.readBytes(iconRaw);
        return new CurrencyProperties(buffer.readByte(), ByteBufUtils.readString(buffer), iconRaw, buffer.readByte(),
                buffer.readByte(), buffer.readByte(), buffer.readByte());
    }

    static CurrencyProperties fromJson(JsonObject jsonObject) {
        int index = jsonObject.get("index").getAsInt();
        String iconPath = OxygenCommon.getConfigFolder() + "/data/client/worlds/" + OxygenClient.getWorldId()
                + "/presets/currency_properties/currency_icon_" + index + ".png";
        return new CurrencyProperties(index, jsonObject.get("name").getAsString(), FileUtils.loadImageBytes(iconPath),
                jsonObject.get("icon_width").getAsInt(), jsonObject.get("icon_height").getAsInt(),
                jsonObject.get("icon_x_offset").getAsInt(), jsonObject.get("icon_y_offset").getAsInt());
    }

    JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("index", index);
        jsonObject.addProperty("name", name);
        jsonObject.addProperty("icon_width", iconWidth);
        jsonObject.addProperty("icon_height", iconHeight);
        jsonObject.addProperty("icon_x_offset", iconXOffset);
        jsonObject.addProperty("icon_y_offset", iconYOffset);
        return jsonObject;
    }
}
