package austeretony.oxygen_core.client.gui.base.core;

import austeretony.oxygen_core.client.gui.base.Layer;
import austeretony.oxygen_core.client.util.MinecraftClient;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class Callback {

    @Nonnull
    protected OxygenScreen screen;
    @Nonnull
    protected Section section;
    protected int x, y, width, height;
    @Nonnull
    protected String name = "Callback";

    protected boolean initialized;

    protected final List<Widget> widgets = new ArrayList<>();

    public Callback(int width, int height) {
        setSize(width, height);
    }

    public final void open() {
        if (!initialized) {
            init();
            initialized = true;
        }
        opened();
    }

    public abstract void init();

    protected void opened() {}

    public boolean close() {
        section.closeCallback();
        closed();
        return true;
    }

    protected void closed() {}

    @Nonnull
    public List<Widget> getWidgets() {
        return widgets;
    }

    public void addWidget(@Nonnull Widget widget) {
        widget.setLayer(Layer.FRONT);
        widget.setPosition(getX() + widget.getX(), getY() + widget.getY());
        widget.setScreenPosition(getX() + widget.getX(), getY() + widget.getY());
        widget.setScreen(screen);
        widget.init();
        widgets.add(widget);
    }

    public void update() {
        for (Widget widget : widgets) {
            widget.update();
        }
    }

    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        for (Widget widget : widgets) {
            widget.drawBackground(mouseX, mouseY, partialTicks);
        }
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        for (Widget widget : widgets) {
            widget.draw(mouseX, mouseY, partialTicks);
        }
    }

    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        for (Widget widget : widgets) {
            widget.drawForeground(mouseX, mouseY, partialTicks);
        }
    }

    public void mouseOver(int mouseX, int mouseY) {
        for (Widget widget : widgets) {
            widget.mouseOver(mouseX, mouseY);
        }
    }

    public void handleMouseInput() {
        for (Widget widget : widgets) {
            widget.handleMouseInput();
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        List<Widget> list = new ArrayList<>(widgets);
        boolean success = false;
        for (Widget widget : list) {
            if (success) {
                widget.setMouseOver(false);
            }
            if (widget.mouseClicked(mouseX, mouseY, mouseButton)) {
                success = true;
            }
        }
    }

    public void mouseReleased(int mouseX, int mouseY, int state) {
        for (Widget widget : widgets) {
            widget.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        for (Widget widget : widgets) {
            widget.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        for (Widget widget : widgets) {
            widget.keyTyped(typedChar, keyCode);
        }
    }

    public void resized(Minecraft mc, int width, int height) {}

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Nonnull
    public OxygenScreen getScreen() {
        return screen;
    }

    public void setScreen(@Nonnull OxygenScreen screen) {
        this.screen = screen;
    }

    @Nonnull
    public Workspace getWorkspace() {
        return getScreen().getWorkspace();
    }

    @Nonnull
    public Section getSection() {
        return getWorkspace().getCurrentSection();
    }

    public void setSection(@Nonnull Section section) {
        this.section = section;
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    protected static String localize(String key, Object... args) {
        return MinecraftClient.localize(key, args);
    }
}
