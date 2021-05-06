package austeretony.oxygen_core.server.operation.action;

import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.operation.FailureReason;
import austeretony.oxygen_core.server.operation.Operation;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class CurrencyGainAction extends AbstractAction {

    private final int currencyIndex;
    private final long value;

    public CurrencyGainAction(Operation operation, int currencyIndex, long value) {
        super(operation);
        this.currencyIndex = currencyIndex;
        this.value = value;
    }

    @Override
    public void validate(EntityPlayerMP playerM) {
        if (value < 0L) {
            validationState = PhaseState.FAIL;
            failureReason = FailureReason.GENERIC;
        } else {
            validationState = PhaseState.SUCCESS;
        }
    }

    @Override
    public void execute(EntityPlayerMP playerMP) {
        if (value == 0L) {
            executed = true;
            return;
        }

        UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
        String username = MinecraftCommon.getEntityName(playerMP);
        OxygenManagerServer.instance().getCurrencyManager()
                .incrementBalance(playerUUID, username, currencyIndex, value, CallingThread.OTHER,
                        newBalance -> {
                            executed = true;
                            executionFailed = newBalance == null;
                        });
    }

    @Override
    public String getName() {
        return "currency gain";
    }

    @Override
    public String getLog() {
        return getName() + ": " + currencyIndex + " - " + value;
    }
}
