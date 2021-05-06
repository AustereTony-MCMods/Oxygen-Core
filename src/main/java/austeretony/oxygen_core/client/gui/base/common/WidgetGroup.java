package austeretony.oxygen_core.client.gui.base.common;

import austeretony.oxygen_core.client.gui.base.core.Widget;

public class WidgetGroup extends Widget<WidgetGroup> {

    public WidgetGroup(int x, int y) {
        setPosition(x, y);

        setEnabled(true);
        setVisible(true);
    }

    public WidgetGroup() {
        this(0, 0);
    }

    @Override
    public int getWidth() {
        int absWidth = 0;
        for (Widget widget : getWidgets()) {
            absWidth = Math.max(widget.getX() + widget.getWidth(), absWidth);
        }
        return absWidth;
    }

    @Override
    public int getHeight() {
        int absHeight = 0;
        for (Widget widget : getWidgets()) {
            absHeight = Math.max(widget.getY() + widget.getHeight(), absHeight);
        }
        return absHeight;
    }
}
