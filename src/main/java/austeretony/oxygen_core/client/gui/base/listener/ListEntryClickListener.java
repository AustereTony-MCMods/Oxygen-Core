package austeretony.oxygen_core.client.gui.base.listener;

import austeretony.oxygen_core.client.gui.base.common.ListEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@FunctionalInterface
public interface ListEntryClickListener<T> {

    void click(@Nullable ListEntry<T> previous, @Nonnull ListEntry<T> current, int mouseX, int mouseY, int mouseButton);
}
