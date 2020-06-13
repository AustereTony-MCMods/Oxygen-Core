package austeretony.oxygen_core.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;

import austeretony.oxygen_core.common.main.OxygenMain;

public class FilesUtils {

    @Nonnull
    public static byte[] loadImageBytes(String pathStr, @Nullable String errorMessage) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(pathStr));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException exception) {
            OxygenMain.LOGGER.error(errorMessage != null ? errorMessage : "[Core] Failed to load image bytes: {}", pathStr);
            exception.printStackTrace();
            return new byte[0];
        }
    }
}
