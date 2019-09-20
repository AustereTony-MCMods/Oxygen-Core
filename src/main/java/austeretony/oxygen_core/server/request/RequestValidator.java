package austeretony.oxygen_core.server.request;

import java.util.UUID;

public interface RequestValidator {

    boolean isValid(UUID senderUUID, UUID requestedUUID);
}
