package austeretony.oxygen.common.privilege.api;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.IPrivilege;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.io.EnumPrivilegeFileKey;
import austeretony.oxygen.common.privilege.io.PrivilegeLoaderServer;
import austeretony.oxygen.util.OxygenUtils;
import austeretony.oxygen.util.PacketBufferUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.TextFormatting;

public class PrivilegedGroup implements IPrivilegedGroup {      

    public final String groupName;    

    private String prefix, suffix;

    private TextFormatting nicknameColor, prefixColor, suffixColor, chatColor;

    private volatile long groupId;

    private final Map<String, IPrivilege> privileges = new ConcurrentHashMap<String, IPrivilege>(10);

    public static final PrivilegedGroup 
    DEFAULT_GROUP = new PrivilegedGroup("defaultGroup"),
    OPERATORS_GROUP = new PrivilegedGroup("operatorsGroup");

    public PrivilegedGroup(String groupName, long groupId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.prefix = this.suffix = "";
        this.nicknameColor = this.prefixColor = this.suffixColor = this.chatColor = TextFormatting.WHITE;
    }

    public PrivilegedGroup(String groupName) {
        this(groupName, OxygenUtils.createDataStampedId());
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
    public Collection<IPrivilege> getPrivileges() {
        return this.privileges.values();
    }

    @Override
    public boolean hasPrivilege(String privilegeName) {
        return this.privileges.containsKey(privilegeName);
    }

    @Override
    public IPrivilege getPrivilege(String privilegeName) {
        return this.privileges.get(privilegeName);
    }

    @Override
    public void addPrivilege(IPrivilege privilege, boolean save) {
        this.privileges.put(privilege.getName(), privilege);
        if (save) {
            this.markEdited();
            PrivilegeLoaderServer.savePrivilegedGroupsDelegated();
        }
    }

    @Override
    public void addPrivileges(boolean save, IPrivilege... privileges) {
        for (IPrivilege privilege : privileges)
            this.privileges.put(privilege.getName(), privilege);
        if (save) {
            this.markEdited();
            PrivilegeLoaderServer.savePrivilegedGroupsDelegated();
        }
    }

    @Override
    public void removePrivilege(String privilegeName, boolean save) {
        this.privileges.remove(privilegeName);
        if (save) {
            this.markEdited();
            PrivilegeLoaderServer.savePrivilegedGroupsDelegated();
        }
    }

    private void markEdited() {
        this.groupId = OxygenUtils.createDataStampedId();
    }

    @Override
    public JsonObject serialize() {
        JsonObject groupObject = new JsonObject();
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.ID), new JsonPrimitive(this.getId()));
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.NAME), new JsonPrimitive(this.getName()));
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PREFIX), new JsonPrimitive(this.getPrefix()));
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.SUFFIX), new JsonPrimitive(this.getSuffix()));
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.USERNAME_COLOR), new JsonPrimitive(OxygenUtils.formattingCode(this.getUsernameColor())));
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PREFIX_COLOR), new JsonPrimitive(OxygenUtils.formattingCode(this.getPrefixColor())));
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.SUFFIX_COLOR), new JsonPrimitive(OxygenUtils.formattingCode(this.getSuffixColor())));
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.CHAT_COLOR), new JsonPrimitive(OxygenUtils.formattingCode(this.getChatColor())));
        JsonArray privilegesArray = new JsonArray();
        for (IPrivilege privilege : this.privileges.values())
            privilegesArray.add(privilege.serialize());
        groupObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PRIVILEGES), privilegesArray);
        return groupObject;
    }

    public static PrivilegedGroup deserializeServer(JsonObject jsonObject) {
        PrivilegedGroup group = new PrivilegedGroup(
                jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.NAME)).getAsString(),
                jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.ID)).getAsLong());
        group.prefix = jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PREFIX)).getAsString();
        group.suffix = jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.SUFFIX)).getAsString();
        group.nicknameColor = OxygenUtils.formattingFromCode(jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.USERNAME_COLOR)).getAsString());
        group.prefixColor = OxygenUtils.formattingFromCode(jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PREFIX_COLOR)).getAsString());
        group.suffixColor = OxygenUtils.formattingFromCode(jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.SUFFIX_COLOR)).getAsString());
        group.chatColor = OxygenUtils.formattingFromCode(jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.CHAT_COLOR)).getAsString());
        JsonArray privilegesArray = jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PRIVILEGES)).getAsJsonArray();
        Privilege privilege;
        for (JsonElement privilegesElement : privilegesArray) {
            privilege = Privilege.deserialize(privilegesElement.getAsJsonObject());
            group.privileges.put(privilege.getName(), privilege);
        }
        OxygenMain.OXYGEN_LOGGER.info("Loaded privileged group: {}.", group.getName());
        return group;
    }

    public static PrivilegedGroup deserializeClient(JsonObject jsonObject) {
        PrivilegedGroup group = new PrivilegedGroup(
                jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.NAME)).getAsString(),
                jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.ID)).getAsLong());
        JsonArray privilegesArray = jsonObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PRIVILEGES)).getAsJsonArray();
        Privilege privilege;
        for (JsonElement privilegesElement : privilegesArray) {
            privilege = Privilege.deserialize(privilegesElement.getAsJsonObject());
            group.privileges.put(privilege.getName(), privilege);
        }
        OxygenMain.OXYGEN_LOGGER.info("Loaded privileged group: {}.", group.getName());
        return group;
    }

    @Override
    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeString(this.getName(), buffer);
        buffer.writeLong(this.getId());
        buffer.writeByte(this.privileges.size());
        for (IPrivilege privilege : this.privileges.values())
            privilege.write(buffer);
    }

    public static PrivilegedGroup read(PacketBuffer buffer) {
        PrivilegedGroup group = new PrivilegedGroup(PacketBufferUtils.readString(buffer), buffer.readLong());
        int amount = buffer.readByte();
        Privilege privilege;
        for (int i = 0; i < amount; i++) {
            privilege = Privilege.read(buffer);
            group.privileges.put(privilege.getName(), privilege);
        }
        return group;
    }
}
