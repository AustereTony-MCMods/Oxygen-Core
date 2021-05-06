package austeretony.oxygen_core.client.gui.base.context;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Layer;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ContextMenu<T> extends Widget<ContextMenu> {

    public static int MENU_WIDTH = 65;

    @Nonnull
    protected final List<ContextAction<T>> actions;
    protected boolean opened;
    @Nullable
    protected T currentEntry;

    public ContextMenu(@Nonnull List<ContextAction<T>> actions) {
        this.actions = actions;
    }

    public boolean open(int mouseX, int mouseY, T entry) {
        setEnabled(true);
        setVisible(true);
        MinecraftClient.playUISound(SoundEffects.uiContextOpen);

        setPosition(adjustMouseX(mouseX) + 4, adjustMouseY(mouseY));
        int i = 0;
        for (ContextAction<T> action : actions) {
            if (!action.isValid(entry)) continue;

            ListEntry<Integer> listEntry;
            addWidget(listEntry = new ListEntry<>(Texts.additional(action.getName(entry)), i));
            listEntry.setPosition(0, LIST_HEIGHT * i++);
            listEntry.setSize(MENU_WIDTH, LIST_HEIGHT);
            listEntry.getText().decrementScale(.05F);
        }

        if (getWidgets().isEmpty()) {
            return false;
        }

        setSize(MENU_WIDTH, LIST_HEIGHT * getWidgets().size());
        currentEntry = entry;
        opened = true;
        return true;
    }

    public void close() {
        getWidgets().clear();
        currentEntry = null;
        if (opened) {
            MinecraftClient.playUISound(SoundEffects.uiContextClose);
        }
        opened = false;

        setEnabled(false);
        setVisible(false);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {}

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.drawRect(0, 0, MENU_WIDTH, getHeight(), CoreSettings.COLOR_BACKGROUND_BASE.asInt());
        GUIUtils.drawFrame(0, 0, getWidth(), getHeight());

        for (Widget widget : getWidgets()) {
            widget.draw(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseOver(int mouseX, int mouseY) {
        if (opened) {
            for (Widget widget : getWidgets()) {
                widget.mouseOver(mouseX - getX(), mouseY - getY());
            }
        }
        if (getLayer() == Layer.MIDDLE) {
            mouseX -= getScreen().getWorkspace().getX();
            mouseY -= getScreen().getWorkspace().getY();
        }
        return mouseOver = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + getHeight();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (opened) {
            for (Widget widget : getWidgets()) {
                ListEntry<Integer> entry = (ListEntry) widget;
                if (entry.mouseClicked(mouseX, mouseY, mouseButton)) {
                    ContextAction<T> action = actions.get(entry.getEntry());
                    action.execute(currentEntry);
                    close();
                    return true;
                }
            }
        }
        close();
        return false;
    }

    public boolean isOpened() {
        return opened;
    }

    @Override
    public String toString() {
        return "ContextMenu[" +
                "x= " + getX() + ", " +
                "y= " + getY() + ", " +
                "entries= " + getWidgets().size() + "" +
                "]";
    }
}
