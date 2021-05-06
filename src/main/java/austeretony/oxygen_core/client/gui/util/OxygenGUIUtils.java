package austeretony.oxygen_core.client.gui.util;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.core.OxygenScreen;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.menu.OxygenMenuHelper;
import austeretony.oxygen_core.client.input.KeyBindEntry;
import austeretony.oxygen_core.client.input.OxygenKeyHandler;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.KeyBinding;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class OxygenGUIUtils {

    private OxygenGUIUtils() {}

    @Nonnull
    public static List<String> getPlayerRoles(PlayerSharedData sharedData) {
        String rolesStr = sharedData.getValue(20, "");
        if (rolesStr.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.asList(rolesStr.split("[,]"));
    }

    @Nonnull
    public static String getLastActivityFormattedString(PlayerSharedData sharedData) {
        Instant lastActivityTime = Instant.ofEpochMilli(OxygenClient.getPlayerLastActivityTimeMillis(sharedData));
        return MinecraftClient.localize("oxygen_core.gui.label.last_activity",
                OxygenClient.getDateTimeFormatter().format(lastActivityTime));
    }

    public static void closeScreenOnKeyPress(OxygenScreen screen, int pressedKeyCode) {
        if (screen.getWorkspace().getCurrentSection().getCurrentCallback() != null) return;
        if (OxygenMenuHelper.isMenuEnabled()) {
            KeyBindEntry entry = OxygenKeyHandler.getRegistry().get(screen.getScreenId());
            if (entry != null && pressedKeyCode == entry.getKeyCodeSupplier().get()) {
                screen.close();
            }
            if (OxygenMenuHelper.getMenuKeyBinding() != null
                    && pressedKeyCode == OxygenMenuHelper.getMenuKeyBinding().getKeyCode()) {
                screen.close();
            }
        } else {
            KeyBinding keyBinding = OxygenClient.getKeyBinding(screen.getScreenId());
            if (keyBinding != null && pressedKeyCode == keyBinding.getKeyCode()) {
                screen.close();
            }
        }
    }

    public static void calculateBottomCenteredOffscreenButtonPosition(KeyButton keyButton, int buttonOrdinal, int buttonsAmount) {
        ScaledResolution sr = GUIUtils.getScaledResolution();
        int segmentWidth = (int) (sr.getScaledWidth() / (float) buttonsAmount);
        int buttonX = segmentWidth * (buttonOrdinal - 1) + (int) ((segmentWidth - (keyButton.getWidth() + 8
                    + GUIUtils.getTextWidth(keyButton.getText().getText(), keyButton.getText().getScale()))) / 2);
        int buttonY = sr.getScaledHeight() - 10;
        keyButton.setPosition(buttonX, buttonY);
    }

    public static String getDurationLocalizedString(long durationMillis) {
        if (durationMillis <= 0L) return MinecraftClient.localize("oxygen_core.gui.duration.undefined");

        Duration duration = Duration.of(durationMillis, ChronoUnit.MILLIS);
        long minutes = duration.toMinutes();
        long hours = duration.toHours();
        long days = duration.toDays();

        if (days > 0L) {
            return days + " " + MinecraftClient.localize("oxygen_core.gui.duration.day_" + days % 10L);
        } else if (hours > 0L) {
            return hours + " " + MinecraftClient.localize("oxygen_core.gui.duration.hour_" + hours % 10L);
        } else {
            return minutes + " " + MinecraftClient.localize("oxygen_core.gui.duration.minute_" + minutes % 10L);
        }
    }

    public static String getExpirationTimeLocalizedString(long startEpochMilli, long durationMillis) {
        if (durationMillis <= 0L) return MinecraftClient.localize("oxygen_core.gui.expire_time.never");
        Instant start = Instant.ofEpochMilli(startEpochMilli);
        Instant now = OxygenClient.getInstant();
        long deltaMillis = durationMillis - (now.toEpochMilli() - start.toEpochMilli());
        return MinecraftClient.localize("oxygen_core.gui.expire_time", getDurationLocalizedString(deltaMillis));
    }

    public static String geTimePassedLocalizedString(long startEpochMilli) {
        if (startEpochMilli <= 0L) return MinecraftClient.localize("oxygen_core.gui.duration.undefined");
        long deltaMillis = OxygenClient.getInstant().toEpochMilli() - startEpochMilli;
        return MinecraftClient.localize("oxygen_core.gui.time_passed", getDurationLocalizedString(deltaMillis));
    }
}
