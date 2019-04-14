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

import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.IOxygenNotification;
import austeretony.oxygen.common.process.ITemporaryProcess;
import austeretony.oxygen.common.util.StreamUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;

public class OxygenPlayerData {

    private UUID playerUUID;

    private EnumStatus status;

    private boolean processesExist;

    private final Map<Long, ITemporaryProcess> temporaryProcesses = new ConcurrentHashMap<Long, ITemporaryProcess>();

    private final Map<Long, FriendListEntry> friendList = new ConcurrentHashMap<Long, FriendListEntry>();

    //cached in memory just for fast access
    private final Map<UUID, Long> friendListAccess = new ConcurrentHashMap<UUID, Long>();

    private int friendsAmount, ignoredAmount, currency;

    private boolean syncing, requesting, requested;

    private boolean opped;//for client only

    public OxygenPlayerData() {
        this.status = EnumStatus.ONLINE;
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

    public EnumStatus getStatus() {
        return this.status;
    }

    public void setStatus(EnumStatus status) {
        this.status = status;
    }

    public Collection<ITemporaryProcess> getProcesses() {
        return this.temporaryProcesses.values();
    }

    public void addProcess(ITemporaryProcess process) {
        this.temporaryProcesses.put(process.getId(), process);
        this.processesExist = true;
    }

    public void removeProcess(long processId) {
        this.temporaryProcesses.remove(processId);
        this.processesExist = this.temporaryProcesses.size() > 0;
    }

    public boolean haveProcess(long processId) {
        return this.temporaryProcesses.containsKey(processId);
    }

    public ITemporaryProcess getProcess(long processId) {
        return this.temporaryProcesses.get(processId);
    }

    public void processRequestReply(EntityPlayer player, EnumRequestReply reply, long id) {
        if (this.haveProcess(id)) {
            switch (reply) {
            case ACCEPT:
                ((IOxygenNotification) this.getProcess(id)).accepted(player);
                break;
            case REJECT:
                ((IOxygenNotification) this.getProcess(id)).rejected(player);
                break;
            }
            this.removeProcess(id);
        }
    }

    public void process() {
        if (this.processesExist) {
            Iterator<ITemporaryProcess> iterator = this.temporaryProcesses.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.processesExist = this.temporaryProcesses.size() > 0;
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

    public int getCurrency() {
        return this.currency;
    }

    public int setCurrency(int value) {
        return this.currency = value;
    }

    public int addCurrency(int value) {
        return this.currency += value;
    }

    public int removeCurrency(int value) {
        return this.currency -= value;
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

    public boolean isOpped() {
        return this.opped;
    }

    public void setOpped(boolean flag) {
        this.opped = flag;
    }

    public void write(BufferedOutputStream bos) throws IOException {
        StreamUtils.write(this.playerUUID, bos);
        StreamUtils.write(this.currency, bos);
        StreamUtils.write((byte) this.status.ordinal(), bos);
        StreamUtils.write((short) this.friendList.size(), bos);
        for (FriendListEntry listEntry : this.friendList.values()) 
            listEntry.write(bos);
    }

    public void read(BufferedInputStream bis) throws IOException {
        this.playerUUID = StreamUtils.readUUID(bis);
        this.currency = StreamUtils.readInt(bis);
        this.status = EnumStatus.values()[StreamUtils.readByte(bis)];
        int amount = StreamUtils.readShort(bis);
        for (int i = 0; i < amount; i++)
            this.addFriendListEntry(FriendListEntry.read(bis));
    }

    public enum EnumStatus {

        ONLINE("oxygen.status.online"),
        AWAY("oxygen.status.away"),
        NOT_DISTURB("oxygen.status.notDisturb"),
        OFFLINE("oxygen.status.offline");

        public final String key;

        EnumStatus(String key) {
            this.key = key;
        }       

        public String getLocalizedName() {
            return I18n.format(this.key);
        }
    }
}
