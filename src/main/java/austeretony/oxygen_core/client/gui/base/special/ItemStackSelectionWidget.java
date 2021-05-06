package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Textures;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.button.ImageButton;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.core.Section;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.special.callback.ItemStackSelectionCallback;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemStackSelectionWidget extends Widget<ItemStackSelectionWidget> {

    public static final Texture PLUS_ICONS_TEXTURE = Texture.builder()
            .texture(Textures.PLUS_ICONS)
            .size(10, 10)
            .imageSize(10 * 3, 10)
            .build();
    public static final Texture CROSS_ICONS_TEXTURE = Texture.builder()
            .texture(Textures.CROSS_ICONS)
            .size(5, 5)
            .imageSize(5 * 3, 5)
            .build();
    public static final Texture RECYCLE_ICONS_TEXTURE = Texture.builder()
            .texture(Textures.RECYCLE_ICONS)
            .size(5, 5)
            .imageSize(5 * 3, 5)
            .build();

    protected final int maxQuantity;
    @Nonnull
    protected SpecialState state;

    private ImageButton selectButton, resetButton, changeButton;

    @Nullable
    private ItemStackSelectionListener itemSelectionListener;

    @Nullable
    protected ItemStackWrapper stackWrapper;
    protected int quantity;

    public ItemStackSelectionWidget(int x, int y, int maxQuantity) {
        setPosition(x, y);
        setSize(16, 16);
        state = SpecialState.NORMAL;
        this.maxQuantity = maxQuantity;

        setEnabled(true);
        setVisible(true);
    }

    @Override
    public void init() {
        addWidget(selectButton = new ImageButton(4, 4, 10, 10, PLUS_ICONS_TEXTURE,
                localize("oxygen_core.gui.item_stack_selector.tooltip.select_item"))
                .setMouseClickListener((mouseX, mouseY, button) -> openItemSelectionCallback()));
        addWidget(resetButton = new ImageButton(14, -3, 5, 5, CROSS_ICONS_TEXTURE,
                localize("oxygen_core.gui.item_stack_selector.tooltip.reset_item"))
                .setMouseClickListener((mouseX, mouseY, button) -> {
                    setItemStack(null, 0);
                    if (itemSelectionListener != null) {
                        itemSelectionListener.selected(null, 0);
                    }
                }));
        addWidget(changeButton = new ImageButton(14, 14, 5, 5, RECYCLE_ICONS_TEXTURE,
                localize("oxygen_core.gui.item_stack_selector.tooltip.edit_item"))
                .setMouseClickListener((mouseX, mouseY, button) -> openItemSelectionCallback()));
    }

    private void openItemSelectionCallback() {
        Callback prevCallback = Section.tryGetCurrentCallback();
        ItemStackSelectionCallback callback = new ItemStackSelectionCallback(
                "oxygen_core.gui.callback.item_selection",
                "oxygen_core.gui.callback.item_selection.message",
                true,
                maxQuantity,
                pair -> {
                    if (prevCallback != null) {
                        Section.tryOpenCallback(prevCallback);
                    }
                    setItemStack(pair.getKey(), pair.getValue());
                    if (itemSelectionListener != null) {
                        itemSelectionListener.selected(pair.getKey(), pair.getValue());
                    }
                },
                () -> {
                    if (prevCallback != null) {
                        Section.tryOpenCallback(prevCallback);
                    }
                });
        Section.tryOpenCallback(callback);

        if (stackWrapper != null) {
            callback.setSelectedItemStack(stackWrapper, quantity);
        }
    }

    public ItemStackSelectionWidget setItemStackSelectionListener(ItemStackSelectionListener listener) {
        this.itemSelectionListener = listener;
        return this;
    }

    public ItemStackSelectionWidget setItemStack(@Nullable ItemStackWrapper stackWrapper, int quantity) {
        this.stackWrapper = stackWrapper;
        this.quantity = quantity;
        return this;
    }

    public ItemStackSelectionWidget setItemStack(@Nullable ItemStackWrapper stackWrapper) {
        return setItemStack(stackWrapper, 0);
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isItemSelected() {
        return stackWrapper != null && quantity > 0;
    }

    @Nullable
    public ItemStackWrapper getStackWrapper() {
        return stackWrapper;
    }

    public int getQuantity() {
        return quantity;
    }

    public ItemStackSelectionWidget setState(@Nonnull SpecialState state) {
        this.state = state;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;

        if (stackWrapper != null) {
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

        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        mouseX -= getX();
        mouseY -= getY();

        if (stackWrapper == null) {
            selectButton.draw(mouseX, mouseY, partialTicks);
        } else {
            resetButton.draw(mouseX, mouseY, partialTicks);
            changeButton.draw(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible() || !isMouseOver()) return;

        if (stackWrapper != null) {
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

    @FunctionalInterface
    public interface ItemStackSelectionListener {

        void selected(ItemStackWrapper stackWrapper, int quantity);
    }
}
