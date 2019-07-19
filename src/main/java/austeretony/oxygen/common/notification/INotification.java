package austeretony.oxygen.common.notification;

import austeretony.oxygen.common.process.ITemporaryProcess;
import net.minecraft.entity.player.EntityPlayer;

public interface INotification extends ITemporaryProcess {

    EnumNotification getType();

    String getDescription();

    String[] getArguments();

    int getIndex();

    void accepted(EntityPlayer player);

    void rejected(EntityPlayer player);
}
