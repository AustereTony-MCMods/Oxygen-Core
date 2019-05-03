package austeretony.oxygen.client.gui.playerlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.contextmenu.AbstractContextAction;
import austeretony.alternateui.screen.contextmenu.GUIContextMenu;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.image.GUIImageLabel;
import austeretony.alternateui.screen.list.GUIDropDownList;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.screen.panel.GUIButtonPanel.GUIEnumOrientation;
import austeretony.alternateui.screen.text.GUITextField;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.client.gui.StatusGUIDropDownElement;
import austeretony.oxygen.client.gui.playerlist.context.AddToFriendsContextAction;
import austeretony.oxygen.client.gui.playerlist.context.IgnoreContextAction;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.main.EnumOxygenPrivileges;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderClient;
import net.minecraft.client.resources.I18n;

public class PlayerListGUISection extends AbstractGUISection {

    private final PlayerListGUIScreen screen;

    private GUIButton searchButton, refreshButton, sortDownStatusButton, sortUpStatusButton, sortDownUsernameButton, sortUpUsernameButton;

    private PlayerListEntryGUIButton currentEntry;

    private GUITextLabel playersOnlineTextLabel, playerNameTextLabel;

    private GUITextField searchField;

    private GUIButtonPanel playersPanel;

    private GUIDropDownList statusDropDownList;

    private GUIImageLabel statusImageLabel;

    private OxygenPlayerData.EnumStatus currentStatus;

    private final Set<SharedPlayerData> players = new HashSet<SharedPlayerData>();

    public PlayerListGUISection(PlayerListGUIScreen screen) {
        super(screen);
        this.screen = screen;
    }

    @Override
    public void init() {
        this.addElement(new PlayerListBackgroundGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new GUITextLabel(2, 4).setDisplayText(I18n.format("oxygen.gui.players.title"), false, GUISettings.instance().getTitleScale()));

        this.addElement(this.searchButton = new GUIButton(4, 15, 7, 7).setSound(OxygenSoundEffects.BUTTON_CLICK).setTexture(OxygenGUITextures.SEARCH_ICONS, 7, 7).initSimpleTooltip(I18n.format("oxygen.tooltip.search"), GUISettings.instance().getTooltipScale()));         
        this.addElement(this.refreshButton = new GUIButton(79, 14, 10, 10).setSound(OxygenSoundEffects.BUTTON_CLICK).setTexture(OxygenGUITextures.REFRESH_ICONS, 9, 9).initSimpleTooltip(I18n.format("oxygen.tooltip.refresh"), GUISettings.instance().getTooltipScale()));         
        this.addElement(this.playerNameTextLabel = new GUITextLabel(91, 15).setDisplayText(OxygenHelperClient.getSharedClientPlayerData().getUsername(), false, GUISettings.instance().getSubTextScale()));
        this.addElement(this.playersOnlineTextLabel = new GUITextLabel(0, 15).setTextScale(GUISettings.instance().getSubTextScale())); 

        this.addElement(this.sortDownStatusButton = new GUIButton(7, 29, 3, 3).setSound(OxygenSoundEffects.BUTTON_CLICK).setTexture(OxygenGUITextures.SORT_DOWN_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortUpStatusButton = new GUIButton(7, 25, 3, 3).setSound(OxygenSoundEffects.BUTTON_CLICK).setTexture(OxygenGUITextures.SORT_UP_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortDownUsernameButton = new GUIButton(19, 29, 3, 3).setSound(OxygenSoundEffects.BUTTON_CLICK).setTexture(OxygenGUITextures.SORT_DOWN_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortUpUsernameButton = new GUIButton(19, 25, 3, 3).setSound(OxygenSoundEffects.BUTTON_CLICK).setTexture(OxygenGUITextures.SORT_UP_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(new GUITextLabel(24, 25).setDisplayText(I18n.format("oxygen.gui.friends.username")).setTextScale(GUISettings.instance().getTextScale())); 
        this.addElement(new GUITextLabel(110, 25).setDisplayText(I18n.format("oxygen.gui.friends.dimension")).setTextScale(GUISettings.instance().getTextScale())); 

        this.playersPanel = new GUIButtonPanel(GUIEnumOrientation.VERTICAL, 0, 35, this.getWidth() - 3, 10).setButtonsOffset(1).setTextScale(GUISettings.instance().getPanelTextScale());
        this.addElement(this.playersPanel);
        this.addElement(this.searchField = new GUITextField(0, 15, 100, 20).setScale(0.7F).enableDynamicBackground().setDisplayText("...", false, GUISettings.instance().getTextScale()).disableFull().cancelDraggedElementLogic());
        this.playersPanel.initSearchField(this.searchField);
        GUIScroller scroller = new GUIScroller(OxygenHelperClient.getMaxPlayers() > 15 ? OxygenHelperClient.getMaxPlayers() : 15, 15);
        this.playersPanel.initScroller(scroller);
        GUISlider slider = new GUISlider(this.getWidth() - 2, 35, 2, this.getHeight() - 35);
        slider.setDynamicBackgroundColor(GUISettings.instance().getEnabledSliderColor(), GUISettings.instance().getDisabledSliderColor(), GUISettings.instance().getHoveredSliderColor());
        scroller.initSlider(slider);   

        GUIContextMenu menu = new GUIContextMenu(GUISettings.instance().getContextMenuWidth(), 10).setScale(GUISettings.instance().getContextMenuScale()).setTextScale(GUISettings.instance().getTextScale()).setTextAlignment(EnumGUIAlignment.LEFT, 2);
        menu.setOpenSound(OxygenSoundEffects.CONTEXT_OPEN);
        menu.setCloseSound(OxygenSoundEffects.CONTEXT_CLOSE);
        this.playersPanel.initContextMenu(menu);
        menu.enableDynamicBackground(GUISettings.instance().getEnabledContextActionColor(), GUISettings.instance().getDisabledContextActionColor(), GUISettings.instance().getHoveredContextActionColor());
        menu.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
        menu.addElement(new AddToFriendsContextAction());
        menu.addElement(new IgnoreContextAction());

        //Support
        for (AbstractContextAction action : OxygenGUIHelper.getContextActions(OxygenMain.PLAYER_LIST_SCREEN_ID))
            menu.addElement(action);

        //Protection
        if (!OxygenGUIHelper.isNeedSync(OxygenMain.PLAYER_LIST_SCREEN_ID) || OxygenGUIHelper.isDataRecieved(OxygenMain.PLAYER_LIST_SCREEN_ID))
            this.sortPlayers(0);

        this.currentStatus = OxygenHelperClient.getClientPlayerStatus();
        int statusOffset = this.playerNameTextLabel.getX() + this.textWidth(this.playerNameTextLabel.getDisplayText(), GUISettings.instance().getSubTextScale());
        this.addElement(this.statusImageLabel = new GUIImageLabel(statusOffset + 4, 17).setTexture(OxygenGUITextures.STATUS_ICONS, 3, 3, this.currentStatus.ordinal() * 3, 0, 12, 3));   
        this.statusDropDownList = new GUIDropDownList(statusOffset + 10, 16, GUISettings.instance().getDropDownListWidth(), 10).setScale(GUISettings.instance().getDropDownListScale()).setDisplayText(this.currentStatus.localizedName()).setTextScale(GUISettings.instance().getTextScale()).setTextAlignment(EnumGUIAlignment.LEFT, 1);
        this.statusDropDownList.setOpenSound(OxygenSoundEffects.DROP_DOWN_LIST_OPEN);
        this.statusDropDownList.setCloseSound(OxygenSoundEffects.CONTEXT_CLOSE);
        StatusGUIDropDownElement profileElement;
        for (OxygenPlayerData.EnumStatus status : OxygenPlayerData.EnumStatus.values()) {
            profileElement = new StatusGUIDropDownElement(status);
            profileElement.setDisplayText(status.localizedName());
            profileElement.enableDynamicBackground(GUISettings.instance().getEnabledContextActionColor(), GUISettings.instance().getDisabledContextActionColor(), GUISettings.instance().getHoveredContextActionColor());
            profileElement.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            this.statusDropDownList.addElement(profileElement);
        }
        this.addElement(this.statusDropDownList);   

        OxygenGUIHelper.screenInitialized(OxygenMain.PLAYER_LIST_SCREEN_ID);
    }

    public void sortPlayers(int mode) {
        this.players.clear();
        for (SharedPlayerData sharedData : OxygenHelperClient.getSharedPlayersData())
            if (OxygenHelperClient.isOnline(sharedData.getPlayerUUID())
                    && (OxygenHelperClient.getPlayerStatus(sharedData) != OxygenPlayerData.EnumStatus.OFFLINE || PrivilegeProviderClient.getPrivilegeValue(EnumOxygenPrivileges.EXPOSE_PLAYERS_OFFLINE.toString(), false)))
                this.players.add(sharedData);

        List<SharedPlayerData> players = new ArrayList<SharedPlayerData>(this.players);
        if (mode == 0 || mode == 1) {//by status: 0 - online -> offline; 1 - vice versa.
            Collections.sort(players, new Comparator<SharedPlayerData>() {

                @Override
                public int compare(SharedPlayerData player1, SharedPlayerData player2) {
                    if (mode == 0)
                        return OxygenHelperClient.getPlayerStatus(player1).ordinal() - OxygenHelperClient.getPlayerStatus(player2).ordinal();
                    else
                        return OxygenHelperClient.getPlayerStatus(player2).ordinal() - OxygenHelperClient.getPlayerStatus(player1).ordinal();
                }
            });
        } else if (mode == 2 || mode == 3) {//by username: 2 - A -> z; 3 - vice versa.
            Collections.sort(players, new Comparator<SharedPlayerData>() {

                @Override
                public int compare(SharedPlayerData player1, SharedPlayerData player2) {
                    if (mode == 2)
                        return player1.getUsername().compareTo(player2.getUsername());
                    else
                        return player2.getUsername().compareTo(player1.getUsername());
                }
            });
        }

        this.playersPanel.reset();
        PlayerListEntryGUIButton button;
        for (SharedPlayerData sharedData : players) {            
            button = new PlayerListEntryGUIButton(sharedData);
            button.enableDynamicBackground(GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getDisabledElementColor(), GUISettings.instance().getHoveredElementColor());
            button.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            this.playersPanel.addButton(button);
        }

        this.playersPanel.getScroller().resetPosition();
        this.playersPanel.getScroller().getSlider().reset();

        this.playersOnlineTextLabel.setDisplayText(this.players.size() + " / " + OxygenHelperClient.getMaxPlayers());
        this.playersOnlineTextLabel.setX(this.getWidth() - 4 - this.textWidth(this.playersOnlineTextLabel.getDisplayText(), GUISettings.instance().getSubTextScale()));

        this.sortUpStatusButton.toggle();
        this.sortDownStatusButton.setToggled(false);
        this.sortDownUsernameButton.setToggled(false);
        this.sortUpUsernameButton.setToggled(false);
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (element == this.searchButton)
            this.searchField.enableFull();
        else if (element == this.refreshButton)
            this.sortPlayers(0);
        else if (element == this.sortDownStatusButton) {
            if (!this.sortDownStatusButton.isToggled()) {
                this.sortPlayers(1);
                this.sortUpStatusButton.setToggled(false);
                this.sortDownStatusButton.toggle(); 

                this.sortDownUsernameButton.setToggled(false);
                this.sortUpUsernameButton.setToggled(false);
            }
        } else if (element == this.sortUpStatusButton) {
            if (!this.sortUpStatusButton.isToggled()) {
                this.sortPlayers(0);
                this.sortDownStatusButton.setToggled(false);
                this.sortUpStatusButton.toggle();

                this.sortDownUsernameButton.setToggled(false);
                this.sortUpUsernameButton.setToggled(false);
            }
        } else if (element == this.sortDownUsernameButton) {
            if (!this.sortDownUsernameButton.isToggled()) {
                this.sortPlayers(3);
                this.sortUpUsernameButton.setToggled(false);
                this.sortDownUsernameButton.toggle(); 

                this.sortDownStatusButton.setToggled(false);
                this.sortUpStatusButton.setToggled(false);
            }
        } else if (element == this.sortUpUsernameButton) {
            if (!this.sortUpUsernameButton.isToggled()) {
                this.sortPlayers(2);
                this.sortDownUsernameButton.setToggled(false);
                this.sortUpUsernameButton.toggle();

                this.sortDownStatusButton.setToggled(false);
                this.sortUpStatusButton.setToggled(false);
            }
        } else if (element instanceof StatusGUIDropDownElement) {
            StatusGUIDropDownElement profileButton = (StatusGUIDropDownElement) element;
            if (profileButton.status != this.currentStatus) {
                OxygenManagerClient.instance().getFriendListManager().changeStatusSynced(profileButton.status);
                this.currentStatus = profileButton.status;
                this.statusImageLabel.setTextureUV(profileButton.status.ordinal() * 3, 0);
                OxygenHelperClient.getSharedClientPlayerData().getData(OxygenMain.STATUS_DATA_ID).put(0, (byte) profileButton.status.ordinal());
                this.sortPlayers(0);
            }
        } else if (element instanceof PlayerListEntryGUIButton) {
            PlayerListEntryGUIButton entry = (PlayerListEntryGUIButton) element;
            if (entry != this.currentEntry)
                this.currentEntry = entry;
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.searchField.isEnabled() && !this.searchField.isHovered())
            this.searchField.disableFull();
        return super.mouseClicked(mouseX, mouseY, mouseButton);              
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {   
        if (keyCode == OxygenKeyHandler.PLAYERS_LIST.getKeyBinding().getKeyCode() && !this.searchField.isDragged() && !this.hasCurrentCallback())
            this.screen.close();
        return super.keyTyped(typedChar, keyCode); 
    }

    public PlayerListEntryGUIButton getCurrentEntry() {
        return this.currentEntry;
    }
}
