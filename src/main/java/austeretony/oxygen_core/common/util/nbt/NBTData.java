package austeretony.oxygen_core.common.util.nbt;

import net.minecraft.nbt.NBTTagCompound;

public interface NBTData {

    NBTTagCompound writeToNBT();

    void readFromNBT(NBTTagCompound tagCompound);
}
