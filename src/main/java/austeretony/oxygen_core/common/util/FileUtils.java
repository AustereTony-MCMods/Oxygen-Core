package austeretony.oxygen_core.common.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;

import austeretony.oxygen_core.common.main.OxygenMain;

public class FileUtils {

    @Nonnull
    public static byte[] loadImageBytes(String pathStr) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(new File(pathStr));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException exception) {
            OxygenMain.logError(1, "[Core] Failed to load image bytes: {}", pathStr);
            exception.printStackTrace();
            return new byte[0];
        }
    }

    @Nonnull
    public static byte[] loadImageBytesFromJar(String assetPathStr) {
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(Objects.requireNonNull(FileUtils.class.getClassLoader()
                    .getResourceAsStream(assetPathStr)));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", baos);
            return baos.toByteArray();
        } catch (IOException exception) {
            OxygenMain.logError(1, "[Core] Failed to load image bytes from jar: {}", assetPathStr);
            exception.printStackTrace();
            return new byte[0];
        }
    }

    public static void saveImage(String pathStr, byte[] imageRaw) {
        Path path = Paths.get(pathStr);
        try {
            if (!Files.exists(path))
                Files.createDirectories(path.getParent());

            ByteArrayInputStream baos = new ByteArrayInputStream(imageRaw);
            BufferedImage bufferedImage = null;
            try {
                bufferedImage = ImageIO.read(baos);
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to create buffered image from bytes: {}", pathStr);
                exception.printStackTrace();
            }

            if (bufferedImage != null)
                ImageIO.write(bufferedImage, "png", path.toFile());
        } catch (IOException exception) {
            OxygenMain.logError(1, "[Core] Failed to save image: {}", pathStr);
            exception.printStackTrace();
        }
    }
}
