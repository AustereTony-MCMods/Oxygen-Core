package austeretony.oxygen.client.gui.friendlist;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.button.GUICheckBoxButton;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
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
import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.client.gui.StatusGUIDropDownElement;
import austeretony.oxygen.client.gui.friendlist.friendlist.FriendListBackgroundGUIFiller;
import austeretony.oxygen.client.gui.friendlist.friendlist.callback.AddFriendGUICallback;
import austeretony.oxygen.client.gui.friendlist.friendlist.callback.DownloadDataGUICallback;
import austeretony.oxygen.client.gui.friendlist.friendlist.callback.EditNoteGUICallback;
import austeretony.oxygen.client.gui.friendlist.friendlist.callback.IgnoreFriendGUICallback;
import austeretony.oxygen.client.gui.friendlist.friendlist.callback.RemoveFriendGUICallback;
import austeretony.oxygen.client.gui.friendlist.friendlist.context.EditNoteContextAction;
import austeretony.oxygen.client.gui.friendlist.friendlist.context.IgnoreContextAction;
import austeretony.oxygen.client.gui.friendlist.friendlist.context.RemoveFriendContextAction;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import austeretony.oxygen.common.util.MathUtils;
import net.minecraft.client.resources.I18n;

public class FriendListGUISection extends AbstractGUISection {

    private final FriendListGUIScreen screen;

    private GUIButton ignoredPageButton, downloadButton, searchButton, refreshButton, addFriendButton, 
    sortDownStatusButton, sortUpStatusButton, sortDownUsernameButton, sortUpUsernameButton;

    private GUICheckBoxButton autoAcceptButton;

    private FriendListEntryGUIButton currentEntry;

    private GUITextLabel friendsOnlineTextLabel, friendsAmountTextLabel, playerNameTextLabel, autoAcceptTextLabel;

    private GUITextField searchField;

    private GUIButtonPanel friendsPanel;

    private GUIDropDownList statusDropDownList;

    private OxygenPlayerData.EnumActivityStatus currentStatus;

    private GUIImageLabel statusImageLabel;

    private AbstractGUICallback downloadCallback, addFriendCallback, removeCallback, ignoreCallback, editNoteCallback;

    private final Set<FriendListEntry> friends = new HashSet<FriendListEntry>();

    public FriendListGUISection(FriendListGUIScreen screen) {
        super(screen);
        this.screen = screen;
    }

    @Override
    public void init() {
        this.addElement(new FriendListBackgroundGUIFiller(0, 0, this.getWidth(), this.getHeight()));
        String title = I18n.format("oxygen.gui.friends.title");
        this.addElement(new GUITextLabel(2, 4).setDisplayText(title, false, GUISettings.instance().getTitleScale()));
        this.addElement(this.downloadButton = new GUIButton(this.textWidth(title, GUISettings.instance().getTitleScale()) + 4, 4, 8, 8).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.DOWNLOAD_ICONS, 8, 8).initSimpleTooltip(I18n.format("oxygen.tooltip.download"), GUISettings.instance().getTooltipScale()));

        this.addElement(new GUIButton(this.getWidth() - 30, 0, 12, 12).setTexture(OxygenGUITextures.FRIENDS_ICONS, 12, 12).initSimpleTooltip(I18n.format("oxygen.gui.friends.tooltip.friends"), GUISettings.instance().getTooltipScale()).toggle()); 
        this.addElement(this.ignoredPageButton = new GUIButton(this.getWidth() - 15, 0, 12, 12).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.IGNORED_ICONS, 12, 12).initSimpleTooltip(I18n.format("oxygen.gui.friends.tooltip.ignored"), GUISettings.instance().getTooltipScale())); 

        this.addElement(this.searchButton = new GUIButton(4, 15, 7, 7).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.SEARCH_ICONS, 7, 7).initSimpleTooltip(I18n.format("oxygen.tooltip.search"), GUISettings.instance().getTooltipScale()));         
        this.addElement(this.refreshButton = new GUIButton(79, 14, 10, 10).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.REFRESH_ICONS, 9, 9).initSimpleTooltip(I18n.format("oxygen.tooltip.refresh"), GUISettings.instance().getTooltipScale()));         
        this.addElement(this.playerNameTextLabel = new GUITextLabel(91, 15).setDisplayText(OxygenHelperClient.getSharedClientPlayerData().getUsername(), false, GUISettings.instance().getSubTextScale()));
        this.addElement(this.friendsOnlineTextLabel = new GUITextLabel(0, 15).setTextScale(GUISettings.instance().getSubTextScale()).initSimpleTooltip(I18n.format("oxygen.tooltip.online"), GUISettings.instance().getTooltipScale())); 

        this.addElement(this.sortDownStatusButton = new GUIButton(7, 29, 3, 3).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.SORT_DOWN_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortUpStatusButton = new GUIButton(7, 25, 3, 3).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.SORT_UP_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortDownUsernameButton = new GUIButton(19, 29, 3, 3).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.SORT_DOWN_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortUpUsernameButton = new GUIButton(19, 25, 3, 3).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent).setTexture(OxygenGUITextures.SORT_UP_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(new GUITextLabel(24, 25).setDisplayText(I18n.format("oxygen.gui.friends.username")).setTextScale(GUISettings.instance().getTextScale())); 
        this.addElement(new GUITextLabel(110, 25).setDisplayText(I18n.format("oxygen.gui.friends.dimension")).setTextScale(GUISettings.instance().getTextScale())); 

        this.friendsPanel = new GUIButtonPanel(GUIEnumOrientation.VERTICAL, 0, 35, this.getWidth() - 3, 10).setButtonsOffset(1).setTextScale(GUISettings.instance().getPanelTextScale());
        this.addElement(this.friendsPanel);
        this.addElement(this.searchField = new GUITextField(0, 15, 100, 20).setScale(0.7F).enableDynamicBackground().setDisplayText("...", false, GUISettings.instance().getTextScale()).disableFull().cancelDraggedElementLogic());
        this.friendsPanel.initSearchField(this.searchField);
        GUIScroller scroller = new GUIScroller(MathUtils.clamp(OxygenConfig.MAX_FRIENDS.getIntValue(), 14, 100), 14);
        this.friendsPanel.initScroller(scroller);
        GUISlider slider = new GUISlider(this.getWidth() - 2, 35, 2, this.getHeight() - 49);
        slider.setDynamicBackgroundColor(GUISettings.instance().getEnabledSliderColor(), GUISettings.instance().getDisabledSliderColor(), GUISettings.instance().getHoveredSliderColor());
        scroller.initSlider(slider);    

        GUIContextMenu menu = new GUIContextMenu(GUISettings.instance().getContextMenuWidth(), 10).setScale(GUISettings.instance().getContextMenuScale()).setTextScale(GUISettings.instance().getTextScale()).setTextAlignment(EnumGUIAlignment.LEFT, 2);
        menu.setOpenSound(OxygenSoundEffects.CONTEXT_OPEN.soundEvent);
        menu.setCloseSound(OxygenSoundEffects.CONTEXT_CLOSE.soundEvent);
        this.friendsPanel.initContextMenu(menu);
        menu.enableDynamicBackground(GUISettings.instance().getEnabledContextActionColor(), GUISettings.instance().getDisabledContextActionColor(), GUISettings.instance().getHoveredContextActionColor());
        menu.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
        menu.addElement(new RemoveFriendContextAction(this));
        menu.addElement(new IgnoreContextAction(this));
        menu.addElement(new EditNoteContextAction(this));

        //Support
        for (AbstractContextAction action : OxygenGUIHelper.getContextActions(OxygenMain.FRIEND_LIST_SCREEN_ID))
            menu.addElement(action);

        this.addElement(this.addFriendButton = new GUIButton(4, this.getHeight() - 11, 40, 10).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent)
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor())
                .setDisplayText(I18n.format("oxygen.gui.addButton"), true, GUISettings.instance().getButtonTextScale()));     
        this.lockAddButton();
        this.addElement(this.friendsAmountTextLabel = new GUITextLabel(0, this.getHeight() - 10).setTextScale(GUISettings.instance().getSubTextScale())); 

        this.addElement(this.autoAcceptButton = new GUICheckBoxButton(48, this.getHeight() - 9, 6).setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent)
                .enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor()));
        this.addElement(this.autoAcceptTextLabel = new GUITextLabel(56, this.getHeight() - 10).setDisplayText(I18n.format("oxygen.gui.friends.autoAccept"), false, GUISettings.instance().getSubTextScale()));
        this.autoAcceptButton.setToggled(OxygenHelperClient.getClientSettingBoolean(OxygenMain.FRIEND_REQUESTS_AUTO_ACCEPT_SETTING));

        //Protection
        if (!OxygenGUIHelper.isNeedSync(OxygenMain.FRIEND_LIST_SCREEN_ID) || OxygenGUIHelper.isDataRecieved(OxygenMain.FRIEND_LIST_SCREEN_ID))
            this.sortPlayers(0);

        this.currentStatus = OxygenHelperClient.getClientPlayerStatus();
        int statusOffset = this.playerNameTextLabel.getX() + this.textWidth(this.playerNameTextLabel.getDisplayText(), GUISettings.instance().getSubTextScale());
        this.addElement(this.statusImageLabel = new GUIImageLabel(statusOffset + 4, 17).setTexture(OxygenGUITextures.STATUS_ICONS, 3, 3, this.currentStatus.ordinal() * 3, 0, 12, 3));   
        this.statusDropDownList = new GUIDropDownList(statusOffset + 10, 16, GUISettings.instance().getDropDownListWidth(), 10).setScale(GUISettings.instance().getDropDownListScale()).setDisplayText(this.currentStatus.localizedName()).setTextScale(GUISettings.instance().getTextScale()).setTextAlignment(EnumGUIAlignment.LEFT, 1);
        this.statusDropDownList.setOpenSound(OxygenSoundEffects.DROP_DOWN_LIST_OPEN.soundEvent);
        this.statusDropDownList.setCloseSound(OxygenSoundEffects.CONTEXT_CLOSE.soundEvent);
        StatusGUIDropDownElement profileElement;
        for (OxygenPlayerData.EnumActivityStatus status : OxygenPlayerData.EnumActivityStatus.values()) {
            profileElement = new StatusGUIDropDownElement(status);
            profileElement.setDisplayText(status.localizedName());
            profileElement.enableDynamicBackground(GUISettings.instance().getEnabledContextActionColor(), GUISettings.instance().getDisabledContextActionColor(), GUISettings.instance().getHoveredContextActionColor());
            profileElement.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            this.statusDropDownList.addElement(profileElement);
        }
        this.addElement(this.statusDropDownList);   

        this.downloadCallback = new DownloadDataGUICallback(this.screen, this, 140, 40).enableDefaultBackground();
        this.addFriendCallback = new AddFriendGUICallback(this.screen, this, 140, 68).enableDefaultBackground();
        this.removeCallback = new RemoveFriendGUICallback(this.screen, this, 140, 48).enableDefaultBackground();
        this.ignoreCallback = new IgnoreFriendGUICallback(this.screen, this, 140, 48).enableDefaultBackground();
        this.editNoteCallback = new EditNoteGUICallback(this.screen, this, 140, 50).enableDefaultBackground();          
    }

    public void sortPlayers(int mode) {
        this.friends.clear();
        for (FriendListEntry listEntry : OxygenHelperClient.getPlayerData().getFriendListEntries())
            if (!listEntry.ignored)
                this.friends.add(listEntry);

        List<FriendListEntry> players = new ArrayList<FriendListEntry>(this.friends);
        if (mode == 0 || mode == 1) {//by status: 0 - online -> offline; 1 - vice versa.
            Collections.sort(players, new Comparator<FriendListEntry>() {

                @Override
                public int compare(FriendListEntry entry1, FriendListEntry entry2) {
                    OxygenPlayerData.EnumActivityStatus 
                    entry1Status = OxygenPlayerData.EnumActivityStatus.OFFLINE,
                    entry2Status = OxygenPlayerData.EnumActivityStatus.OFFLINE;
                    if (OxygenHelperClient.isOnline(entry1.playerUUID))
                        entry1Status = OxygenHelperClient.getPlayerStatus(entry1.playerUUID);
                    if (OxygenHelperClient.isOnline(entry2.playerUUID))
                        entry2Status = OxygenHelperClient.getPlayerStatus(entry2.playerUUID);
                    if (mode == 0)
                        return entry1Status.ordinal() - entry2Status.ordinal();
                    else
                        return entry2Status.ordinal() - entry1Status.ordinal();
                }
            });
        } else if (mode == 2 || mode == 3) {//by username: 2 - A -> z; 3 - vice versa.
            Collections.sort(players, new Comparator<FriendListEntry>() {

                @Override
                public int compare(FriendListEntry entry1, FriendListEntry entry2) {
                    String 
                    username1 = OxygenHelperClient.getObservedSharedData(entry1.playerUUID).getUsername(), 
                    username2 = OxygenHelperClient.getObservedSharedData(entry2.playerUUID).getUsername();
                    if (mode == 2)
                        return username1.compareTo(username2);
                    else
                        return username2.compareTo(username1);
                }
            });
        }

        this.friendsPanel.reset();
        FriendListEntryGUIButton button;
        int onlinePlayers = 0;
        OxygenPlayerData.EnumActivityStatus status;
        for (FriendListEntry entry : players) {     
            status = OxygenPlayerData.EnumActivityStatus.OFFLINE;
            if (OxygenHelperClient.isOnline(entry.playerUUID)) {
                status = OxygenHelperClient.getPlayerStatus(entry.playerUUID);
                if (status != OxygenPlayerData.EnumActivityStatus.OFFLINE)
                    onlinePlayers++;
            }
            button = new FriendListEntryGUIButton(entry, status);
            button.enableDynamicBackground(GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getDisabledElementColor(), GUISettings.instance().getHoveredElementColor());
            button.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            this.friendsPanel.addButton(button);
        }

        this.friendsPanel.getScroller().resetPosition();
        this.friendsPanel.getScroller().getSlider().reset();

        this.friendsOnlineTextLabel.setDisplayText(onlinePlayers + " / " + this.friends.size());
        this.friendsOnlineTextLabel.setX(this.getWidth() - 4 - this.textWidth(this.friendsOnlineTextLabel.getDisplayText(), GUISettings.instance().getSubTextScale()));
        this.friendsAmountTextLabel.setDisplayText(this.friends.size() + " / " + OxygenConfig.MAX_FRIENDS.getIntValue());
        this.friendsAmountTextLabel.setX(this.getWidth() - 4 - this.textWidth(this.friendsAmountTextLabel.getDisplayText(), GUISettings.instance().getSubTextScale()));

        this.sortUpStatusButton.toggle();
        this.sortDownStatusButton.setToggled(false);
        this.sortDownUsernameButton.setToggled(false);
        this.sortUpUsernameButton.setToggled(false);
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (element == this.ignoredPageButton)
            this.screen.getIgnoreListSection().open();
        else if (element == this.downloadButton)
            this.downloadCallback.open();
        else if (element == this.searchButton)
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
        } else if (element == this.addFriendButton)
            this.addFriendCallback.open();
        else if (element instanceof StatusGUIDropDownElement) {
            StatusGUIDropDownElement profileButton = (StatusGUIDropDownElement) element;
            if (profileButton.status != this.currentStatus) {
                OxygenManagerClient.instance().getFriendListManager().changeStatusSynced(profileButton.status);
                this.currentStatus = profileButton.status;
                this.statusImageLabel.setTextureUV(this.currentStatus.ordinal() * 3, 0);
            }
        } else if (element instanceof FriendListEntryGUIButton) {
            FriendListEntryGUIButton entry = (FriendListEntryGUIButton) element;
            if (entry != this.currentEntry)
                this.currentEntry = entry;
        } else if (element == this.autoAcceptButton) {
            if (this.autoAcceptButton.isToggled()) {
                OxygenHelperClient.setClientSetting(OxygenMain.FRIEND_REQUESTS_AUTO_ACCEPT_SETTING, true);
                OxygenHelperClient.saveClientSettings();
            } else {
                OxygenHelperClient.setClientSetting(OxygenMain.FRIEND_REQUESTS_AUTO_ACCEPT_SETTING, false);
                OxygenHelperClient.saveClientSettings();
            }
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
        if (keyCode == OxygenKeyHandler.FRIENDS_LIST.getKeyBinding().getKeyCode() && !this.searchField.isDragged() && !this.hasCurrentCallback())
            this.screen.close();
        return super.keyTyped(typedChar, keyCode); 
    }

    public FriendListEntryGUIButton getCurrentEntry() {
        return this.currentEntry;
    }

    public void openRemoveCallback() {
        this.removeCallback.open();
    }

    public void openIgnoreFriendCallback() {
        this.ignoreCallback.open();
    }

    public void openEditNoteCallback() {
        this.editNoteCallback.open();
    }

    public void lockAddButton() {
        if (OxygenManagerClient.instance().getPlayerData().getFriendsAmount() >= OxygenConfig.MAX_FRIENDS.getIntValue())
            this.addFriendButton.disable();
    }

    public void unlockAddButton() {
        if (OxygenManagerClient.instance().getPlayerData().getFriendsAmount() < OxygenConfig.MAX_FRIENDS.getIntValue())
            this.addFriendButton.enable();
    }
}
