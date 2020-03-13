package austeretony.oxygen_core.server.battle;

import java.util.UUID;

@FunctionalInterface
public interface PlayerVersusPlayerValidator {

    boolean canAttack(UUID attackerUUID, UUID attackedUUID);
}
