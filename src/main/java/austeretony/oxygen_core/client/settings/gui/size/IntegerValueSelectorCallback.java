package austeretony.oxygen_core.client.settings.gui.size;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.base.Keys;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.background.Background;
import austeretony.oxygen_core.client.gui.base.button.HorizontalSlider;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.base.text.NumberField;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;
import austeretony.oxygen_core.client.settings.SettingValue;
import austeretony.oxygen_core.client.settings.SettingsManagerClient;
import austeretony.oxygen_core.common.util.value.IntegerValue;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class IntegerValueSelectorCallback extends Callback {

    private final SettingValue<IntegerValue, Integer> settingValue;
    private final int minValue, maxValue;
    @Nonnull
    private final String description;
    @Nullable
    private final String postfix;

    private NumberField numberField;
    private TextLabel sizeLabel;
    private HorizontalSlider sizeSlider;
    private KeyButton confirmButton, defaultButton;

    private int sizeDefault, sizeCurrent, sizeNew;

    public IntegerValueSelectorCallback(SettingValue<IntegerValue, Integer> value, int minValue, int maxValue,
                                        @Nonnull String description, @Nullable String postfix) {
        super(160, 54);
        settingValue = value;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.description = description;
        this.postfix = postfix;
    }

    @Override
    public void init() {
        addWidget(new Background.UnderlinedTitleBottom(this));
        addWidget(new TextLabel(4, 12, Texts.title(SettingsManagerClient.getDisplayName(settingValue))));

        sizeDefault = settingValue.getDefault();
        sizeCurrent = sizeNew = settingValue.asInt();
        addWidget(numberField = new NumberField(6, 18, 30, minValue, maxValue, false, 0)
                .setKeyPressListener((keyCode, keyChar) -> numberFieldUpdated()));
        addWidget(new TextLabel(6 + 30 + 4, 27, Texts.additionalDark(description)));

        int sliderWidth = 128;
        addWidget(sizeSlider = new HorizontalSlider(6, 31, sliderWidth)
                .setInstantValueChangeListener((from, to) -> slidersUpdated()));
        addWidget(sizeLabel = new TextLabel(6 + sliderWidth + 4, 38, Texts.additionalDark("")));

        int buttonPosSegment = (int) (getWidth() / 3F);
        addWidget(confirmButton = new KeyButton(0, getHeight() - 10, Keys.CONFIRM_KEY, "oxygen_core.gui.button.confirm")
                .setPressListener(this::confirm)
                .setEnabled(false));
        confirmButton.setX(getX() + (int) ((buttonPosSegment - confirmButton.getText().getWidth()) / 2F));
        addWidget(defaultButton = new KeyButton(0, getHeight() - 10, Keyboard.KEY_Z, "oxygen_core.gui.button.default")
                .setPressListener(this::resetToDefault)
                .setEnabled(sizeCurrent != sizeDefault));
        defaultButton.setX(getX() + buttonPosSegment + (int) ((buttonPosSegment - defaultButton.getText().getWidth()) / 2F));
        KeyButton closeButton;
        addWidget(closeButton = new KeyButton(0, getHeight() - 10, Keys.CLOSE_KEY, "oxygen_core.gui.button.close")
                .setPressListener(this::close));
        closeButton.setX(getX() + buttonPosSegment * 2 + (int) ((buttonPosSegment - closeButton.getText().getWidth()) / 2F));

        numberField.setText(String.valueOf(sizeCurrent));
        numberFieldUpdated();
    }

    @Override
    public boolean close() {
        if (!numberField.isFocused()) {
            super.close();
            return true;
        }
        return false;
    }

    private void numberFieldUpdated() {
        int sizePixelsTemp = (int) numberField.getTypedNumberAsLong();
        if (sizePixelsTemp < minValue || sizePixelsTemp > maxValue) return;

        sizeSlider.setValue((sizePixelsTemp - minValue) / ((float) maxValue - minValue));
        if (postfix != null) {
            sizeLabel.getText().setText(sizePixelsTemp + " " + localize(postfix));
        } else {
            sizeLabel.getText().setText(String.valueOf(sizePixelsTemp));
        }

        sizeNew = sizePixelsTemp;
        confirmButton.setEnabled(sizeNew != sizeCurrent);
    }

    private void slidersUpdated() {
        int sizePixels = (int) sizeSlider.calculateValue(minValue, maxValue);
        sizeNew = sizePixels;

        if (postfix != null) {
            sizeLabel.getText().setText(sizePixels + " " + localize(postfix));
        } else {
            sizeLabel.getText().setText(String.valueOf(sizePixels));
        }
        numberField.setText(String.valueOf(sizePixels));

        confirmButton.setEnabled(sizeNew != sizeCurrent);
    }

    private void confirm() {
        if (!numberField.isFocused() && sizeCurrent != sizeNew) {
            settingValue.get().setValue(sizeNew);
            OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();

            sizeCurrent = sizeNew;
            confirmButton.setEnabled(false);
            defaultButton.setEnabled(sizeCurrent != sizeDefault);
        }
    }

    private void resetToDefault() {
        if (!numberField.isFocused()) {
            settingValue.reset();
            OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();

            numberField.setText(String.valueOf(sizeDefault));
            numberFieldUpdated();
            confirmButton.setEnabled(false);
            defaultButton.setEnabled(false);
        }
    }
}
