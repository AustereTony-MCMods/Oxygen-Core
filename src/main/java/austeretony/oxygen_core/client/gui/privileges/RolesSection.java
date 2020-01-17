package austeretony.oxygen_core.client.gui.privileges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenButton;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu;
import austeretony.oxygen_core.client.gui.elements.OxygenPanelEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenSectionSwitcher;
import austeretony.oxygen_core.client.gui.elements.OxygenSorter;
import austeretony.oxygen_core.client.gui.elements.OxygenSorter.EnumSorting;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.roles.RolesBackgroundFiller;
import austeretony.oxygen_core.client.gui.privileges.roles.callback.AddPrivilegeCallback;
import austeretony.oxygen_core.client.gui.privileges.roles.callback.EditRoleCallback;
import austeretony.oxygen_core.client.gui.privileges.roles.callback.RemovePrivilegeCallback;
import austeretony.oxygen_core.client.gui.privileges.roles.callback.RemoveRoleCallback;
import austeretony.oxygen_core.client.gui.privileges.roles.callback.RoleCreationCallback;
import austeretony.oxygen_core.client.gui.privileges.roles.context.AddPrivilegeContextAction;
import austeretony.oxygen_core.client.gui.privileges.roles.context.EditContextAction;
import austeretony.oxygen_core.client.gui.privileges.roles.context.RemoveContextAction;
import austeretony.oxygen_core.client.gui.privileges.roles.context.RemovePrivilegeContextAction;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.util.MathUtils;
import net.minecraft.util.text.TextFormatting;

public class RolesSection extends AbstractGUISection {

    private final PrivilegesScreen screen;

    private OxygenTextLabel rolesAmountLabel, roleNameLabel, roleChatFormattingLabel, chatFormattingTitleLabel, privilegesTitleLabel;

    private OxygenButton createButton;

    private OxygenSorter prioritySorter, nameSorter;

    private OxygenScrollablePanel rolesPanel, privilegesPanel;

    private AbstractGUICallback roleCreationCallback, editRoleCallback, removeRoleCallback, addPrivilegeCallback, removePrivilegeCallback;

    //cache

    private Role currRole;

    private int currPrivilegeId;

    public RolesSection(PrivilegesScreen screen) {
        super(screen);
        this.screen = screen;
        this.setDisplayText(ClientReference.localize("oxygen_core.gui.privileges.roles"));
    }

    @Override
    public void init() {
        this.addElement(new RolesBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.title"), EnumBaseGUISetting.TEXT_TITLE_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.rolesAmountLabel = new OxygenTextLabel(0, 23, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.prioritySorter = new OxygenSorter(6, 18, EnumSorting.DOWN, ClientReference.localize("oxygen_core.gui.priority")));   
        this.prioritySorter.setClickListener((sorting)->{
            this.nameSorter.reset();
            this.sortRoles(sorting == EnumSorting.DOWN ? 0 : 1);
        });

        this.addElement(this.nameSorter = new OxygenSorter(12, 18, EnumSorting.INACTIVE, ClientReference.localize("oxygen_core.gui.name")));  
        this.nameSorter.setClickListener((sorting)->{
            this.prioritySorter.reset();
            this.sortRoles(sorting == EnumSorting.DOWN ? 2 : 3);
        });

        this.addElement(this.rolesPanel = new OxygenScrollablePanel(this.screen, 6, 26, 80, 10, 1, 126, 9, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));

        this.rolesPanel.<OxygenPanelEntry<Role>>setClickListener((previous, clicked, mouseX, mouseY, mouseButton)->{
            if (previous != null)
                previous.setToggled(false);
            clicked.toggle();
            this.currRole = clicked.index;
            this.displayRoleInformation(clicked.index);
        });

        this.rolesPanel.initContextMenu(new OxygenContextMenu(
                new EditContextAction(),
                new AddPrivilegeContextAction(),
                new RemoveContextAction()));

        this.addElement(this.createButton = new OxygenButton(6, this.getHeight() - 12, 40, 10, ClientReference.localize("oxygen_core.gui.create")));     
        this.createButton.setKeyPressListener(Keyboard.KEY_E, ()->this.roleCreationCallback.open());

        this.addElement(new OxygenSectionSwitcher(this.getWidth() - 4, 4, this, this.screen.getInformationSection(), this.screen.getPlayersSection()));

        this.initRoleElements();

        this.roleCreationCallback = new RoleCreationCallback(this.screen, this, 140, 178);       
        this.editRoleCallback = new EditRoleCallback(this.screen, this, 140, 178);
        this.addPrivilegeCallback = new AddPrivilegeCallback(this.screen, this, 190, 136);
        this.removePrivilegeCallback = new RemovePrivilegeCallback(this.screen, this, 140, 38); 
        this.removeRoleCallback = new RemoveRoleCallback(this.screen, this, 140, 38);
    }

    private void initRoleElements() {
        this.addElement(this.roleNameLabel = new OxygenTextLabel(94, 25, "", EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()).disableFull());

        this.addElement(this.chatFormattingTitleLabel = new OxygenTextLabel(94, 35, ClientReference.localize("oxygen_core.gui.privileges.chatFormatting"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()).disableFull());
        this.addElement(this.roleChatFormattingLabel = new OxygenTextLabel(94, 44, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()).disableFull());

        this.addElement(this.privilegesTitleLabel = new OxygenTextLabel(94, 57, ClientReference.localize("oxygen_core.gui.privileges.privileges"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()).disableFull());
        this.addElement(this.privilegesPanel = new OxygenScrollablePanel(this.screen, 94, 59, this.getWidth() - 103, 10, 1, 180, 6, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));
        this.privilegesPanel.disableFull();
        this.privilegesPanel.<PrivilegePanelEntry>setClickListener((previous, clicked, mouseX, mouseY, mouseButton)->this.currPrivilegeId = clicked.index);

        this.privilegesPanel.initContextMenu(new OxygenContextMenu(new RemovePrivilegeContextAction()));
    }

    private void sortRoles(int mode) {
        List<Role> roles = new ArrayList<>(OxygenManagerClient.instance().getPrivilegesManager().getRoles());

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
        this.rolesAmountLabel.setX(89 - this.textWidth(this.rolesAmountLabel.getDisplayText(), this.rolesAmountLabel.getTextScale()));

        this.rolesPanel.getScroller().reset();
        this.rolesPanel.getScroller().updateRowsAmount(MathUtils.clamp(roles.size(), 9, 126));
    }

    private void displayRoleInformation(Role role) {
        this.chatFormattingTitleLabel.enable();
        this.privilegesTitleLabel.enable();

        this.roleNameLabel.setDisplayText(role.getNameColor() + role.getName());

        List<Privilege> privileges = new ArrayList(role.getPrivileges());

        Collections.sort(privileges, (p1, p2)->p1.getId() - p2.getId());

        this.privilegesPanel.reset();
        for (Privilege privilege : privileges)
            this.privilegesPanel.addEntry(new PrivilegePanelEntry(privilege));

        this.privilegesPanel.getScroller().reset();
        this.privilegesPanel.getScroller().updateRowsAmount(MathUtils.clamp(role.getPrivileges().size(), 6, 180));

        if (PrivilegesConfig.ENABLE_FORMATTED_CHAT.asBoolean()) {
            if (PrivilegesConfig.ENABLE_CUSTOM_FORMATTED_CHAT.asBoolean()) {
                String prefix = null, username, result;

                TextFormatting defColor = TextFormatting.values()[PrivilegesConfig.DEFAULT_CHAT_COLOR.asInt()];

                if (!role.getPrefix().isEmpty())
                    prefix = role.getPrefixColor() + role.getPrefix() + TextFormatting.RESET;

                username = role.getUsernameColor() + ClientReference.getClientPlayer().getName() + TextFormatting.RESET;

                result = !role.getPrefix().isEmpty() ? PrivilegesConfig.FORMATTED_CHAT_PREFIX_PATTERN.asString() : PrivilegesConfig.FORMATTED_CHAT_PATTERN.asString();
                if (prefix != null)
                    result = defColor + result.replace("@prefix", prefix + defColor);
                result = result.replace("@username", username + defColor);

                result += role.getChatColor() + ClientReference.localize("oxygen_core.gui.privileges.testMessage");

                this.roleChatFormattingLabel.setDisplayText(result);
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

                this.roleChatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", formattedUsername.toString(), role.getChatColor() + ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
            }
        } else
            this.roleChatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", ClientReference.getClientPlayer().getName(), ClientReference.localize("oxygen_core.gui.privileges.testMessage")));

        this.roleNameLabel.enableFull();

        this.chatFormattingTitleLabel.enableFull();
        this.roleChatFormattingLabel.enableFull();

        this.privilegesTitleLabel.enableFull();
        this.privilegesPanel.enableFull();
    }

    public void resetRoleInfo() {
        this.roleNameLabel.disableFull();

        this.chatFormattingTitleLabel.disableFull();
        this.roleChatFormattingLabel.disableFull();

        this.privilegesTitleLabel.disableFull();
        this.privilegesPanel.disableFull();
    }

    private void updateCreateButtonState() {
        this.createButton.setEnabled(OxygenManagerClient.instance().getPrivilegesManager().getRoles().size() < 127);
    }   

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) {
            if (element == this.createButton)
                this.roleCreationCallback.open();
        }
    }

    public void privilegesDataReceived() {
        this.sortRoles(0);
        this.updateCreateButtonState();
    }

    public void roleCreated(Role role) {
        this.resetRoleInfo();
        this.prioritySorter.setSorting(EnumSorting.DOWN);
        this.nameSorter.reset();
        this.sortRoles(0);

        this.updateCreateButtonState();
    }

    public void roleRemoved(Role role) {
        this.resetRoleInfo();
        this.prioritySorter.setSorting(EnumSorting.DOWN);
        this.nameSorter.reset();
        this.sortRoles(0);

        this.updateCreateButtonState();
    }

    public void privilegeAdded(int roleId, Privilege privilege) {
        if (this.currRole != null)
            this.displayRoleInformation(this.currRole);
    }

    public void privilegeRemoved(int roleId, Privilege privilege) {
        if (this.currRole != null)
            this.displayRoleInformation(this.currRole);
    }

    public Role getCurrentRole() {
        return this.currRole;
    }

    public int getCurrentPrivilegeId() {
        return this.currPrivilegeId;
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
}
