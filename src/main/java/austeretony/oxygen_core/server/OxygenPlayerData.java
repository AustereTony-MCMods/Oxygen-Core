package austeretony.oxygen_core.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.common.EnumActivityStatus;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.instant.InstantData;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncInstantData;
import austeretony.oxygen_core.common.network.client.CPSyncWatchedValue;
import austeretony.oxygen_core.common.notification.EnumRequestReply;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.persistent.AbstractPersistentData;
import austeretony.oxygen_core.common.process.TemporaryProcess;
import austeretony.oxygen_core.common.util.StreamUtils;
import austeretony.oxygen_core.common.watcher.WatchedValue;
import austeretony.oxygen_core.server.api.CurrencyHelperServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.currency.CurrencyProvider;
import austeretony.oxygen_core.server.instant.InstantDataRegistryServer;
import austeretony.oxygen_core.server.network.NetworkRequestEntry;
import austeretony.oxygen_core.server.network.NetworkRequestsRegistryServer;
import austeretony.oxygen_core.server.timeout.TimeOutEntry;
import austeretony.oxygen_core.server.timeout.TimeOutRegistryServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenPlayerData extends AbstractPersistentData {

    private UUID playerUUID;

    private EnumActivityStatus status;

    private final Map<Long, TemporaryProcess> processes = new ConcurrentHashMap<>(5);

    private final Map<Integer, Long> currency = new ConcurrentHashMap<>(5);

    private final Map<Integer, NetworkRequestEntry> networkRequests = new ConcurrentHashMap<>();

    private final Map<Integer, TimeOutEntry> timeOutEntries = new ConcurrentHashMap<>(5);

    private final Map<Integer, WatchedValue> watchedValues = new ConcurrentHashMap<>(5);

    private final Map<UUID, Boolean> trackedEntities = new ConcurrentHashMap<>();

    public final String dataPath;

    public OxygenPlayerData(UUID playerUUID) {
        this.status = EnumActivityStatus.ONLINE;    
        this.playerUUID = playerUUID;

        this.dataPath = OxygenHelperServer.getDataFolder() + "/server/players/" + this.playerUUID + "/core/player_data.dat";

        for (CurrencyProvider provider : CurrencyHelperServer.getCurrencyProviders())
            this.currency.put(provider.getIndex(), 0L);

        NetworkRequestsRegistryServer.REGISTRY.forEach((data)->this.networkRequests.put(data.id, new NetworkRequestEntry(data.cooldownMillis)));
        TimeOutRegistryServer.REGISTRY.forEach((data)->this.timeOutEntries.put(data.id, new TimeOutEntry(data.timeOutMillis)));

        WatchedValuesRegistryServer.REGISTRY.forEach((value)->this.watchedValues.put(value.id, value.copy()));
    }

    public void init() {
        OxygenHelperServer.getSchedulerExecutorService().schedule(()->{
            for (WatchedValue value : this.watchedValues.values())
                value.init(this.playerUUID);
        }, 3L, TimeUnit.SECONDS);
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public EnumActivityStatus getActivityStatus() {
        return this.status;
    }

    public void setActivityStatus(EnumActivityStatus status) {
        this.status = status;
    }

    public void addTemporaryProcess(TemporaryProcess process) {
        this.processes.put(process.getId(), process);
    }

    public void removeTemporaryProcess(long processId) {
        this.processes.remove(processId);
    }

    public TemporaryProcess getTemporaryProcess(long processId) {
        return this.processes.get(processId);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long processId) {
        TemporaryProcess process = this.getTemporaryProcess(processId);
        if (process != null) {
            switch (reply) {
            case ACCEPT:
                ((Notification) process).accepted(player);
                break;
            case REJECT:
                ((Notification) process).rejected(player);
                break;
            }
            this.removeTemporaryProcess(processId);
        }
    }

    public long getCurrency(int index) {
        return this.currency.get(index);
    }

    public void setCurrency(int index, long value) {
        this.currency.put(index, value);
    }

    public boolean isNetworkRequestAvailable(int id) {
        NetworkRequestEntry entry = this.networkRequests.get(id);
        return entry != null && entry.requestAvailable();
    }

    public boolean checkTimeOut(int id) {
        return this.timeOutEntries.get(id).checkTimeOut();
    }

    public void resetTimeOut(int id) {
        this.timeOutEntries.get(id).resetTimeOut();
    }

    public WatchedValue getWatchedValue(int id) {
        return this.watchedValues.get(id);
    }

    public void setWatchedValueBoolean(int id, boolean value) {
        this.watchedValues.get(id).setBoolean(value);
    }

    public void setWatchedValueByte(int id, int value) {
        this.watchedValues.get(id).setByte(value);
    }

    public void setWatchedValueShort(int id, int value) {
        this.watchedValues.get(id).setShort(value);
    }

    public void setWatchedValueInt(int id, int value) {
        this.watchedValues.get(id).setInt(value);
    }

    public void setWatchedValueLong(int id, long value) {
        this.watchedValues.get(id).setLong(value);
    }

    public void setWatchedValueFloat(int id, float value) {
        this.watchedValues.get(id).setFloat(value);
    }

    public void setWatchedValueDouble(int id, double value) {
        this.watchedValues.get(id).setDouble(value);
    }

    public void addTrackedEntity(UUID entityUUID, boolean persistent) {
        this.trackedEntities.put(entityUUID, persistent);
    }

    public void removeTrackedEntity(UUID entityUUID, boolean ignorePersistance) {
        if (ignorePersistance) {
            this.trackedEntities.remove(entityUUID);
        } else {
            Boolean persistent = this.trackedEntities.get(entityUUID);
            if (persistent != null) {
                if (!persistent.booleanValue())
                    this.trackedEntities.remove(entityUUID);
            }
        }
    }

    public void clearTrackedEntities() {
        this.trackedEntities.clear();
    }

    public void process() {
        if (!this.processes.isEmpty()) {
            Iterator<TemporaryProcess> iterator = this.processes.values().iterator();
            while (iterator.hasNext())
                if (iterator.next().isExpired())
                    iterator.remove();
        }

        for (CurrencyProvider provider : CurrencyHelperServer.getCurrencyProviders())
            if (provider.forceSync())
                this.getWatchedValue(provider.getIndex()).setChanged(true);

        EntityPlayerMP playerMP = CommonReference.playerByUUID(this.playerUUID);
        if (playerMP == null) return;

        for (WatchedValue value : this.watchedValues.values()) {
            if (value.isChanged()) {
                value.setChanged(false);
                OxygenMain.network().sendTo(new CPSyncWatchedValue(value.id, value.getBuffer()), playerMP);
            }
        }

        this.sync(playerMP);
    }

    public void sync(EntityPlayerMP playerMP) {
        if (this.trackedEntities.isEmpty()) return;

        ByteBuf buffer = null;
        try {
            buffer = Unpooled.buffer();

            buffer.writeShort(this.trackedEntities.size());
            EntityLivingBase entityLiving;
            for (UUID entityUUID : this.trackedEntities.keySet()) {
                entityLiving = (EntityLivingBase) CommonReference.getServer().getEntityFromUuid(entityUUID);
                if (entityLiving != null) {
                    buffer.writeInt(entityLiving.getEntityId());
                    for (InstantData data : InstantDataRegistryServer.REGISTRY) {
                        if (data.isValid()) {
                            buffer.writeByte(data.getIndex());
                            data.write(entityLiving, buffer);
                        } else
                            buffer.writeByte(- 1);
                    }
                } else
                    buffer.writeInt(- 1);
            }

            byte[] compressed = new byte[buffer.writerIndex()];
            buffer.getBytes(0, compressed);

            OxygenMain.network().sendTo(new CPSyncInstantData(compressed), playerMP);
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    @Override
    public String getDisplayName() {
        return "core:player_data_server";
    }

    @Override
    public String getPath() {
        return this.dataPath;
    }

    @Override
    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.playerUUID, bos);
        StreamUtils.write((byte) this.status.ordinal(), bos);

        StreamUtils.write((byte) this.currency.size(), bos);
        for (Map.Entry<Integer, Long> entry : this.currency.entrySet()) {
            StreamUtils.write(entry.getKey().byteValue(), bos);
            StreamUtils.write(entry.getValue(), bos);
        }
    }

    @Override
    public void read(BufferedInputStream bis) throws IOException {
        this.playerUUID = StreamUtils.readUUID(bis);
        this.status = EnumActivityStatus.values()[StreamUtils.readByte(bis)];

        int amount = StreamUtils.readByte(bis);
        for (int i = 0; i < amount; i++)
            this.currency.put((int) StreamUtils.readByte(bis), StreamUtils.readLong(bis));
    }

    @Override
    public void reset() {}
}
