package austeretony.oxygen_core.server;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

public class TimeManagerServer {

    private final OxygenManagerServer manager;

    private final ZoneId zoneId;

    private final Clock clock;

    public TimeManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
        this.zoneId = initZoneId();
        this.clock = Clock.system(this.zoneId);
    }

    private static ZoneId initZoneId() {
        ZoneId zoneId = ZoneId.systemDefault();
        if (!OxygenConfig.SERVER_REGION_ID.asString().isEmpty()) {
            try {
                zoneId = ZoneId.of(OxygenConfig.SERVER_REGION_ID.asString());
            } catch (DateTimeException exception) {
                OxygenMain.LOGGER.error("[Core] Server ZoneId parse failure! System default ZoneId <{}> will be used.", zoneId.getId());
                exception.printStackTrace();
            }
        }
        return zoneId;
    }

    public ZoneId getZoneId() {
        return this.zoneId;
    }

    public Clock getClock() {
        return this.clock;
    }

    public Instant getInstant() {
        return this.clock.instant();
    }

    public ZonedDateTime getZonedDateTime() {
        return ZonedDateTime.now(this.clock);
    }
}
