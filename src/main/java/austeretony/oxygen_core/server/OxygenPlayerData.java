package austeretony.oxygen_core.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.common.notification.EnumRequestReply;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.persistent.AbstractPersistentData;
import austeretony.oxygen_core.common.process.TemporaryProcess;
import austeretony.oxygen_core.common.util.MathUtils;
import austeretony.oxygen_core.common.util.StreamUtils;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import net.minecraft.entity.player.EntityPlayer;

public class OxygenPlayerData extends AbstractPersistentData {

    public static final int 
    CURRENCY_COINS_WATCHER_ID = 0,
    CURRENCY_COINS_INDEX = 0;

    private UUID playerUUID;

    private EnumActivityStatus status;

    private final Map<Long, TemporaryProcess> processes = new ConcurrentHashMap<>(5);

    private final Map<Integer, Long> currency = new ConcurrentHashMap<>(3);

    public final String dataPath;

    public OxygenPlayerData(UUID playerUUID) {
        this.status = EnumActivityStatus.ONLINE;    
        this.currency.put(CURRENCY_COINS_INDEX, 0L);
        this.playerUUID = playerUUID;
        this.dataPath = OxygenHelperServer.getDataFolder() + "/server/players/" + this.playerUUID + "/core/player_data.dat";
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

    public boolean haveTemporaryProcess(long processId) {
        return this.processes.containsKey(processId);
    }

    public TemporaryProcess getTemporaryProcess(long processId) {
        return this.processes.get(processId);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        if (this.haveTemporaryProcess(id)) {
            switch (reply) {
            case ACCEPT:
                ((Notification) this.getTemporaryProcess(id)).accepted(player);
                break;
            case REJECT:
                ((Notification) this.getTemporaryProcess(id)).rejected(player);
                break;
            }
            this.removeTemporaryProcess(id);
        }
    }

    public void runTemporaryProcesses() {
        if (!this.processes.isEmpty()) {
            Iterator<TemporaryProcess> iterator = this.processes.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                }
            }
        }
    }

    public void registerCurrency(int index) {
        this.currency.put(index, 0L);
    }

    public boolean currencyExist(int index) {
        return this.currency.containsKey(index);
    }

    public long getCurrency(int index) {
        return this.currency.get(index);
    }

    public boolean enoughCurrency(int index, long required) {
        return this.currency.get(index) >= required;
    }

    public void setCurrency(int index, long value) {
        this.currency.put(index, MathUtils.clamp(value, 0L, Long.MAX_VALUE));
    }

    public void addCurrency(int index, long value) {
        this.currency.put(index, MathUtils.clamp(this.currency.get(index) + value, 0L, Long.MAX_VALUE));
    }

    public void removeCurrency(int index, long value) {
        this.currency.put(index, MathUtils.clamp(this.currency.get(index) - value, 0L, Long.MAX_VALUE));
    }

    @Override
    public String getDisplayName() {
        return "oxygen_player_data";
    }

    @Override
    public long getSaveDelayMinutes() {
        return 0L;//unused
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

    public void reset() {
        this.processes.clear();
    }

    public enum EnumActivityStatus {

        ONLINE,
        AWAY,
        NOT_DISTURB,
        OFFLINE;

        public String localizedName() {
            return ClientReference.localize("oxygen.status." + this.toString().toLowerCase());
        }
    }
}
