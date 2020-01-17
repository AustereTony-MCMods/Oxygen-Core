package austeretony.oxygen_core.common.privilege;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.OxygenUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextFormatting;

public class RoleImpl implements Role {  

    public static final int 
    ROLE_NAME_MAX_LENGTH = 24,
    PREFIX_MAX_LENGTH = 16;

    public final int roleId;

    private String name, prefix;

    private TextFormatting nameColor, usernameColor, prefixColor, chatColor;

    private final Map<Integer, Privilege> privileges = new ConcurrentHashMap<>(5);

    public RoleImpl(String name, int roleId, TextFormatting color) {
        this.roleId = roleId;
        this.name = name;
        this.nameColor = color;
        this.prefix = "";
        this.usernameColor = this.prefixColor = this.chatColor = TextFormatting.GRAY;
    }

    @Override
    public int getId() {
        return this.roleId;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public TextFormatting getNameColor() {
        return this.nameColor;
    }

    public void setNameColor(TextFormatting color) {
        this.nameColor = color;
    }

    @Override
    public TextFormatting getUsernameColor() {
        return this.usernameColor;
    }

    public void setUsernameColor(TextFormatting color) {
        this.usernameColor = color;
    }

    @Override
    public TextFormatting getPrefixColor() {
        return this.prefixColor;
    }

    public void setPrefixColor(TextFormatting color) {
        this.prefixColor = color;
    }

    @Override
    public TextFormatting getChatColor() {
        return this.chatColor;
    }

    public void setChatColor(TextFormatting color) {
        this.chatColor = color;
    }

    @Override
    public Collection<Privilege> getPrivileges() {
        return this.privileges.values();
    }

    @Override
    public Privilege getPrivilege(int id) {
        return this.privileges.get(id);
    }

    @Override
    public void addPrivilege(Privilege privilege) {
        this.privileges.put(privilege.getId(), privilege);
    }

    @Override
    public void addPrivileges(Privilege... privileges) {
        for (Privilege privilege : privileges)
            this.privileges.put(privilege.getId(), privilege);
    }

    @Override
    public Privilege removePrivilege(int id) {
        return this.privileges.remove(id);
    }

    @Override
    public JsonObject serialize() {
        JsonObject roleObject = new JsonObject();
        roleObject.add(EnumPrivilegeFileKey.ID.name, new JsonPrimitive(this.getId()));
        roleObject.add(EnumPrivilegeFileKey.NAME.name, new JsonPrimitive(this.getName()));
        roleObject.add(EnumPrivilegeFileKey.NAME_COLOR.name, new JsonPrimitive(OxygenUtils.formattingCode(this.getNameColor())));
        roleObject.add(EnumPrivilegeFileKey.PREFIX.name, new JsonPrimitive(this.getPrefix()));
        roleObject.add(EnumPrivilegeFileKey.USERNAME_COLOR.name, new JsonPrimitive(OxygenUtils.formattingCode(this.getUsernameColor())));
        roleObject.add(EnumPrivilegeFileKey.PREFIX_COLOR.name, new JsonPrimitive(OxygenUtils.formattingCode(this.getPrefixColor())));
        roleObject.add(EnumPrivilegeFileKey.CHAT_COLOR.name, new JsonPrimitive(OxygenUtils.formattingCode(this.getChatColor())));
        JsonArray privilegesArray = new JsonArray();
        for (Privilege privilege : this.getPrivileges())
            privilegesArray.add(privilege.toJson());
        roleObject.add(EnumPrivilegeFileKey.PRIVILEGES.name, privilegesArray);
        return roleObject;
    }

    public static RoleImpl deserialize(JsonObject jsonObject) {
        RoleImpl role = new RoleImpl(
                jsonObject.get(EnumPrivilegeFileKey.NAME.name).getAsString(),
                jsonObject.get(EnumPrivilegeFileKey.ID.name).getAsInt(),
                OxygenUtils.formattingFromCode(jsonObject.get(EnumPrivilegeFileKey.NAME_COLOR.name).getAsString()));
        role.setPrefix(jsonObject.get(EnumPrivilegeFileKey.PREFIX.name).getAsString());
        role.setUsernameColor(OxygenUtils.formattingFromCode(jsonObject.get(EnumPrivilegeFileKey.USERNAME_COLOR.name).getAsString()));
        role.setPrefixColor(OxygenUtils.formattingFromCode(jsonObject.get(EnumPrivilegeFileKey.PREFIX_COLOR.name).getAsString()));
        role.setChatColor(OxygenUtils.formattingFromCode(jsonObject.get(EnumPrivilegeFileKey.CHAT_COLOR.name).getAsString()));
        JsonArray privilegesArray = jsonObject.get(EnumPrivilegeFileKey.PRIVILEGES.name).getAsJsonArray();
        for (JsonElement privilegesElement : privilegesArray)
            role.addPrivilege(PrivilegeUtils.deserialize(privilegesElement.getAsJsonObject()));
        OxygenMain.LOGGER.info("Loaded role <{}> ({}).", role.getName(), role.getId());
        return role;
    }

    @Override
    public void write(ByteBuf buffer) {
        ByteBufUtils.writeString(this.getName(), buffer);
        buffer.writeByte(this.getId());
        buffer.writeByte(this.getNameColor().ordinal());
        ByteBufUtils.writeString(this.getPrefix(), buffer);
        buffer.writeByte(this.getUsernameColor().ordinal());
        buffer.writeByte(this.getPrefixColor().ordinal());
        buffer.writeByte(this.getChatColor().ordinal());
        buffer.writeByte(this.getPrivileges().size());
        for (Privilege privilege : this.getPrivileges())
            privilege.write(buffer);
    }

    public static RoleImpl read(ByteBuf buffer) {
        RoleImpl role = new RoleImpl(
                ByteBufUtils.readString(buffer), 
                buffer.readByte(), 
                TextFormatting.values()[buffer.readByte()]);
        role.setPrefix(ByteBufUtils.readString(buffer));
        role.setUsernameColor(TextFormatting.values()[buffer.readByte()]);
        role.setPrefixColor(TextFormatting.values()[buffer.readByte()]);
        role.setChatColor(TextFormatting.values()[buffer.readByte()]);
        int amount = buffer.readByte();
        for (int i = 0; i < amount; i++)
            role.addPrivilege(PrivilegeUtils.read(buffer));
        return role;
    }
}
