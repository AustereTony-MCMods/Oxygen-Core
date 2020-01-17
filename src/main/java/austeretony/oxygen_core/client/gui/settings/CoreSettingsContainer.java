package austeretony.oxygen_core.client.gui.settings;

import austeretony.alternateui.screen.framework.GUIElementsFramework;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenCheckBoxButton;
import austeretony.oxygen_core.client.gui.elements.OxygenDropDownList;
import austeretony.oxygen_core.client.gui.elements.OxygenDropDownList.OxygenDropDownListEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.settings.gui.OffsetButton;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetColorCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetKeyCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetOffsetCallback;
import austeretony.oxygen_core.client.gui.settings.gui.callback.SetScaleCallback;
import austeretony.oxygen_core.client.settings.EnumCoreClientSetting;
import austeretony.oxygen_core.client.settings.gui.EnumCoreGUISetting;

public class CoreSettingsContainer implements ElementsContainer {

    //common

    private OxygenCheckBoxButton 
    hideOverlayButton,
    addNotificationsMenuButton, addSettingsMenuButton, addPrivilegesMenuButton;

    //interface

    private OxygenDropDownList alignmentNotificationsMenu, alignmentSettingsMenu, alignmentPrivilegesMenu;

    private OffsetButton requestOverlayOffset;

    private SetColorCallback setColorCallback;

    private SetScaleCallback setScaleCallback;

    private SetOffsetCallback setOffsetCallback;

    private SetKeyCallback setKeyCallback;

    @Override
    public String getLocalizedName() {
        return ClientReference.localize("oxygen_core.gui.settings.module.core");
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

        //hide overlay
        framework.addElement(new OxygenTextLabel(78, 34, ClientReference.localize("oxygen_core.gui.settings.option.hideOverlay"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.hideOverlayButton = new OxygenCheckBoxButton(68, 29));

        this.hideOverlayButton.setToggled(EnumCoreClientSetting.HIDE_REQUESTS_OVERLAY.get().asBoolean());

        this.hideOverlayButton.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumCoreClientSetting.HIDE_REQUESTS_OVERLAY.get().setValue(String.valueOf(this.hideOverlayButton.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        framework.addElement(new OxygenTextLabel(68, 45, ClientReference.localize("oxygen_core.gui.settings.option.oxygenMenu"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        //add notifications menu
        framework.addElement(new OxygenTextLabel(78, 54, ClientReference.localize("oxygen_core.gui.settings.option.addNotificationsMenu"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.addNotificationsMenuButton = new OxygenCheckBoxButton(68, 49));

        this.addNotificationsMenuButton.setToggled(EnumCoreClientSetting.ADD_NOTIFICATIONS_MENU.get().asBoolean());

        this.addNotificationsMenuButton.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumCoreClientSetting.ADD_NOTIFICATIONS_MENU.get().setValue(String.valueOf(this.addNotificationsMenuButton.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        //add settings menu
        framework.addElement(new OxygenTextLabel(78, 64, ClientReference.localize("oxygen_core.gui.settings.option.addSettingsMenu"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.addSettingsMenuButton = new OxygenCheckBoxButton(68, 59));

        this.addSettingsMenuButton.setToggled(EnumCoreClientSetting.ADD_SETTINGS_MENU.get().asBoolean());

        this.addSettingsMenuButton.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumCoreClientSetting.ADD_SETTINGS_MENU.get().setValue(String.valueOf(this.addSettingsMenuButton.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        //add privileges menu
        framework.addElement(new OxygenTextLabel(78, 74, ClientReference.localize("oxygen_core.gui.settings.option.addPrivilegesMenu"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.addPrivilegesMenuButton = new OxygenCheckBoxButton(68, 69));

        this.addPrivilegesMenuButton.setToggled(EnumCoreClientSetting.ADD_PRIVILEGES_MENU.get().asBoolean());

        this.addPrivilegesMenuButton.setClickListener((mouseX, mouseY, mouseButton)->{
            EnumCoreClientSetting.ADD_PRIVILEGES_MENU.get().setValue(String.valueOf(this.addPrivilegesMenuButton.isToggled()));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });
    }

    @Override
    public void addGUI(GUIElementsFramework framework) {  
        framework.addElement(new OxygenTextLabel(68, 93, ClientReference.localize("oxygen_core.gui.settings.option.offset"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        //notification overlay vertical offset
        framework.addElement(new OxygenTextLabel(68, 102, ClientReference.localize("oxygen_core.gui.settings.option.offsetRequestOverlay"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        framework.addElement(this.requestOverlayOffset = new OffsetButton(68, 105, EnumCoreGUISetting.REQUEST_OVERLAY_OFFSET.get()));

        this.requestOverlayOffset.setClickListener((mouseX, mouseY, mouseButton)->{
            this.requestOverlayOffset.setHovered(false);
            this.setOffsetCallback.open(this.requestOverlayOffset);
        });

        framework.addElement(new OxygenTextLabel(68, 25, ClientReference.localize("oxygen_core.gui.settings.option.alignment"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        //privileges menu alignment
        String currAlignmentStr = null;
        switch (EnumCoreGUISetting.PRIVILEGES_MENU_ALIGNMENT.get().asInt()) {
        case - 1: 
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.left");
            break;
        case 0:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.center");
            break;
        case 1:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.right");
            break;    
        default:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.center");
            break;
        }
        framework.addElement(this.alignmentPrivilegesMenu = new OxygenDropDownList(68, 73, 55, currAlignmentStr));
        this.alignmentPrivilegesMenu.addElement(new OxygenDropDownListEntry<Integer>(- 1, ClientReference.localize("oxygen_core.alignment.left")));
        this.alignmentPrivilegesMenu.addElement(new OxygenDropDownListEntry<Integer>(0, ClientReference.localize("oxygen_core.alignment.center")));
        this.alignmentPrivilegesMenu.addElement(new OxygenDropDownListEntry<Integer>(1, ClientReference.localize("oxygen_core.alignment.right")));

        this.alignmentPrivilegesMenu.<OxygenDropDownListEntry<Integer>>setClickListener((element)->{
            EnumCoreGUISetting.PRIVILEGES_MENU_ALIGNMENT.get().setValue(String.valueOf(element.index));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        framework.addElement(new OxygenTextLabel(68, 71, ClientReference.localize("oxygen_core.gui.settings.option.alignmentPrivilegesMenu"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));


        //settings menu alignment
        switch (EnumCoreGUISetting.SETTINGS_MENU_ALIGNMENT.get().asInt()) {
        case - 1: 
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.left");
            break;
        case 0:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.center");
            break;
        case 1:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.right");
            break;    
        default:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.center");
            break;
        }
        framework.addElement(this.alignmentSettingsMenu = new OxygenDropDownList(68, 54, 55, currAlignmentStr));
        this.alignmentSettingsMenu.addElement(new OxygenDropDownListEntry<Integer>(- 1, ClientReference.localize("oxygen_core.alignment.left")));
        this.alignmentSettingsMenu.addElement(new OxygenDropDownListEntry<Integer>(0, ClientReference.localize("oxygen_core.alignment.center")));
        this.alignmentSettingsMenu.addElement(new OxygenDropDownListEntry<Integer>(1, ClientReference.localize("oxygen_core.alignment.right")));

        this.alignmentSettingsMenu.<OxygenDropDownListEntry<Integer>>setClickListener((element)->{
            EnumCoreGUISetting.SETTINGS_MENU_ALIGNMENT.get().setValue(String.valueOf(element.index));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        framework.addElement(new OxygenTextLabel(68, 52, ClientReference.localize("oxygen_core.gui.settings.option.alignmentSettingsMenu"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

        //notifications menu alignment

        switch (EnumCoreGUISetting.NOTIFICATIONS_MENU_ALIGNMENT.get().asInt()) {
        case - 1: 
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.left");
            break;
        case 0:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.center");
            break;
        case 1:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.right");
            break;    
        default:
            currAlignmentStr = ClientReference.localize("oxygen_core.alignment.center");
            break;
        }
        framework.addElement(this.alignmentNotificationsMenu = new OxygenDropDownList(68, 35, 55, currAlignmentStr));
        this.alignmentNotificationsMenu.addElement(new OxygenDropDownListEntry<Integer>(- 1, ClientReference.localize("oxygen_core.alignment.left")));
        this.alignmentNotificationsMenu.addElement(new OxygenDropDownListEntry<Integer>(0, ClientReference.localize("oxygen_core.alignment.center")));
        this.alignmentNotificationsMenu.addElement(new OxygenDropDownListEntry<Integer>(1, ClientReference.localize("oxygen_core.alignment.right")));

        this.alignmentNotificationsMenu.<OxygenDropDownListEntry<Integer>>setClickListener((element)->{
            EnumCoreGUISetting.NOTIFICATIONS_MENU_ALIGNMENT.get().setValue(String.valueOf(element.index));
            OxygenManagerClient.instance().getClientSettingManager().changed();
        });

        framework.addElement(new OxygenTextLabel(68, 33, ClientReference.localize("oxygen_core.gui.settings.option.alignmentNotificationsMenu"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.1F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));
    }

    @Override
    public void resetCommon() {
        //hide overlay
        this.hideOverlayButton.setToggled(false);
        EnumCoreClientSetting.HIDE_REQUESTS_OVERLAY.get().reset();     

        //add notifications menu
        this.addNotificationsMenuButton.setToggled(true);
        EnumCoreClientSetting.ADD_NOTIFICATIONS_MENU.get().reset();

        //add settings menu
        this.addSettingsMenuButton.setToggled(true);
        EnumCoreClientSetting.ADD_SETTINGS_MENU.get().reset();

        //add privileges menu
        this.addPrivilegesMenuButton.setToggled(false);
        EnumCoreClientSetting.ADD_PRIVILEGES_MENU.get().reset();

        OxygenManagerClient.instance().getClientSettingManager().changed();
    }

    @Override
    public void resetGUI() {       
        //notifications menu alignment
        this.alignmentNotificationsMenu.setDisplayText(ClientReference.localize("oxygen_core.alignment.center"));
        EnumCoreGUISetting.NOTIFICATIONS_MENU_ALIGNMENT.get().reset();

        //settings menu alignment
        this.alignmentSettingsMenu.setDisplayText(ClientReference.localize("oxygen_core.alignment.center"));
        EnumCoreGUISetting.SETTINGS_MENU_ALIGNMENT.get().reset();

        //privileges menu alignment
        this.alignmentPrivilegesMenu.setDisplayText(ClientReference.localize("oxygen_core.alignment.center"));
        EnumCoreGUISetting.PRIVILEGES_MENU_ALIGNMENT.get().reset();

        //request overlay offset
        EnumCoreGUISetting.REQUEST_OVERLAY_OFFSET.get().reset();
        this.requestOverlayOffset.setDisplayText(EnumCoreGUISetting.REQUEST_OVERLAY_OFFSET.get().getUserValue());

        OxygenManagerClient.instance().getClientSettingManager().changed();
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
