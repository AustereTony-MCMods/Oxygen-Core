package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.player.inventory.InventoryHelperClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.item.ItemStackWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class InventoryLoad extends Widget<InventoryLoad> {

    @Nonnull
    protected Text text;
    @Nonnull
    protected SpecialState state;

    protected int occupiedSlots, inventorySize;

    public InventoryLoad(int x, int y, @Nonnull Text text) {
        setPosition(x, y);
        setHeight(8);
        this.text = text;
        state = SpecialState.NORMAL;

        setEnabled(true);
        setVisible(true);
    }

    public InventoryLoad(int x, int y) {
        this(x , y, Texts.additional(""));
    }

    public InventoryLoad updateLoad(@Nullable Map<ItemStackWrapper, Integer> inventoryContentMap) {
        Map<ItemStackWrapper, Integer> contentMap = inventoryContentMap != null
                ? inventoryContentMap : InventoryHelperClient.getInventoryContent();
        occupiedSlots = calculateOccupiedSlots(contentMap);
        inventorySize = InventoryHelperClient.getInventorySize();
        text.setText(occupiedSlots + "/" + inventorySize);

        setWidth((int) Math.ceil(GUIUtils.getTextWidth(text.getText(), text.getScale())));
        setState(occupiedSlots >= inventorySize ? SpecialState.INACTIVE : SpecialState.NORMAL);
        return this;
    }

    private int calculateOccupiedSlots(Map<ItemStackWrapper, Integer> inventoryContentMap) {
        int amount = 0;
        for (Map.Entry<ItemStackWrapper, Integer> entry : inventoryContentMap.entrySet()) {
            float occupiedSlots = (float) entry.getValue() / OxygenCommon.getMaxItemStackSize(entry.getKey());
            int occupiedSlotsInt = (int) Math.floor(occupiedSlots);
            amount += occupiedSlotsInt;
            if (occupiedSlots > occupiedSlotsInt) {
                amount += 1;
            }
        }
        return amount;
    }

    public InventoryLoad updateLoad() {
        return updateLoad(null);
    }

    public InventoryLoad setLoad(int occupiedSlots) {
        this.occupiedSlots = occupiedSlots;
        text.setText(occupiedSlots + "/" + inventorySize);

        setWidth((int) Math.ceil(GUIUtils.getTextWidth(text.getText(), text.getScale())));
        setState(occupiedSlots >= inventorySize ? SpecialState.INACTIVE : SpecialState.NORMAL);
        return this;
    }

    public InventoryLoad setState(@Nonnull SpecialState state) {
        this.state = state;
        return this;
    }

    public int getOccupiedSlots() {
        return occupiedSlots;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.pushMatrix();
        GUIUtils.translate(0F, getHeight() - GUIUtils.getTextHeight(text.getScale()));
        GUIUtils.scale(text.getScale(), text.getScale());
        GUIUtils.drawString(text.getText(), 0, 0, state.colorHex, text.isShadowEnabled());
        GUIUtils.popMatrix();

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible() || !isMouseOver()) return;
        drawToolTip(this, localize("oxygen_core.gui.widget.inventory"));
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    public void setText(@Nonnull Text text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "InventoryLoad[" +
                "x= " + getX() + ", " +
                "y= " + getY() + ", " +
                "text= " + text + "" +
                "]";
    }
}
