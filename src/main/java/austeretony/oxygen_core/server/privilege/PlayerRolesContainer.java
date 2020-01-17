package austeretony.oxygen_core.server.privilege;

import java.util.Set;
import java.util.TreeSet;

import austeretony.oxygen_core.common.main.OxygenMain;

public class PlayerRolesContainer {

    public final Set<Integer> roles = new TreeSet<>((i, j)->(j - i));

    private int chatFormattingRoleId = OxygenMain.DEFAULT_ROLE_INDEX;

    protected PlayerRolesContainer() {}

    public int getChatFormattingRoleId() {
        return this.chatFormattingRoleId;
    }

    public void setChatFormattingRoleId(int roleId) {
        this.chatFormattingRoleId = roleId;
    }
}
