package austeretony.oxygen_core.common.persistent;

import net.minecraft.nbt.NBTTagCompound;

public interface PersistentData {

    String getName();

    String getPath();

    boolean isChanged();

    void markChanged();

    void resetChangedMark();

    void writeToNBT(NBTTagCompound tagCompound);

    void readFromNBT(NBTTagCompound tagCompound);

    void reset();
}
