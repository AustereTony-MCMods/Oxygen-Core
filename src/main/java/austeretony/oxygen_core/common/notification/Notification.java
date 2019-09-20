package austeretony.oxygen_core.common.notification;

import austeretony.oxygen_core.common.process.TemporaryProcess;
import net.minecraft.entity.player.EntityPlayer;

public interface Notification extends TemporaryProcess {

    EnumNotification getType();

    String getDescription();

    String[] getArguments();

    int getIndex();

    void accepted(EntityPlayer player);

    void rejected(EntityPlayer player);
}
