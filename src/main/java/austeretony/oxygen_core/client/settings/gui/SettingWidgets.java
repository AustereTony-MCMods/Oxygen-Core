package austeretony.oxygen_core.client.settings.gui;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.button.CheckBox;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.common.WidgetGroup;
import austeretony.oxygen_core.client.gui.base.core.Section;
import austeretony.oxygen_core.client.gui.base.list.DropDownList;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;
import austeretony.oxygen_core.client.settings.SettingValue;
import austeretony.oxygen_core.client.settings.SettingsManagerClient;
import austeretony.oxygen_core.client.settings.gui.alignment.ScreenAlignment;
import austeretony.oxygen_core.client.settings.gui.color.ColorPickerCallback;
import austeretony.oxygen_core.client.settings.gui.color.ColorButton;
import austeretony.oxygen_core.client.settings.gui.size.IntegerValueSelectorCallback;
import austeretony.oxygen_core.client.settings.gui.common.IntegerValueButton;
import austeretony.oxygen_core.client.settings.gui.scale.misc.FloatNormalizedPercentSelectorCallback;
import austeretony.oxygen_core.client.settings.gui.scale.misc.PercentScaleButton;
import austeretony.oxygen_core.client.settings.gui.scale.text.TextScaleAdjusterCallback;
import austeretony.oxygen_core.client.settings.gui.scale.text.TextScaleButton;

import java.util.List;
import java.util.function.Function;

public final class SettingWidgets {

    private SettingWidgets() {}

    public static Function<SettingValue, WidgetGroup> checkBox() {
        return value -> {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(new CheckBox(0, 0, value.asBoolean() ? CheckBox.State.ACTIVE : CheckBox.State.INACTIVE)
                    .setStateChangeListener(state -> {
                        value.get().setValue(state == CheckBox.State.ACTIVE);
                        OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();
                    }));
            group.addWidget(new TextLabel(9, 6, Texts.additionalDark(SettingsManagerClient.getDisplayName(value))));
            return group;
        };
    }

    public static Function<SettingValue, WidgetGroup> screenAlignmentList(ScreenAlignment... possibleValues) {
        return value -> {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(new TextLabel(0, 6, Texts.additionalDark(SettingsManagerClient.getDisplayName(value))));

            String current = ScreenAlignment.valueOf(value.asString()).getDisplayName();
            DropDownList<String> list;
            group.addWidget(list = new DropDownList<>(0, 8, 60, current)
                    .setEntryMouseClickListener((prev, curr, x, y, button) -> {
                        value.get().setValue(curr.getEntry());
                        OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();
                    }));

            ScreenAlignment[] values = possibleValues;
            if (values.length == 0) {
                values = ScreenAlignment.values();
            }

            for (ScreenAlignment alignment : values) {
                list.addElement(ListEntry.of(alignment.getDisplayName(), alignment.toString()));
            }
            return group;
        };
    }

    public static Function<SettingValue, WidgetGroup> colorPicker() {
        return value -> {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(new ColorButton(0, 0, value)
                    .setMouseClickListener((x, y, button) ->
                            Section.tryOpenCallback(new ColorPickerCallback(value))));
            group.addWidget(new TextLabel(9, 6, Texts.additionalDark(SettingsManagerClient.getDisplayName(value))));
            return group;
        };
    }

    public static Function<SettingValue, WidgetGroup> textScaleScrollBar() {
        return value -> {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(new TextScaleButton(0, 0, value)
                    .setMouseClickListener((x, y, button) ->
                            Section.tryOpenCallback(new TextScaleAdjusterCallback(value))));
            group.addWidget(new TextLabel(28, 6, Texts.additionalDark(SettingsManagerClient.getDisplayName(value))));
            return group;
        };
    }

    public static Function<SettingValue, WidgetGroup> floatNormalizedPercentSelector() {
        return value -> {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(new PercentScaleButton(0, 0, value)
                    .setMouseClickListener((x, y, button) ->
                            Section.tryOpenCallback(new FloatNormalizedPercentSelectorCallback(value))));
            group.addWidget(new TextLabel(20, 6, Texts.additionalDark(SettingsManagerClient.getDisplayName(value))));
            return group;
        };
    }

    public static Function<SettingValue, WidgetGroup> integerValueSelector(int minValue, int maxValue) {
        return value -> {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(new IntegerValueButton(0, 0, value, null)
                    .setMouseClickListener((x, y, button) ->
                            Section.tryOpenCallback(new IntegerValueSelectorCallback(value, minValue, maxValue,
                                    "oxygen_core.gui.settings.widget.value_integer", null))));
            group.addWidget(new TextLabel(28, 6, Texts.additionalDark(SettingsManagerClient.getDisplayName(value))));
            return group;
        };
    }

    public static Function<SettingValue, WidgetGroup> pixelsSizeValueSelector(int minValue, int maxValue) {
        return value -> {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(new IntegerValueButton(0, 0, value, "oxygen_core.gui.settings.widget.pixels")
                    .setMouseClickListener((x, y, button) ->
                            Section.tryOpenCallback(new IntegerValueSelectorCallback(value, minValue, maxValue,
                                    "oxygen_core.gui.settings.widget.size_pixels", "oxygen_core.gui.settings.widget.pixels"))));
            group.addWidget(new TextLabel(28, 6, Texts.additionalDark(SettingsManagerClient.getDisplayName(value))));
            return group;
        };
    }

    public static Function<SettingValue, WidgetGroup> genericDropDownList(List<String> optionNames) {
        return value -> {
            WidgetGroup group = new WidgetGroup();
            group.addWidget(new TextLabel(0, 6, Texts.additionalDark(SettingsManagerClient.getDisplayName(value))));

            String current = optionNames.get(value.asInt());
            DropDownList<Integer> list;
            group.addWidget(list = new DropDownList<>(0, 8, 60, current)
                    .setEntryMouseClickListener((prev, curr, x, y, button) -> {
                        value.get().setValue(curr.getEntry());
                        OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();
                    }));

            int index = 0;
            for (String optionName : optionNames) {
                list.addElement(ListEntry.of(optionName, index++));
            }
            return group;
        };
    }
}
