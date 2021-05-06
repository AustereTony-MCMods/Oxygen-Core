package austeretony.oxygen_core.client.settings.gui.scale.misc;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.base.Keys;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.background.Background;
import austeretony.oxygen_core.client.gui.base.button.HorizontalSlider;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;
import austeretony.oxygen_core.client.settings.SettingValue;
import austeretony.oxygen_core.client.settings.SettingsManagerClient;
import austeretony.oxygen_core.common.util.value.FloatValue;
import org.lwjgl.input.Keyboard;

public class FloatNormalizedPercentSelectorCallback extends Callback {

    private static int MIN_SIZE_PERCENTS = 50;
    private static int MAX_SIZE_PERCENTS = 200;

    private final SettingValue<FloatValue, Float> settingValue;
    private TextLabel sizeLabel;
    private HorizontalSlider sizeSlider;
    private KeyButton confirmButton, defaultButton;

    private float scaleDefault, scaleCurrent, scaleNew;

    public FloatNormalizedPercentSelectorCallback(SettingValue<FloatValue, Float> value) {
        super(158, 48);
        settingValue = value;
    }

    @Override
    public void init() {
        addWidget(new Background.UnderlinedTitleBottom(this));
        addWidget(new TextLabel(4, 12, Texts.title(SettingsManagerClient.getDisplayName(settingValue))));

        scaleDefault = settingValue.getDefault();
        scaleCurrent = scaleNew = settingValue.asFloat();

        int sliderWidth = 128;
        addWidget(new TextLabel(4, 24, Texts.common("oxygen_core.gui.settings.widget.scale_percents")));
        addWidget(sizeSlider = new HorizontalSlider(6, 25, sliderWidth)
                .setInstantValueChangeListener((from, to) -> slidersUpdated()));
        addWidget(sizeLabel = new TextLabel(6 + sliderWidth + 4, 32, Texts.additionalDark("")));

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

        float sizePercents = settingValue.asFloat() * 100F;
        sizeSlider.setValue(((scaleCurrent * 100F) - MIN_SIZE_PERCENTS) / ((float) MAX_SIZE_PERCENTS - MIN_SIZE_PERCENTS));
        sizeLabel.getText().setText(Math.round(sizePercents) + "%");
    }

    private void slidersUpdated() {
        float sizePercents = sizeSlider.calculateValue(MIN_SIZE_PERCENTS, MAX_SIZE_PERCENTS);
        scaleNew = sizePercents / 100F;
        sizeLabel.getText().setText(Math.round(sizePercents) + "%");

        confirmButton.setEnabled(scaleNew != scaleCurrent);
    }

    private void confirm() {
        if (scaleCurrent != scaleNew) {
            settingValue.get().setValue(scaleNew);
            OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();

            scaleCurrent = scaleNew;
            confirmButton.setEnabled(false);
            defaultButton.setEnabled(scaleCurrent != scaleDefault);
        }
    }

    private void resetToDefault() {
        settingValue.reset();
        OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();

        float sizePercents = settingValue.asFloat() * 100F;
        sizeSlider.setValue((sizePercents - MIN_SIZE_PERCENTS) / 100F);
        sizeLabel.getText().setText(Math.round(sizePercents) + "%");

        confirmButton.setEnabled(false);
        defaultButton.setEnabled(false);
    }
}
