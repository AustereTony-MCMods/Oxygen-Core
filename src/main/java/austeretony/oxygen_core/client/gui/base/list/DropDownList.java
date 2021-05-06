package austeretony.oxygen_core.client.gui.base.list;

import austeretony.oxygen_core.client.gui.base.*;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.ListEntryClickListener;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DropDownList<T> extends Widget<DropDownList> {

    public static int Y_OFFSET = 1;

    @Nonnull
    protected Fill fill;
    @Nonnull
    protected Text text;
    @Nonnull
    protected Texture texture;

    @Nullable
    protected ListEntryClickListener clickListener;

    @Nullable
    protected ListEntry<T> previousClicked;
    protected boolean opened;

    public DropDownList(int x, int y, int width, @Nonnull Fill fill, @Nonnull Text text) {
        setPosition(x, y);
        setSize(width, LIST_HEIGHT);
        this.fill = fill;
        this.text = text;
        texture = Texture.builder()
                .texture(Textures.SORT_DOWN_ICONS)
                .size(3, 3)
                .imageSize(9, 3)
                .build();

        setVisible(true);
    }

    public DropDownList(int x, int y, int width, @Nonnull String text) {
        this(x, y, width, Fills.element(), Texts.panel(text).decrementScale(.05F));
    }

    public <V> DropDownList setEntryMouseClickListener(ListEntryClickListener<V> listener) {
        clickListener = listener;
        return this;
    }

    public void addElement(@Nonnull ListEntry<T> entry) {
        setEnabled(true);

        addWidget(entry);
        entry.setSize(getWidth(), getHeight());
        entry.setPosition(0, getHeight() + Y_OFFSET + getHeight() * (getWidgets().size() - 1));
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int
                color = fill.getColorEnabled(),
                iconU = texture.getU();
        if (!isEnabled())
            color = fill.getColorDisabled();
        else if (isMouseOver() && !opened)
            color = fill.getColorMouseOver();
        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), color);
        GUIUtils.drawFrame(0, 0, getWidth(), getHeight());

        GUIUtils.pushMatrix();
        GUIUtils.translate(2, (getHeight() - GUIUtils.getTextHeight(text.getScale())) / 2 + .5F);
        GUIUtils.scale(text.getScale(), text.getScale());

        color = text.getColorEnabled();
        if (!isEnabled()) {
            color = text.getColorDisabled();
            iconU += texture.getWidth();
        } else if (isMouseOver() && !opened) {
            color = text.getColorMouseOver();
            iconU += texture.getWidth() * 2;
        }

        GUIUtils.drawString(text.getText(), 0, 0, color, text.isShadowEnabled());
        GUIUtils.popMatrix();

        GUIUtils.colorDef();
        GUIUtils.drawTexturedRect(getWidth() - 5F, (getHeight() - texture.getHeight()) / 2F,
                texture.getWidth(), texture.getHeight(), texture.getTexture(), iconU, texture.getV(),
                texture.getImageWidth(), texture.getImageHeight());

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        if (opened) {
            int size = getWidgets().size();
            GUIUtils.drawRect(0, getHeight() + Y_OFFSET, getWidth(), Y_OFFSET + getHeight() * (size + 1),
                    CoreSettings.COLOR_BACKGROUND_BASE.asInt());
            GUIUtils.drawFrame(0, getHeight() + Y_OFFSET, getWidth(), getHeight() * size);
        }

        if (opened)
            for (Widget widget : getWidgets())
                widget.draw(mouseX, mouseY, partialTicks);

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseOver(int mouseX, int mouseY) {
        if (opened) {
            for (Widget widget : getWidgets())
                widget.mouseOver(mouseX - getX(), mouseY - getY());
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
                ListEntry<T> entry = (ListEntry) widget;
                if (entry.mouseClicked(mouseX, mouseY, mouseButton)) {
                    MinecraftClient.playUISound(SoundEffects.uiContextClose);
                    opened = false;
                    text.setText(entry.getText().getText());
                    if (clickListener != null)
                        clickListener.click(previousClicked, entry, mouseX, mouseY, mouseButton);
                    previousClicked = entry;
                    return true;
                }
            }
        }
        opened = false;

        if (isEnabled() && isMouseOver() && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiDropDownListOpen);
            opened = true;
        }

        return false;
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public void setFill(@Nonnull Fill fill) {
        this.fill = fill;
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    public void setText(@Nonnull Text text) {
        this.text = text;
    }

    @Nonnull
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(@Nonnull Texture texture) {
        this.texture = texture;
    }

    @Override
    public String toString() {
        return "DropDownList[" +
                "x= " + getX() + ", " +
                "y= " + getY() + ", " +
                "entries= " + getWidgets().size() + "" +
                "]";
    }
}