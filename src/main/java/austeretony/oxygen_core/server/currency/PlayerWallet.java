package austeretony.oxygen_core.server.currency;

import austeretony.oxygen_core.common.persistent.AbstractPersistentData;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerWallet extends AbstractPersistentData {

    private UUID playerUUID;

    private Map<Integer, Long> currenciesMap = new ConcurrentHashMap<>(3);

    public PlayerWallet(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public long getBalance(int index) {
        return currenciesMap.getOrDefault(index, 0L);
    }

    public void setBalance(int index, long value) {
        currenciesMap.put(index, value);
    }

    @Override
    public String getName() {
        return "core:player_wallet_server";
    }

    @Override
    public String getPath() {
        return OxygenServer.getDataFolder() + "/players/" + playerUUID + "/core/player_wallet.dat";
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        NBTTagList tagList = new NBTTagList();
        for (Map.Entry<Integer, Long> entry : currenciesMap.entrySet()) {
            NBTTagCompound entryTag = new NBTTagCompound();
            entryTag.setByte("currency_index", entry.getKey().byteValue());
            entryTag.setLong("value", entry.getValue());
            tagList.appendTag(entryTag);
        }
        tagCompound.setTag("currencies_list", tagList);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        NBTTagList tagList = tagCompound.getTagList("currencies_list", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound entryTag = tagList.getCompoundTagAt(i);
            currenciesMap.put((int) entryTag.getByte("currency_index"), entryTag.getLong("value"));
        }
    }

    @Override
    public void reset() {
        currenciesMap.clear();
    }
}
