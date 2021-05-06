package austeretony.oxygen_core.client.settings.gui;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.gui.base.*;
import austeretony.oxygen_core.client.gui.base.button.VerticalSlider;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.common.ScrollableWidgetGroup;
import austeretony.oxygen_core.client.gui.base.common.WidgetGroup;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.core.Section;
import austeretony.oxygen_core.client.gui.base.list.ScrollableList;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.base.background.Background;
import austeretony.oxygen_core.client.gui.base.special.*;
import austeretony.oxygen_core.client.gui.base.special.callback.YesNoCallback;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;
import austeretony.oxygen_core.client.gui.util.OxygenGUIUtils;
import austeretony.oxygen_core.client.settings.SettingType;
import austeretony.oxygen_core.client.settings.SettingValue;
import austeretony.oxygen_core.client.settings.SettingsManagerClient;

import java.util.List;
import java.util.stream.Collectors;

public class CommonSettingsSection extends Section {

    private final SettingsScreen screen;

    private List<SettingValue> settings;
    private List<String> categories;

    private ScrollableList<String> modulesList;
    private String currentModuleName = "";
    private ScrollableWidgetGroup moduleSettings;

    public CommonSettingsSection(SettingsScreen screen) {
        super(screen, localize("oxygen_core.gui.settings.common.title"), true);
        this.screen = screen;
    }

    @Override
    public void init() {
        addWidget(new Background.UnderlinedTitleButtons(this));
        addWidget(new TextLabel(4, 12, Texts.title("oxygen_core.gui.settings.title")));
        addWidget(new SectionSwitcher(this));

        settings = OxygenManagerClient.instance().getSettingsManager().getSettingsMap().values()
                .stream()
                .filter(e -> e.getType() == SettingType.COMMON)
                .collect(Collectors.toList());
        categories = settings
                .stream()
                .map(SettingValue::getCategory)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

        addWidget(modulesList = new ScrollableList<>(6, 16, 18, 60, 10)
                .<String>setEntryMouseClickListener((previous, current, x, y, button) -> {
                    if (currentModuleName.equals(current.getEntry())) return;
                    if (previous != null) {
                        previous.setSelected(false);
                    }
                    current.setSelected(true);
                    displayModuleSettings(current.getEntry());
                }));

        List<String> modules = settings
                .stream()
                .map(SettingValue::getModuleName)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
        for (String moduleName : modules) {
            modulesList.addElement(new ListEntry(Texts.common(moduleName), moduleName).setFill(Fills.def()));
        }
        modulesList.setFirstElementSelected();

        int settingsX = 72;
        int settingsY = 16;
        int settingsWidth = getWorkspace().getWidth() - settingsX - 6 - 3;
        int settingsHeight = getWorkspace().getHeight() - 18;
        addWidget(moduleSettings = new ScrollableWidgetGroup(settingsX, settingsY, settingsWidth, settingsHeight));
        VerticalSlider settingsSlider;
        addWidget(settingsSlider = new VerticalSlider(settingsX + settingsWidth + 1, settingsY + 2, 2, settingsHeight - 2));
        moduleSettings.setSlider(settingsSlider);

        String keyButtonText = localize("oxygen_core.gui.settings.button.reset_module_settings");
        KeyButton keyButton;
        addWidget(keyButton = new KeyButton(0, 0, Keys.RESET_KEY, keyButtonText)
                .setLayer(Layer.FRONT)
                .setPressListener(() -> {
                    Callback callback = new YesNoCallback(
                            "oxygen_core.gui.settings.callback.reset_module_settings",
                            "oxygen_core.gui.settings.callback.reset_module_settings_message",
                            this::resetCurrentModuleSettingsToDefault);
                    openCallback(callback);
                }));
        OxygenGUIUtils.calculateBottomCenteredOffscreenButtonPosition(keyButton, 1, 1);

        displayModuleSettings(SettingsScreen.MAIN_MODULE_NAME);
    }

    private void displayModuleSettings(String moduleName) {
        moduleSettings.clear();
        currentModuleName = moduleName;

        int y = 0;
        for (String category : categories) {
            List<SettingValue> categorySettings = settings
                    .stream()
                    .filter(e -> e.getModuleName().equals(moduleName) && e.getCategory().equals(category))
                    .collect(Collectors.toList());
            if (categorySettings.isEmpty()) continue;

            y += 11;
            moduleSettings.addWidget(new TextLabel(0, y,
                    Texts.common(SettingsManagerClient.getCategoryDisplayName(categorySettings.get(0)))));
            y += 4;

            for (SettingValue settingValue : categorySettings) {
                WidgetGroup widgetGroup = (WidgetGroup) settingValue.getWidgetSupplier().apply(settingValue);
                widgetGroup.setPosition(0, y);
                y += widgetGroup.getHeight();
                moduleSettings.addWidget(widgetGroup);
            }
        }
    }

    private void resetCurrentModuleSettingsToDefault() {
        for (SettingValue setting : settings) {
            if (setting.getModuleName().equals(currentModuleName))
                setting.reset();
        }
        OxygenManagerClient.instance().getSettingsManager().markSettingsUpdated();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        OxygenGUIUtils.closeScreenOnKeyPress(getScreen(), keyCode);
        super.keyTyped(typedChar, keyCode);
    }
}
