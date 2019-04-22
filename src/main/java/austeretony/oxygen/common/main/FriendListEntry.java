package austeretony.oxygen.common.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import austeretony.oxygen.common.util.PacketBufferUtils;
import austeretony.oxygen.common.util.StreamUtils;
import net.minecraft.network.PacketBuffer;

public class FriendListEntry {

    public static final int MAX_NOTE_LENGTH = 40;

    private long id;

    public final UUID playerUUID;

    public final String username;

    private String note;

    public final boolean ignored;

    private int dimension;

    private long lastActivityTime;

    public FriendListEntry(UUID playerUUID, String username, boolean ignored) {
        this.playerUUID = playerUUID;
        this.username = username;
        this.ignored = ignored;
        this.note = "";
    }

    public long getId() {
        return this.id;
    }

    public FriendListEntry createId() {
        this.id = Long.parseLong(OxygenMain.SIMPLE_ID_DATE_FORMAT.format(new Date()));
        return this;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(int id) {
        this.dimension = id;
    }

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getLastActivityTime() {
        return this.lastActivityTime;
    }

    public void setLastActivityTime(long value) {
        this.lastActivityTime = value;
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.playerUUID, bos);
        StreamUtils.write(this.username, bos);
        StreamUtils.write(this.ignored, bos);
        StreamUtils.write(this.id, bos);
        StreamUtils.write(this.dimension, bos);
        StreamUtils.write(this.lastActivityTime, bos);
        StreamUtils.write(this.note, bos);
    }

    public static FriendListEntry read(BufferedInputStream bis) throws IOException {
        FriendListEntry friend = new FriendListEntry(StreamUtils.readUUID(bis), StreamUtils.readString(bis), StreamUtils.readBoolean(bis));
        friend.setId(StreamUtils.readLong(bis));
        friend.setDimension(StreamUtils.readInt(bis));
        friend.setLastActivityTime(StreamUtils.readLong(bis));
        friend.setNote(StreamUtils.readString(bis));
        return friend;
    }

    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeUUID(this.playerUUID, buffer);
        PacketBufferUtils.writeString(this.username, buffer);
        buffer.writeBoolean(this.ignored);
        buffer.writeLong(this.id);
        buffer.writeInt(this.dimension);
        buffer.writeLong(this.lastActivityTime);
        PacketBufferUtils.writeString(this.note, buffer);
    }

    public static FriendListEntry read(PacketBuffer buffer) {
        FriendListEntry friend = new FriendListEntry(PacketBufferUtils.readUUID(buffer), PacketBufferUtils.readString(buffer), buffer.readBoolean());
        friend.setId(buffer.readLong());
        friend.setDimension(buffer.readInt());
        friend.setLastActivityTime(buffer.readLong());
        friend.setNote(PacketBufferUtils.readString(buffer));
        return friend;
    }
}
