package austeretony.oxygen_core.server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen_core.server.battle.PlayerVersusPlayerValidator;
import austeretony.oxygen_core.server.request.RequestValidator;

public class ValidatorsManagerServer {

    private Set<RequestValidator> requestValidators;

    private Set<PlayerVersusPlayerValidator> restrictedAttacksValidators, allowedAttacksValidators;

    public void registerRequestValidator(RequestValidator validator) {
        if (this.requestValidators == null)
            this.requestValidators = new HashSet<>(1);
        this.requestValidators.add(validator);
    }

    public boolean validateRequest(UUID senderUUID, UUID requestedUUID) {
        if (this.requestValidators == null)
            return true;
        for (RequestValidator validator : this.requestValidators)
            if (!validator.isValid(senderUUID, requestedUUID))
                return false;
        return true;
    }

    public void registerRestrictedAttacksValidator(PlayerVersusPlayerValidator validator) {
        if (this.restrictedAttacksValidators == null)
            this.restrictedAttacksValidators = new HashSet<>(1);
        this.restrictedAttacksValidators.add(validator);
    }

    public void registerAllowedAttacksValidator(PlayerVersusPlayerValidator validator) {
        if (this.allowedAttacksValidators == null)
            this.allowedAttacksValidators = new HashSet<>(1);
        this.allowedAttacksValidators.add(validator);
    }

    public boolean canPlayerAttack(UUID attackerUUID, UUID attackedUUID) {
        if (this.allowedAttacksValidators != null)
            for (PlayerVersusPlayerValidator validator : this.allowedAttacksValidators)
                if (validator.canAttack(attackerUUID, attackedUUID))
                    return true;
        if (this.restrictedAttacksValidators != null)
            for (PlayerVersusPlayerValidator validator : this.restrictedAttacksValidators)
                if (!validator.canAttack(attackerUUID, attackedUUID))
                    return false;
        return true;
    }
}
