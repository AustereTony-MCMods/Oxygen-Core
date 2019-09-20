package austeretony.oxygen_core.server;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen_core.server.request.RequestValidator;

public class RequestsManagerServer {

    private Set<RequestValidator> requestValidators;

    public void registerRequestValidator(RequestValidator validator) {
        if (this.requestValidators == null)
            this.requestValidators = new HashSet<>(3);
        this.requestValidators.add(validator);
    }

    public Set<RequestValidator> getRequestValidators() {
        return this.requestValidators;
    }

    public boolean validateRequest(UUID senderUUID, UUID requestedUUID) {
        if (this.requestValidators == null)
            return true;
        for (RequestValidator validator : this.requestValidators)
            if (!validator.isValid(senderUUID, requestedUUID))
                return false;
        return true;
    }
}