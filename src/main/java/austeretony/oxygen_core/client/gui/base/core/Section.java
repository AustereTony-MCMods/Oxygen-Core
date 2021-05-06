package austeretony.oxygen_core.client.gui.base.core;

import austeretony.oxygen_core.client.gui.base.Layer;
import austeretony.oxygen_core.client.util.MinecraftClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class Section {

    @Nonnull
    protected OxygenScreen screen;
    protected int x, y, width, height;
    @Nonnull
    protected String name;
    protected boolean enabled;

    protected final List<Widget> widgets = new ArrayList<>();

    @Nullable
    private Callback currentCallback;
    @Nullable
    private Widget mouseOverWidget;

    public Section(@Nonnull OxygenScreen screen, @Nonnull String name, boolean enabled) {
        this.screen = screen;
        this.name = name;
        this.enabled = enabled;
    }

    public Section(@Nonnull OxygenScreen screen) {
        this(screen, "", true);
    }

    public void open() {
        getWorkspace().setCurrentSection(this);
    }

    public abstract void init();

    @Nonnull
    public List<Widget> getWidgets() {
        return widgets;
    }

    public void addWidget(@Nonnull Widget widget) {
        widget.setScreen(screen);
        if (widget.getLayer() == Layer.MIDDLE) {
            widget.setScreenPosition(getX() + widget.getX(), getY() + widget.getY());
        } else {
            widget.setScreenPosition(widget.getX(), widget.getY());
        }
        widget.init();
        widgets.add(widget);
    }

    public void update() {
        for (Widget widget : widgets) {
            widget.update();
        }
        if (currentCallback != null) {
            currentCallback.update();
        }
    }

    public void drawBackgroundLayer(int mouseX, int mouseY, float partialTicks) {
        List<Widget> backgroundWidgets = widgets
                .stream()
                .filter(e -> e.getLayer() == Layer.BACKGROUND)
                .collect(Collectors.toList());

        for (Widget widget : backgroundWidgets) {
            widget.drawBackground(mouseX, mouseY, partialTicks);
        }
        for (Widget widget : backgroundWidgets) {
            widget.draw(mouseX, mouseY, partialTicks);
        }
        for (Widget widget : backgroundWidgets) {
            widget.drawForeground(mouseX, mouseY, partialTicks);
        }
    }

    public void drawMiddleLayer(int mouseX, int mouseY, float partialTicks) {
        List<Widget> middleWidgets = widgets
                .stream()
                .filter(e -> e.getLayer() == Layer.MIDDLE)
                .collect(Collectors.toList());

        for (Widget widget : middleWidgets) {
            widget.drawBackground(mouseX, mouseY, partialTicks);
        }
        for (Widget widget : middleWidgets) {
            widget.draw(mouseX, mouseY, partialTicks);
        }
        for (Widget widget : middleWidgets) {
            widget.drawForeground(mouseX, mouseY, partialTicks);
        }
    }

    public void drawFrontLayer(int mouseX, int mouseY, float partialTicks) {
        List<Widget> frontWidgets = widgets
                .stream()
                .filter(e -> e.getLayer() == Layer.FRONT)
                .collect(Collectors.toList());

        for (Widget widget : frontWidgets) {
            widget.drawBackground(mouseX, mouseY, partialTicks);
        }
        for (Widget widget : frontWidgets) {
            widget.draw(mouseX, mouseY, partialTicks);
        }
        for (Widget widget : frontWidgets) {
            widget.drawForeground(mouseX, mouseY, partialTicks);
        }
    }

    public void drawCallback(int mouseX, int mouseY, float partialTicks) {
        if (currentCallback != null) {
            currentCallback.mouseOver(mouseX, mouseY);
            currentCallback.drawBackground(mouseX, mouseY, partialTicks);
            currentCallback.draw(mouseX, mouseY, partialTicks);
            currentCallback.drawForeground(mouseX, mouseY, partialTicks);
        }
    }

    public void handleMouseInput() {
        if (currentCallback != null) {
            currentCallback.handleMouseInput();
            return;
        }
        for (Widget widget : widgets) {
            widget.handleMouseInput();
        }
    }

    public void mouseOver(int mouseX, int mouseY) {
        if (currentCallback != null) return;
        for (Widget widget : widgets) {
            if (widget.mouseOver(mouseX, mouseY)) {
                mouseOverWidget = widget;
            }
        }
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (currentCallback != null) {
            currentCallback.mouseClicked(mouseX, mouseY, mouseButton);
            return;
        }
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
        if (currentCallback != null) {
            currentCallback.mouseReleased(mouseX, mouseY, state);
            return;
        }
        for (Widget widget : widgets) {
            widget.mouseReleased(mouseX, mouseY, state);
        }
    }

    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (currentCallback != null) {
            currentCallback.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
            return;
        }
        for (Widget widget : widgets) {
            widget.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    public void keyTyped(char typedChar, int keyCode) {
        if (currentCallback != null) {
            currentCallback.keyTyped(typedChar, keyCode);
            return;
        }
        for (Widget widget : widgets) {
            widget.keyTyped(typedChar, keyCode);
        }
    }

    public void resized(Minecraft mc, int width, int height) {
        if (currentCallback != null) {
            currentCallback.resized(mc, width, height);
        }
    }

    public void closed() {}

    @Nullable
    public Callback getCurrentCallback() {
        return currentCallback;
    }

    @Nullable
    public static Callback tryGetCurrentCallback() {
        GuiScreen screen = MinecraftClient.getCurrentScreen();
        if (screen instanceof OxygenScreen) {
            return ((OxygenScreen) screen).getWorkspace().getCurrentSection().getCurrentCallback();
        }
        return null;
    }

    public void openCallback(@Nonnull Callback callback) {
        if (mouseOverWidget != null) {
            mouseOverWidget.setMouseOver(false);
        }
        currentCallback = callback;
        callback.setScreen(screen);
        callback.setSection(this);
        callback.setPosition((screen.width - callback.getWidth()) / 2, (screen.height - callback.getHeight()) / 2);
        callback.open();
    }

    public static boolean tryOpenCallback(@Nonnull Callback callback) {
        GuiScreen screen = MinecraftClient.getCurrentScreen();
        if (screen instanceof OxygenScreen) {
            ((OxygenScreen) screen).getWorkspace().getCurrentSection().openCallback(callback);
            return true;
        }
        return false;
    }

    public void closeCallback() {
        currentCallback = null;
    }

    public static void tryCloseCallback() {
        GuiScreen screen = MinecraftClient.getCurrentScreen();
        if (screen instanceof OxygenScreen) {
            ((OxygenScreen) screen).getWorkspace().getCurrentSection().closeCallback();
        }
    }

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

    public Workspace getWorkspace() {
        return getScreen().getWorkspace();
    }

    @Nonnull
    public String getName() {
        return name;
    }

    public void setName(@Nonnull String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean flag) {
        enabled = flag;
    }

    protected static String localize(String key, Object... args) {
        return MinecraftClient.localize(key, args);
    }
}
