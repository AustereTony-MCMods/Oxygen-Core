package austeretony.oxygen_core.client.gui.base;

import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.settings.CoreSettings;

public final class Fills {

    private Fills() {}

    public static Fill def() {
        return Fill.builder().build();
    }

    public static Fill element() {
        return Fill.builder()
                .colorEnabled(CoreSettings.COLOR_ELEMENT_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_ELEMENT_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_ELEMENT_MOUSE_OVER.asInt())
                .build();
    }

    public static Fill button() {
        return Fill.builder()
                .colorEnabled(CoreSettings.COLOR_BUTTON_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_BUTTON_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_BUTTON_MOUSE_OVER.asInt())
                .build();
    }

    public static Fill slider() {
        return Fill.builder()
                .colorEnabled(CoreSettings.COLOR_SLIDER_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_SLIDER_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_SLIDER_MOUSE_OVER.asInt())
                .build();
    }

    public static Fill textField() {
        return Fill.builder()
                .colorEnabled(CoreSettings.COLOR_TEXT_FIELD_ENABLED.asInt())
                .colorDisabled(CoreSettings.COLOR_TEXT_FIELD_DISABLED.asInt())
                .colorHovered(CoreSettings.COLOR_TEXT_FIELD_MOUSE_OVER.asInt())
                .build();
    }
}
