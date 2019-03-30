package austeretony.oxygen.common.main;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenChatHandler {

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        IPrivilegedGroup group = PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(OxygenHelperServer.uuid(event.getPlayer()));
        StringBuilder username = new StringBuilder();
        if (!group.getPrefix().isEmpty()) {
            username.append(group.getPrefixColor());
            username.append("[");
            username.append(group.getPrefix());
            username.append("]");
            username.append(TextFormatting.RESET);
        }
        username.append(group.getNicknameColor());
        username.append(event.getUsername());
        username.append(TextFormatting.RESET);
        if (!group.getSuffix().isEmpty()) {
            username.append(group.getSuffixColor());
            username.append("(");
            username.append(group.getSuffix());
            username.append(")");
            username.append(TextFormatting.RESET);
        }
        event.setComponent(new TextComponentTranslation("chat.type.text", username.toString(), ForgeHooks.newChatWithLinks(group.getChatColor() + event.getMessage())));
    }
}
