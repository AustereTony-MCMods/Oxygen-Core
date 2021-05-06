package austeretony.oxygen_core.client.settings.gui.scale.text;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
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
import austeretony.oxygen_core.common.util.value.FloatValue;
import org.lwjgl.input.Keyboard;

public class TextScaleAdjusterCallback extends Callback {

    private static int MIN_TEXT_SIZE_PIXELS = 4;
    private static int MAX_TEXT_SIZE_PIXELS = 18;

    private final SettingValue<FloatValue, Float> settingValue;
    private TextScaleWidget scaleWidget;
    private NumberField numberField;
    private TextLabel sizeLabel;
    private HorizontalSlider sizeSlider;
    private KeyButton confirmButton, defaultButton;

    private float scaleDefault, scaleCurrent, scaleNew;

    public TextScaleAdjusterCallback(SettingValue<FloatValue, Float> value) {
        super(164, 54);
        settingValue = value;
    }

    @Override
    public void init() {
        addWidget(new Background.UnderlinedTitleBottom(this));
        addWidget(new TextLabel(4, 12, Texts.title(SettingsManagerClient.getDisplayName(settingValue))));

        scaleDefault = settingValue.getDefault();
        scaleCurrent = scaleNew = settingValue.asFloat();
        addWidget(scaleWidget = new TextScaleWidget(6, 20, scaleCurrent));
        addWidget(numberField = new NumberField(6 + 30 + 4, 18, 30, MIN_TEXT_SIZE_PIXELS, MAX_TEXT_SIZE_PIXELS, true, 1)
                .setKeyPressListener((keyCode, keyChar) -> numberFieldUpdated()));
        addWidget(new TextLabel(6 + 30 + 4 + 30 + 4, 27,
                Texts.additionalDark("oxygen_core.gui.settings.widget.text_size_pixels")));

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
                .setEnabled(scaleCurrent != scaleDefault));
        defaultButton.setX(getX() + buttonPosSegment + (int) ((buttonPosSegment - defaultButton.getText().getWidth()) / 2F));
        KeyButton closeButton;
        addWidget(closeButton = new KeyButton(0, getHeight() - 10, Keys.CLOSE_KEY, "oxygen_core.gui.button.close")
                .setPressListener(this::close));
        closeButton.setX(getX() + buttonPosSegment * 2 + (int) ((buttonPosSegment - closeButton.getText().getWidth()) / 2F));

        numberField.setText(String.format("%.1f", GUIUtils.getTextHeight(scaleCurrent)));
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
        float sizePixelsTemp = (float) numberField.getTypedNumberAsDouble();
        if (sizePixelsTemp < MIN_TEXT_SIZE_PIXELS || sizePixelsTemp > MAX_TEXT_SIZE_PIXELS) return;

        sizeSlider.setValue((sizePixelsTemp - MIN_TEXT_SIZE_PIXELS) / (MAX_TEXT_SIZE_PIXELS - MIN_TEXT_SIZE_PIXELS));
        sizeLabel.getText().setText(String.format("%.1f", sizePixelsTemp)
                + " " + localize("oxygen_core.gui.settings.widget.pixels"));

        float scale = sizePixelsTemp / GUIUtils.getTextHeight(1F);
        scaleNew = scale;
        scaleWidget.setTextScale(scaleNew);
        confirmButton.setEnabled(scaleNew != scaleCurrent);
    }

    private void slidersUpdated() {
        float sizePixels = sizeSlider.calculateValue(MIN_TEXT_SIZE_PIXELS, MAX_TEXT_SIZE_PIXELS);
        scaleNew = sizePixels / GUIUtils.getTextHeight(1F);

        sizeLabel.getText().setText(String.format("%.1f", sizePixels)
                + " " + localize("oxygen_core.gui.settings.widget.pixels"));
        numberField.setText(String.format("%.1f", sizePixels));

        scaleWidget.setTextScale(scaleNew);
        confirmButton.setEnabled(scaleNew != scaleCurrent);
    }

    private void confirm() {
        if (!numberField.isFocused() && scaleCurrent != scaleNew) {
            settingValue.get().setValue(scaleNew);
            OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();

            scaleCurrent = scaleNew;
            confirmButton.setEnabled(false);
            defaultButton.setEnabled(scaleCurrent != scaleDefault);
        }
    }

    private void resetToDefault() {
        if (!numberField.isFocused()) {
            settingValue.reset();
            OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();

            numberField.setText(String.format("%.1f", GUIUtils.getTextHeight(scaleDefault)));
            numberFieldUpdated();
            confirmButton.setEnabled(false);
            defaultButton.setEnabled(false);
        }
    }
}
