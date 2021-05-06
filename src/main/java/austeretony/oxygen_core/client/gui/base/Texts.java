package austeretony.oxygen_core.client.gui.base;

import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;

import javax.annotation.Nonnull;

public final class Texts {

    private Texts() {}

    public static Text def() {
        return Text.builder().build();
    }

    public static Text title(@Nonnull String str) {
        return Text.builder()
                .text(MinecraftClient.localize(str))
                .scale(CoreSettings.SCALE_TEXT_TITLE.asFloat())
                .colorEnabled(CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_TEXT_BASE_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_TEXT_BASE_MOUSE_OVER.asInt())
                .build();
    }

    public static Text common(@Nonnull String str) {
        return Text.builder()
                .text(MinecraftClient.localize(str))
                .scale(CoreSettings.SCALE_TEXT_BASE.asFloat())
                .colorEnabled(CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_TEXT_BASE_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_TEXT_BASE_MOUSE_OVER.asInt())
                .build();
    }

    public static Text additional(@Nonnull String str) {
        return Text.builder()
                .text(MinecraftClient.localize(str))
                .scale(CoreSettings.SCALE_TEXT_ADDITIONAL.asFloat())
                .colorEnabled(CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_TEXT_BASE_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_TEXT_BASE_MOUSE_OVER.asInt())
                .build();
    }

    public static Text commonDark(@Nonnull String str) {
        return Text.builder()
                .text(MinecraftClient.localize(str))
                .scale(CoreSettings.SCALE_TEXT_BASE.asFloat())
                .colorEnabled(CoreSettings.COLOR_TEXT_ADDITIONAL_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_TEXT_ADDITIONAL_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_TEXT_ADDITIONAL_MOUSE_OVER.asInt())
                .build();
    }

    public static Text additionalDark(@Nonnull String str) {
        return Text.builder()
                .text(MinecraftClient.localize(str))
                .scale(CoreSettings.SCALE_TEXT_ADDITIONAL.asFloat())
                .colorEnabled(CoreSettings.COLOR_TEXT_ADDITIONAL_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_TEXT_ADDITIONAL_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_TEXT_ADDITIONAL_MOUSE_OVER.asInt())
                .build();
    }
    
    public static Text panel(@Nonnull String str) {
        return Text.builder()
                .text(MinecraftClient.localize(str))
                .scale(CoreSettings.SCALE_TEXT_PANEL.asFloat())
                .colorEnabled(CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_TEXT_BASE_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_TEXT_BASE_MOUSE_OVER.asInt())
                .build();
    }

    public static Text button(@Nonnull String str) {
        return Text.builder()
                .text(MinecraftClient.localize(str))
                .scale(CoreSettings.SCALE_TEXT_BUTTON.asFloat())
                .colorEnabled(CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_TEXT_BASE_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_TEXT_BASE_MOUSE_OVER.asInt())
                .build();
    }
}
