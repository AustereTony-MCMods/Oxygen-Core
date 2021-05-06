package austeretony.oxygen_core.client.gui.base.core;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Layer;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Widget<T extends Widget> {

    public static final double FRAME_WIDTH = .4D;
    public static final int LIST_HEIGHT = 9;
    public static final int TOOLTIP_HEIGHT = 9;

    @Nonnull
    protected OxygenScreen screen;
    protected int x, y, screenX, screenY, width, height;
    protected boolean visible, enabled, mouseOver;
    protected Layer layer = Layer.MIDDLE;
    @Nullable
    protected List<Widget> widgets;

    public void init() {
        for (Widget widget : getWidgets()) {
            widget.setScreen(screen);
            widget.setLayer(layer);
            widget.setEnabled(enabled);
            widget.setVisible(visible);
            widget.init();
        }
    }

    public void update() {
        if (!isEnabled()) return;
        for (Widget widget : getWidgets()) {
            widget.update();
        }
    }

    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        mouseX -= getX();
        mouseY -= getY();

        for (Widget widget : getWidgets()) {
            widget.drawBackground(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();
    }

    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        mouseX -= getX();
        mouseY -= getY();

        for (Widget widget : getWidgets()) {
            widget.draw(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();
    }

    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        mouseX -= getX();
        mouseY -= getY();

        for (Widget widget : getWidgets()) {
            widget.drawForeground(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();
    }

    public boolean mouseOver(int mouseX, int mouseY) {
        for (Widget widget : getWidgets()) {
            widget.mouseOver(mouseX - getX(), mouseY - getY());
        }
        if (getLayer() == Layer.MIDDLE) {
            mouseX -= getScreen().getWorkspace().getX();
            mouseY -= getScreen().getWorkspace().getY();
        }
        return mouseOver = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + getHeight();
    }

    public void handleMouseInput() {
        if (!isEnabled()) return;
        for (Widget widget : getWidgets()) {
            widget.handleMouseInput();
        }
    }

    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        for (Widget widget : getWidgets()) {
            if (widget.mouseClicked(mouseX - getX(), mouseY - getY(), mouseButton)) {
                return true;
            }
        }
        return isEnabled() && isMouseOver();
    }

    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (!isEnabled()) return;
        for (Widget widget : getWidgets()) {
            widget.mouseReleased(mouseX - getX(), mouseY - getY(), state);
        }
    }

    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (!isEnabled()) return;
        for (Widget widget : getWidgets()) {
            widget.mouseClickMove(mouseX - getX(), mouseY - getY(), clickedMouseButton, timeSinceLastClick);
        }
    }

    public boolean keyTyped(char typedChar, int keyCode) {
        if (!isEnabled()) return false;
        for (Widget widget : getWidgets()) {
            widget.keyTyped(typedChar, keyCode);
        }
        return false;
    }

    public void clear() {
        if (widgets != null) {
            widgets.clear();
        }
    }

    public OxygenScreen getScreen() {
        return screen;
    }

    public void setScreen(OxygenScreen screen) {
        this.screen = screen;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        setScreenPosition(getScreenX() + (x - this.x) , getScreenY() + (y - this.y));
        this.x = x;
        this.y = y;
    }

    public void setX(int value) {
        setScreenPosition(getScreenX() + (value - x), getScreenY());
        x = value;
    }

    public void setY(int value) {
        setScreenPosition(getScreenX(), getScreenY() + (value - y));
        y = value;
    }

    public int getScreenX() {
        return screenX;
    }

    public int getScreenY() {
        return screenY;
    }

    public void setScreenPosition(int x, int y) {
        screenX = x;
        screenY = y;
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

    public void setWidth(int value) {
        width = value;
    }

    public void setHeight(int value) {
        height = value;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public T setEnabled(boolean flag) {
        enabled = flag;
        for (Widget widget : getWidgets()) {
            widget.setEnabled(flag);
        }
        return (T) this;
    }

    public boolean isVisible() {
        return visible;
    }

    public T setVisible(boolean flag) {
        visible = flag;
        for (Widget widget : getWidgets()) {
            widget.setVisible(flag);
        }
        return (T) this;
    }

    @Nonnull
    public Layer getLayer() {
        return layer;
    }

    public T setLayer(@Nonnull Layer layer) {
        this.layer = layer;
        for (Widget widget : getWidgets()) {
            widget.setLayer(layer);
        }
        return (T) this;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean flag) {
        for (Widget widget : getWidgets()) {
            widget.setMouseOver(flag);
        }
        mouseOver = flag;
    }

    @Nonnull
    public List<Widget> getWidgets() {
        return widgets == null ? Collections.emptyList() : widgets;
    }

    public void addWidget(@Nonnull Widget widget) {
        if (widgets == null) {
            widgets = new ArrayList<>(1);
        }
        widget.setScreen(screen);
        widget.setScreenPosition(getScreenX() + widget.getX(), getScreenY() + widget.getY());
        widget.setLayer(layer);
        widget.setEnabled(enabled);
        widget.setVisible(visible);
        widget.init();
        widgets.add(widget);
    }

    @Override
    public String toString() {
        return "Widget[" +
                "x= " + getX() + ", " +
                "y= " + getY() + ", " +
                "width= " + getWidth() + ", " +
                "height= " + getHeight() + "" +
                "]";
    }

    public int adjustMouseX(int mouseX) {
        if (getLayer() == Layer.MIDDLE) {
            mouseX -= getScreen().getWorkspace().getX();
        }
        return mouseX;
    }

    public int adjustMouseY(int mouseY) {
        if (getLayer() == Layer.MIDDLE) {
            mouseY -= getScreen().getWorkspace().getY();
        }
        return mouseY;
    }

    public int getColorFromState(@Nonnull Fill fill) {
        if (!isEnabled()) {
            return fill.getColorDisabled();
        } else if (isMouseOver()) {
            return fill.getColorMouseOver();
        }
        return fill.getColorEnabled();
    }

    public int getColorFromState(@Nonnull Text text) {
        if (!isEnabled()) {
            return text.getColorDisabled();
        } else if (isMouseOver()) {
            return text.getColorMouseOver();
        }
        return text.getColorEnabled();
    }

    public static void drawToolTip(@Nonnull Widget widget, @Nonnull String text) {
        float width = GUIUtils.getTextWidth(text, CoreSettings.SCALE_TEXT_TOOLTIP.asFloat()) + 6F;

        float x = (widget.getX() + widget.getWidth() / 2F) - (width / 2F);
        float startX = widget.getScreenX() + width > widget.getScreen().width ? x - width / 2F : x;

        drawToolTip(startX, widget.getY() - TOOLTIP_HEIGHT - 1F, text);
    }

    public static void drawToolTip(float x, float y, @Nonnull String text) {
        float textScale = CoreSettings.SCALE_TEXT_TOOLTIP.asFloat();
        float width = GUIUtils.getTextWidth(text, textScale) + 6F;
        float height = TOOLTIP_HEIGHT;

        GUIUtils.pushMatrix();
        GUIUtils.translate(x, y);

        GUIUtils.drawRect(0, 0, width, height,
                GUIUtils.scaleAlpha(CoreSettings.COLOR_BACKGROUND_BASE.asInt(), 1F));
        GUIUtils.drawFrame(0, 0, width, height);

        float textY = (height - GUIUtils.getTextHeight(textScale)) / 2F + .5F;
        GUIUtils.drawString(text, 3F, textY, textScale, CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt(), false);

        GUIUtils.popMatrix();
    }

    public static void drawToolTip(float x, float y, @Nonnull List<String> lines) {
        float textScale = CoreSettings.SCALE_TEXT_TOOLTIP.asFloat();
        float width = 0;
        for (String line : lines) {
            float lineWidth = GUIUtils.getTextWidth(line, textScale) + 6F;
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        float height = TOOLTIP_HEIGHT * lines.size();

        GUIUtils.pushMatrix();
        GUIUtils.translate(x, y);

        GUIUtils.drawRect(0, 0, width, height,
                GUIUtils.scaleAlpha(CoreSettings.COLOR_BACKGROUND_BASE.asInt(), 1F));
        GUIUtils.drawFrame(0, 0, width, height);

        float textY = (TOOLTIP_HEIGHT - GUIUtils.getTextHeight(textScale)) / 2F + .5F;
        for (int i = 0; i < lines.size(); i++) {
            GUIUtils.drawString(lines.get(i), 3F, textY + GUIUtils.getTextHeight(1F) * i, textScale,
                    CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt(), false);
        }

        GUIUtils.popMatrix();
    }

    public static void drawToolTip(float x, float y, @Nonnull ItemStack itemStack) {
        drawToolTip(x, y, GUIUtils.getItemStackToolTip(itemStack));
    }

    protected static String localize(String key, Object... args) {
        return MinecraftClient.localize(key, args);
    }
}
