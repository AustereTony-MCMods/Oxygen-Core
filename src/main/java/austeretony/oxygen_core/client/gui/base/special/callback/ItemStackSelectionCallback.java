package austeretony.oxygen_core.client.gui.base.special.callback;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Keys;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.Textures;
import austeretony.oxygen_core.client.gui.base.background.Background;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.button.HorizontalSlider;
import austeretony.oxygen_core.client.gui.base.button.ImageButton;
import austeretony.oxygen_core.client.gui.base.button.VerticalSlider;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.list.ScrollableList;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.base.text.NumberField;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;
import austeretony.oxygen_core.client.player.inventory.InventoryHelperClient;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.util.objects.Pair;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ItemStackSelectionCallback extends Callback {

    public static final Texture MINUS_ICONS_TEXTURE = Texture.builder()
            .texture(Textures.MINUS_ICONS)
            .size(5, 5)
            .imageSize(5 * 3, 5)
            .build();
    public static final Texture PLUS_ICONS_TEXTURE = Texture.builder()
            .texture(Textures.PLUS_ICONS)
            .size(5, 5)
            .imageSize(5 * 3, 5)
            .build();

    private static int VISIBLE_ITEMS = 5;

    private final String title;
    private final List<String> messageLines;
    private final boolean enableQuantitySelection;
    private final int maxQuantity;
    @Nonnull
    private final Consumer<Pair<ItemStackWrapper, Integer>> selectionItemSupplier;
    @Nullable
    private final Runnable cancelTask;

    private final Map<ItemStackWrapper, Integer> inventoryContentMap;

    private ScrollableList<ItemStackWrapper> inventoryItemsList;
    private NumberField numberField;
    private HorizontalSlider horizontalSlider;
    private KeyButton confirmButton;

    private ItemStackWrapper selectedItem;
    private int quantity = 1, itemStock;

    public ItemStackSelectionCallback(@Nonnull String title, @Nonnull String displayMessage,
                                      boolean enableQuantitySelection, int maxQuantity,
                                      @Nonnull Consumer<Pair<ItemStackWrapper, Integer>> selectionItemSupplier,
                                      @Nullable Runnable cancelTask) {
        super(0, 0);
        this.title = title;
        this.enableQuantitySelection = enableQuantitySelection;
        this.maxQuantity = maxQuantity;
        inventoryContentMap = InventoryHelperClient.getInventoryContent();

        int width = 180;
        Text commonText = Texts.common(displayMessage).decrementScale(.05F);
        messageLines = GUIUtils.splitTextToLines(commonText.getText(), commonText.getScale(), width - 6 * 2);
        int messageHeight = (int) ((commonText.getHeight() + 3F) * messageLines.size());
        int itemSelectionBlockHeight = 10 + 16 * 5 + 4;
        int quantityBlockHeight = enableQuantitySelection ? 20 : 0;
        setSize(width, 30 + messageHeight + itemSelectionBlockHeight + quantityBlockHeight);

        this.selectionItemSupplier = selectionItemSupplier;
        this.cancelTask = cancelTask;
    }

    public ItemStackSelectionCallback(@Nonnull String title, @Nonnull String displayMessage, int maxQuantity,
                                      @Nonnull Consumer<Pair<ItemStackWrapper, Integer>> selectionItemSupplier) {
        this(title, displayMessage, true, maxQuantity, selectionItemSupplier, null);
    }

    public ItemStackSelectionCallback(@Nonnull String title, @Nonnull String displayMessage,
                                      @Nonnull Consumer<Pair<ItemStackWrapper, Integer>> selectionItemSupplier) {
        this(title, displayMessage, false, 0, selectionItemSupplier, null);
    }

    @Override
    public void init() {
        addWidget(new Background.UnderlinedTitleBottom(this));
        addWidget(new TextLabel(4, 12, Texts.title(title)));

        int index = 0;
        float lineHeight = 0F;
        for (String line : messageLines) {
            Text lineText = Texts.common(line).decrementScale(.05F);
            lineHeight = lineText.getHeight() + 3F;
            addWidget(new TextLabel(6, 23 + (int) lineHeight * index++, lineText));
        }

        int itemSelectionBlockHeight = 10 + 16 * 5 + 4;
        addWidget(new TextLabel(6, 24 + (int) lineHeight * index + 2,
                Texts.commonDark(localize("oxygen_core.gui.label.item")).decrementScale(.05F)));
        addWidget(inventoryItemsList = new ScrollableList<>(6, 23 + (int) lineHeight * index + 4, VISIBLE_ITEMS, getWidth() - 6 * 2 - 3, 16)
                .<ItemStackWrapper>setEntryMouseClickListener((previous, current, x, y, button) -> {
                    if (current == previous) return;
                    if (previous != null) {
                        previous.setSelected(false);
                    }
                    current.setSelected(true);
                    selectedItem = current.getEntry();

                    itemStock = inventoryContentMap.getOrDefault(selectedItem, 0);
                    int maxSelectableQuantity = getMaxSelectableQuantity(selectedItem);
                    horizontalSlider.setValue(1, 1, maxSelectableQuantity);
                    numberField.setValueRange(1, maxSelectableQuantity);
                    numberField.setText(String.valueOf(1));

                    confirmButton.setEnabled(true);
                }));
        VerticalSlider slider = new VerticalSlider(6 + getWidth() - 6 * 2 - 2, 23 + (int) lineHeight * index + 4, 2, 16 * 5 + 4);
        addWidget(slider);
        inventoryItemsList.setSlider(slider);

        for (Map.Entry<ItemStackWrapper, Integer> entry : inventoryContentMap.entrySet()) {
            inventoryItemsList.addElement(new ItemStackListEntry(entry.getKey(), entry.getValue()));
        }

        if (enableQuantitySelection) {
            addWidget(new TextLabel(6, 26 + (int) lineHeight * index + itemSelectionBlockHeight,
                    Texts.commonDark(localize("oxygen_core.gui.label.quantity")).decrementScale(.05F)));
            addWidget(numberField = new NumberField(6 + 6, 27 + (int) lineHeight * index + itemSelectionBlockHeight,
                    13, 1, 1)
                    .setKeyPressListener((keyChar, keyCode) -> {
                        if (selectedItem == null) {
                            numberField.setText(String.valueOf(1));
                        }
                        quantity = (int) numberField.getTypedNumberAsLong();
                        if (quantity == 0) {
                            quantity = 1;
                        }
                    }));
            numberField.setText(String.valueOf(1));

            addWidget(new ImageButton(6, 29 + (int) lineHeight * index + itemSelectionBlockHeight, 5, 5,
                    MINUS_ICONS_TEXTURE, "")
                    .setMouseClickListener((mouseX, mouseY, button) -> {
                        if (selectedItem == null || quantity == 1) return;
                        quantity--;
                        numberField.setText(String.valueOf(quantity));
                        horizontalSlider.setValue(quantity, 1, getMaxSelectableQuantity(selectedItem));
                    }));
            addWidget(new ImageButton(26, 29 + (int) lineHeight * index + itemSelectionBlockHeight, 5, 5,
                    PLUS_ICONS_TEXTURE, "")
                    .setMouseClickListener((mouseX, mouseY, button) -> {
                        if (selectedItem == null || quantity == getMaxSelectableQuantity(selectedItem)) return;
                        quantity++;
                        numberField.setText(String.valueOf(quantity));
                        horizontalSlider.setValue(quantity, 1, getMaxSelectableQuantity(selectedItem));
                    }));

            addWidget(horizontalSlider = new HorizontalSlider(35, 27 + (int) lineHeight * index + itemSelectionBlockHeight, getWidth() - 6 * 2 - 30)
                    .setInstantValueChangeListener((from, to) -> {
                        quantity = (int) (1 + (getMaxSelectableQuantity(selectedItem) - 1) * to);
                        numberField.setText(String.valueOf(quantity));
                    }));
            horizontalSlider.setValue(1, 1, 1);
        }

        int buttonPosSegment = (int) (getWidth() / 2F);
        addWidget(confirmButton = new KeyButton(0, getHeight() - 10, Keys.CONFIRM_KEY, "oxygen_core.gui.button.confirm")
                .setPressListener(this::confirm).setEnabled(false));
        confirmButton.setX(getX() + (int) ((buttonPosSegment - confirmButton.getText().getWidth()) / 2F));
        KeyButton cancelButton;
        addWidget(cancelButton = new KeyButton(0, getHeight() - 10, Keys.CANCEL_KEY, "oxygen_core.gui.button.cancel")
                .setPressListener(this::close));
        cancelButton.setX(getX() + buttonPosSegment + (int) ((buttonPosSegment - cancelButton.getText().getWidth()) / 2F));
    }

    private int getMaxSelectableQuantity(@Nullable ItemStackWrapper stackWrapper) {
        if (maxQuantity < 0 && stackWrapper != null) {
            return Math.min(itemStock, OxygenCommon.getMaxItemStackSize(stackWrapper));
        }
        return Math.min(itemStock, maxQuantity);
    }

    public void setSelectedItemStack(ItemStackWrapper stackWrapper, int quantity) {
        selectedItem = stackWrapper;
        itemStock = inventoryContentMap.getOrDefault(selectedItem, 0);
        int maxSelectableQuantity = getMaxSelectableQuantity(selectedItem);
        this.quantity = Math.min(quantity, maxSelectableQuantity);

        horizontalSlider.setValue(this.quantity, 1, maxSelectableQuantity);
        numberField.setValueRange(1, maxSelectableQuantity);
        numberField.setText(String.valueOf(this.quantity));

        confirmButton.setEnabled(true);
        setSelectedInList(stackWrapper);
    }

    private void setSelectedInList(ItemStackWrapper stackWrapper) {
        if (inventoryContentMap.isEmpty()) return;

        int index = 0;
        for (ItemStackWrapper stack : inventoryContentMap.keySet()) {
            if (stack.isEquals(stackWrapper)) {
                break;
            }
            index++;
        }

        if (index >= VISIBLE_ITEMS) {
            inventoryItemsList.setScrollPosition(index - VISIBLE_ITEMS + 1);
        }

        int elementToSelected = index;
        if (index >= VISIBLE_ITEMS) {
            elementToSelected = inventoryItemsList.getWidgets().size() - 1;
        }
        if (elementToSelected < inventoryItemsList.getWidgets().size()) {
            ItemStackListEntry entry = (ItemStackListEntry) inventoryItemsList.getWidgets().get(elementToSelected);
            entry.setSelected(true);
            inventoryItemsList.setPreviousClicked(entry);
        }
    }

    @Override
    public boolean close() {
        boolean flag = super.close();
        if (cancelTask != null) {
            cancelTask.run();
        }
        return flag;
    }

    private void confirm() {
        close();
        selectionItemSupplier.accept(Pair.of(selectedItem, quantity));
    }

    private class ItemStackListEntry extends ListEntry<ItemStackWrapper> {

        private final int playerStock;

        public ItemStackListEntry(@Nonnull ItemStackWrapper stackWrapper, int playerStock) {
            super("", stackWrapper);
            this.playerStock = playerStock;
        }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            if (!isVisible()) return;

            int color = fill.getColorEnabled();
            if (!isEnabled())
                color = fill.getColorDisabled();
            else if (isMouseOver() || selected)
                color = fill.getColorMouseOver();
            GUIUtils.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), color);

            ItemStack itemStack = entry.getItemStackCached();
            GUIUtils.renderItemStack(itemStack, getX() + 2, getY(),
                    CoreSettings.ENABLE_DURABILITY_BARS_GUI_DISPLAY.asBoolean());

            GUIUtils.pushMatrix();
            GUIUtils.translate(getX(), getY());

            if (playerStock > 1) {
                color = text.getColorEnabled();
                if (!isEnabled())
                    color = text.getColorDisabled();
                else if (isMouseOver() || selected)
                    color = text.getColorMouseOver();
                GUIUtils.drawString(String.valueOf(playerStock), 16, 10, text.getScale() - .08F, color, true);
            }

            float textY = (getHeight() - GUIUtils.getTextHeight(text.getScale() - .05F)) / 2F + .5F;
            String itemDisplayName = itemStack.getDisplayName();
            if (CoreSettings.ENABLE_RARITY_COLORS_GUI_DISPLAY.asBoolean()) {
                itemDisplayName = GUIUtils.getItemStackRarityColor(itemStack) + itemDisplayName;
            }
            GUIUtils.drawString(itemDisplayName, 30, textY, text.getScale() - .05F, getColorFromState(text), false);

            GUIUtils.popMatrix();
        }

        @Override
        public void drawForeground(int mouseX, int mouseY, float partialTicks) {
            if (!isVisible()) return;
            if (mouseX >= getX() + 2 && mouseY >= getY() && mouseX < getX() + 18 && mouseY < getY() + getHeight()) {
                drawToolTip(mouseX + 6, mouseY, entry.getItemStackCached());
            }
        }
    }
}
