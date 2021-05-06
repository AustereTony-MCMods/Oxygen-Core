package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.List;

public class ItemStackWidget extends Widget<ItemStackWidget> {

    protected ItemStackWrapper stackWrapper;
    protected int quantity;
    @Nonnull
    protected SpecialState state;

    public ItemStackWidget(int x, int y) {
        setPosition(x, y);
        setSize(16, 16);
        state = SpecialState.NORMAL;

        setEnabled(true);
        setVisible(true);
    }

    public ItemStackWidget setItemStack(ItemStackWrapper stackWrapper, int quantity) {
        this.stackWrapper = stackWrapper;
        this.quantity = quantity;
        return this;
    }

    public ItemStackWidget setItemStack(ItemStackWrapper stackWrapper) {
        return setItemStack(stackWrapper, 0);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ItemStackWrapper getStackWrapper() {
        return stackWrapper;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItemStackWidget setState(@Nonnull SpecialState state) {
        this.state = state;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;

        ItemStack itemStack = stackWrapper.getItemStackCached();
        GUIUtils.renderItemStack(itemStack, getX(), getY(),
                CoreSettings.ENABLE_DURABILITY_BARS_GUI_DISPLAY.asBoolean());

        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        if (quantity > 1) {
            GUIUtils.drawString(String.valueOf(quantity), 16, 10, CoreSettings.SCALE_TEXT_ADDITIONAL.asFloat() - .08F,
                    state.colorHex, true);
        }

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible() || !isMouseOver()) return;
        int offset = getWidth() + 8;
        int x = getX() + offset;
        int y = getY() + 2;

        ItemStack itemStack = stackWrapper.getItemStackCached();
        List<String> tooltipLines = GUIUtils.getItemStackToolTip(itemStack);
        float width = 0;
        for (String line : tooltipLines) {
            float lineWidth = GUIUtils.getTextWidth(line, CoreSettings.SCALE_TEXT_TOOLTIP.asFloat()) + 6F;
            if (lineWidth > width) {
                width = lineWidth;
            }
        }
        int startX = getScreenX() + width + offset > getScreen().width ? (int) (x - width - offset) : x;

        drawToolTip(startX, y, tooltipLines);
    }
}
