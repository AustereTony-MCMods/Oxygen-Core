package austeretony.oxygen_core.client.gui.base.core;

import austeretony.oxygen_core.client.gui.base.Alignment;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Workspace {

    protected OxygenScreen screen;
    protected int x, y, width, height;
    protected final List<Section> sections = new ArrayList<>(1);
    protected Section currentSection;

    public Workspace(OxygenScreen screen, int width, int height) {
        this.screen = screen;
        this.x = (screen.width - width) / 2;
        this.y = (screen.height - height) / 2;
        this.width = width;
        this.height = height;
    }

    public List<Section> getSections() {
        return sections;
    }

    public Section getCurrentSection() {
        return currentSection;
    }

    public void addSection(Section section) {
        section.setScreen(screen);
        section.setPosition(x, y);
        section.setSize(width, height);
        sections.add(section);
    }

    public void setCurrentSection(@Nonnull Section section) {
        currentSection = section;
    }

    public void setAlignment(Alignment alignment, int xOffset, int yOffset) {
        int xPos = 0;
        int yPos = 0;

        switch (alignment) {
            case BOTTOM:
                xPos = (screen.width - width) / 2;
                yPos = screen.height - height;
                break;
            case BOTTOM_LEFT:
                xPos = 0;
                yPos = screen.height - height;
                break;
            case BOTTOM_RIGHT:
                xPos = screen.width - width;
                yPos = screen.height - height;
                break;
            case CENTER:
                xPos = (screen.width - width) / 2;
                yPos = (screen.height - height) / 2;
                break;
            case LEFT:
                xPos = 0;
                yPos = (screen.height - height) / 2;
                break;
            case RIGHT:
                xPos = screen.width - width;
                yPos = (screen.height - height) / 2;
                break;
            case TOP:
                xPos = (screen.width - width) / 2;
                yPos = 0;
                break;
            case TOP_LEFT:
                xPos = 0;
                yPos = 0;
                break;
            case TOP_RIGHT:
                xPos = screen.width - width;
                yPos = 0;
                break;
        }
        x = xPos + xOffset;
        y = yPos + yOffset;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (currentSection != null) {
            currentSection.mouseOver(mouseX, mouseY);
            currentSection.drawBackgroundLayer(mouseX, mouseY, partialTicks);
            GUIUtils.pushMatrix();
            GUIUtils.translate(x, y);
            currentSection.drawMiddleLayer(mouseX - x, mouseY - y, partialTicks);
            GUIUtils.popMatrix();
            currentSection.drawFrontLayer(mouseX, mouseY, partialTicks);
            currentSection.drawCallback(mouseX, mouseY, partialTicks);
        }
    }

    public void handleMouseInput() throws IOException {
        if (getCurrentSection() != null) {
            getCurrentSection().handleMouseInput();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (getCurrentSection() != null) {
            getCurrentSection().mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (getCurrentSection() != null) {
            getCurrentSection().mouseReleased(mouseX, mouseY, state);
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (getCurrentSection() != null) {
            getCurrentSection().mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if (getCurrentSection() != null) {
            Section section = getCurrentSection();
            boolean callbackOpened = section.getCurrentCallback() != null;

            if (keyCode == Keyboard.KEY_ESCAPE) {
                if (callbackOpened) {
                    if (section.getCurrentCallback().close()) {
                        return;
                    }
                }
            }

            section.keyTyped(typedChar, keyCode);
            if (keyCode == Keyboard.KEY_ESCAPE && !callbackOpened) {
                screen.close();
            }
        }
    }

    public void updateScreen() {
        if (getCurrentSection() != null) {
            getCurrentSection().update();
        }
    }

    public void clearSections() {
        currentSection = null;
        sections.clear();
    }

    public void resized(Minecraft mc, int width, int height) {
        if (currentSection != null) {
            currentSection.resized(mc, width, height);
        }
    }

    public void closed() {
        if (currentSection != null) {
            currentSection.closed();
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
