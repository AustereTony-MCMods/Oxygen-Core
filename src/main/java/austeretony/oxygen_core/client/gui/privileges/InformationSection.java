package austeretony.oxygen_core.client.gui.privileges;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenDefaultBackgroundFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenDropDownList;
import austeretony.oxygen_core.client.gui.elements.OxygenDropDownList.OxygenDropDownListEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenPanelEntry;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenSectionSwitcher;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.util.MathUtils;
import net.minecraft.util.text.TextFormatting;

public class InformationSection extends AbstractGUISection {

    private final PrivilegesScreen screen;

    private OxygenTextLabel chatFormattingLabel, roleChatFormattingLabel;

    private OxygenScrollablePanel privilegesPanel;

    public InformationSection(PrivilegesScreen screen) {
        super(screen);
        this.screen = screen;
        this.setDisplayText(ClientReference.localize("oxygen_core.gui.privileges.information"));
    }

    @Override
    public void init() {
        this.addElement(new OxygenDefaultBackgroundFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.title"), EnumBaseGUISetting.TEXT_TITLE_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        if (!OxygenConfig.ENABLE_PRIVILEGES.asBoolean()) {
            String disabledStr = ClientReference.localize("oxygen_core.gui.privileges.disabled");   
            this.addElement(new OxygenTextLabel((this.getWidth() - this.textWidth(disabledStr, EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F)) / 2, 25, disabledStr, EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));
        } else { 
            Set<Integer> rolesIds = OxygenManagerClient.instance().getPrivilegesManager().getPlayerRolesIds();
            Role role;

            //player roles
            this.addElement(new OxygenTextLabel(6, 52, ClientReference.localize("oxygen_core.gui.privileges.playerRoles"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

            OxygenScrollablePanel rolesPanel;
            this.addElement(rolesPanel = new OxygenScrollablePanel(this.screen, 6, 54, 80, 10, 1, OxygenMain.MAX_ROLES_PER_PLAYER, 5, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));

            for (int roleId : rolesIds) {
                role = OxygenManagerClient.instance().getPrivilegesManager().getPlayerRole(roleId);
                rolesPanel.addEntry(new OxygenPanelEntry(role, role.getNameColor() + role.getName(), true));
            }

            rolesPanel.getScroller().updateRowsAmount(MathUtils.clamp(rolesIds.size(), 5, OxygenMain.MAX_ROLES_PER_PLAYER));

            rolesPanel.<OxygenPanelEntry<Role>>setClickListener((previous, clicked, mouseX, mouseY, mouseButton)->{
                if (previous != null)
                    previous.setToggled(false);
                clicked.toggle();
                this.displayRoleInformation(clicked.index);
            });

            //chat formatting
            this.addElement(new OxygenTextLabel(6, 24, ClientReference.localize("oxygen_core.gui.privileges.chatFormatting"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

            this.addElement(this.chatFormattingLabel = new OxygenTextLabel(6, 33, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));
            this.updateChatFormatting(OxygenManagerClient.instance().getPrivilegesManager().getChatFormattingRoleId());

            Role chatFormattingRole = OxygenManagerClient.instance().getPrivilegesManager().getChatFormattingRole();
            String 
            defaultFormattingStr = ClientReference.localize("oxygen_core.gui.privileges.defaultFormatting"),
            chatFormattingRoleNameStr = chatFormattingRole == null ? defaultFormattingStr : chatFormattingRole.getName();

            OxygenDropDownList rolesList;
            this.addElement(rolesList = new OxygenDropDownList(6, 35, 80, (chatFormattingRole == null ? TextFormatting.GRAY : chatFormattingRole.getNameColor()) + chatFormattingRoleNameStr).setEnabled(PrivilegesConfig.ENABLE_CHAT_FORMATTING_ROLE_MANAGEMENT.asBoolean()));
            for (int roleId : rolesIds) {
                role = OxygenManagerClient.instance().getPrivilegesManager().getPlayerRole(roleId);
                rolesList.addElement(new OxygenDropDownListEntry<Integer>(role.getId(), role.getNameColor() + role.getName()));
            }
            rolesList.addElement(new OxygenDropDownListEntry<Integer>(OxygenMain.DEFAULT_ROLE_INDEX, defaultFormattingStr));

            rolesList.<OxygenDropDownListEntry<Integer>>setClickListener((element)->{
                OxygenManagerClient.instance().getPrivilegesManager().setChatFormattingRoleSynced(element.index);
                this.updateChatFormatting(element.index);
            });         

            if (!rolesIds.isEmpty()) {
                this.addElement(new OxygenTextLabel(92, 52, ClientReference.localize("oxygen_core.gui.privileges.chatFormatting"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
                this.addElement(this.roleChatFormattingLabel = new OxygenTextLabel(92, 61, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_DARK_ENABLED_COLOR.get().asInt()));

                this.addElement(new OxygenTextLabel(92, 71, ClientReference.localize("oxygen_core.gui.privileges.privileges"), EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() + 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));
                role = OxygenManagerClient.instance().getPrivilegesManager().getPlayerRole(((TreeSet<Integer>) rolesIds).first());
                this.addElement(this.privilegesPanel = new OxygenScrollablePanel(this.screen, 92, 73, this.getWidth() - 101, 10, 1, 180, 6, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));
                this.displayRoleInformation(role);

                rolesPanel.setPreviousClickedButton(rolesPanel.visibleButtons.get(0).toggle());
            }

            if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean())
                this.addElement(new OxygenSectionSwitcher(this.getWidth() - 4, 4, this, this.screen.getRolesSection(), this.screen.getPlayersSection()));
        }
    }

    private void updateChatFormatting(int roleId) {     
        if (PrivilegesConfig.ENABLE_FORMATTED_CHAT.asBoolean()) {
            Role role = OxygenManagerClient.instance().getPrivilegesManager().getPlayerRole(roleId);
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

                    this.chatFormattingLabel.setDisplayText(result);
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

                    this.chatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", formattedUsername.toString(), role.getChatColor() + ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
                }
            } else if (PrivilegesConfig.ENABLE_CUSTOM_FORMATTED_CHAT.asBoolean()) {
                this.chatFormattingLabel.setDisplayText(
                        TextFormatting.values()[PrivilegesConfig.DEFAULT_CHAT_COLOR.asInt()] + PrivilegesConfig.FORMATTED_CHAT_PATTERN.asString().replace("@username", ClientReference.getClientPlayer().getName()) + ClientReference.localize("oxygen_core.gui.privileges.testMessage"));
            } else
                this.chatFormattingLabel.setDisplayText(ClientReference.localize("chat.type.text", ClientReference.getClientPlayer().getName(), ClientReference.localize("oxygen_core.gui.privileges.testMessage")));
        } else
            this.chatFormattingLabel.setDisplayText(ClientReference.localize("oxygen_core.gui.privileges.chatFormattingDisabled"));
    }

    private void displayRoleInformation(Role role) {
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
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {}
}
