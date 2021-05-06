package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.gui.base.Textures;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.button.HorizontalSlider;
import austeretony.oxygen_core.client.gui.base.button.ImageButton;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.text.NumberField;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class QuantitySelectionWidget extends Widget<QuantitySelectionWidget> {

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

    private final boolean enableSlideBar;
    private final int slideBarWidth;
    @Nonnull
    protected SpecialState state;

    private NumberField numberField;
    private ImageButton minusButton, plusButton;
    @Nullable
    private HorizontalSlider horizontalSlider;

    protected QuantityChangeListener quantityChangeListener;
    protected int maxQuantity, quantity = 1;

    public QuantitySelectionWidget(int x, int y, boolean enableSlideBar, int slideBarWidth, int maxQuantity) {
        setPosition(x, y);
        setSize(31 + 4 + slideBarWidth, 8);
        this.enableSlideBar = enableSlideBar;
        this.slideBarWidth = slideBarWidth;
        this.maxQuantity = maxQuantity;
        state = SpecialState.NORMAL;

        setEnabled(true);
        setVisible(true);
    }

    public QuantitySelectionWidget(int x, int y, int maxQuantity) {
        this(x, y, false, 0, maxQuantity);
        setSize(31, 8);
    }

    @Override
    public void init() {
        addWidget(numberField = new NumberField(7, 0, 15, 1, Short.MAX_VALUE)
                .setKeyPressListener((keyChar, keyCode) -> {
                    quantity = (int) numberField.getTypedNumberAsLong();
                    if (quantity <= 0) {
                        quantity = 1;
                    }
                    if (quantity > maxQuantity) {
                        quantity = maxQuantity;
                    }
                    if (enableSlideBar) {
                        horizontalSlider.setValue(quantity, 1, maxQuantity);
                    }
                    updateButtonsState();

                    if (quantityChangeListener != null) {
                        quantityChangeListener.changed(quantity);
                    }
                }));
        numberField.setText(String.valueOf(quantity));

        addWidget(minusButton = new ImageButton(0, 1, 6, 6,
                MINUS_ICONS_TEXTURE, "")
                .setMouseClickListener((mouseX, mouseY, button) -> {
                    if (quantity == 1) return;
                    quantity--;
                    numberField.setText(String.valueOf(quantity));
                    if (enableSlideBar) {
                        horizontalSlider.setValue(quantity, 1, maxQuantity);
                    }
                    updateButtonsState();

                    if (quantityChangeListener != null) {
                        quantityChangeListener.changed(quantity);
                    }
                }));
        addWidget(plusButton = new ImageButton(23, 1, 6, 6,
                PLUS_ICONS_TEXTURE, "")
                .setMouseClickListener((mouseX, mouseY, button) -> {
                    if (quantity == maxQuantity) return;
                    quantity++;
                    numberField.setText(String.valueOf(quantity));
                    if (enableSlideBar) {
                        horizontalSlider.setValue(quantity, 1, maxQuantity);
                    }
                    updateButtonsState();

                    if (quantityChangeListener != null) {
                        quantityChangeListener.changed(quantity);
                    }
                }));

        if (enableSlideBar) {
            addWidget(horizontalSlider = new HorizontalSlider(35, 0, slideBarWidth)
                    .setInstantValueChangeListener((from, to) -> {
                        quantity = (int) (1 + (maxQuantity - 1) * to);
                        numberField.setText(String.valueOf(quantity));
                        updateButtonsState();

                        if (quantityChangeListener != null) {
                            quantityChangeListener.changed(quantity);
                        }
                    }));
            horizontalSlider.setValue(1, 1, maxQuantity);
        }
    }

    private void updateButtonsState() {
        minusButton.setEnabled(true);
        plusButton.setEnabled(true);

        if (quantity == 1) {
            minusButton.setEnabled(false);
        }
        if (quantity == maxQuantity) {
            plusButton.setEnabled(false);
        }
    }

    public QuantitySelectionWidget setQuantityChangeListener(QuantityChangeListener listener) {
        quantityChangeListener = listener;
        return this;
    }

    public QuantitySelectionWidget setMaxQuantity(int maxQuantity) {
        quantity = 1;
        this.maxQuantity = maxQuantity;
        numberField.setValueRange(1, maxQuantity);
        numberField.setText(String.valueOf(quantity));
        if (enableSlideBar) {
            horizontalSlider.setValue(quantity, 1, maxQuantity);
        }
        updateButtonsState();
        return this;
    }

    public QuantitySelectionWidget setQuantity(int quantity) {
        this.quantity = quantity;
        numberField.setText(String.valueOf(quantity));
        if (enableSlideBar) {
            horizontalSlider.setValue(quantity, 1, maxQuantity);
        }
        updateButtonsState();
        return this;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @FunctionalInterface
    public interface QuantityChangeListener {

        void changed(int newQuantity);
    }
}
