package austeretony.oxygen_core.server.operation;

import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import austeretony.oxygen_core.server.operation.action.*;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class Operation {

    private final String description;
    private final EntityPlayerMP playerMP;

    @Nullable
    private Runnable successTask;
    @Nullable
    private Consumer<FailureReason> failTask;

    private final List<AbstractAction> actionsList = new ArrayList<>();
    private int currentAction;

    private boolean log = true;
    private State state = State.INITIAL;
    private FailureReason failureReason = FailureReason.NO_FAILURE;

    private static final long
            VALIDATION_TIMEOUT_MILLIS = 10_000L,
            EXECUTION_AWAIT_TIMEOUT_MILLIS = 10_000L;
    private long stateUpdateMillis;

    private Operation(String description, EntityPlayerMP playerMP) {
        this.description = description;
        this.playerMP = playerMP;
    }

    public static Operation of(String description, EntityPlayerMP playerMP) {
        return new Operation(description, playerMP);
    }

    public Operation withLogging(boolean flag) {
        log = flag;
        return this;
    }

    public Operation withSuccessTask(Runnable successTask) {
        this.successTask = successTask;
        return this;
    }

    public Operation withFailTask(Consumer<FailureReason> failTask) {
        this.failTask = failTask;
        return this;
    }

    public Operation withCurrencyGain(int currencyIndex, long value) {
        actionsList.add(new CurrencyGainAction(this, currencyIndex, value));
        return this;
    }

    public Operation withCurrencyWithdraw(int currencyIndex, long value) {
        actionsList.add(new CurrencyWithdrawAction(this, currencyIndex, value));
        return this;
    }

    public Operation withItemsAdd(Map<ItemStackWrapper, Integer> itemsMap) {
        actionsList.add(new ItemsAddAction(this, itemsMap));
        return this;
    }

    public Operation withItemsWithdraw(Map<ItemStackWrapper, Integer> itemsMap) {
        actionsList.add(new ItemsWithdrawAction(this, itemsMap));
        return this;
    }

    public Operation withItemAdd(ItemStackWrapper stackWrapper, int quantity) {
        actionsList.add(new ItemsAddAction(this, Collections.singletonMap(stackWrapper, quantity)));
        return this;
    }

    public Operation withItemWithdraw(ItemStackWrapper stackWrapper, int quantity) {
        actionsList.add(new ItemsWithdrawAction(this, Collections.singletonMap(stackWrapper, quantity)));
        return this;
    }

    public Operation withGenericAction(CallingThread callFrom, @Nullable Predicate<EntityPlayerMP> predicate,
                                       Function<EntityPlayerMP, Boolean> task, String description) {
        actionsList.add(new GenericAction(this, callFrom, predicate, task, description));
        return this;
    }

    public void process() {
        stateUpdateMillis = OxygenServer.getCurrentTimeMillis();
        if (!actionsList.isEmpty()) {
            OxygenManagerServer.instance().getOperationsManager().addOperation(playerMP, this);
        } else {
            success();
        }
    }

    boolean update() {
        State tempState = state;
        switch (tempState) {
            case INITIAL:
                currentAction = 0;
                state = State.VALIDATION;
                break;
            case VALIDATION:
                AbstractAction action = actionsList.get(currentAction);
                action.startValidation();

                if (action.getValidationState() == AbstractAction.PhaseState.FAIL) {
                    state = State.FAIL;
                    failureReason = action.getFailureReason();
                    fail();
                } else if (action.getValidationState() == AbstractAction.PhaseState.SUCCESS) {
                    currentAction++;
                    if (currentAction >= actionsList.size()) {
                        state = State.EXECUTION_START;
                        stateUpdateMillis = OxygenServer.getCurrentTimeMillis();
                    }
                }

                if (OxygenServer.getCurrentTimeMillis() > stateUpdateMillis + VALIDATION_TIMEOUT_MILLIS) {
                    state = State.FAIL;
                    failureReason = FailureReason.VALIDATION_TIMEOUT;
                    fail();
                }
                break;
            case EXECUTION_START:
                for (AbstractAction act : actionsList) {
                    act.execute(playerMP);
                }
                state = State.EXECUTION;
                break;
            case EXECUTION:
                boolean allExecuted = true;
                for (AbstractAction act : actionsList) {
                    if (!act.isExecuted()) {
                        allExecuted = false;
                        break;
                    }
                }
                if (allExecuted
                        || OxygenServer.getCurrentTimeMillis() > stateUpdateMillis + EXECUTION_AWAIT_TIMEOUT_MILLIS) {
                    state = State.SUCCESS;
                    success();
                }
                break;
        }
        return state == State.SUCCESS || state == State.FAIL;
    }

    private void success() {
        if (successTask != null) {
            OxygenServer.addTask(successTask);
        }
        log();
    }

    private void fail() {
        if (failTask != null) {
            OxygenServer.addTask(() -> failTask.accept(failureReason));
        }
    }

    private void log() {
        if (!log) return;
        // " 'operation_name' | 'username' / 'player uuid' | 'actions logs list' | failed: 'failed actions names'"
        String logStr = " " + description + " | " + MinecraftCommon.getEntityName(playerMP) + " / " + MinecraftCommon.getEntityUUID(playerMP) + " | " +
                actionsList.stream().map(AbstractAction::getLog).collect(Collectors.joining("; "));

        String failedStr = actionsList.stream().filter(AbstractAction::isExecutionFailed).map(AbstractAction::getName).collect(Collectors.joining(", "));
        if (!failedStr.isEmpty()) {
            logStr += " | failed: " + failedStr;
        }
        OxygenMain.OPERATIONS_LOGGER.info(logStr);
    }

    public EntityPlayerMP getPlayer() {
        return playerMP;
    }

    public String getDescription() {
        return description;
    }

    public State getState() {
        return state;
    }

    public boolean isSuccessful() {
        return getState() == State.SUCCESS;
    }

    public FailureReason getFailureReason() {
        return failureReason;
    }

    public enum State {

        INITIAL,
        VALIDATION,
        EXECUTION_START,
        EXECUTION,
        FAIL,
        SUCCESS
    }
}
