package austeretony.oxygen.common.request;

import java.util.UUID;

public interface IRequestValidator {

    boolean isValid(UUID senderUUID, UUID requestedUUID);
}
