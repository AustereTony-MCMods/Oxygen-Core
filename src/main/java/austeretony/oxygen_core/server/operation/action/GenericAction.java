package austeretony.oxygen_core.server.operation.action;

import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.api.OxygenServer;
import austeretony.oxygen_core.server.operation.Operation;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.function.Function;
import java.util.function.Predicate;

public class GenericAction extends AbstractAction {

    private final CallingThread callFrom;
    @Nullable
    private final Predicate<EntityPlayerMP> predicate;
    private final Function<EntityPlayerMP, Boolean> task;
    private final String description;

    public GenericAction(Operation operation, CallingThread callFrom, @Nullable Predicate<EntityPlayerMP> predicate,
                         Function<EntityPlayerMP, Boolean> task, String description) {
        super(operation);
        this.callFrom = callFrom;
        this.predicate = predicate;
        this.task = task;
        this.description = description;
    }

    @Override
    public void validate(EntityPlayerMP playerMP) {
        if (predicate != null) {
            final Runnable task = () -> {
                if (predicate.test(playerMP)) {
                    validationState = PhaseState.SUCCESS;
                } else {
                    validationState = PhaseState.FAIL;
                }
            };

            if (callFrom == CallingThread.OXYGEN) {
                OxygenServer.addTask(task);
            } else if (callFrom == CallingThread.MINECRAFT) {
                MinecraftCommon.delegateToServerThread(task);
            } else {
                task.run();
            }
        } else {
            validationState = PhaseState.SUCCESS;
        }
    }

    @Override
    public void execute(EntityPlayerMP playerMP) {
        final Runnable runnable = () -> {
            if (!task.apply(playerMP)) {
                executionFailed = true;
            }
            executed = true;
        };

        if (callFrom == CallingThread.OXYGEN) {
            OxygenServer.addTask(runnable);
        } else if (callFrom == CallingThread.MINECRAFT) {
            MinecraftCommon.delegateToServerThread(runnable);
        } else {
            runnable.run();
        }
    }

    @Override
    public String getName() {
        return "generic operation";
    }

    @Override
    public String getLog() {
        return getName() + ": " + description;
    }
}
