package austeretony.oxygen_core.common.util;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.TimeHelperClient;
import net.minecraft.util.text.TextFormatting;

public class OxygenUtils {

    public static String formattingCode(TextFormatting enumFormatting) {
        return enumFormatting.getFriendlyName();
    }

    public static TextFormatting formattingFromCode(String code) {
        return TextFormatting.getValueByName(code);
    }

    public static String formatCurrencyValue(String input) {
        int index = 1;
        StringBuilder builder = new StringBuilder();
        for (char c : new StringBuilder(input).reverse().toString().toCharArray()) {
            builder.append(c);
            if (index % 3 == 0 && index != input.length())
                builder.append(',');
            index++;
        }
        return builder.reverse().toString();
    }

    public static String formatDecimalCurrencyValue(String input) {
        int index = 1;
        StringBuilder builder = new StringBuilder();
        String[] array = input.split("[.]");
        for (char c : new StringBuilder(array[0]).reverse().toString().toCharArray()) {
            builder.append(c);
            if (index % 3 == 0 && index != array[0].length())
                builder.append(',');
            index++;
        }
        if (array.length == 2)
            return builder.reverse().append('.').append(array[1]).toString();
        return builder.reverse().toString();
    }

    public static String getTimePassedLocalizedString(long epochMilli) {        
        if (epochMilli > 0L) {
            ZonedDateTime 
            currentTime = TimeHelperClient.getZonedDateTime(),
            timeStamp = TimeHelperClient.getZonedDateTime(epochMilli);

            long 
            minutes = ChronoUnit.MINUTES.between(timeStamp, currentTime),
            hours = ChronoUnit.HOURS.between(timeStamp, currentTime),
            days = ChronoUnit.DAYS.between(timeStamp, currentTime);

            if (days > 0L) {
                if (days % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.day", days);
                else               
                    return ClientReference.localize("oxygen_core.gui.days", days);
            } else if (hours > 0L) {
                if (hours % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.hour", hours);
                else
                    return ClientReference.localize("oxygen_core.gui.hours", hours);
            } else {
                if (minutes % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.minute", minutes);
                else               
                    return ClientReference.localize("oxygen_core.gui.minutes", minutes);
            }
        } else
            return ClientReference.localize("oxygen_core.gui.undef");
    }

    public static String getExpirationTimeLocalizedString(long expiresInMilli, long epochMilli) {
        if (epochMilli > 0L) {
            ZonedDateTime 
            currentTime = TimeHelperClient.getZonedDateTime(),
            timeStamp = TimeHelperClient.getZonedDateTime(epochMilli),
            expireTime = TimeHelperClient.getZonedDateTime(epochMilli + expiresInMilli);

            Duration 
            expirationDuration = Duration.between(timeStamp, expireTime),
            timeLeftDuration = Duration.between(timeStamp, currentTime),
            deltaDuration = expirationDuration.minus(timeLeftDuration);

            long 
            minutes = deltaDuration.toMinutes(),
            hours = deltaDuration.toHours(),
            days = deltaDuration.toDays();

            if (minutes < 0L)
                return ClientReference.localize("oxygen_core.gui.expired");

            if (days > 0L) {
                if (days % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.day", days);
                else               
                    return ClientReference.localize("oxygen_core.gui.days", days);
            } else if (hours > 0L) {
                if (hours % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.hour", hours);
                else
                    return ClientReference.localize("oxygen_core.gui.hours", hours);
            } else {
                if (minutes % 10L == 1L)
                    return ClientReference.localize("oxygen_core.gui.minute", minutes);
                else               
                    return ClientReference.localize("oxygen_core.gui.minutes", minutes);
            }
        } else
            return ClientReference.localize("oxygen_core.gui.undef");
    }
}
