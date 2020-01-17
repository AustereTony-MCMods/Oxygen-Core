package austeretony.oxygen_core.client.util;

import java.awt.image.BufferedImage;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import austeretony.oxygen_core.client.api.ClientReference;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.Framebuffer;

public class ScreenshotHelper {

    public static BufferedImage createScreenshot(int width, int height) {
        return resize(prepareImage(ClientReference.getMinecraft().displayWidth, ClientReference.getMinecraft().displayHeight, 
                ClientReference.getMinecraft().getFramebuffer()), width, height);
    }

    public static BufferedImage prepareImage(int width, int height, Framebuffer frameBuffer) {   	
        if (OpenGlHelper.isFramebufferEnabled()) {        	
            width = frameBuffer.framebufferTextureWidth;        
            height = frameBuffer.framebufferTextureHeight;
        }
        int i = width * height;
        IntBuffer pixelBuffer = null;
        int[] pixelValues = null;
        if (pixelBuffer == null || pixelBuffer.capacity() < i) {
            pixelBuffer = BufferUtils.createIntBuffer(i);
            pixelValues = new int[i];
        }
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();
        if (OpenGlHelper.isFramebufferEnabled()) {       	
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, frameBuffer.framebufferTexture);
            GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        } else
            GL11.glReadPixels(0, 0, width, height, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        pixelBuffer.get(pixelValues);
        processPixelValues(pixelValues, width, height);
        BufferedImage bufferedImage = new BufferedImage(width, height, 1);
        bufferedImage.setRGB(0, 0, width, height, pixelValues, 0, width);
        return bufferedImage;
    }

    public static BufferedImage resize(BufferedImage bufferedImage, int newWidth, int newHeight) {    	
        BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        int x, y, color,
        oldWidth = bufferedImage.getWidth(),
        oldHeight = bufferedImage.getHeight();
        for (x = 0; x < newWidth; x++) {
            for (y = 0; y < newHeight; y++) {
                color = bufferedImage.getRGB(x * oldWidth / newWidth, y * oldHeight / newHeight);
                resizedImage.setRGB(x, y, color);
            }
        }
        return resizedImage;
    }

    public static void processPixelValues(int[] values, int width, int height) {
        int[] aint = new int[width];
        int i = height / 2;
        for (int j = 0; j < i; ++j) {       	
            System.arraycopy(values, j * width, aint, 0, width);
            System.arraycopy(values, (height - 1 - j) * width, values, j * width, width);
            System.arraycopy(aint, 0, values, (height - 1 - j) * width, width);
        }
    }
}
