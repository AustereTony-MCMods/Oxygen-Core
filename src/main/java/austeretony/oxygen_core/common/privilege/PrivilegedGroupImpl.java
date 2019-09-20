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
import austeretony.oxygen_core.server.privilege.PrivilegesLoaderServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.text.TextFormatting;

public class PrivilegedGroupImpl implements PrivilegedGroup {      

    public final String groupName;    

    private String prefix, suffix;

    private TextFormatting nicknameColor, prefixColor, suffixColor, chatColor;

    private volatile long groupId;

    private final Map<String, Privilege> privileges = new ConcurrentHashMap<>(10);

    public static final PrivilegedGroupImpl 
    DEFAULT_GROUP = new PrivilegedGroupImpl("defaultGroup"),
    OPERATORS_GROUP = new PrivilegedGroupImpl("operatorsGroup");

    public PrivilegedGroupImpl(String groupName, long groupId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.prefix = this.suffix = "";
        this.nicknameColor = this.prefixColor = this.suffixColor = this.chatColor = TextFormatting.WHITE;
    }

    public PrivilegedGroupImpl(String groupName) {
        this(groupName, System.currentTimeMillis());
    }

    @Override
    public long getId() {
        return this.groupId;
    }

    @Override
    public String getName() {
        return this.groupName;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
        this.markEdited();
    }

    @Override
    public String getSuffix() {
        return this.suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
        this.markEdited();
    }

    @Override
    public TextFormatting getUsernameColor() {
        return this.nicknameColor;
    }

    public void setUsernameColor(TextFormatting color) {
        this.nicknameColor = color;
        this.markEdited();
    }

    @Override
    public TextFormatting getPrefixColor() {
        return this.prefixColor;
    }

    public void setPrefixColor(TextFormatting color) {
        this.prefixColor = color;
        this.markEdited();
    }

    @Override
    public TextFormatting getSuffixColor() {
        return this.suffixColor;
    }

    public void setSuffixColor(TextFormatting color) {
        this.suffixColor = color;
        this.markEdited();
    }

    @Override
    public TextFormatting getChatColor() {
        return this.chatColor;
    }

    public void setChatColor(TextFormatting color) {
        this.markEdited();
        this.chatColor = color;
    }


    @Override
    public Collection<Privilege> getPrivileges() {
        return this.privileges.values();
    }

    @Override
    public boolean hasPrivilege(String privilegeName) {
        return this.privileges.containsKey(privilegeName);
    }

    @Override
    public Privilege getPrivilege(String privilegeName) {
        return this.privileges.get(privilegeName);
    }

    @Override
    public void addPrivilege(Privilege privilege, boolean save) {
        this.privileges.put(privilege.getName(), privilege);
        if (save) {
            this.markEdited();
            PrivilegesLoaderServer.savePrivilegedGroupsAsync();
        }
    }

    @Override
    public void addPrivileges(boolean save, Privilege... privileges) {
        for (Privilege privilege : privileges)
            this.privileges.put(privilege.getName(), privilege);
        if (save) {
            this.markEdited();
            PrivilegesLoaderServer.savePrivilegedGroupsAsync();
        }
    }

    @Override
    public void removePrivilege(String privilegeName, boolean save) {
        this.privileges.remove(privilegeName);
        if (save) {
            this.markEdited();
            PrivilegesLoaderServer.savePrivilegedGroupsAsync();
        }
    }

    private void markEdited() {
        this.groupId = System.currentTimeMillis();
    }

    @Override
    public JsonObject serialize() {
        JsonObject groupObject = new JsonObject();
        groupObject.add(EnumPrivilegeFileKey.ID.name, new JsonPrimitive(this.getId()));
        groupObject.add(EnumPrivilegeFileKey.NAME.name, new JsonPrimitive(this.getName()));
        groupObject.add(EnumPrivilegeFileKey.PREFIX.name, new JsonPrimitive(this.getPrefix()));
        groupObject.add(EnumPrivilegeFileKey.SUFFIX.name, new JsonPrimitive(this.getSuffix()));
        groupObject.add(EnumPrivilegeFileKey.USERNAME_COLOR.name, new JsonPrimitive(OxygenUtils.formattingCode(this.getUsernameColor())));
        groupObject.add(EnumPrivilegeFileKey.PREFIX_COLOR.name, new JsonPrimitive(OxygenUtils.formattingCode(this.getPrefixColor())));
        groupObject.add(EnumPrivilegeFileKey.SUFFIX_COLOR.name, new JsonPrimitive(OxygenUtils.formattingCode(this.getSuffixColor())));
        groupObject.add(EnumPrivilegeFileKey.CHAT_COLOR.name, new JsonPrimitive(OxygenUtils.formattingCode(this.getChatColor())));
        JsonArray privilegesArray = new JsonArray();
        for (Privilege privilege : this.privileges.values())
            privilegesArray.add(privilege.serialize());
        groupObject.add(EnumPrivilegeFileKey.PRIVILEGES.name, privilegesArray);
        return groupObject;
    }

    public static PrivilegedGroupImpl deserializeServer(JsonObject jsonObject) {
        PrivilegedGroupImpl group = new PrivilegedGroupImpl(
                jsonObject.get(EnumPrivilegeFileKey.NAME.name).getAsString(),
                jsonObject.get(EnumPrivilegeFileKey.ID.name).getAsLong());
        group.prefix = jsonObject.get(EnumPrivilegeFileKey.PREFIX.name).getAsString();
        group.suffix = jsonObject.get(EnumPrivilegeFileKey.SUFFIX.name).getAsString();
        group.nicknameColor = OxygenUtils.formattingFromCode(jsonObject.get(EnumPrivilegeFileKey.USERNAME_COLOR.name).getAsString());
        group.prefixColor = OxygenUtils.formattingFromCode(jsonObject.get(EnumPrivilegeFileKey.PREFIX_COLOR.name).getAsString());
        group.suffixColor = OxygenUtils.formattingFromCode(jsonObject.get(EnumPrivilegeFileKey.SUFFIX_COLOR.name).getAsString());
        group.chatColor = OxygenUtils.formattingFromCode(jsonObject.get(EnumPrivilegeFileKey.CHAT_COLOR.name).getAsString());
        JsonArray privilegesArray = jsonObject.get(EnumPrivilegeFileKey.PRIVILEGES.name).getAsJsonArray();
        PrivilegeImpl privilege;
        for (JsonElement privilegesElement : privilegesArray) {
            privilege = PrivilegeImpl.deserialize(privilegesElement.getAsJsonObject());
            group.privileges.put(privilege.getName(), privilege);
        }
        OxygenMain.LOGGER.info("Loaded privileged group: {}.", group.getName());
        return group;
    }

    public static PrivilegedGroupImpl deserializeClient(JsonObject jsonObject) {
        PrivilegedGroupImpl group = new PrivilegedGroupImpl(
                jsonObject.get(EnumPrivilegeFileKey.NAME.name).getAsString(),
                jsonObject.get(EnumPrivilegeFileKey.ID.name).getAsLong());
        JsonArray privilegesArray = jsonObject.get(EnumPrivilegeFileKey.PRIVILEGES.name).getAsJsonArray();
        PrivilegeImpl privilege;
        for (JsonElement privilegesElement : privilegesArray) {
            privilege = PrivilegeImpl.deserialize(privilegesElement.getAsJsonObject());
            group.privileges.put(privilege.getName(), privilege);
        }
        OxygenMain.LOGGER.info("Loaded privileged group: {}.", group.getName());
        return group;
    }

    @Override
    public void write(ByteBuf buffer) {
        ByteBufUtils.writeString(this.getName(), buffer);
        buffer.writeLong(this.getId());
        buffer.writeByte(this.privileges.size());
        for (Privilege privilege : this.privileges.values())
            privilege.write(buffer);
    }

    public static PrivilegedGroupImpl read(ByteBuf buffer) {
        PrivilegedGroupImpl group = new PrivilegedGroupImpl(ByteBufUtils.readString(buffer), buffer.readLong());
        int amount = buffer.readByte();
        PrivilegeImpl privilege;
        for (int i = 0; i < amount; i++) {
            privilege = PrivilegeImpl.read(buffer);
            group.privileges.put(privilege.getName(), privilege);
        }
        return group;
    }
}
