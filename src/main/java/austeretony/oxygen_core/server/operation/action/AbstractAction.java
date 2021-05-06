package austeretony.oxygen_core.server.operation.action;

import austeretony.oxygen_core.server.operation.FailureReason;
import austeretony.oxygen_core.server.operation.Operation;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class AbstractAction {

    protected final Operation operation;

    protected volatile PhaseState validationState = PhaseState.INITIAL;
    protected volatile FailureReason failureReason = FailureReason.NO_FAILURE;

    protected volatile boolean executed;
    protected volatile boolean executionFailed;

    public AbstractAction(Operation operation) {
        this.operation = operation;
    }

    public PhaseState getValidationState() {
        return validationState;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public void startValidation() {
        if (validationState == PhaseState.INITIAL) {
            validationState = PhaseState.IN_PROGRESS;
            validate(operation.getPlayer());
        }
    }

    public abstract void validate(EntityPlayerMP playerMP);

    public abstract void execute(EntityPlayerMP playerMP);

    public abstract String getName();

    public abstract String getLog();

    public boolean isExecuted() {
        return executed;
    }

    public boolean isExecutionFailed() {
        return executionFailed;
    }

    public enum PhaseState {

        INITIAL,
        IN_PROGRESS,
        FAIL,
        SUCCESS
    }
}
