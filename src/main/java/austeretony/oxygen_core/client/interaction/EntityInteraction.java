package austeretony.oxygen_core.client.interaction;

import net.minecraft.entity.Entity;

import javax.annotation.Nullable;

public interface EntityInteraction {

    int getId();

    boolean isValid(Entity entity);

    String getDisplayName(Entity entity);

    @Nullable
    String getDisplayProfession(Entity entity);

    void process(Entity entity);
}
