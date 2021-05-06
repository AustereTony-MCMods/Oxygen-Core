package austeretony.oxygen_core.client.settings.gui.color;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.base.Keys;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.background.Background;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.base.text.TextField;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;
import austeretony.oxygen_core.client.settings.SettingValue;
import austeretony.oxygen_core.client.settings.SettingsManagerClient;
import austeretony.oxygen_core.common.util.value.HexValue;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ColorPickerCallback extends Callback {

    private final SettingValue<HexValue, Integer> settingValue;
    private ColorWidget colorWidget;
    private TextField textField;
    private TextLabel hueLabel, saturationLabel, brightnessLabel, opacityLabel;
    private HueHorizontalSlider hueSlider;
    private SBHorizontalSlider saturationSlider;
    private SBHorizontalSlider brightnessSlider;
    private OpacityHorizontalSlider opacitySlider;
    private KeyButton confirmButton, defaultButton;

    private int colorDefault, colorCurrent, colorNew;

    public ColorPickerCallback(SettingValue<HexValue, Integer> value) {
        super(164, 100);
        settingValue = value;
    }

    @Override
    public void init() {
        addWidget(new Background.UnderlinedTitleBottom(this));
        addWidget(new TextLabel(4, 12, Texts.title(SettingsManagerClient.getDisplayName(settingValue))));

        colorDefault = settingValue.getDefault();
        colorCurrent = colorNew = settingValue.asInt();
        addWidget(colorWidget = new ColorWidget(6, 18, colorCurrent));
        addWidget(textField = new TextField(6 + 9 + 4, 19, 40, 8)
                .setKeyPressListener((keyCode, keyChar) -> textFieldUpdated()));
        addWidget(new TextLabel(6 + 9 + 4 + 40 + 4, 27,
                Texts.additionalDark("oxygen_core.gui.settings.widget.hex_argb_color")));

        addWidget(new TextLabel(6, 39, Texts.common("oxygen_core.gui.settings.widget.hsv_color_model")));
        int sliderWidth = 128;
        addWidget(hueSlider = new HueHorizontalSlider(6, 41, sliderWidth, 0F));
        hueSlider.setInstantValueChangeListener((from, to) -> slidersUpdated());
        addWidget(hueLabel = new TextLabel(6 + sliderWidth + 4, 47, Texts.additionalDark("")));

        addWidget(saturationSlider = new SBHorizontalSlider(6, 51, sliderWidth, 0F, 0xFFFFFFFF,
                colorCurrent));
        saturationSlider.setInstantValueChangeListener((from, to) -> slidersUpdated());
        addWidget(saturationLabel = new TextLabel(6 + sliderWidth + 4, 57, Texts.additionalDark("")));

        addWidget(brightnessSlider = new SBHorizontalSlider(6, 61, sliderWidth, 0F, 0,
                colorCurrent));
        brightnessSlider.setInstantValueChangeListener((from, to) -> slidersUpdated());
        addWidget(brightnessLabel = new TextLabel(6 + sliderWidth + 4, 67, Texts.additionalDark("")));

        addWidget(new TextLabel(6, 77, Texts.common("oxygen_core.gui.settings.widget.opacity")));
        addWidget(opacitySlider = new OpacityHorizontalSlider(6, 79, sliderWidth, 0F,
                colorCurrent));
        opacitySlider.setInstantValueChangeListener((from, to) -> slidersUpdated());
        addWidget(opacityLabel = new TextLabel(6 + sliderWidth + 4, 85, Texts.additionalDark("")));

        int buttonPosSegment = (int) (getWidth() / 3F);
        addWidget(confirmButton = new KeyButton(0, getHeight() - 10, Keys.CONFIRM_KEY, "oxygen_core.gui.button.confirm")
                .setPressListener(this::confirm)
                .setEnabled(false));
        confirmButton.setX(getX() + (int) ((buttonPosSegment - confirmButton.getText().getWidth()) / 2F));
        addWidget(defaultButton = new KeyButton(0, getHeight() - 10, Keyboard.KEY_Z, "oxygen_core.gui.button.default")
                .setPressListener(this::resetToDefault)
                .setEnabled(colorCurrent != colorDefault));
        defaultButton.setX(getX() + buttonPosSegment + (int) ((buttonPosSegment - defaultButton.getText().getWidth()) / 2F));
        KeyButton closeButton;
        addWidget(closeButton = new KeyButton(0, getHeight() - 10, Keys.CLOSE_KEY, "oxygen_core.gui.button.close")
                .setPressListener(this::close));
        closeButton.setX(getX() + buttonPosSegment * 2 + (int) ((buttonPosSegment - closeButton.getText().getWidth()) / 2F));

        textField.setText(settingValue.get().toString());
        textFieldUpdated();
    }

    @Override
    public boolean close() {
        if (!textField.isFocused()) {
            super.close();
            return true;
        }
        return false;
    }

    private void textFieldUpdated() {
        int colorTemp;
        try {
            colorTemp = (int) Long.parseLong(textField.getTypedText(), 16);
        } catch (NumberFormatException exception) {
            return;
        }
        colorNew = colorTemp;

        int alpha = colorNew >> 24 & 0xFF;
        int r = colorNew >> 16 & 0xFF;
        int g = colorNew >> 8 & 0xFF;
        int b = colorNew & 0xFF;
        float[] hsbColor = Color.RGBtoHSB(r, g, b, null);

        hueSlider.setValue(hsbColor[0]);
        hueLabel.getText().setText("H: " + Math.round(360F * hsbColor[0]) + "\u00B0");

        saturationSlider.setValue(hsbColor[1]);
        saturationSlider.setBarSecondColor(colorNew);
        saturationLabel.getText().setText("S: " + Math.round(hsbColor[1] * 100F) + "%");

        brightnessSlider.setValue(hsbColor[2]);
        brightnessSlider.setBarSecondColor(colorNew);
        brightnessLabel.getText().setText("V: " + Math.round(hsbColor[2] * 100F) + "%");

        opacitySlider.setValue(alpha / 255F);
        opacitySlider.setBarSecondColor(colorNew);
        opacityLabel.getText().setText(Math.round(opacitySlider.getValue() * 100F) + "%");

        colorWidget.setColor(colorNew);
        confirmButton.setEnabled(colorNew != colorCurrent);
    }

    private void slidersUpdated() {
        float hue = hueSlider.getValue();
        float saturation = saturationSlider.getValue();
        float brightness = brightnessSlider.getValue();
        float alpha = opacitySlider.getValue();

        colorNew = (Color.HSBtoRGB(hue, saturation, brightness) & 0xFFFFFF) | ((int) (255F * alpha) << 24);
        textField.setText(Integer.toHexString(colorNew));

        hueLabel.getText().setText("H: " + Math.round(360F * hueSlider.getValue()) + "\u00B0");

        saturationSlider.setBarSecondColor(colorNew);
        saturationLabel.getText().setText("S: " + Math.round(saturationSlider.getValue() * 100F) + "%");

        brightnessSlider.setBarSecondColor(colorNew);
        brightnessLabel.getText().setText("V: " + Math.round(brightnessSlider.getValue() * 100F) + "%");

        opacitySlider.setBarSecondColor(colorNew);
        opacityLabel.getText().setText(Math.round(opacitySlider.getValue() * 100F) + "%");

        colorWidget.setColor(colorNew);
        confirmButton.setEnabled(colorNew != colorCurrent);
    }

    private void confirm() {
        if (!textField.isFocused() && colorCurrent != colorNew) {
            settingValue.get().setValue(colorNew);
            OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();

            colorCurrent = colorNew;
            confirmButton.setEnabled(false);
            defaultButton.setEnabled(colorCurrent != colorDefault);
        }
    }

    private void resetToDefault() {
        if (!textField.isFocused()) {
            settingValue.reset();
            OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();

            textField.setText(settingValue.get().toString());
            textFieldUpdated();
            confirmButton.setEnabled(false);
            defaultButton.setEnabled(false);
        }
    }
}
