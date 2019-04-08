package austeretony.oxygen.common.main;

import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.notification.IOxygenNotification;
import austeretony.oxygen.common.process.ITemporaryProcess;
import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class OxygenPlayerData implements Comparable<OxygenPlayerData> {

    private UUID uuid;

    private String username, title;

    private int dimension;

    private boolean processesExist;

    @SideOnly(Side.CLIENT)
    private boolean opped;

    private final Map<Long, ITemporaryProcess> processes = new ConcurrentHashMap<Long, ITemporaryProcess>();

    private final Map<Integer, ByteBuffer> additionalData = new ConcurrentHashMap<Integer, ByteBuffer>();

    public OxygenPlayerData() {}

    public OxygenPlayerData(UUID uuid, String username, String title, int dimension) {
        this.uuid = uuid;
        this.username = username;
        this.title = title;
        this.dimension = dimension;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDimension() {
        return this.dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    @SideOnly(Side.CLIENT)
    public boolean isOpped() {
        return this.opped;
    }

    @SideOnly(Side.CLIENT)
    public void setOpped(boolean flag) {
        this.opped = flag;
    }

    public Map<Long, ITemporaryProcess> getProcesses() {
        return this.processes;
    }

    public void addProcess(ITemporaryProcess process) {
        this.processes.put(process.getId(), process);
        this.processesExist = true;
    }

    public boolean haveProcess(long processId) {
        return this.processes.containsKey(processId);
    }

    public ITemporaryProcess getProcess(long processId) {
        return this.processes.get(processId);
    }

    public void process() {
        if (this.processesExist) {
            Iterator<ITemporaryProcess> iterator = this.processes.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.processesExist = this.processes.size() > 0;
                }
            }
        }
    }

    public Map<Integer, ByteBuffer> getData() {
        return this.additionalData;
    }

    public void addData(int id, ByteBuffer buffer) {
        this.additionalData.put(id, buffer);
    }

    public boolean exist(int id) {
        return this.additionalData.containsKey(id);
    }

    public ByteBuffer getData(int id) {
        return this.additionalData.get(id);
    }

    public void write(PacketBuffer buffer, int... identifiers) {
        PacketBufferUtils.writeString(this.getUsername(), buffer);
        PacketBufferUtils.writeString(this.getTitle(), buffer);
        buffer.writeInt(this.getDimension());
        ByteBuffer byteBuffer;
        for (int id : identifiers) {
            byteBuffer = this.additionalData.get(id);
            buffer.writeInt(byteBuffer.capacity());
            buffer.writeBytes(byteBuffer.array());//writing array because PacketBuffer#writeBytes(ByteBuffer) isn't working properly
        }
    }

    public void read(PacketBuffer buffer, int... identifiers) {
        this.setUsername(PacketBufferUtils.readString(buffer)); 
        this.setTitle(PacketBufferUtils.readString(buffer));
        this.setDimension(buffer.readInt());
        ByteBuffer byteBuffer;
        for (int id : identifiers) {
            byteBuffer = ByteBuffer.allocate(buffer.readInt());
            buffer.readBytes(byteBuffer);
            this.additionalData.put(id, byteBuffer);
        }
    }

    @Override
    public int compareTo(OxygenPlayerData other) {        
        return this.username.compareTo(other.username);
    }
}
