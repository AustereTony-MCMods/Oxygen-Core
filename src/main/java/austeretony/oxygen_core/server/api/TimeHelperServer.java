package austeretony.oxygen_core.server.api;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import austeretony.oxygen_core.server.OxygenManagerServer;

public class TimeHelperServer {

    public static ZoneId getZoneId() {
        return OxygenManagerServer.instance().getTimeManager().getZoneId();
    }

    public static Clock getClock() {
        return OxygenManagerServer.instance().getTimeManager().getClock();
    }

    public static long getCurrentMillis() {
        return OxygenManagerServer.instance().getTimeManager().getClock().millis();
    }

    public static Instant getInstant() {
        return OxygenManagerServer.instance().getTimeManager().getInstant();
    }

    public static ZonedDateTime getZonedDateTime() {
        return OxygenManagerServer.instance().getTimeManager().getZonedDateTime();
    }

    public static ZonedDateTime getZonedDateTime(long epochMilli) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), getZoneId());
    }
}
