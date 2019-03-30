package austeretony.oxygen.common.main;

import austeretony.oxygen.client.reference.ClientReference;
import austeretony.oxygen.common.privilege.command.EnumCommandPrivilegeArgs;
import austeretony.oxygen.common.util.OxygenUtils;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumChatMessages {

    COMMAND_PRIVILEGE_HELP,
    COMMAND_PRIVILEGE_INFO;

    @SideOnly(Side.CLIENT)
    public void show(String... args) {
        ITextComponent msg1, msg2, msg3;
        switch (this) {
        case COMMAND_PRIVILEGE_HELP:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.command.help.title"));
            for (EnumCommandPrivilegeArgs arg : EnumCommandPrivilegeArgs.values()) {
                if (arg != EnumCommandPrivilegeArgs.HELP) {
                    msg1 = new TextComponentString("/privilege " + OxygenUtils.keyFromEnum(arg));
                    msg2 = new TextComponentString(" - ");
                    msg1.getStyle().setColor(TextFormatting.GREEN);  
                    msg2.getStyle().setColor(TextFormatting.WHITE); 
                    ClientReference.showMessage(msg1.appendSibling(msg2.appendSibling(new TextComponentTranslation("oxygen.command.privilege.help." + OxygenUtils.keyFromEnum(arg)))));
                }
            }
            break;
        case COMMAND_PRIVILEGE_INFO:
            ClientReference.showMessage(new TextComponentString("Info for group: " + args[0]));
        }
    }
}
