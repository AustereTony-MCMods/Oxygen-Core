package austeretony.oxygen_core.common.privilege;

public enum EnumPrivilegeFileKey {

    ID("id"),
    NAME("name"),
    TYPE("type"),
    PREFIX("prefix"),
    NAME_COLOR("name_color"),
    USERNAME_COLOR("username_color"),
    PREFIX_COLOR("prefix_color"),
    CHAT_COLOR("chat_color"),
    CHAT_FORMATTING_ROLE("chat_formatting_role"),
    PRIVILEGES("privileges"),
    VALUE("value"),
    PLAYER_UUID("player_uuid"),
    ROLES("roles");

    public final String name;

    EnumPrivilegeFileKey(String name) {
        this.name = name;
    }
}
