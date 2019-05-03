package austeretony.oxygen.client;

import java.util.LinkedHashSet;
import java.util.Set;

import austeretony.oxygen.client.gui.interaction.InteractionGUIScreen;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.core.api.ClientReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPOxygenRequest;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class InteractionManagerClient {

    private final OxygenManagerClient manager;

    public static final int MAX_ACTIONS_AMOUNT = 6;//only 6 shared actions for player interaction menu (7 in total)

    private final Set<IInteractionExecutor> actions = new LinkedHashSet<IInteractionExecutor>(MAX_ACTIONS_AMOUNT);

    public InteractionManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public Set<IInteractionExecutor> getActions() {
        return this.actions;
    }

    public void registerAction(IInteractionExecutor action) {
        if (this.actions.size() < MAX_ACTIONS_AMOUNT)
            this.actions.add(action);
    }

    public void openPlayerInteractionMenuSynced() {
        Entity target = ClientReference.getMinecraft().pointedEntity;
        if (target != null && target instanceof EntityPlayer) {
            OxygenGUIHelper.needSync(OxygenMain.INTERACTION_SCREEN_ID);
            OxygenMain.network().sendToServer(new SPOxygenRequest(SPOxygenRequest.EnumRequest.OPEN_INTERACT_PLAYER_MENU));
        }
    }

    public void openPlayerInteractionMenuDelegated() {
        ClientReference.getMinecraft().addScheduledTask(new Runnable() {

            @Override
            public void run() {
                openPlayerInteractionMenu();
            }
        });
    }

    private void openPlayerInteractionMenu() {
        Entity target = ClientReference.getMinecraft().pointedEntity;
        if (target != null && target instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) target;
            ClientReference.displayGuiScreen(new InteractionGUIScreen(player.getGameProfile().getId()));//not safest way to get uuid on client
        }
    }
}
