package austeretony.oxygen.common.privilege;

import austeretony.oxygen.common.reference.CommonReference;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PrivilegesChatHandler {

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        IPrivilegedGroup group = PrivilegeManagerServer.instance().getPlayerPrivilegedGroup(CommonReference.uuid(event.getPlayer()));
        StringBuilder username = new StringBuilder();
        if (!group.getPrefix().isEmpty()) {
            username.append(group.getPrefixColor());
            username.append("[");
            username.append(group.getPrefix());
            username.append("]");
            username.append(TextFormatting.RESET);
        }
        username.append(group.getUsernameColor());
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
