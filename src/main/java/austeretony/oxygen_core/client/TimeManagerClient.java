package austeretony.oxygen_core.client;

import java.time.Clock;
import java.time.DateTimeException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

public class TimeManagerClient {

    private final OxygenManagerClient manager;

    private final DateTimeFormatter dateTimeFormatter;

    private final ZoneId zoneId;

    private final Clock clock;

    private ZoneId serverZoneId;

    public TimeManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(OxygenConfig.DATE_TIME_FORMATTER_PATTERN.asString());
        this.zoneId = initZoneId();
        this.clock = Clock.system(this.zoneId);
    }

    private static ZoneId initZoneId() {
        ZoneId zoneId = ZoneId.systemDefault();
        if (!OxygenConfig.CLIENT_REGION_ID.asString().isEmpty()) {
            try {
                zoneId = ZoneId.of(OxygenConfig.CLIENT_REGION_ID.asString());
            } catch (DateTimeException exception) {
                OxygenMain.LOGGER.error("[Core] Client ZoneId parse failure! System default ZoneId <{}> will be used.", zoneId.getId());
                exception.printStackTrace();
            }
        }
        return zoneId;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return this.dateTimeFormatter;
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

    public void initServerTime(String serverRegionId) {
        this.serverZoneId = ZoneId.of(serverRegionId);
        OxygenMain.LOGGER.info("[Core] Server zone-time data: {}", OxygenMain.DEBUG_DATE_TIME_FORMATTER.format(this.getServerZonedDateTime()));
    }

    public ZoneId getServerZoneId() {
        return this.serverZoneId;
    }

    public ZonedDateTime getServerZonedDateTime() {
        return ZonedDateTime.now(this.serverZoneId);
    }
}
