package austeretony.oxygen_core.common.util;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.zip.Deflater;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

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

    public static final Logger getLogger(String cat, String file, String name){
        LoggerContext loggerContext = (LoggerContext) LogManager.getContext(false);
        final Configuration configuration = loggerContext.getConfiguration();
        PatternLayout layout = PatternLayout.newBuilder().withPattern("[%d{dd-MMM-yyyy HH:mm:ss}] [%t] [%c]: %m%n").withConfiguration(configuration).build();
        RollingFileAppender appender = RollingFileAppender.newBuilder()
                .setConfiguration(configuration)
                .withFileName("./logs/oxygen/" + cat + "/" + file + ".log")
                .withFilePattern("./logs/oxygen/" + cat + "/" + file + "-%d{yyyy-MM-dd}.%i.log.gz")
                .withName(name)
                .withAppend(true)
                .withImmediateFlush(true)
                .withBufferedIo(true)
                .withBufferSize(8192)
                .withCreateOnDemand(false)
                .withLocking(false)
                .withLayout(layout)
                .withPolicy(CompositeTriggeringPolicy.createPolicy(SizeBasedTriggeringPolicy.createPolicy("10 M"), TimeBasedTriggeringPolicy.createPolicy("1", null)))
                .withStrategy(DefaultRolloverStrategy.createStrategy(Integer.MAX_VALUE + "", "1", "max", Deflater.NO_COMPRESSION + "", null, true, configuration)).build();
        appender.start();
        configuration.addAppender(appender);
        AppenderRef ref = AppenderRef.createAppenderRef(name, null, null);
        LoggerConfig loggerConfig = LoggerConfig.createLogger(true, Level.INFO, name, "true", new AppenderRef[] {ref}, null, configuration, null);
        configuration.addLogger(name, loggerConfig);
        loggerContext.getLogger(name).addAppender(appender);
        loggerContext.updateLoggers();

        return loggerContext.getLogger(name);
    }
}
