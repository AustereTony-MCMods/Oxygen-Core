package austeretony.oxygen_core.client.gui.base.list;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.button.VerticalSlider;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.context.ContextAction;
import austeretony.oxygen_core.client.gui.base.context.ContextMenu;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.ListEntryClickListener;
import austeretony.oxygen_core.client.gui.base.text.TextField;
import org.lwjgl.input.Mouse;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ScrollableList<T> extends Widget<ScrollableList> {

    protected final List<ListEntry<T>>
            buffer = new ArrayList<>(1),
            searchResult = new ArrayList<>(1);

    @Nonnull
    protected Fill fill;
    protected final int visibleElements, elementWidth, elementHeight, verticalOffset;
    protected final boolean doubleClick;

    protected int maxPosition, scrollPosition, prevScrollPosition;

    public static final long DOUBLE_CLICK_TIME_MILLIS = 400L;
    protected long lastClickTimeMillis;

    @Nullable
    protected ListEntry<T> previousClicked;

    @Nullable
    protected ListEntryClickListener clickListener;

    @Nullable
    protected ContextMenu<T> contextMenu;
    @Nullable
    protected VerticalSlider slider;

    @Nullable
    protected TextField searchField;
    protected boolean searching;
    @Nullable
    protected String prevSearchQuery;

    public ScrollableList(int x, int y, @Nonnull Fill fill, int visibleElements, int elementWidth, int elementHeight,
                          int verticalOffset, boolean doubleClick) {
        setPosition(x, y);
        setSize(elementWidth, (elementHeight + verticalOffset) * visibleElements - verticalOffset);
        this.fill = fill;
        this.visibleElements = visibleElements;
        this.elementWidth = elementWidth;
        this.elementHeight = elementHeight;
        this.verticalOffset = verticalOffset;
        this.doubleClick = doubleClick;

        setEnabled(true);
        setVisible(true);
    }

    public ScrollableList(int x, int y, int visibleElements, int elementWidth, int elementHeight) {
        this(x, y, Fills.def(), visibleElements, elementWidth, elementHeight, 1, false);
    }

    public <V> ScrollableList setEntryMouseClickListener(ListEntryClickListener<V> listener) {
        clickListener = listener;
        return this;
    }

    public void addElement(@Nonnull ListEntry<T> entry) {
        entry.setSize(elementWidth, elementHeight);
        buffer.add(entry);

        int yStep = elementHeight + verticalOffset;
        if (getWidgets().size() < visibleElements) {
            entry.setPosition(0, yStep * getWidgets().size());
            addWidget(entry);

            entry.setEnabled(true);
            entry.setVisible(true);
        } else {
            maxPosition++;
        }

        if (slider != null) {
            slider.calculateCarriageSize(visibleElements, buffer.size());
        }
    }

    public List<ListEntry<T>> getBufferList() {
        return buffer;
    }

    public void createContextMenu(@Nonnull List<ContextAction<T>> actions) {
        contextMenu = new ContextMenu<>(actions);
        contextMenu.setLayer(getLayer());
        contextMenu.setScreen(screen);
        contextMenu.init();
    }

    public void setSlider(VerticalSlider slider) {
        this.slider = slider;
    }

    public void setSearchField(TextField textField) {
        searchField = textField;
    }

    public void setFirstElementSelected() {
        if (widgets != null && !widgets.isEmpty()) {
            previousClicked = (ListEntry<T>) widgets.get(0);
            previousClicked.setSelected(true);
        }
    }

    public int getScrollPosition() {
        return scrollPosition;
    }

    public void setScrollPosition(int position) {
        prevScrollPosition = 0;
        scrollPosition = Math.min(position, maxPosition);
        updateBasedOnScrollPosition();
    }

    @Override
    public void clear() {
        buffer.clear();
        getWidgets().clear();
        scrollPosition = maxPosition = 0;

        if (slider != null) {
            slider.calculateCarriageSize(visibleElements, 0);
        }
        if (contextMenu != null) {
            contextMenu.close();
        }
        if (searchField != null) {
            searchField.setText("");
            searchResult.clear();
        }
    }

    public void reset() {
        getWidgets().clear();
        scrollPosition = 0;

        int yStep = elementHeight + verticalOffset;
        int i = 0;
        for (Widget widget : buffer) {
            if (i < visibleElements) {
                int newY = i++ * yStep;
                widget.setY(newY);

                widget.setEnabled(true);
                widget.setVisible(true);
            } else {
                break;
            }
        }

        if (slider != null) {
            slider.calculateCarriageSize(visibleElements, buffer.size());
        }
        if (contextMenu != null) {
            contextMenu.close();
        }
        if (searchField != null) {
            searchField.setText("");
            searchResult.clear();
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), fill.getColorDefault());

        for (Widget widget : getWidgets()) {
            widget.draw(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        super.drawForeground(mouseX, mouseY, partialTicks);
        if (contextMenu != null) {
            contextMenu.drawForeground(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public boolean mouseOver(int mouseX, int mouseY) {
        if (contextMenu != null) {
            contextMenu.mouseOver(mouseX, mouseY);
            if (contextMenu.isOpened()) return false;
        }
        return super.mouseOver(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        boolean click = false;
        if (doubleClick) {
            long now = OxygenClient.getCurrentTimeMillis();
            if (now - lastClickTimeMillis < DOUBLE_CLICK_TIME_MILLIS) {
                click = true;
                lastClickTimeMillis = 0L;
            } else {
                lastClickTimeMillis = now;
            }
        } else {
            click = true;
        }

        if (isMouseOver()) {
            for (Widget widget : getWidgets()) {
                ListEntry<T> entry = (ListEntry<T>) widget;
                if (entry.isMouseOver() && mouseButton == MouseButtons.RIGHT_BUTTON) {
                    if (contextMenu != null && !contextMenu.isOpened()
                            && contextMenu.open(mouseX, mouseY, entry.getEntry())) {
                        if (click) {
                            if (clickListener != null) {
                                clickListener.click(previousClicked, entry, mouseX, mouseY, mouseButton);
                            }
                            previousClicked = entry;
                        }
                        setMouseOver(false);
                        return true;
                    }
                }
                if (click && entry.mouseClicked(mouseX, mouseY, mouseButton)) {
                    if (clickListener != null) {
                        clickListener.click(previousClicked, entry, mouseX, mouseY, mouseButton);
                    }
                    previousClicked = entry;
                    return true;
                }
            }
        }

        if (contextMenu != null) {
            return contextMenu.mouseClicked(mouseX, mouseY, mouseButton);
        }
        return false;
    }

    @Override
    public void handleMouseInput() {
        if (!isEnabled() || !isMouseOver() || (contextMenu != null && contextMenu.isOpened())) return;
        final int delta = Mouse.getDWheel();
        if (delta == 0) return;

        if (delta < 0) {
            if (scrollPosition >= maxPosition) return;
            scrollPosition++;
        }
        if (delta > 0) {
            if (scrollPosition <= 0) return;
            scrollPosition--;
        }
        updateBasedOnScrollPosition();
    }

    public void updateBasedOnScrollPosition() {
        if (scrollPosition == prevScrollPosition) return;
        int index = 0;
        int yStep = elementHeight + verticalOffset;
        getWidgets().clear();

        List<ListEntry<T>> targetList = searching ? searchResult : buffer;
        for (Widget widget : targetList) {
            if (index >= scrollPosition && index <= (scrollPosition + visibleElements - 1)) {
                widget.setPosition(0, getWidgets().size() * yStep);
                addWidget(widget);

                widget.setEnabled(true);
                widget.setVisible(true);
            }
            index++;
        }

        if (slider != null) {
            slider.updateCarriagePosition(visibleElements, searching ? searchResult.size() : buffer.size(), scrollPosition);
        }
        prevScrollPosition = scrollPosition;
    }

    @Override
    public void update() {
        if (slider != null) {
            scrollPosition = Math.round(slider.getValue() * maxPosition);
            updateBasedOnScrollPosition();
        }
        if (searchField != null) {
            String currentQuery = searchField.getTypedText().toLowerCase();
            searching = !currentQuery.isEmpty();

            if (!currentQuery.equals(prevSearchQuery)) {
                scrollPosition = maxPosition = 0;
                searchResult.clear();

                for (Widget widget : buffer) {
                    ListEntry<T> entry = (ListEntry<T>) widget;
                    String entryText = entry.getText().getText().toLowerCase();
                    if (entryText.startsWith(currentQuery) || entryText.contains(" " + currentQuery)) {
                        searchResult.add(entry);
                    }
                }

                int yStep = elementHeight + verticalOffset;
                getWidgets().clear();
                for (Widget widget : searchResult) {
                    if (getWidgets().size() < visibleElements) {
                        widget.setPosition(0, yStep * getWidgets().size());
                        addWidget(widget);

                        widget.setEnabled(true);
                        widget.setVisible(true);
                    } else {
                        maxPosition++;
                    }
                }

                if (slider != null) {
                    slider.calculateCarriageSize(visibleElements, searchResult.size());
                }
            }
            prevSearchQuery = currentQuery;
        }
        super.update();
    }

    @Nullable
    public ListEntry<T> getPreviousClicked() {
        return previousClicked;
    }

    public void setPreviousClicked(ListEntry<T> entry) {
        previousClicked = entry;
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public void setFill(@Nonnull Fill fill) {
        this.fill = fill;
    }

    @Override
    public String toString() {
        return "ScrollableList[" +
                "x= " + getX() + ", " +
                "y= " + getY() + ", " +
                "entries= " + getWidgets().size() +
                "]";
    }
}
