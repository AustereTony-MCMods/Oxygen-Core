package austeretony.oxygen_core.client.gui.privileges.information;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.api.PrivilegesProviderClient;
import austeretony.oxygen_core.client.gui.elements.OxygenDefaultBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenDropDownList;
import austeretony.oxygen_core.client.gui.elements.OxygenDropDownList.OxygenDropDownListWrapperEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenPanelEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.management.PrivilegePanelEntry;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.util.MathUtils;
import net.minecraft.util.text.TextFormatting;

public class PrivilegesSection extends AbstractGUISection {

    private OxygenTextLabel currentRoleNameLabel, roleChatFormattingLabel, currentRoleChatFormattingLabel, rolePrivilegesLabel, activeChatFormattingLabel;

    private OxygenScrollablePanel rolePrivilegesPanel;

    public PrivilegesSection(PrivilegesScreen screen) {
        super(screen);
    }

    @Override
    public void init() {
        this.addElement(new OxygenDefaultBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.privileges"), EnumBaseGUISetting.TEXT_TITLE_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.currentRoleNameLabel = new OxygenTextLabel(92, 24, "", EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.roleChatFormattingLabel = new OxygenTextLabel(92, 33, ClientReference.localize("oxygen_core.gui.privileges.chatFormatting"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()).disableFull());
        this.addElement(this.currentRoleChatFormattingLabel = new OxygenTextLabel(92, 42, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.rolePrivilegesLabel = new OxygenTextLabel(92, 54, ClientReference.localize("oxygen_core.gui.privileges.privileges"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()).disableFull());
        this.addElement(this.rolePrivilegesPanel = new OxygenScrollablePanel(this.screen, 92, 56, 200, 10, 1, 200, 5, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));
        this.rolePrivilegesPanel.disableFull();

        //ROLES
        this.addElement(new OxygenTextLabel(6, 24, ClientReference.localize("oxygen_core.gui.privileges.information.playerRoles"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        Set<Integer> ids = PrivilegesProviderClient.getPlayerRolesIds();
        OxygenScrollablePanel rolesPanel;
        this.addElement(rolesPanel = new OxygenScrollablePanel(this.screen, 6, 26, 80, 10, 1, OxygenMain.MAX_ROLES_PER_PLAYER, 5, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));

        Role role;
        for (int roleId : ids) {
            role = PrivilegesProviderClient.getPlayerRole(roleId);
            rolesPanel.addEntry(new OxygenPanelEntry(role, role.getNameColor() + role.getName(), true));
        }
        rolesPanel.getScroller().updateRowsAmount(MathUtils.clamp(ids.size(), 5, OxygenMain.MAX_ROLES_PER_PLAYER));

        rolesPanel.<OxygenPanelEntry<Role>>setElementClickListener((previous, clicked, mouseX, mouseY, mouseButton)->{
            if (previous != null)
                previous.setToggled(false);
            clicked.toggle();
            this.displayRoleInformation(clicked.getWrapped());
        });

        if (!ids.isEmpty())
            this.displayRoleInformation(PrivilegesProviderClient.getPlayerRole(((TreeSet<Integer>) ids).first()));

        //DEFAULT PRIVILEGES
        this.addElement(new OxygenTextLabel(6, 144, ClientReference.localize("oxygen_core.gui.privileges.defaultPrivileges"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        OxygenScrollablePanel defaultPrivilegesPanel;
        this.addElement(defaultPrivilegesPanel = new OxygenScrollablePanel(this.screen, 6, 146, 200, 10, 1, 200, 5, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));

        Collection<Privilege> privileges = OxygenManagerClient.instance().getPrivilegesContainer().getDefaultPrivileges();
        for (Privilege privilege : privileges)
            defaultPrivilegesPanel.addEntry(new PrivilegePanelEntry(privilege));
        defaultPrivilegesPanel.getScroller().updateRowsAmount(MathUtils.clamp(privileges.size(), 5, 200));

        //CHAT FORMATTING
        this.addElement(new OxygenTextLabel(6, 122, ClientReference.localize("oxygen_core.gui.privileges.chatFormatting"), EnumBaseGUISetting.TEXT_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.activeChatFormattingLabel = new OxygenTextLabel(90, 131, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));
        this.updateChatFormatting(OxygenManagerClient.instance().getPrivilegesContainer().getChatFormattingRoleId());

        Role chatFormattingRole = OxygenManagerClient.instance().getPrivilegesContainer().getChatFormattingRole();
        String 
        defaultFormattingStr = ClientReference.localize("oxygen_core.gui.privileges.defaultFormatting"),
        chatFormattingRoleNameStr = chatFormattingRole == null ? defaultFormattingStr : chatFormattingRole.getName();

        OxygenDropDownList rolesList;
        this.addElement(rolesList = new OxygenDropDownList(6, 124, 80, (chatFormattingRole == null ? TextFormatting.GRAY : chatFormattingRole.getNameColor()) + chatFormattingRoleNameStr).setEnabled(PrivilegesConfig.ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT.asBoolean()));
        for (int roleId : ids) {
            role = PrivilegesProviderClient.getPlayerRole(roleId);
            rolesList.addElement(new OxygenDropDownListWrapperEntry<Integer>(role.getId(), role.getNameColor() + role.getName()));
        }
        rolesList.addElement(new OxygenDropDownListWrapperEntry<Integer>(OxygenMain.DEFAULT_ROLE_INDEX, defaultFormattingStr));

        rolesList.<OxygenDropDownListWrapperEntry<Integer>>setElementClickListener((element)->{
            OxygenManagerClient.instance().getPrivilegesManager().setChatFormattingRoleSynced(element.getWrapped());
            this.updateChatFormatting(element.getWrapped());
        });   
    }

    private void displayRoleInformation(Role role) {
        this.roleChatFormattingLabel.enableFull();
        this.rolePrivilegesLabel.enableFull();
        this.rolePrivilegesPanel.enableFull();

        this.currentRoleNameLabel.setDisplayText(role.getName());

        List<Privilege> privileges = new ArrayList(role.getPrivileges());

        Collections.sort(privileges, (p1, p2)->p1.getId() - p2.getId());

        this.rolePrivilegesPanel.reset();
        for (Privilege privilege : privileges)
            this.rolePrivilegesPanel.addEntry(new PrivilegePanelEntry(privilege));

        this.rolePrivilegesPanel.getScroller().reset();
        this.rolePrivilegesPanel.getScroller().updateRowsAmount(MathUtils.clamp(role.getPrivileges().size(), 5, 200));

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

    private void updateChatFormatting(int roleId) {     
        if (PrivilegesConfig.ENABLE_FORMATTED_CHAT.asBoolean()) {
            Role role = PrivilegesProviderClient.getPlayerRole(roleId);
            if (role != null) {
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

                    this.activeChatFormattingLabel.setDisplayText(result);
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

                    this.activeChatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", formattedUsername.toString(), role.getChatColor() + ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
                }
            } else if (PrivilegesConfig.ENABLE_CUSTOM_FORMATTED_CHAT.asBoolean()) {
                this.activeChatFormattingLabel.setDisplayText(
                        TextFormatting.values()[PrivilegesConfig.DEFAULT_CHAT_COLOR.asInt()] + PrivilegesConfig.FORMATTED_CHAT_PATTERN.asString().replace("@username", ClientReference.getClientPlayer().getName()) + ClientReference.localize("oxygen_core.gui.privileges.testMessage"));
            } else
                this.activeChatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", ClientReference.getClientPlayer().getName(), ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
        } else
            this.activeChatFormattingLabel.setDisplayText(ClientReference.localize("oxygen_core.gui.privileges.chatFormattingDisabled"));
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {}
}
