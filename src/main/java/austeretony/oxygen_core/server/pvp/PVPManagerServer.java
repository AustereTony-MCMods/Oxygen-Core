package austeretony.oxygen_core.server.pvp;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

public class PVPManagerServer {

    private final List<PVPValidator> validators = new ArrayList<>();

    public void registerPVPValidator(PVPValidator validator) {
        validators.add(validator);
    }

    public boolean canPlayerAttack(EntityPlayerMP attacker, EntityPlayerMP victim) {
        for (PVPValidator validator : validators) {
            if (!validator.canPlayerAttack(attacker, victim)) {
                return false;
            }
        }
        return true;
    }
}
