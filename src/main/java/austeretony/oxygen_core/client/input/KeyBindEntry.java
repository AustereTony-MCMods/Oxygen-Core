package austeretony.oxygen_core.client.input;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class KeyBindEntry {

    private final String name, category;
    private final int id;
    private final boolean checkMenuEnabled;
    @Nonnull
    private Supplier<Integer> keyCodeSupplier;
    @Nonnull
    private Supplier<Boolean> validationSupplier;
    @Nonnull
    private final Runnable task;

    public KeyBindEntry(int id, String name, String category, @Nonnull Supplier<Integer> keyCodeSupplier,
                        @Nonnull Supplier<Boolean> validationSupplier, boolean checkMenuEnabled, @Nonnull Runnable task) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.keyCodeSupplier = keyCodeSupplier;
        this.validationSupplier = validationSupplier;
        this.checkMenuEnabled = checkMenuEnabled;
        this.task = task;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    @Nonnull
    public Supplier<Integer> getKeyCodeSupplier() {
        return keyCodeSupplier;
    }

    @Nonnull
    public Supplier<Boolean> getValidationSupplier() {
        return validationSupplier;
    }

    public boolean checkMenuEnabled() {
        return checkMenuEnabled;
    }

    @Nonnull
    public Runnable getTask() {
        return task;
    }
}
