package austeretony.oxygen.client.gui.friends;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.contextmenu.GUIContextAction;
import austeretony.alternateui.screen.contextmenu.GUIContextMenu;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.image.GUIImageLabel;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.screen.panel.GUIButtonPanel.GUIEnumOrientation;
import austeretony.alternateui.screen.text.GUITextField;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.PlayerListEntryGUIButton;
import austeretony.oxygen.client.gui.friends.ignored.AddIgnoredGUICallback;
import austeretony.oxygen.client.gui.friends.ignored.EditNoteContextAction;
import austeretony.oxygen.client.gui.friends.ignored.EditNoteGUICallback;
import austeretony.oxygen.client.gui.friends.ignored.RemoveIgnoredContextAction;
import austeretony.oxygen.client.gui.friends.ignored.RemoveIgnoredGUICallback;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.handler.OxygenKeyHandler;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.OxygenPlayerData;
import net.minecraft.client.resources.I18n;

public class IgnoredGUISection extends AbstractGUISection {

    private FriendsListGUIScreen screen;

    private GUIButton friendsPageButton, searchButton, refreshButton, addIgnoredButton, 
    sortDownStatusButton, sortUpStatusButton, sortDownUsernameButton, sortUpUsernameButton;

    private PlayerListEntryGUIButton currentEntry;

    private GUITextLabel ignoredAmountTextLabel;

    private GUITextField searchField;

    private GUIButtonPanel ignoredPanel;

    private AbstractGUICallback addIgnoredCallback, removeCallback, editNoteCallback;

    private final Set<FriendListEntry> ignored = new HashSet<FriendListEntry>();

    public IgnoredGUISection(FriendsListGUIScreen screen) {
        super(screen);
        this.screen = screen;
    }

    @Override
    protected void init() {
        this.addElement(new GUIImageLabel(- 1, - 1, this.getWidth() + 2, this.getHeight() + 2).enableStaticBackground(GUISettings.instance().getBaseGUIBackgroundColor()));//main background
        this.addElement(new GUIImageLabel(0, 0, this.getWidth(), 15).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//title background
        this.addElement(new GUIImageLabel(0, 17, 76, 9).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//search background
        this.addElement(new GUIImageLabel(77, 17, this.getWidth() - 77, 9).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//online background
        this.addElement(new GUIImageLabel(0, 27, this.getWidth(), 10).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//sorters background
        this.addElement(new GUIImageLabel(0, 38, this.getWidth() - 3, this.getHeight() - 52).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//list background
        this.addElement(new GUIImageLabel(this.getWidth() - 2, 38, 2, this.getHeight() - 52).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//slider background
        this.addElement(new GUIImageLabel(0, this.getHeight() - 12, this.getWidth(), 12).enableStaticBackground(GUISettings.instance().getAdditionalGUIBackgroundColor()));//slider background
        this.addElement(new GUITextLabel(2, 4).setDisplayText(I18n.format("oxygen.gui.ignored.title"), false, GUISettings.instance().getTitleScale()));

        this.addElement(this.friendsPageButton = new GUIButton(this.getWidth() - 30, 1,  14, 14).setTexture(FriendsListGUIScreen.FRIENDS_ICONS, 14, 14).initSimpleTooltip(I18n.format("oxygen.gui.friends.tooltip.friends"), GUISettings.instance().getTooltipScale())); 
        this.addElement(new GUIButton(this.getWidth() - 15, 1,  14, 14).setTexture(FriendsListGUIScreen.IGNORED_ICONS, 14, 14).initSimpleTooltip(I18n.format("oxygen.gui.friends.tooltip.ignored"), GUISettings.instance().getTooltipScale()).toggle()); 

        this.addElement(this.searchButton = new GUIButton(4, 18, 7, 7).setTexture(FriendsListGUIScreen.SEARCH_ICONS, 7, 7).initSimpleTooltip(I18n.format("oxygen.tooltip.search"), GUISettings.instance().getTooltipScale()));         
        this.addElement(this.refreshButton = new GUIButton(79, 17, 10, 10).setTexture(FriendsListGUIScreen.REFRESH_ICONS, 9, 9).initSimpleTooltip(I18n.format("oxygen.tooltip.refresh"), GUISettings.instance().getTooltipScale()));         

        this.addElement(this.sortDownStatusButton = new GUIButton(7, 32, 3, 3).setTexture(FriendsListGUIScreen.SORT_DOWN_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortUpStatusButton = new GUIButton(7, 28, 3, 3).setTexture(FriendsListGUIScreen.SORT_UP_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortDownUsernameButton = new GUIButton(19, 32, 3, 3).setTexture(FriendsListGUIScreen.SORT_DOWN_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(this.sortUpUsernameButton = new GUIButton(19, 28, 3, 3).setTexture(FriendsListGUIScreen.SORT_UP_ICONS, 3, 3).initSimpleTooltip(I18n.format("oxygen.tooltip.sort"), GUISettings.instance().getTooltipScale())); 
        this.addElement(new GUITextLabel(24, 28).setDisplayText(I18n.format("oxygen.gui.friends.username")).setTextScale(GUISettings.instance().getTextScale())); 
        this.addElement(new GUITextLabel(110, 28).setDisplayText(I18n.format("oxygen.gui.friends.dimension")).setTextScale(GUISettings.instance().getTextScale())); 

        this.ignoredPanel = new GUIButtonPanel(GUIEnumOrientation.VERTICAL, 0, 38, this.getWidth() - 3, 10).setButtonsOffset(1).setTextScale(GUISettings.instance().getTextScale());
        this.addElement(this.ignoredPanel);
        this.addElement(this.searchField = new GUITextField(0, 18, 100, 20).setScale(0.7F).enableDynamicBackground().setDisplayText("...", false, GUISettings.instance().getTextScale()).disableFull().cancelDraggedElementLogic());
        this.ignoredPanel.initSearchField(this.searchField);
        GUIScroller panelScroller = new GUIScroller(OxygenConfig.MAX_FRIENDS.getIntValue(), 14);
        this.ignoredPanel.initScroller(panelScroller);
        GUISlider panelSlider = new GUISlider(this.getWidth() - 2, 38, 2, this.getHeight() - 52);
        panelScroller.initSlider(panelSlider);    

        GUIContextMenu menu = new GUIContextMenu(GUISettings.instance().getContextMenuWidth(), 10).setScale(GUISettings.instance().getContextMenuScale()).setTextScale(GUISettings.instance().getTextScale()).setTextAlignment(EnumGUIAlignment.LEFT, 2);
        this.ignoredPanel.initContextMenu(menu);
        menu.enableDynamicBackground(GUISettings.instance().getEnabledContextActionColor(), GUISettings.instance().getDisabledContextActionColor(), GUISettings.instance().getHoveredContextActionColor());
        menu.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
        menu.addElement(new GUIContextAction(new RemoveIgnoredContextAction(this)));
        menu.addElement(new GUIContextAction(new EditNoteContextAction(this)));

        this.addElement(this.addIgnoredButton = new GUIButton(4, this.getHeight() - 11,  40, 10).enableDynamicBackground(GUISettings.instance().getEnabledButtonColor(), GUISettings.instance().getDisabledButtonColor(), GUISettings.instance().getHoveredButtonColor()).setDisplayText(I18n.format("oxygen.friends.addButton"), true, GUISettings.instance().getButtonTextScale()));     
        this.lockAddButton();
        this.addElement(this.ignoredAmountTextLabel = new GUITextLabel(0, this.getHeight() - 9).setTextScale(GUISettings.instance().getSubTextScale())); 

        this.sortPlayers(0);

        this.addIgnoredCallback = new AddIgnoredGUICallback(this.screen, this, 140, 68).enableDefaultBackground();
        this.removeCallback = new RemoveIgnoredGUICallback(this.screen, this, 180, 40).enableDefaultBackground();
        this.editNoteCallback = new EditNoteGUICallback(this.screen, this, 140, 50).enableDefaultBackground();
    }

    public void sortPlayers(int mode) {
        this.ignored.clear();
        for (FriendListEntry listEntry : OxygenHelperClient.getPlayerData().getFriendListEntries())
            if (listEntry.ignored)
                this.ignored.add(listEntry);

        List<FriendListEntry> players = new ArrayList<FriendListEntry>(this.ignored);
        if (mode == 0 || mode == 1) {//by status, starting from /online/
            Collections.sort(players, new Comparator<FriendListEntry>() {

                @Override
                public int compare(FriendListEntry entry1, FriendListEntry entry2) {
                    OxygenPlayerData.EnumStatus 
                    entry1Status = OxygenPlayerData.EnumStatus.OFFLINE,
                    entry2Status = OxygenPlayerData.EnumStatus.OFFLINE;
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
        } else if (mode == 2 || mode == 3) {//by username, A-z
            Collections.sort(players, new Comparator<FriendListEntry>() {

                @Override
                public int compare(FriendListEntry entry1, FriendListEntry entry2) {
                    if (mode == 2)
                        return entry1.username.compareTo(entry2.username);
                    else
                        return entry2.username.compareTo(entry1.username);
                }
            });
        }

        this.ignoredPanel.reset();
        PlayerListEntryGUIButton button;
        int onlinePlayers = 0;
        OxygenPlayerData.EnumStatus status = OxygenPlayerData.EnumStatus.OFFLINE;
        for (FriendListEntry playerList : players) {            
            if (OxygenHelperClient.isOnline(playerList.playerUUID)) {
                status = OxygenHelperClient.getPlayerStatus(playerList.playerUUID);
                if (status == OxygenPlayerData.EnumStatus.ONLINE)
                    onlinePlayers++;
            }
            button = new PlayerListEntryGUIButton(playerList, status);
            button.enableDynamicBackground(GUISettings.instance().getEnabledElementColor(), GUISettings.instance().getDisabledElementColor(), GUISettings.instance().getHoveredElementColor());
            button.setTextDynamicColor(GUISettings.instance().getEnabledTextColor(), GUISettings.instance().getDisabledTextColor(), GUISettings.instance().getHoveredTextColor());
            this.ignoredPanel.addButton(button);
        }

        this.ignoredPanel.getScroller().resetPosition();
        this.ignoredPanel.getScroller().getSlider().reset();

        this.ignoredAmountTextLabel.setDisplayText(this.ignored.size() + " / " + OxygenConfig.MAX_FRIENDS.getIntValue());
        this.ignoredAmountTextLabel.setX(this.getWidth() - 4 - this.width(this.ignoredAmountTextLabel.getDisplayText(), GUISettings.instance().getSubTextScale()));

        this.sortUpStatusButton.toggle();
        this.sortDownStatusButton.setToggled(false);
        this.sortDownUsernameButton.setToggled(false);
        this.sortUpUsernameButton.setToggled(false);
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {
        if (element == this.friendsPageButton)
            this.screen.getFriendsSection().open();
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
                this.sortPlayers(2);
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
        } else if (element == this.addIgnoredButton)
            this.addIgnoredCallback.open();
        else if (element instanceof PlayerListEntryGUIButton) {
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
        if (keyCode == OxygenKeyHandler.FRIENDS_LIST.getKeyBinding().getKeyCode() && !this.searchField.isDragged() && !this.hasCurrentCallback())
            this.screen.close();
        return super.keyTyped(typedChar, keyCode); 
    }

    public PlayerListEntryGUIButton getCurrentEntry() {
        return this.currentEntry;
    }

    public void openRemoveCallback() {
        this.removeCallback.open();
    }

    public void openEditNoteCallback() {
        this.editNoteCallback.open();
    }

    public void lockAddButton() {
        if (OxygenManagerClient.instance().getPlayerData().getIgnoredAmount() >= OxygenConfig.MAX_IGNORED.getIntValue())
            this.addIgnoredButton.disable();
    }

    public void unlockAddButton() {
        if (OxygenManagerClient.instance().getPlayerData().getIgnoredAmount() < OxygenConfig.MAX_IGNORED.getIntValue())
            this.addIgnoredButton.enable();
    }
}
