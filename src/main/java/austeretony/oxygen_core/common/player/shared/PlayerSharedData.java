package austeretony.oxygen_core.common.player.shared;

import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.nbt.NBTUtils;
import austeretony.oxygen_core.common.util.value.TypedValue;
import austeretony.oxygen_core.common.util.value.ValueType;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class PlayerSharedData {

    private UUID playerUUID;
    private String username;
    private int entityId;

    private final Map<Integer, TypedValue> valuesMap = new LinkedHashMap<>(3);

    private boolean isOnline;

    public PlayerSharedData() {
        for (Map.Entry<Integer, ValueType> entry : SharedDataRegistry.getRegistryMap().entrySet()) {
            valuesMap.put(entry.getKey(), ValueType.createValue(entry.getValue()));
        }
    }

    public PlayerSharedData(UUID playerUUID, String username) {
        this();
        this.playerUUID = playerUUID;
        this.username = username;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getUsername() {
        return username;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int id) {
        entityId = id;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean flag) {
        isOnline = flag;
    }

    public void initValue(int id, ValueType type) {
        valuesMap.put(id, ValueType.createValue(type));
    }

    @Nullable
    public <T> TypedValue<T> get(int id) {
        return valuesMap.get(id);
    }

    @Nonnull
    public <T> T getValue(int id, T defaultValue) {
        TypedValue<T> typedValue = get(id);
        return typedValue != null ? typedValue.getValue() : defaultValue;
    }

    public <T> void setValue(int id, T value) {
        TypedValue<T> typedValue = get(id);
        if (typedValue != null) {
            typedValue.setValue(value);
        }
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setTag("player_uuid", NBTUtils.toNBTUUID(playerUUID));
        tagCompound.setString("username", username);

        NBTTagList valuesList = new NBTTagList();
        for (Map.Entry<Integer, TypedValue> entry : valuesMap.entrySet()) {
            NBTTagCompound valueTag = new NBTTagCompound();
            valueTag.setShort("id", entry.getKey().shortValue());
            valueTag.setTag("value", ValueType.toNBT(entry.getValue()));
            valuesList.appendTag(valueTag);
        }
        tagCompound.setTag("values_list", valuesList);

        return tagCompound;
    }

    public static PlayerSharedData readFromNBT(NBTTagCompound tagCompound) {
        PlayerSharedData sharedData = new PlayerSharedData();
        sharedData.playerUUID = NBTUtils.fromNBTUUID(tagCompound.getTag("player_uuid"));
        sharedData.username = tagCompound.getString("username");

        NBTTagList valuesList = tagCompound.getTagList("values_list", 10);
        for (int i = 0; i < valuesList.tagCount(); i++) {
            NBTTagCompound valueTag = valuesList.getCompoundTagAt(i);

            int id = valueTag.getShort("id");
            if (!SharedDataRegistry.getRegistryMap().containsKey(id)) continue;
            TypedValue value = ValueType.fromNBT(valueTag.getTag("value"));

            sharedData.valuesMap.put(id, value);
        }

        return sharedData;
    }

    public void write(ByteBuf buffer) {
        ByteBufUtils.writeUUID(playerUUID, buffer);
        ByteBufUtils.writeString(username, buffer);
        buffer.writeInt(entityId);
        buffer.writeBoolean(isOnline);

        for (TypedValue value : valuesMap.values()) {
            value.write(buffer);
        }
    }

    public void read(ByteBuf buffer) {
        playerUUID = ByteBufUtils.readUUID(buffer);
        username = ByteBufUtils.readString(buffer);
        entityId = buffer.readInt();
        isOnline = buffer.readBoolean();

        for (TypedValue value : valuesMap.values()) {
            value.read(buffer);
        }
    }

    public PlayerSharedData copy() {
        PlayerSharedData sharedData = new PlayerSharedData(playerUUID, username);
        sharedData.isOnline = isOnline;

        for (Map.Entry<Integer, TypedValue> entry : valuesMap.entrySet()) {
            sharedData.valuesMap.put(entry.getKey(), entry.getValue().copy());
        }

        return sharedData;
    }

    @Override
    public String toString() {
        return "PlayerSharedData[" +
                "playerUUID= " + playerUUID + ", " +
                "username= " + username +
                "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerSharedData that = (PlayerSharedData) o;
        return playerUUID.equals(that.playerUUID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerUUID);
    }
}
