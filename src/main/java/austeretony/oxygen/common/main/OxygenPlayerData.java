package austeretony.oxygen.common.main;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.INotification;
import austeretony.oxygen.common.process.ITemporaryProcess;
import austeretony.oxygen.common.util.MathUtils;
import austeretony.oxygen.common.util.StreamUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class OxygenPlayerData implements IPersistentData {

    private UUID playerUUID;

    private EnumActivityStatus status;

    private final Map<Long, ITemporaryProcess> temporaryProcesses = new ConcurrentHashMap<Long, ITemporaryProcess>(5);

    private final Map<Integer, Integer> currency = new ConcurrentHashMap<Integer, Integer>(3);

    public static final int CURRENCY_GOLD_INDEX = 0;

    private final Map<Long, FriendListEntry> friendList = new ConcurrentHashMap<Long, FriendListEntry>(5);

    //cached in memory just for fast access
    private final Map<UUID, Long> friendListAccess = new ConcurrentHashMap<UUID, Long>(5);

    private int friendsAmount, ignoredAmount;

    private volatile boolean syncing, requesting, requested, temporaryProcessesExist;

    public OxygenPlayerData() {
        this.status = EnumActivityStatus.ONLINE;    
        this.currency.put(CURRENCY_GOLD_INDEX, 0);
    }

    public OxygenPlayerData(UUID playerUUID) {
        this();
        this.playerUUID = playerUUID;
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

    public Set<Long> getFriendListEntriesIds() {
        return this.friendList.keySet();
    }

    public Collection<FriendListEntry> getFriendListEntries() {
        return this.friendList.values();
    }

    public void clearFriendListEntries() {
        this.friendList.clear();
        this.friendListAccess.clear();
        this.friendsAmount = 0;
        this.ignoredAmount = 0;
    }

    public int getFriendsAmount() {
        return this.friendsAmount;
    }

    public int getIgnoredAmount() {
        return this.ignoredAmount;
    }

    public boolean haveFriendListEntryForUUID(UUID playerUUID) {
        return this.friendListAccess.containsKey(playerUUID);
    }

    public long getFriendListEntryIdByUUID(UUID playerUUID) {
        return this.friendListAccess.get(playerUUID);
    }

    public FriendListEntry getFriendListEntryByUUID(UUID playerUUID) {
        return this.friendList.get(this.friendListAccess.get(playerUUID));
    }

    public FriendListEntry getFriendListEntry(long id) {
        return this.friendList.get(id);
    }

    public void addFriendListEntry(FriendListEntry friend) {
        if (friend.ignored)
            this.ignoredAmount++;
        else
            this.friendsAmount++;
        this.friendList.put(friend.getId(), friend);
        this.friendListAccess.put(friend.playerUUID, friend.getId());
    }

    public void removeFriendListEntry(long id) {
        if (this.friendList.containsKey(id)) {
            FriendListEntry entry = this.friendList.remove(id);
            if (entry.ignored)
                this.ignoredAmount--;
            else
                this.friendsAmount--;
            this.friendListAccess.remove(entry.playerUUID);
        }
    } 

    public void removeFriendListEntry(UUID playerUUID) {
        if (this.friendListAccess.containsKey(playerUUID)) {
            FriendListEntry entry = this.friendList.remove(this.friendListAccess.remove(playerUUID));
            if (entry.ignored)
                this.ignoredAmount--;
            else
                this.friendsAmount--;
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
        return "oxygen player data";
    }

    @Override
    public String getModId() {
        return OxygenMain.MODID;
    }

    @Override
    public String getPath() {
        return "oxygen/profile.dat";
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

        StreamUtils.write((short) this.friendList.size(), bos);
        for (FriendListEntry listEntry : this.friendList.values()) 
            listEntry.write(bos);
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

        amount = StreamUtils.readShort(bis);
        for (i = 0; i < amount; i++)
            this.addFriendListEntry(FriendListEntry.read(bis));
    }

    public void reset() {
        this.temporaryProcesses.clear();
        this.clearFriendListEntries();
    }

    public enum EnumActivityStatus {

        ONLINE,
        AWAY,
        NOT_DISTURB,
        OFFLINE;

        public String localizedName() {
            return I18n.format("oxygen.status." + this.toString().toLowerCase());
        }
    }
}
