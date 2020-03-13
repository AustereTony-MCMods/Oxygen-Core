package austeretony.oxygen_core.client.gui.settings;

import austeretony.alternateui.screen.framework.GUIElementsFramework;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseClientSetting;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenCheckBoxButton;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.settings.gui.ColorButton;
import austeretony.oxygen_core.client.gui.settings.gui.ScaleButton;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetColorCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetKeyCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetOffsetCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetScaleCallback;

public class BaseSettingsContainer implements ElementsContainer {

    //common

    private OxygenCheckBoxButton interactWithRMBButton, enableSoundEffects, enableRarityColors, enableItemsDurabilityBar, enableStatusMessages;

    //interface

    private ColorButton 
    fillScreenColor, fillCallbackColor,
    guiBaseBackgroundColor, guiAdditionalBackgroundColor, 
    frameEnabledColor, frameDisabledColor, frameHoveredColor,
    elementEnabledColor, elementDisabledColor, elementHoveredColor,
    sliderEnabledColor, sliderDisabledColor, sliderHoveredColor,
    textEnabledColor, textDisabledColor, textHoveredColor,
    textDarkEnabledColor, textDarkDisabledColor, textDarkHoveredColor,
    textFieldEnabledColor, textFieldDisabledColor, textFieldHoveredColor,
    overlayBaseTextColor, overlayAdditionalTextColor,
    tooltipBackgroundColor, tooltipTextColor,
    activeTextColor, activeElementColor,
    inactiveTextColor, inactiveElementColor,
    statusTextColor, statusElementColor;

    private ScaleButton
    textScale,
    textSubScale,
    textTitleScale,
    textPanelScale,
    textButtonScale,
    textTooltipScale,
    overlayScale;

    private OxygenCheckBoxButton verticalGradientButton;

    private SetColorCallback setColorCallback;

    private SetScaleCallback setScaleCallback;

    private SetOffsetCallback setOffsetCallback;

    private SetKeyCallback setKeyCallback;

    @Override
    public String getLocalizedName() {
        return ClientReference.localize("oxygen_core.gui.settings.module.base");
    }

    @Override
    public boolean hasCommonSettings() {
        return true;
    }

    @Override
    public boolean hasGUISettings() {
        return true;
    }

    @Override
    public void addCommon(GUIElementsFramework framework) {
        framework.addElement(new OxygenTextLabel(68, 25, ClientReference.localize("oxygen_core.gui.settings.option.misc"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        //interact with RMB
        framework.addElement(new OxygenTextLabel(78, 34, ClientReference.localize("oxygen_core.gui.settings.option.interactWithRMB"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.interactWithRMBButton = new OxygenCheckBoxButton(68, 29));

        this.interactWithRMBButton.setToggled(EnumBaseClientSetting.INTERACT_WITH_RMB.get().asBoolean());

        this.interactWithRMBButton.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumBaseClientSetting.INTERACT_WITH_RMB.get().setValue(String.valueOf(this.interactWithRMBButton.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        //enable sound effects
        framework.addElement(new OxygenTextLabel(78, 44, ClientReference.localize("oxygen_core.gui.settings.option.enableSoundEffects"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.enableSoundEffects = new OxygenCheckBoxButton(68, 39));

        this.enableSoundEffects.setToggled(EnumBaseClientSetting.ENABLE_SOUND_EFFECTS.get().asBoolean());

        this.enableSoundEffects.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumBaseClientSetting.ENABLE_SOUND_EFFECTS.get().setValue(String.valueOf(this.enableSoundEffects.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        //enable rarity colors
        framework.addElement(new OxygenTextLabel(78, 54, ClientReference.localize("oxygen_core.gui.settings.option.enableRarityColors"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.enableRarityColors = new OxygenCheckBoxButton(68, 49));

        this.enableRarityColors.setToggled(EnumBaseClientSetting.ENABLE_RARITY_COLORS.get().asBoolean());

        this.enableRarityColors.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumBaseClientSetting.ENABLE_RARITY_COLORS.get().setValue(String.valueOf(this.enableRarityColors.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        //enable items durability bar
        framework.addElement(new OxygenTextLabel(78, 64, ClientReference.localize("oxygen_core.gui.settings.option.enableItemsDurabilityBar"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.enableItemsDurabilityBar = new OxygenCheckBoxButton(68, 59));

        this.enableItemsDurabilityBar.setToggled(EnumBaseClientSetting.ENABLE_ITEMS_DURABILITY_BAR.get().asBoolean());

        this.enableItemsDurabilityBar.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumBaseClientSetting.ENABLE_ITEMS_DURABILITY_BAR.get().setValue(String.valueOf(this.enableItemsDurabilityBar.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        //enable status messages
        framework.addElement(new OxygenTextLabel(78, 74, ClientReference.localize("oxygen_core.gui.settings.option.enableStatusMessages"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.enableStatusMessages = new OxygenCheckBoxButton(68, 69));

        this.enableStatusMessages.setToggled(EnumBaseClientSetting.ENABLE_STATUS_MESSAGES.get().asBoolean());

        this.enableStatusMessages.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumBaseClientSetting.ENABLE_STATUS_MESSAGES.get().setValue(String.valueOf(this.enableStatusMessages.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });
    }

    @Override
    public void addGUI(GUIElementsFramework framework) {
        framework.addElement(new OxygenTextLabel(68, 25, ClientReference.localize("oxygen_core.gui.settings.option.color"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        //interface background color
        framework.addElement(new OxygenTextLabel(68, 33, ClientReference.localize("oxygen_core.gui.settings.option.screenFill"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.fillScreenColor = new ColorButton(68, 35, EnumBaseGUISetting.FILL_SCREEN_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.screen")));

        this.fillScreenColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.fillScreenColor.setHovered(false);
            this.setColorCallback.open(this.fillScreenColor);
        });

        framework.addElement(this.fillCallbackColor = new ColorButton(78, 35, EnumBaseGUISetting.FILL_CALLBACK_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.callback")));

        this.fillCallbackColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.fillCallbackColor.setHovered(false);
            this.setColorCallback.open(this.fillCallbackColor);
        });

        //interface background color
        framework.addElement(new OxygenTextLabel(68, 50, ClientReference.localize("oxygen_core.gui.settings.option.guiBackground"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.guiBaseBackgroundColor = new ColorButton(68, 52, EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.base")));

        this.guiBaseBackgroundColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.guiBaseBackgroundColor.setHovered(false);
            this.setColorCallback.open(this.guiBaseBackgroundColor);
        });

        framework.addElement(this.guiAdditionalBackgroundColor = new ColorButton(78, 52, EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.additional")));

        this.guiAdditionalBackgroundColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.guiAdditionalBackgroundColor.setHovered(false);
            this.setColorCallback.open(this.guiAdditionalBackgroundColor);
        });

        //button color
        framework.addElement(new OxygenTextLabel(68, 67, ClientReference.localize("oxygen_core.gui.settings.option.button"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.frameEnabledColor = new ColorButton(68, 69, EnumBaseGUISetting.BUTTON_ENABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.enabled")));

        this.frameEnabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.frameEnabledColor.setHovered(false);
            this.setColorCallback.open(this.frameEnabledColor);
        });

        framework.addElement(this.frameDisabledColor = new ColorButton(78, 69, EnumBaseGUISetting.BUTTON_DISABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.disabled")));

        this.frameDisabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.frameDisabledColor.setHovered(false);
            this.setColorCallback.open(this.frameDisabledColor);
        });

        framework.addElement(this.frameHoveredColor = new ColorButton(88, 69, EnumBaseGUISetting.BUTTON_HOVERED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.hovered")));

        this.frameHoveredColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.frameHoveredColor.setHovered(false);
            this.setColorCallback.open(this.frameHoveredColor);
        });

        //elements color
        framework.addElement(new OxygenTextLabel(68, 84, ClientReference.localize("oxygen_core.gui.settings.option.element"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.elementEnabledColor = new ColorButton(68, 86, EnumBaseGUISetting.ELEMENT_ENABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.enabled")));

        this.elementEnabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.elementEnabledColor.setHovered(false);
            this.setColorCallback.open(this.elementEnabledColor);
        });

        framework.addElement(this.elementDisabledColor = new ColorButton(78, 86, EnumBaseGUISetting.ELEMENT_DISABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.disabled")));

        this.elementDisabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.elementDisabledColor.setHovered(false);
            this.setColorCallback.open(this.elementDisabledColor);
        });

        framework.addElement(this.elementHoveredColor = new ColorButton(88, 86, EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.hovered")));

        this.elementHoveredColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.elementHoveredColor.setHovered(false);
            this.setColorCallback.open(this.elementHoveredColor);
        });

        //slider color
        framework.addElement(new OxygenTextLabel(68, 101, ClientReference.localize("oxygen_core.gui.settings.option.slider"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.sliderEnabledColor = new ColorButton(68, 103, EnumBaseGUISetting.SLIDER_ENABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.enabled")));

        this.sliderEnabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.sliderEnabledColor.setHovered(false);
            this.setColorCallback.open(this.sliderEnabledColor);
        });

        framework.addElement(this.sliderDisabledColor = new ColorButton(78, 103, EnumBaseGUISetting.SLIDER_DISABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.disabled")));

        this.sliderDisabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.sliderDisabledColor.setHovered(false);
            this.setColorCallback.open(this.sliderDisabledColor);
        });

        framework.addElement(this.sliderHoveredColor = new ColorButton(88, 103, EnumBaseGUISetting.SLIDER_HOVERED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.hovered")));

        this.sliderHoveredColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.sliderHoveredColor.setHovered(false);
            this.setColorCallback.open(this.sliderHoveredColor);
        });

        //text color
        framework.addElement(new OxygenTextLabel(68, 118, ClientReference.localize("oxygen_core.gui.settings.option.text"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textEnabledColor = new ColorButton(68, 120, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.enabled")));

        this.textEnabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textEnabledColor.setHovered(false);
            this.setColorCallback.open(this.textEnabledColor);
        });

        framework.addElement(this.textDisabledColor = new ColorButton(78, 120, EnumBaseGUISetting.TEXT_DISABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.disabled")));

        this.textDisabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textDisabledColor.setHovered(false);
            this.setColorCallback.open(this.textDisabledColor);
        });

        framework.addElement(this.textHoveredColor = new ColorButton(88, 120, EnumBaseGUISetting.TEXT_HOVERED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.hovered")));

        this.textHoveredColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textHoveredColor.setHovered(false);
            this.setColorCallback.open(this.textHoveredColor);
        });

        //text dark color
        framework.addElement(new OxygenTextLabel(68, 135, ClientReference.localize("oxygen_core.gui.settings.option.textDark"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textDarkEnabledColor = new ColorButton(68, 137, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.enabled")));

        this.textDarkEnabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textDarkEnabledColor.setHovered(false);
            this.setColorCallback.open(this.textDarkEnabledColor);
        });

        framework.addElement(this.textDarkDisabledColor = new ColorButton(78, 137, EnumBaseGUISetting.TEXT_DARK_DISABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.disabled")));

        this.textDarkDisabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textDarkDisabledColor.setHovered(false);
            this.setColorCallback.open(this.textDarkDisabledColor);
        });

        framework.addElement(this.textDarkHoveredColor = new ColorButton(88, 137, EnumBaseGUISetting.TEXT_DARK_HOVERED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.hovered")));

        this.textDarkHoveredColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textDarkHoveredColor.setHovered(false);
            this.setColorCallback.open(this.textDarkHoveredColor);
        });

        //textfield color
        framework.addElement(new OxygenTextLabel(68, 152, ClientReference.localize("oxygen_core.gui.settings.option.textfield"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textFieldEnabledColor = new ColorButton(68, 154, EnumBaseGUISetting.TEXTFIELD_ENABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.enabled")));

        this.textFieldEnabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textFieldEnabledColor.setHovered(false);
            this.setColorCallback.open(this.textFieldEnabledColor);
        });

        framework.addElement(this.textFieldDisabledColor = new ColorButton(78, 154, EnumBaseGUISetting.TEXTFIELD_DISABLED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.disabled")));

        this.textFieldDisabledColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textFieldDisabledColor.setHovered(false);
            this.setColorCallback.open(this.textFieldDisabledColor);
        });

        framework.addElement(this.textFieldHoveredColor = new ColorButton(88, 154, EnumBaseGUISetting.TEXTFIELD_HOVERED_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.hovered")));

        this.textFieldHoveredColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textFieldHoveredColor.setHovered(false);
            this.setColorCallback.open(this.textFieldHoveredColor);
        });

        //overlay text color
        framework.addElement(new OxygenTextLabel(68, 169, ClientReference.localize("oxygen_core.gui.settings.option.overlayText"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.overlayBaseTextColor = new ColorButton(68, 171, EnumBaseGUISetting.OVERLAY_TEXT_BASE_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.base")));

        this.overlayBaseTextColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.overlayBaseTextColor.setHovered(false);
            this.setColorCallback.open(this.overlayBaseTextColor);
        });

        framework.addElement(this.overlayAdditionalTextColor = new ColorButton(78, 171, EnumBaseGUISetting.OVERLAY_TEXT_ADDITIONAL_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.additional")));

        this.overlayAdditionalTextColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.overlayAdditionalTextColor.setHovered(false);
            this.setColorCallback.open(this.overlayAdditionalTextColor);
        });

        //tooltip color
        framework.addElement(new OxygenTextLabel(68, 186, ClientReference.localize("oxygen_core.gui.settings.option.tooltip"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.tooltipBackgroundColor = new ColorButton(68, 188, EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.background")));

        this.tooltipBackgroundColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.tooltipBackgroundColor.setHovered(false);
            this.setColorCallback.open(this.tooltipBackgroundColor);
        });

        framework.addElement(this.tooltipTextColor = new ColorButton(78, 188, EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.text")));

        this.tooltipTextColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.tooltipTextColor.setHovered(false);
            this.setColorCallback.open(this.tooltipTextColor);
        });

        //active element color
        framework.addElement(new OxygenTextLabel(68, 203, ClientReference.localize("oxygen_core.gui.settings.option.activeElement"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.activeElementColor = new ColorButton(68, 205, EnumBaseGUISetting.ACTIVE_ELEMENT_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.element")));

        this.activeElementColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.activeElementColor.setHovered(false);
            this.setColorCallback.open(this.activeElementColor);
        });

        framework.addElement(this.activeTextColor = new ColorButton(78, 205, EnumBaseGUISetting.ACTIVE_TEXT_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.text")));

        this.activeTextColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.activeTextColor.setHovered(false);
            this.setColorCallback.open(this.activeTextColor);
        });

        //inactive element color
        framework.addElement(new OxygenTextLabel(135, 23, ClientReference.localize("oxygen_core.gui.settings.option.inactiveElement"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.inactiveElementColor = new ColorButton(135, 25, EnumBaseGUISetting.INACTIVE_ELEMENT_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.element")));

        this.inactiveElementColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.inactiveElementColor.setHovered(false);
            this.setColorCallback.open(this.inactiveElementColor);
        });

        framework.addElement(this.inactiveTextColor = new ColorButton(145, 25, EnumBaseGUISetting.INACTIVE_TEXT_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.text")));

        this.inactiveTextColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.inactiveTextColor.setHovered(false);
            this.setColorCallback.open(this.inactiveTextColor);
        });

        //status element color
        framework.addElement(new OxygenTextLabel(135, 40, ClientReference.localize("oxygen_core.gui.settings.option.statusElement"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.statusElementColor = new ColorButton(135, 42, EnumBaseGUISetting.STATUS_ELEMENT_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.element")));

        this.statusElementColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.statusElementColor.setHovered(false);
            this.setColorCallback.open(this.statusElementColor);
        });

        framework.addElement(this.statusTextColor = new ColorButton(145, 42, EnumBaseGUISetting.STATUS_TEXT_COLOR.get(), ClientReference.localize("oxygen_core.gui.settings.tooltip.text")));

        this.statusTextColor.setClickListener((mouseX, mouseY, mouseButton)->{
            this.statusTextColor.setHovered(false);
            this.setColorCallback.open(this.statusTextColor);
        });

        framework.addElement(new OxygenTextLabel(135, 58, ClientReference.localize("oxygen_core.gui.settings.option.scale"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        //text scale
        framework.addElement(new OxygenTextLabel(135, 67, ClientReference.localize("oxygen_core.gui.settings.option.text"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textScale = new ScaleButton(135, 71, EnumBaseGUISetting.TEXT_SCALE.get()));

        this.textScale.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textScale.setHovered(false);
            this.setScaleCallback.open(this.textScale);
        });

        //sub text scale
        framework.addElement(new OxygenTextLabel(135, 84, ClientReference.localize("oxygen_core.gui.settings.option.subText"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textSubScale = new ScaleButton(135, 87, EnumBaseGUISetting.TEXT_SUB_SCALE.get()));

        this.textSubScale.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textSubScale.setHovered(false);
            this.setScaleCallback.open(this.textSubScale);
        });

        //title text scale
        framework.addElement(new OxygenTextLabel(135, 101, ClientReference.localize("oxygen_core.gui.settings.option.titleText"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textTitleScale = new ScaleButton(135, 104, EnumBaseGUISetting.TEXT_TITLE_SCALE.get()));

        this.textTitleScale.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textTitleScale.setHovered(false);
            this.setScaleCallback.open(this.textTitleScale);
        });

        //panel text scale
        framework.addElement(new OxygenTextLabel(135, 118, ClientReference.localize("oxygen_core.gui.settings.option.panelText"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textPanelScale = new ScaleButton(135, 121, EnumBaseGUISetting.TEXT_PANEL_SCALE.get()));

        this.textPanelScale.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textPanelScale.setHovered(false);
            this.setScaleCallback.open(this.textPanelScale);
        });

        //button text scale
        framework.addElement(new OxygenTextLabel(135, 135, ClientReference.localize("oxygen_core.gui.settings.option.buttonText"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textButtonScale = new ScaleButton(135, 138, EnumBaseGUISetting.TEXT_BUTTON_SCALE.get()));

        this.textButtonScale.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textButtonScale.setHovered(false);
            this.setScaleCallback.open(this.textButtonScale);
        });

        //tooltip text scale
        framework.addElement(new OxygenTextLabel(135, 152, ClientReference.localize("oxygen_core.gui.settings.option.tooltipText"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.textTooltipScale = new ScaleButton(135, 155, EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get()));

        this.textTooltipScale.setClickListener((mouseX, mouseY, mouseButton)->{
            this.textTooltipScale.setHovered(false);
            this.setScaleCallback.open(this.textTooltipScale);
        });

        //overlay scale
        framework.addElement(new OxygenTextLabel(135, 169, ClientReference.localize("oxygen_core.gui.settings.option.overlay"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.overlayScale = new ScaleButton(135, 172, EnumBaseGUISetting.OVERLAY_SCALE.get()));

        this.overlayScale.setClickListener((mouseX, mouseY, mouseButton)->{
            this.overlayScale.setHovered(false);
            this.setScaleCallback.open(this.overlayScale);
        });

        framework.addElement(new OxygenTextLabel(135, 187, ClientReference.localize("oxygen_core.gui.settings.option.misc"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        //background vertical gradient
        framework.addElement(new OxygenTextLabel(145, 195, ClientReference.localize("oxygen_core.gui.settings.option.verticalGradient"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.verticalGradientButton = new OxygenCheckBoxButton(135, 190));

        this.verticalGradientButton.setToggled(EnumBaseGUISetting.VERTICAL_GRADIENT.get().asBoolean());

        this.verticalGradientButton.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumBaseGUISetting.VERTICAL_GRADIENT.get().setValue(String.valueOf(this.verticalGradientButton.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        //confirm key
        //TODO

        //cancel key
        //TODO

        //action key
        //TODO
    }

    @Override
    public void resetCommon() {
        //interact with rmb
        this.interactWithRMBButton.setToggled(false);
        EnumBaseClientSetting.INTERACT_WITH_RMB.get().reset();

        //enable sound effects
        this.enableSoundEffects.setToggled(true);
        EnumBaseClientSetting.ENABLE_SOUND_EFFECTS.get().reset();

        //enable rarity colors
        this.enableRarityColors.setToggled(true);
        EnumBaseClientSetting.ENABLE_RARITY_COLORS.get().reset();

        //enable items durability bar
        this.enableItemsDurabilityBar.setToggled(true);
        EnumBaseClientSetting.ENABLE_ITEMS_DURABILITY_BAR.get().reset();

        //enable status messages
        this.enableStatusMessages.setToggled(true);
        EnumBaseClientSetting.ENABLE_STATUS_MESSAGES.get().reset();

        OxygenManagerClient.instance().getClientSettingManager().changed();
    }

    @Override
    public void resetGUI() {
        //fill color
        EnumBaseGUISetting.FILL_SCREEN_COLOR.get().reset();
        this.guiBaseBackgroundColor.setButtonColor(EnumBaseGUISetting.FILL_SCREEN_COLOR.get().asInt());

        EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().reset();
        this.guiAdditionalBackgroundColor.setButtonColor(EnumBaseGUISetting.FILL_CALLBACK_COLOR.get().asInt());

        //gui background color
        EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().reset();
        this.guiBaseBackgroundColor.setButtonColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt());

        EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().reset();
        this.guiAdditionalBackgroundColor.setButtonColor(EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt());

        //buttons color
        EnumBaseGUISetting.BUTTON_ENABLED_COLOR.get().reset();
        this.frameEnabledColor.setButtonColor(EnumBaseGUISetting.BUTTON_ENABLED_COLOR.get().asInt());

        EnumBaseGUISetting.BUTTON_DISABLED_COLOR.get().reset();
        this.frameDisabledColor.setButtonColor(EnumBaseGUISetting.BUTTON_DISABLED_COLOR.get().asInt());

        EnumBaseGUISetting.BUTTON_HOVERED_COLOR.get().reset();
        this.frameHoveredColor.setButtonColor(EnumBaseGUISetting.BUTTON_HOVERED_COLOR.get().asInt());

        //elements color
        EnumBaseGUISetting.ELEMENT_ENABLED_COLOR.get().reset();
        this.elementEnabledColor.setButtonColor(EnumBaseGUISetting.ELEMENT_ENABLED_COLOR.get().asInt());

        EnumBaseGUISetting.ELEMENT_DISABLED_COLOR.get().reset();
        this.elementDisabledColor.setButtonColor(EnumBaseGUISetting.ELEMENT_DISABLED_COLOR.get().asInt());

        EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().reset();
        this.elementHoveredColor.setButtonColor(EnumBaseGUISetting.ELEMENT_HOVERED_COLOR.get().asInt());

        //slider color
        EnumBaseGUISetting.SLIDER_ENABLED_COLOR.get().reset();
        this.sliderEnabledColor.setButtonColor(EnumBaseGUISetting.SLIDER_ENABLED_COLOR.get().asInt());

        EnumBaseGUISetting.SLIDER_DISABLED_COLOR.get().reset();
        this.sliderDisabledColor.setButtonColor(EnumBaseGUISetting.SLIDER_DISABLED_COLOR.get().asInt());

        EnumBaseGUISetting.SLIDER_HOVERED_COLOR.get().reset();
        this.sliderHoveredColor.setButtonColor(EnumBaseGUISetting.SLIDER_HOVERED_COLOR.get().asInt());

        //text color
        EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().reset();
        this.textEnabledColor.setButtonColor(EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt());

        EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().reset();
        this.textDisabledColor.setButtonColor(EnumBaseGUISetting.TEXT_DISABLED_COLOR.get().asInt());

        EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().reset();
        this.textHoveredColor.setButtonColor(EnumBaseGUISetting.TEXT_HOVERED_COLOR.get().asInt());

        //text dark color
        EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().reset();
        this.textDarkEnabledColor.setButtonColor(EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt());

        EnumBaseGUISetting.TEXT_DARK_DISABLED_COLOR.get().reset();
        this.textDarkDisabledColor.setButtonColor(EnumBaseGUISetting.TEXT_DARK_DISABLED_COLOR.get().asInt());

        EnumBaseGUISetting.TEXT_DARK_HOVERED_COLOR.get().reset();
        this.textDarkHoveredColor.setButtonColor(EnumBaseGUISetting.TEXT_DARK_HOVERED_COLOR.get().asInt());

        //text dark color
        EnumBaseGUISetting.TEXTFIELD_ENABLED_COLOR.get().reset();
        this.textFieldEnabledColor.setButtonColor(EnumBaseGUISetting.TEXTFIELD_ENABLED_COLOR.get().asInt());

        EnumBaseGUISetting.TEXTFIELD_DISABLED_COLOR.get().reset();
        this.textFieldDisabledColor.setButtonColor(EnumBaseGUISetting.TEXTFIELD_DISABLED_COLOR.get().asInt());

        EnumBaseGUISetting.TEXTFIELD_HOVERED_COLOR.get().reset();
        this.textFieldHoveredColor.setButtonColor(EnumBaseGUISetting.TEXTFIELD_HOVERED_COLOR.get().asInt());

        //overlay text color
        EnumBaseGUISetting.OVERLAY_TEXT_BASE_COLOR.get().reset();
        this.overlayBaseTextColor.setButtonColor(EnumBaseGUISetting.OVERLAY_TEXT_BASE_COLOR.get().asInt());

        EnumBaseGUISetting.OVERLAY_TEXT_ADDITIONAL_COLOR.get().reset();
        this.overlayAdditionalTextColor.setButtonColor(EnumBaseGUISetting.OVERLAY_TEXT_ADDITIONAL_COLOR.get().asInt());

        //tooltip color
        EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().reset();
        this.tooltipBackgroundColor.setButtonColor(EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().asInt());

        EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().reset();
        this.tooltipTextColor.setButtonColor(EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().asInt());

        //active element color
        EnumBaseGUISetting.ACTIVE_ELEMENT_COLOR.get().reset();
        this.activeElementColor.setButtonColor(EnumBaseGUISetting.ACTIVE_ELEMENT_COLOR.get().asInt());

        EnumBaseGUISetting.ACTIVE_TEXT_COLOR.get().reset();
        this.activeTextColor.setButtonColor(EnumBaseGUISetting.ACTIVE_TEXT_COLOR.get().asInt());

        //inactive element color
        EnumBaseGUISetting.INACTIVE_ELEMENT_COLOR.get().reset();
        this.inactiveElementColor.setButtonColor(EnumBaseGUISetting.INACTIVE_ELEMENT_COLOR.get().asInt());

        EnumBaseGUISetting.INACTIVE_TEXT_COLOR.get().reset();
        this.inactiveTextColor.setButtonColor(EnumBaseGUISetting.INACTIVE_TEXT_COLOR.get().asInt());      

        //status element color
        EnumBaseGUISetting.STATUS_ELEMENT_COLOR.get().reset();
        this.statusElementColor.setButtonColor(EnumBaseGUISetting.STATUS_ELEMENT_COLOR.get().asInt());

        EnumBaseGUISetting.STATUS_TEXT_COLOR.get().reset();
        this.statusTextColor.setButtonColor(EnumBaseGUISetting.STATUS_TEXT_COLOR.get().asInt());    

        //text scale
        EnumBaseGUISetting.TEXT_SCALE.get().reset();
        this.textScale.setDisplayText(EnumBaseGUISetting.TEXT_SCALE.get().getUserValue());

        //sub text scale
        EnumBaseGUISetting.TEXT_SUB_SCALE.get().reset();
        this.textSubScale.setDisplayText(EnumBaseGUISetting.TEXT_SUB_SCALE.get().getUserValue());

        //title text scale
        EnumBaseGUISetting.TEXT_TITLE_SCALE.get().reset();
        this.textTitleScale.setDisplayText(EnumBaseGUISetting.TEXT_TITLE_SCALE.get().getUserValue());

        //panel text scale
        EnumBaseGUISetting.TEXT_PANEL_SCALE.get().reset();
        this.textPanelScale.setDisplayText(EnumBaseGUISetting.TEXT_PANEL_SCALE.get().getUserValue());

        //button text scale
        EnumBaseGUISetting.TEXT_BUTTON_SCALE.get().reset();
        this.textButtonScale.setDisplayText(EnumBaseGUISetting.TEXT_BUTTON_SCALE.get().getUserValue());

        //tooltip text scale
        EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().reset();
        this.textTooltipScale.setDisplayText(EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().getUserValue());

        //overlay scale
        EnumBaseGUISetting.OVERLAY_SCALE.get().reset();
        this.overlayScale.setDisplayText(EnumBaseGUISetting.OVERLAY_SCALE.get().getUserValue());

        //background vertical gradient
        this.verticalGradientButton.setToggled(false);
        EnumBaseGUISetting.VERTICAL_GRADIENT.get().reset();
    }

    @Override
    public void initSetColorCallback(SetColorCallback callback) {
        this.setColorCallback = callback;
    }

    @Override
    public void initSetScaleCallback(SetScaleCallback callback) {
        this.setScaleCallback = callback;
    }

    @Override
    public void initSetOffsetCallback(SetOffsetCallback callback) {
        this.setOffsetCallback = callback;
    }

    @Override
    public void initSetKeyCallback(SetKeyCallback callback) {
        this.setKeyCallback = callback;
    }
}
