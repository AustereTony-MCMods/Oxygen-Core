package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.settings.CoreSettings;

public enum SpecialState {

    NORMAL(CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt()),
    INACTIVE(CoreSettings.COLOR_TEXT_INACTIVE.asInt()),
    ACTIVE(CoreSettings.COLOR_TEXT_ACTIVE.asInt()),
    SPECIAL(CoreSettings.COLOR_TEXT_SPECIAL.asInt());

    final int colorHex;

    SpecialState(int colorHex) {
        this.colorHex = colorHex;
    }
}
