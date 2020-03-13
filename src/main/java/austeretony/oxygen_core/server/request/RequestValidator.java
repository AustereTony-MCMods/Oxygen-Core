package austeretony.oxygen_core.server.request;

import java.util.UUID;

@FunctionalInterface
public interface RequestValidator {

    boolean isValid(UUID senderUUID, UUID requestedUUID);
}
