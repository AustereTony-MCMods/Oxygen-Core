package austeretony.oxygen_core.client.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import austeretony.oxygen_core.client.api.ClientReference;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class ClientUtils {

    @Nonnull
    public static ResourceLocation getTexturePathFromBytes(byte[] imageRaw) {
        if (imageRaw.length > 0) {
            ByteArrayInputStream baos = new ByteArrayInputStream(imageRaw);
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(baos);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            if (bufferedImage != null)
                return ClientReference.getMinecraft().getTextureManager().getDynamicTextureLocation("oxygen_texture", 
                        new DynamicTexture(bufferedImage));
        }
        return ClientReference.getMinecraft().getTextureManager().RESOURCE_LOCATION_EMPTY;
    }
}
