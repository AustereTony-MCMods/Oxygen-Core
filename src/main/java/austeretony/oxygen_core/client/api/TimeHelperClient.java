package austeretony.oxygen_core.client.api;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import austeretony.oxygen_core.client.OxygenManagerClient;

public class TimeHelperClient {

    //client

    public static ZoneId getZoneId() {
        return OxygenManagerClient.instance().getTimeManager().getZoneId();
    }

    public static Clock getClock() {
        return OxygenManagerClient.instance().getTimeManager().getClock();
    }

    public static long getCurrentMillis() {
        return OxygenManagerClient.instance().getTimeManager().getClock().millis();
    }

    public static Instant getInstant() {
        return OxygenManagerClient.instance().getTimeManager().getInstant();
    }

    public static ZonedDateTime getZonedDateTime() {
        return OxygenManagerClient.instance().getTimeManager().getZonedDateTime();
    }

    public static ZonedDateTime getZonedDateTime(long epochMilli) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), getZoneId());
    }

    public static DateTimeFormatter getDateTimeFormatter() {
        return OxygenManagerClient.instance().getTimeManager().getDateTimeFormatter();
    }

    //server

    public static ZoneId getServerZoneId() {
        return OxygenManagerClient.instance().getTimeManager().getServerZoneId();
    }

    public static ZonedDateTime getServerZonedDateTime() {
        return OxygenManagerClient.instance().getTimeManager().getServerZonedDateTime();
    }

    public static ZonedDateTime getServerZonedDateTime(long epochMilli) {
        return ZonedDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), getServerZoneId());
    }
}
