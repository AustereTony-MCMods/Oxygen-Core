package austeretony.oxygen_core.server.operation.action;

import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import austeretony.oxygen_core.server.operation.FailureReason;
import austeretony.oxygen_core.server.operation.Operation;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.UUID;

public class CurrencyWithdrawAction extends AbstractAction {

    private final int currencyIndex;
    private final long value;

    public CurrencyWithdrawAction(Operation operation, int currencyIndex, long value) {
        super(operation);
        this.currencyIndex = currencyIndex;
        this.value = value;
    }

    @Override
    public void validate(EntityPlayerMP playerMP) {
        if (value < 0L) {
            validationState = PhaseState.FAIL;
            failureReason = FailureReason.GENERIC;
            return;
        }

        if (value == 0L) {
            validationState = PhaseState.SUCCESS;
        } else {
            UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
            String username = MinecraftCommon.getEntityName(playerMP);
            OxygenServer.queryBalance(playerUUID, username, currencyIndex,
                    balance -> {
                        if (balance == null) {
                            validationState = PhaseState.FAIL;
                            failureReason = FailureReason.INSUFFICIENT_FUNDS;
                            return;
                        }

                        if (balance >= value) {
                            validationState = PhaseState.SUCCESS;
                        } else {
                            validationState = PhaseState.FAIL;
                            failureReason = FailureReason.INSUFFICIENT_FUNDS;
                        }
                    },
                    CallingThread.OTHER);
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
                .decrementBalance(playerUUID, username, currencyIndex, value, CallingThread.OTHER,
                        newBalance -> {
                            executed = true;
                            executionFailed = newBalance == null;
                        });
    }

    @Override
    public String getName() {
        return "currency withdraw";
    }

    @Override
    public String getLog() {
        return getName() + ": " + currencyIndex + " - " + value;
    }
}
