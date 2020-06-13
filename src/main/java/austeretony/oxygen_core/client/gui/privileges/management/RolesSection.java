package austeretony.oxygen_core.client.gui.privileges.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu;
import austeretony.oxygen_core.client.gui.elements.OxygenDefaultBackgroundWithButtonsFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenKeyButton;
import austeretony.oxygen_core.client.gui.elements.OxygenPanelEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenSectionSwitcher;
import austeretony.oxygen_core.client.gui.elements.OxygenSorter;
import austeretony.oxygen_core.client.gui.elements.OxygenSorter.EnumSorting;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.management.players.PlayerPanelEntry;
import austeretony.oxygen_core.client.gui.privileges.management.roles.callback.AddPrivilegeCallback;
import austeretony.oxygen_core.client.gui.privileges.management.roles.callback.EditRoleCallback;
import austeretony.oxygen_core.client.gui.privileges.management.roles.callback.RemovePlayerCallback;
import austeretony.oxygen_core.client.gui.privileges.management.roles.callback.RemovePrivilegeCallback;
import austeretony.oxygen_core.client.gui.privileges.management.roles.callback.RemoveRoleCallback;
import austeretony.oxygen_core.client.gui.privileges.management.roles.callback.RoleCreationCallback;
import austeretony.oxygen_core.client.gui.privileges.management.roles.context.AddRolePrivilegeContextAction;
import austeretony.oxygen_core.client.gui.privileges.management.roles.context.EditRoleContextAction;
import austeretony.oxygen_core.client.gui.privileges.management.roles.context.RemoveRoleContextAction;
import austeretony.oxygen_core.client.gui.privileges.management.roles.context.RemoveRoleFromPlayerContextAction;
import austeretony.oxygen_core.client.gui.privileges.management.roles.context.RemoveRolePrivilegeContextAction;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.util.MathUtils;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.text.TextFormatting;

public class RolesSection extends AbstractGUISection {

    private final PrivilegesManagementScreen screen;

    private OxygenTextLabel rolesAmountLabel, currentRoleNameLabel, roleChatFormattingLabel, currentRoleChatFormattingLabel, rolePrivilegesLabel, rolePlayersLabel;

    private OxygenKeyButton createRoleButton;

    private OxygenSorter prioritySorter, nameSorter;

    private OxygenScrollablePanel rolesPanel, rolePrivilegesPanel, rolePlayersPanel;

    private AbstractGUICallback roleCreationCallback, editRoleCallback, removeRoleCallback, addPrivilegeCallback, removePrivilegeCallback, removePlayerCallback;

    //cache

    private OxygenPanelEntry<Role> currRoleEntry;

    private PrivilegePanelEntry currPrivilegeEntry;

    private PlayerPanelEntry currPlayerEntry;

    private final Multimap<Integer, PlayerSharedData> playersPerRole = HashMultimap.<Integer, PlayerSharedData>create();

    public RolesSection(PrivilegesManagementScreen screen) {
        super(screen);
        this.screen = screen;
        this.setDisplayText(ClientReference.localize("oxygen_core.gui.privileges.roles"));
    }

    @Override
    public void init() {
        this.roleCreationCallback = new RoleCreationCallback(this.screen, this, 140, 176).enableDefaultBackground();       
        this.editRoleCallback = new EditRoleCallback(this.screen, this, 140, 176).enableDefaultBackground();
        this.removeRoleCallback = new RemoveRoleCallback(this.screen, this, 140, 36).enableDefaultBackground();
        this.addPrivilegeCallback = new AddPrivilegeCallback(this.screen, this, 190, 153).enableDefaultBackground();
        this.removePrivilegeCallback = new RemovePrivilegeCallback(this.screen, this, 140, 36).enableDefaultBackground(); 
        this.removePlayerCallback = new RemovePlayerCallback(this.screen, this, 140, 36).enableDefaultBackground(); 

        this.addElement(new OxygenDefaultBackgroundWithButtonsFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.management.title"), EnumBaseGUISetting.TEXT_TITLE_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.rolesAmountLabel = new OxygenTextLabel(0, 22, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.prioritySorter = new OxygenSorter(6, 18, EnumSorting.DOWN, ClientReference.localize("oxygen_core.gui.priority")));   
        this.prioritySorter.setSortingListener((sorting)->{
            this.nameSorter.reset();
            this.sortRoles(sorting == EnumSorting.DOWN ? 0 : 1);
        });

        this.addElement(this.nameSorter = new OxygenSorter(12, 18, EnumSorting.INACTIVE, ClientReference.localize("oxygen_core.gui.name")));  
        this.nameSorter.setSortingListener((sorting)->{
            this.prioritySorter.reset();
            this.sortRoles(sorting == EnumSorting.DOWN ? 2 : 3);
        });

        this.addElement(this.rolesPanel = new OxygenScrollablePanel(this.screen, 6, 24, 80, 10, 1, 200, 14, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));

        this.rolesPanel.<OxygenPanelEntry<Role>>setElementClickListener((previous, clicked, mouseX, mouseY, mouseButton)->{
            if (previous != null)
                previous.setToggled(false);
            clicked.toggle();
            this.currRoleEntry = clicked;
            this.displayRoleInformation(clicked.getWrapped());
        });

        this.rolesPanel.initContextMenu(new OxygenContextMenu(
                new EditRoleContextAction(),
                new AddRolePrivilegeContextAction(),
                new RemoveRoleContextAction()));

        this.addElement(this.createRoleButton = new OxygenKeyButton(0, this.getY() + this.getHeight() + this.screen.guiTop - 8, ClientReference.localize("oxygen_core.gui.privileges.management.button.createRole"), Keyboard.KEY_E, this.roleCreationCallback::open));     

        this.addElement(new OxygenSectionSwitcher(this.getWidth() - 4, 4, this, this.screen.getDefaultPrivilegesSection(), this.screen.getPlayersSection()));

        this.addElement(this.currentRoleNameLabel = new OxygenTextLabel(92, 24, "", EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.roleChatFormattingLabel = new OxygenTextLabel(92, 33, ClientReference.localize("oxygen_core.gui.privileges.chatFormatting"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()).disableFull());
        this.addElement(this.currentRoleChatFormattingLabel = new OxygenTextLabel(92, 42, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.rolePrivilegesLabel = new OxygenTextLabel(92, 54, ClientReference.localize("oxygen_core.gui.privileges.privileges"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()).disableFull());
        this.addElement(this.rolePrivilegesPanel = new OxygenScrollablePanel(this.screen, 92, 56, 200, 10, 1, 200, 5, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));
        this.rolePrivilegesPanel.disableFull();
        this.rolePrivilegesPanel.<PrivilegePanelEntry>setElementClickListener((previous, clicked, mouseX, mouseY, mouseButton)->this.currPrivilegeEntry = clicked);
        this.rolePrivilegesPanel.initContextMenu(new OxygenContextMenu(new RemoveRolePrivilegeContextAction()));

        this.addElement(this.rolePlayersLabel = new OxygenTextLabel(92, 121, ClientReference.localize("oxygen_core.gui.privileges.players"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()).disableFull());
        this.addElement(this.rolePlayersPanel = new OxygenScrollablePanel(this.screen, 92, 123, 200, 10, 1, 500, 5, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));
        this.rolePlayersPanel.disableFull();
        this.rolePlayersPanel.<PlayerPanelEntry>setElementClickListener((previous, clicked, mouseX, mouseY, mouseButton)->this.currPlayerEntry = clicked);
        this.rolePlayersPanel.initContextMenu(new OxygenContextMenu(new RemoveRoleFromPlayerContextAction()));
    }

    private void calculateButtonsHorizontalPosition() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.createRoleButton.setX((sr.getScaledWidth() - (10 + this.textWidth(this.createRoleButton.getDisplayText(), this.createRoleButton.getTextScale()))) / 2 - this.screen.guiLeft);
    }

    private void collectPlayersPerRole() {
        int i, roleId;
        for (PlayerSharedData sharedData : OxygenManagerClient.instance().getPrivilegesContainer().getServerPlayersData()) {
            for (i = 0; i < OxygenMain.MAX_ROLES_PER_PLAYER; i++) {
                roleId = sharedData.getByte(i + OxygenMain.ROLES_SHARED_DATA_STARTING_INDEX);
                if (roleId != OxygenMain.DEFAULT_ROLE_INDEX)
                    this.playersPerRole.put(roleId, sharedData);
            }
        }
    }

    private void sortRoles(int mode) {
        List<Role> roles = new ArrayList<>(OxygenManagerClient.instance().getPrivilegesContainer().getServerRoles());

        if (mode == 0)
            Collections.sort(roles, (r1, r2)->r2.getId() - r1.getId());
        else if (mode == 1)
            Collections.sort(roles, (r1, r2)->r1.getId() - r2.getId());
        else if (mode == 2)
            Collections.sort(roles, (r1, r2)->r1.getName().compareTo(r2.getName()));
        else if (mode == 3)
            Collections.sort(roles, (r1, r2)->r2.getName().compareTo(r1.getName()));

        this.rolesPanel.reset();
        for (Role role : roles)
            this.rolesPanel.addEntry(new OxygenPanelEntry(role, String.format("%s [%s]", role.getNameColor() + role.getName(), role.getId()), true));

        this.rolesAmountLabel.setDisplayText(String.valueOf(roles.size()) + "/126");     

        this.rolesAmountLabel.setDisplayText(String.format("%s/126", roles.size()));     
        this.rolesAmountLabel.setX(86 - this.textWidth(this.rolesAmountLabel.getDisplayText(), this.rolesAmountLabel.getTextScale()));

        this.rolesPanel.getScroller().reset();
        this.rolesPanel.getScroller().updateRowsAmount(MathUtils.clamp(roles.size(), 10, 200));
    }

    private void displayRoleInformation(Role role) {
        this.roleChatFormattingLabel.enableFull();
        this.currentRoleChatFormattingLabel.enableFull();

        this.rolePrivilegesLabel.enableFull();
        this.rolePrivilegesPanel.enableFull();

        this.rolePlayersLabel.enableFull();
        this.rolePlayersPanel.enableFull();

        //role name
        this.currentRoleNameLabel.setDisplayText(role.getNameColor() + role.getName());

        //privileges
        List<Privilege> privileges = new ArrayList<>(role.getPrivileges());

        Collections.sort(privileges, (p1, p2)->p1.getId() - p2.getId());

        this.rolePrivilegesPanel.reset();
        for (Privilege privilege : privileges)
            this.rolePrivilegesPanel.addEntry(new PrivilegePanelEntry(privilege));

        this.rolePrivilegesPanel.getScroller().reset();
        this.rolePrivilegesPanel.getScroller().updateRowsAmount(MathUtils.clamp(privileges.size(), 5, 200));

        //players
        List<PlayerSharedData> players = new ArrayList<>(this.playersPerRole.get(role.getId()));

        Collections.sort(players, (p1, p2)->p1.getUsername().compareTo(p2.getUsername()));

        this.rolePlayersPanel.reset();
        for (PlayerSharedData sharedData : players)
            this.rolePlayersPanel.addEntry(new PlayerPanelEntry(sharedData));

        this.rolePlayersPanel.getScroller().reset();
        this.rolePlayersPanel.getScroller().updateRowsAmount(MathUtils.clamp(players.size(), 5, 500));

        //chat formatting
        if (PrivilegesConfig.ENABLE_FORMATTED_CHAT.asBoolean()) {
            if (PrivilegesConfig.ENABLE_CUSTOM_FORMATTED_CHAT.asBoolean()) {
                String prefix = null, username, result;

                TextFormatting defaultColor = TextFormatting.values()[PrivilegesConfig.DEFAULT_CHAT_COLOR.asInt()];

                if (!role.getPrefix().isEmpty())
                    prefix = role.getPrefixColor() + role.getPrefix() + TextFormatting.RESET;

                username = role.getUsernameColor() + ClientReference.getClientPlayer().getName() + TextFormatting.RESET;

                result = !role.getPrefix().isEmpty() ? PrivilegesConfig.FORMATTED_CHAT_PREFIX_PATTERN.asString() : PrivilegesConfig.FORMATTED_CHAT_PATTERN.asString();
                if (prefix != null)
                    result = defaultColor + result.replace("@prefix", prefix + defaultColor);
                result = result.replace("@username", username + defaultColor);

                result += role.getChatColor() + ClientReference.localize("oxygen_core.gui.privileges.testMessage");

                this.currentRoleChatFormattingLabel.setDisplayText(result);
            } else {
                StringBuilder formattedUsername = new StringBuilder();

                if (!role.getPrefix().isEmpty()) {
                    formattedUsername.append(role.getPrefixColor());
                    formattedUsername.append("[");
                    formattedUsername.append(role.getPrefix());
                    formattedUsername.append("]");
                    formattedUsername.append(TextFormatting.RESET);
                }

                formattedUsername.append(role.getUsernameColor());
                formattedUsername.append(ClientReference.getClientPlayer().getName());
                formattedUsername.append(TextFormatting.RESET);

                this.currentRoleChatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", formattedUsername.toString(), role.getChatColor() + ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
            }
        } else
            this.currentRoleChatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", ClientReference.getClientPlayer().getName(), ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
    }

    public void resetRoleInformation() {
        this.currentRoleNameLabel.disableFull();

        this.roleChatFormattingLabel.disableFull();
        this.currentRoleChatFormattingLabel.disableFull();

        this.rolePrivilegesLabel.disableFull();
        this.rolePrivilegesPanel.disableFull();

        this.rolePlayersLabel.disableFull();
        this.rolePlayersPanel.disableFull();
    }

    private void updateCreateButtonState() {
        this.createRoleButton.setEnabled(OxygenManagerClient.instance().getPrivilegesContainer().getServerRoles().size() <= 126);
    }   

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) {
            if (element == this.createRoleButton)
                this.roleCreationCallback.open();
        }
    }

    public void managementDataReceived() {
        this.collectPlayersPerRole();
        this.sortRoles(0);
        this.updateCreateButtonState();

        this.calculateButtonsHorizontalPosition();
    }

    public void roleCreated(Role role) {
        this.resetRoleInformation();
        this.prioritySorter.setSorting(EnumSorting.DOWN);
        this.nameSorter.reset();
        this.sortRoles(0);

        this.updateCreateButtonState();
    }

    public void roleRemoved(Role role) {
        this.resetRoleInformation();
        this.prioritySorter.setSorting(EnumSorting.DOWN);
        this.nameSorter.reset();
        this.sortRoles(0);

        this.updateCreateButtonState();
    }

    public void rolePrivilegeAdded(int roleId, Privilege privilege) {
        if (this.currRoleEntry != null)
            this.displayRoleInformation(this.currRoleEntry.getWrapped());
    }

    public void rolePrivilegeRemoved(int roleId, Privilege privilege) {
        if (this.currRoleEntry != null)
            this.displayRoleInformation(this.currRoleEntry.getWrapped());
    }

    public void playerRolesChanged(int roleId, PlayerSharedData sharedData) {
        this.playersPerRole.put(roleId, sharedData);
        if (this.currRoleEntry != null && this.currRoleEntry.getWrapped().getId() == roleId) {
            List<PlayerSharedData> players = new ArrayList<>(this.playersPerRole.get(roleId));

            Collections.sort(players, (p1, p2)->p1.getUsername().compareTo(p2.getUsername()));

            this.rolePlayersPanel.reset();
            for (PlayerSharedData data : players)
                this.rolePlayersPanel.addEntry(new PlayerPanelEntry(data));

            this.rolePlayersPanel.getScroller().reset();
            this.rolePlayersPanel.getScroller().updateRowsAmount(MathUtils.clamp(players.size(), 5, 500));
        }
    }

    public OxygenPanelEntry<Role> getCurrentRoleEntry() {
        return this.currRoleEntry;
    }

    public PrivilegePanelEntry getCurrentPrivilegeEntry() {
        return this.currPrivilegeEntry;
    }

    public PlayerPanelEntry getCurrentPlayerEntry() {
        return this.currPlayerEntry;
    }

    public void openEditRoleCallback() {
        this.editRoleCallback.open();
    }

    public void openRemoveRoleCallback() {
        this.removeRoleCallback.open();
    }

    public void openAddPrivilegeCallback() {
        this.addPrivilegeCallback.open();
    }

    public void openRemovePrivilegeCallback() {
        this.removePrivilegeCallback.open();
    }

    public void openRemoveRoleFromPlayerCallback() {
        this.removePlayerCallback.open();
    }
}
