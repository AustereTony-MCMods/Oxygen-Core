package austeretony.oxygen_core.server;

import austeretony.oxygen_core.common.config.CoreConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

import java.time.*;

public class TimeManagerServer {

    private final ZoneId zoneId;
    private final Clock clock;

    public TimeManagerServer() {
        zoneId = initZoneId();
        clock = Clock.system(zoneId);
    }

    private static ZoneId initZoneId() {
        ZoneId zoneId = ZoneId.systemDefault();
        if (!CoreConfig.SERVER_REGION_ID.asString().isEmpty()) {
            try {
                zoneId = ZoneId.of(CoreConfig.SERVER_REGION_ID.asString());
            } catch (DateTimeException exception) {
                OxygenMain.logError(1, "[Core] Server ZoneId parse failure! System default ZoneId <{}> will be used.",
                        zoneId.getId());
                exception.printStackTrace();
            }
        }
        return zoneId;
    }

    public ZoneId getZoneId() {
        return zoneId;
    }

    public Clock getClock() {
        return clock;
    }

    public Instant getInstant() {
        return clock.instant();
    }

    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.now(clock);
    }

    public ZonedDateTime getZonedDateTime(long epochMilli) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), getZoneId());
    }
}
