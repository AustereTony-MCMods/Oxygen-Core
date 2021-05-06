package austeretony.oxygen_core.client.gui.base.common;

import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.core.Widget;

import javax.annotation.Nonnull;

public class ListEntry<T> extends Widget<ListEntry> {

    @Nonnull
    protected final T entry;
    @Nonnull
    protected Fill fill;
    @Nonnull
    protected Text text;

    protected boolean selected;

    public ListEntry(@Nonnull Text text, @Nonnull T entry) {
        this.entry = entry;
        fill = Fills.element();
        this.text = text;

        setEnabled(true);
        setVisible(true);
    }

    public ListEntry(@Nonnull String text, @Nonnull T entry) {
        this(Texts.panel(text), entry);
    }

    public static <V> ListEntry<V> of(@Nonnull String text, @Nonnull V entry) {
        return new ListEntry<>(text, entry);
    }

    @Nonnull
    public T getEntry() {
        return entry;
    }

    public boolean isSelected() {
        return selected;
    }

    public ListEntry setSelected(boolean flag) {
        selected = flag;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int color = fill.getColorEnabled();
        if (!isEnabled())
            color = fill.getColorDisabled();
        else if (isMouseOver() || selected)
            color = fill.getColorMouseOver();
        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), color);

        color = text.getColorEnabled();
        if (!isEnabled())
            color = text.getColorDisabled();
        else if (isMouseOver() || selected)
            color = text.getColorMouseOver();
        GUIUtils.drawString(text, 2F, (getHeight() - GUIUtils.getTextHeight(text.getScale())) / 2F + .5F, color);

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return super.mouseClicked(mouseX, mouseY, mouseButton) && mouseButton == MouseButtons.LEFT_BUTTON;
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    @Nonnull
    public ListEntry setText(@Nonnull Text text) {
        this.text = text;
        return this;
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    @Nonnull
    public ListEntry setFill(@Nonnull Fill fill) {
        this.fill = fill;
        return this;
    }

    @Override
    public String toString() {
        return "ListEntry[entry= " + entry.toString() + "]";
    }
}
