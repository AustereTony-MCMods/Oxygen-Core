package austeretony.oxygen.common.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.INotification;
import austeretony.oxygen.common.process.ITemporaryProcess;
import austeretony.oxygen.util.MathUtils;
import austeretony.oxygen.util.StreamUtils;
import net.minecraft.entity.player.EntityPlayer;

public class OxygenPlayerData implements IPersistentData {

    public static final int 
    CURRENCY_COINS_WATCHER_ID = 0,//'oxygen coins' - default currency for regular operations
    CURRENCY_COINS_INDEX = 0;//currency index for 'oxygen coins', there are supposed to be several currencies

    private UUID playerUUID;

    private EnumActivityStatus status;

    private final Map<Long, ITemporaryProcess> processes = new ConcurrentHashMap<Long, ITemporaryProcess>(5);

    private final Map<Integer, Integer> currency = new ConcurrentHashMap<Integer, Integer>(3);

    private volatile boolean syncing, requesting, requested, process;

    public final String dataPath;

    public OxygenPlayerData(UUID playerUUID) {
        this.status = EnumActivityStatus.ONLINE;    
        this.currency.put(CURRENCY_COINS_INDEX, 0);
        this.playerUUID = playerUUID;
        this.dataPath = "players/" + this.playerUUID + "/core/player_data.dat";
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public EnumActivityStatus getStatus() {
        return this.status;
    }

    public void setStatus(EnumActivityStatus status) {
        this.status = status;
    }

    public void addTemporaryProcess(ITemporaryProcess process) {
        this.processes.put(process.getId(), process);
        this.process = true;
    }

    public void removeTemporaryProcess(long processId) {
        this.processes.remove(processId);
        this.process = !this.processes.isEmpty();
    }

    public boolean haveTemporaryProcess(long processId) {
        return this.processes.containsKey(processId);
    }

    public ITemporaryProcess getTemporaryProcess(long processId) {
        return this.processes.get(processId);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        if (this.haveTemporaryProcess(id)) {
            switch (reply) {
            case ACCEPT:
                ((INotification) this.getTemporaryProcess(id)).accepted(player);
                break;
            case REJECT:
                ((INotification) this.getTemporaryProcess(id)).rejected(player);
                break;
            }
            this.removeTemporaryProcess(id);
        }
    }

    public void runTemporaryProcesses() {
        if (this.process) {
            Iterator<ITemporaryProcess> iterator = this.processes.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.process = !this.processes.isEmpty();
                }
            }
        }
    }

    public void registerCurrency(int index) {
        this.currency.put(index, 0);
    }

    public boolean currencyExist(int index) {
        return this.currency.containsKey(index);
    }

    public synchronized int getCurrency(int index) {
        return this.currency.get(index);
    }

    public synchronized boolean enoughCurrency(int index, int required) {
        return this.currency.get(index) >= required;
    }

    public synchronized void setCurrency(int index, int value) {
        this.currency.put(index, MathUtils.clamp(value, 0, Integer.MAX_VALUE));
    }

    public synchronized void addCurrency(int index, int value) {
        this.currency.put(index, MathUtils.clamp(this.currency.get(index) + value, 0, Integer.MAX_VALUE));
    }

    public synchronized void removeCurrency(int index, int value) {
        this.currency.put(index, MathUtils.clamp(this.currency.get(index) - value, 0, Integer.MAX_VALUE));
    }

    public boolean isSyncing() {
        return this.syncing;
    }

    public void setSyncing(boolean flag) {
        this.syncing = flag;
    }

    public boolean isRequesting() {
        return this.requesting;
    }

    public void setRequesting(boolean flag) {
        this.requesting = flag;
    }

    public boolean isRequested() {
        return this.requested;
    }

    public void setRequested(boolean flag) {
        this.requested = flag;
    }

    @Override
    public String getName() {
        return "player_data";
    }

    @Override
    public String getModId() {
        return OxygenMain.MODID;
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
        for (Map.Entry<Integer, Integer> entry : this.currency.entrySet()) {
            StreamUtils.write(entry.getKey().byteValue(), bos);
            StreamUtils.write(entry.getValue(), bos);
        }
    }

    @Override
    public void read(BufferedInputStream bis) throws IOException {
        this.playerUUID = StreamUtils.readUUID(bis);
        this.status = EnumActivityStatus.values()[StreamUtils.readByte(bis)];

        int 
        amount = StreamUtils.readByte(bis),
        i;
        for (i = 0; i < amount; i++)
            this.currency.put((int) StreamUtils.readByte(bis), StreamUtils.readInt(bis));
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
