package austeretony.oxygen_core.client;

import austeretony.oxygen_core.common.config.CoreConfig;
import austeretony.oxygen_core.common.main.OxygenMain;

import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeManagerClient {

    private final DateTimeFormatter dateTimeFormatter;
    private final ZoneId zoneId;
    private final Clock clock;
    private ZoneId serverZoneId;

    public TimeManagerClient() {
        serverZoneId = zoneId = initZoneId();
        dateTimeFormatter = DateTimeFormatter.ofPattern(CoreConfig.DATE_TIME_FORMATTER_PATTERN.asString()).withZone(zoneId);
        clock = Clock.system(zoneId);
    }

    private static ZoneId initZoneId() {
        ZoneId zoneId = ZoneId.systemDefault();
        if (!CoreConfig.CLIENT_REGION_ID.asString().isEmpty()) {
            try {
                zoneId = ZoneId.of(CoreConfig.CLIENT_REGION_ID.asString());
            } catch (DateTimeException exception) {
                OxygenMain.logError(1, "[Core] Client ZoneId parse failure! System default ZoneId <{}> will be used.",
                        zoneId.getId());
                exception.printStackTrace();
            }
        }
        return zoneId;
    }

    public DateTimeFormatter getDateTimeFormatter() {
        return dateTimeFormatter;
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

    public void initServerTime(String serverRegionId) {
        serverZoneId = ZoneId.of(serverRegionId);
        OxygenMain.logInfo(1, "[Core] Server zone-time data: {}",
                OxygenMain.DEBUG_DATE_TIME_FORMATTER.format(getServerZonedDateTime()));
    }

    public ZoneId getServerZoneId() {
        return serverZoneId;
    }

    public ZonedDateTime getServerZonedDateTime() {
        return ZonedDateTime.now(serverZoneId);
    }

    public ZonedDateTime getServerZonedDateTime(long epochMilli) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), getServerZoneId());
    }
}
