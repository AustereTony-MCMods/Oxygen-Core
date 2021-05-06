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
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.base.text.NumberField;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class SelectQuantityCallback extends Callback {

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

    private final String title;
    private final List<String> messageLines;
    private final long minValue, maxValue, defaultValue;
    @Nonnull
    private final Consumer<Long> confirmTask;
    @Nullable
    private final Consumer<Long> cancelTask;

    private NumberField numberField;
    private HorizontalSlider horizontalSlider;
    private KeyButton confirmButton;

    private long quantity;

    public SelectQuantityCallback(@Nonnull String title, @Nonnull String displayMessage, long minValue, long maxValue,
                                  long defaultValue, @Nonnull Consumer<Long> confirmTask, @Nullable Consumer<Long> cancelTask) {
        super(0, 0);
        this.title = title;
        this.minValue = minValue;
        this.maxValue = maxValue;
        quantity = this.defaultValue = defaultValue;

        int width = 180;
        Text commonText = Texts.common(displayMessage).decrementScale(.05F);
        messageLines = GUIUtils.splitTextToLines(commonText.getText(), commonText.getScale(), width - 6 * 2);
        setSize(width, (int) (47 + (commonText.getHeight() + 3F) * messageLines.size()));

        this.confirmTask = confirmTask;
        this.cancelTask = cancelTask;
    }

    public SelectQuantityCallback(@Nonnull String title, @Nonnull String displayMessage, long minValue, long maxValue,
                                  long defaultValue, @Nonnull Consumer<Long> confirmTask) {
        this(title, displayMessage, minValue, maxValue, defaultValue, confirmTask, null);
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

        addWidget(new TextLabel(6, 24 + (int) lineHeight * index,
                Texts.commonDark(localize("oxygen_core.gui.label.quantity")).decrementScale(.05F)));
        addWidget(numberField = new NumberField(6 + 6, 25 + (int) lineHeight * index, 13, minValue, maxValue)
                .setKeyPressListener((keyChar, keyCode) -> {
                    quantity = numberField.getTypedNumberAsLong();
                    if (quantity == 0) {
                        quantity = 1;
                    }
                    horizontalSlider.setValue(quantity, minValue, maxValue);
                }));
        numberField.setText(String.valueOf(defaultValue));

        addWidget(new ImageButton(6, 26 + (int) lineHeight * index, 5, 5,
                MINUS_ICONS_TEXTURE, "")
                .setMouseClickListener((mouseX, mouseY, button) -> {
                    if (quantity == minValue) return;
                    quantity--;
                    numberField.setText(String.valueOf(quantity));
                    horizontalSlider.setValue(quantity, minValue, maxValue);
                }));
        addWidget(new ImageButton(26, 26 + (int) lineHeight * index, 5, 5,
                PLUS_ICONS_TEXTURE, "")
                .setMouseClickListener((mouseX, mouseY, button) -> {
                    if (quantity == maxValue) return;
                    quantity++;
                    numberField.setText(String.valueOf(quantity));
                    horizontalSlider.setValue(quantity, minValue, maxValue);
                }));

        addWidget(horizontalSlider = new HorizontalSlider(35, 33, getWidth() - 6 * 2 - 30)
                .setInstantValueChangeListener((from, to) -> {
                    quantity = (long) (minValue + (maxValue - minValue) * to);
                    numberField.setText(String.valueOf(quantity));
                }));
        horizontalSlider.setValue(defaultValue, minValue, maxValue);

        int buttonPosSegment = (int) (getWidth() / 2F);
        addWidget(confirmButton = new KeyButton(0, getHeight() - 10, Keys.CONFIRM_KEY, "oxygen_core.gui.button.confirm")
                .setPressListener(this::confirm));
        confirmButton.setX(getX() + (int) ((buttonPosSegment - confirmButton.getText().getWidth()) / 2F));
        KeyButton cancelButton;
        addWidget(cancelButton = new KeyButton(0, getHeight() - 10, Keys.CANCEL_KEY, "oxygen_core.gui.button.cancel")
                .setPressListener(this::close));
        cancelButton.setX(getX() + buttonPosSegment + (int) ((buttonPosSegment - cancelButton.getText().getWidth()) / 2F));
    }

    @Override
    public boolean close() {
        if (cancelTask != null) {
            cancelTask.accept(quantity);
        }
        return super.close();
    }

    private void confirm() {
        if (numberField.isFocused()) return;
        confirmTask.accept(quantity);
        close();
    }
}
