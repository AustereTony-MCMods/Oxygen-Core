package austeretony.oxygen_core.client.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;

public class ClientUtils {

    @Nonnull
    public static ResourceLocation getTextureLocationFromBytes(@Nonnull byte[] imageRaw) {
        if (imageRaw.length > 0) {
            ByteArrayInputStream baos = new ByteArrayInputStream(imageRaw);
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(baos);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            if (bufferedImage != null) {
                return MinecraftClient.getMinecraft().getTextureManager().getDynamicTextureLocation("oxygen_texture",
                        new DynamicTexture(bufferedImage));
            }
        }
        return TextureManager.RESOURCE_LOCATION_EMPTY;
    }
}
