package austeretony.oxygen.common.telemetry.api;

import java.util.UUID;

public interface IPlayerLog extends ILog {

    UUID getPlayerUUID();

    String getPlayerName();
}
