package austeretony.oxygen_core.server.operation.action;

import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.CommonUtils;
import austeretony.oxygen_core.server.operation.FailureReason;
import austeretony.oxygen_core.server.operation.Operation;
import austeretony.oxygen_core.server.player.inventory.InventoryHelperServer;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Map;

public class ItemsWithdrawAction extends AbstractAction {

    private final Map<ItemStackWrapper, Integer> itemsMap;

    public ItemsWithdrawAction(Operation operation, Map<ItemStackWrapper, Integer> itemsMap) {
        super(operation);
        this.itemsMap = itemsMap;
    }

    @Override
    public void validate(EntityPlayerMP playerMP) {
        if (itemsMap.isEmpty()) {
            validationState = PhaseState.SUCCESS;
        } else {
            InventoryHelperServer.queryHaveItems(playerMP, itemsMap,
                    result -> {
                        if (result) {
                            validationState = PhaseState.SUCCESS;
                        } else {
                            validationState = PhaseState.FAIL;
                            failureReason = FailureReason.NOT_ENOUGH_ITEMS;
                        }
                    },
                    CallingThread.OTHER);
        }
    }

    @Override
    public void execute(EntityPlayerMP playerMP) {
        if (itemsMap.isEmpty()) {
            executed = true;
            return;
        }

        InventoryHelperServer.removeItems(playerMP, itemsMap, CallingThread.OTHER,
                success -> {
                    executed = true;
                    executionFailed = !success;
                });
    }

    @Override
    public String getName() {
        return "items remove";
    }

    @Override
    public String getLog() {
        return getName() + ": " + CommonUtils.formatForLogging(itemsMap);
    }
}
