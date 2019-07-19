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
    CURRENCY_GOLD_ID = 0,
    CURRENCY_GOLD_INDEX = 0;

    private UUID playerUUID;

    private EnumActivityStatus status;

    private final Map<Long, ITemporaryProcess> temporaryProcesses = new ConcurrentHashMap<Long, ITemporaryProcess>(5);

    private final Map<Integer, Integer> currency = new ConcurrentHashMap<Integer, Integer>(3);

    private volatile boolean syncing, requesting, requested, temporaryProcessesExist;

    public final String dataPath;

    public OxygenPlayerData(UUID playerUUID) {
        this.status = EnumActivityStatus.ONLINE;    
        this.currency.put(CURRENCY_GOLD_INDEX, 0);
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
        this.temporaryProcesses.put(process.getId(), process);
        this.temporaryProcessesExist = true;
    }

    public void removeTemporaryProcess(long processId) {
        this.temporaryProcesses.remove(processId);
        this.temporaryProcessesExist = this.temporaryProcesses.size() > 0;
    }

    public boolean haveTemporaryProcess(long processId) {
        return this.temporaryProcesses.containsKey(processId);
    }

    public ITemporaryProcess getTemporaryProcess(long processId) {
        return this.temporaryProcesses.get(processId);
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
        if (this.temporaryProcessesExist) {
            Iterator<ITemporaryProcess> iterator = this.temporaryProcesses.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.temporaryProcessesExist = this.temporaryProcesses.size() > 0;
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

    public int getCurrency(int index) {
        return this.currency.get(index);
    }

    public boolean enoughCurrency(int index, int required) {
        return this.currency.get(index) >= required;
    }

    public int setCurrency(int index, int value) {
        return this.currency.put(index, MathUtils.clamp(value, 0, Integer.MAX_VALUE));
    }

    public int addCurrency(int index, int value) {
        return this.currency.put(index, MathUtils.clamp(this.currency.get(index) + value, 0, Integer.MAX_VALUE));
    }

    public int removeCurrency(int index, int value) {
        return this.currency.put(index, MathUtils.clamp(this.currency.get(index) - value, 0, Integer.MAX_VALUE));
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
        this.temporaryProcesses.clear();
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
