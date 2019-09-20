package austeretony.oxygen_core.common.privilege;

public enum EnumPrivilegeFileKey {

    ID("id"),
    NAME("name"),
    TYPE("type"),
    PREFIX("prefix"),
    SUFFIX("suffix"),
    USERNAME_COLOR("username_color"),
    PREFIX_COLOR("prefix_color"),
    SUFFIX_COLOR("suffix_color"),
    CHAT_COLOR("chat_color"),
    PRIVILEGES("privileges"),
    VALUE("value"),
    PLAYER_UUID("player_uuid"),
    GROUP("group");
    
    public final String name;
    
    EnumPrivilegeFileKey(String name) {
        this.name = name;
    }
}
