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

    private String note;

    public final boolean ignored;

    public FriendListEntry(UUID playerUUID, boolean ignored) {
        this.playerUUID = playerUUID;
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

    public String getNote() {
        return this.note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.playerUUID, bos);
        StreamUtils.write(this.ignored, bos);
        StreamUtils.write(this.id, bos);
        StreamUtils.write(this.note, bos);
    }

    public static FriendListEntry read(BufferedInputStream bis) throws IOException {
        FriendListEntry friend = new FriendListEntry(StreamUtils.readUUID(bis), StreamUtils.readBoolean(bis));
        friend.setId(StreamUtils.readLong(bis));
        friend.setNote(StreamUtils.readString(bis));
        return friend;
    }

    public void write(PacketBuffer buffer) {
        PacketBufferUtils.writeUUID(this.playerUUID, buffer);
        buffer.writeBoolean(this.ignored);
        buffer.writeLong(this.id);
        PacketBufferUtils.writeString(this.note, buffer);
    }

    public static FriendListEntry read(PacketBuffer buffer) {
        FriendListEntry friend = new FriendListEntry(PacketBufferUtils.readUUID(buffer), buffer.readBoolean());
        friend.setId(buffer.readLong());
        friend.setNote(PacketBufferUtils.readString(buffer));
        return friend;
    }
}
