package austeretony.oxygen_core.client.gui.base.common;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Layer;
import austeretony.oxygen_core.client.gui.base.button.VerticalSlider;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import org.lwjgl.input.Mouse;

import javax.annotation.Nullable;
import java.util.List;

public class ScrollableWidgetGroup extends Widget<ScrollableWidgetGroup> {

    protected int scroll;

    @Nullable
    protected VerticalSlider slider;

    public ScrollableWidgetGroup(int x, int y, int width, int height) {
        setPosition(x, y);
        setSize(width, height);

        setEnabled(true);
        setVisible(true);
    }

    public void setSlider(VerticalSlider slider) {
        this.slider = slider;
    }

    @Override
    public void addWidget(Widget widget) {
        super.addWidget(widget);
        if (slider != null) {
            slider.calculateCarriageSize(getHeight(), calculateTotalElementsHeight());
        }
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.scissor(getX() + getScreen().getWorkspace().getX() - 1, getY() + getScreen().getWorkspace().getY(),
                getWidth(), getHeight());
        GUIUtils.pushScissor();

        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY() - scroll);

        for (Widget widget : getWidgets()) {
            widget.drawBackground(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();

        GUIUtils.popScissor();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.scissor(getX() + getScreen().getWorkspace().getX() - 1, getY() + getScreen().getWorkspace().getY(),
                getWidth(), getHeight());
        GUIUtils.pushScissor();

        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY() - scroll);

        for (Widget widget : getWidgets()) {
            widget.draw(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();

        GUIUtils.popScissor();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.scissor(getX() + getScreen().getWorkspace().getX() - 1, getY() + getScreen().getWorkspace().getY(),
                getWidth(), getHeight());
        GUIUtils.pushScissor();

        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY() - scroll);

        for (Widget widget : getWidgets()) {
            widget.drawForeground(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();

        GUIUtils.popScissor();
    }

    @Override
    public boolean mouseOver(int mouseX, int mouseY) {
        super.mouseOver(mouseX, mouseY + scroll);
        if (getLayer() == Layer.MIDDLE) {
            mouseX -= getScreen().getWorkspace().getX();
            mouseY -= getScreen().getWorkspace().getY();
        }
        return mouseOver = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + getHeight();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        return super.mouseClicked(mouseX, mouseY + scroll, mouseButton);
    }

    @Override
    public void handleMouseInput() {
        if (!isEnabled() || !isMouseOver()) return;

        int delta = Mouse.getEventDWheel();
        if (delta != 0) {
            if (delta > 0) {
                delta = -1;
            } else {
                delta = 1;
            }
            float amountScrolled = (float) (delta * 10);
            if (scroll + amountScrolled > 0) {
                if (scroll >= (calculateTotalElementsHeight() - getHeight()) && delta > 0) return;
                scroll += amountScrolled;
            } else
                scroll = 0;
        }

        if (slider != null) {
            slider.updateCarriagePosition(getHeight(), calculateTotalElementsHeight(), scroll);
        }

        super.handleMouseInput();
    }

    private int calculateTotalElementsHeight() {
        List<Widget> widgets = getWidgets();
        if (!widgets.isEmpty()) {
            Widget first = widgets.get(0);
            int yStart = first.getY();
            if (widgets.size() > 1) {
                Widget last = widgets.get(widgets.size() - 1);
                return last.getY() - yStart + last.getHeight() + 10;
            } else {
                return yStart + first.getHeight() + 10;
            }
        }
        return 0;
    }

    @Override
    public void update() {
        super.update();
        if (slider != null) {
            scroll = Math.round(slider.getValue() * (calculateTotalElementsHeight() - getHeight()));
        }
    }

    @Override
    public void clear() {
        getWidgets().clear();
        scroll = 0;
        if (slider != null) {
            slider.calculateCarriageSize(5, 0);
        }
    }
}
