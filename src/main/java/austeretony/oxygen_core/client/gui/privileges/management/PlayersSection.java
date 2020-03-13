package austeretony.oxygen_core.client.gui.privileges.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu;
import austeretony.oxygen_core.client.gui.elements.OxygenDefaultBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenSectionSwitcher;
import austeretony.oxygen_core.client.gui.elements.OxygenSorter;
import austeretony.oxygen_core.client.gui.elements.OxygenSorter.EnumSorting;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.management.players.PlayerPanelEntry;
import austeretony.oxygen_core.client.gui.privileges.management.players.callback.AddPlayerRoleCallback;
import austeretony.oxygen_core.client.gui.privileges.management.players.callback.RemovePlayerRoleCallback;
import austeretony.oxygen_core.client.gui.privileges.management.players.context.AddRoleContextAction;
import austeretony.oxygen_core.client.gui.privileges.management.players.context.RemoveRoleContextAction;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.util.MathUtils;

public class PlayersSection extends AbstractGUISection {

    private final PrivilegesManagementScreen screen;

    private OxygenSorter statusSorter, usernameSorter;

    private OxygenScrollablePanel playersPanel;

    private AbstractGUICallback addPlayerRoleCallback, removePlayerRoleCallback;

    //cache

    private PlayerPanelEntry currPlayerEntry;

    public PlayersSection(PrivilegesManagementScreen screen) {
        super(screen);
        this.screen = screen;
        this.setDisplayText(ClientReference.localize("oxygen_core.gui.privileges.management.players"));
    }

    @Override
    public void init() {
        this.addPlayerRoleCallback = new AddPlayerRoleCallback(this.screen, this, 140, 105).enableDefaultBackground();
        this.removePlayerRoleCallback = new RemovePlayerRoleCallback(this.screen, this, 140, 94).enableDefaultBackground();

        this.addElement(new OxygenDefaultBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.management.title"), EnumBaseGUISetting.TEXT_TITLE_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.statusSorter = new OxygenSorter(6, 18, EnumSorting.DOWN, ClientReference.localize("oxygen_core.gui.status")));   
        this.statusSorter.setSortingListener((sorting)->{
            this.usernameSorter.reset();
            this.sortPlayers(sorting == EnumSorting.DOWN ? 0 : 1);
        });

        this.addElement(this.usernameSorter = new OxygenSorter(12, 18, EnumSorting.INACTIVE, ClientReference.localize("oxygen_core.gui.username")));  
        this.usernameSorter.setSortingListener((sorting)->{
            this.statusSorter.reset();
            this.sortPlayers(sorting == EnumSorting.DOWN ? 2 : 3);
        });

        this.addElement(this.playersPanel = new OxygenScrollablePanel(this.screen, 6, 24, this.getWidth() - 15, 10, 1, 140, 14, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));
        this.playersPanel.<PlayerPanelEntry>setElementClickListener((previous, clicked, mouseX, mouseY, mouseButton)->this.currPlayerEntry = clicked);

        this.playersPanel.initContextMenu(new OxygenContextMenu(
                new AddRoleContextAction(),
                new RemoveRoleContextAction()));

        this.addElement(new OxygenSectionSwitcher(this.getWidth() - 4, 4, this, this.screen.getDefaultPrivilegesSection(), this.screen.getRolesSection()));
    }

    private void sortPlayers(int mode) {
        List<PlayerSharedData> players = new ArrayList<>(OxygenHelperClient.getPlayersSharedData());
        for (PlayerSharedData sharedData : OxygenManagerClient.instance().getPrivilegesContainer().getServerPlayersData()) 
            if (!players.contains(sharedData))
                players.add(sharedData);

        if (mode == 0)
            Collections.sort(players, (s1, s2)->OxygenHelperClient.getPlayerActivityStatus(s1).ordinal() - OxygenHelperClient.getPlayerActivityStatus(s2).ordinal());
        else if (mode == 1)
            Collections.sort(players, (s1, s2)->OxygenHelperClient.getPlayerActivityStatus(s2).ordinal() - OxygenHelperClient.getPlayerActivityStatus(s1).ordinal());
        else if (mode == 2)
            Collections.sort(players, (s1, s2)->s1.getUsername().compareTo(s2.getUsername()));
        else if (mode == 3)
            Collections.sort(players, (s1, s2)->s2.getUsername().compareTo(s1.getUsername()));

        this.playersPanel.reset();
        for (PlayerSharedData sharedData : players)
            this.playersPanel.addEntry(new PlayerPanelEntry(sharedData));

        this.playersPanel.getScroller().reset();
        this.playersPanel.getScroller().updateRowsAmount(MathUtils.clamp(players.size(), 14, MathUtils.greaterOfTwo(players.size(), 140)));
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {}

    public void privilegesDataReceived() {
        this.sortPlayers(0);
    }

    public void playerRolesChanged(int roleId, PlayerSharedData sharedData) {
        this.statusSorter.setSorting(EnumSorting.DOWN);
        this.usernameSorter.reset();
        this.sortPlayers(0);
    }

    public PlayerPanelEntry getCurrentPlayerEntry() {
        return this.currPlayerEntry;
    }

    public void openAddRoleCallback() {
        this.addPlayerRoleCallback.open();
    }

    public void openRemoveRoleCallback() {
        this.removePlayerRoleCallback.open();
    }
}
