package austeretony.oxygen_core.client.gui.base.context;

import javax.annotation.Nonnull;

public interface ContextAction<T> {

    @Nonnull
    String getName(T entry);

    boolean isValid(T entry);

    void execute(T entry);
}
