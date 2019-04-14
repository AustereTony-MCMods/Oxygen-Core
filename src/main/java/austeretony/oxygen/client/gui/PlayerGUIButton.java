package austeretony.oxygen.client.gui;

import java.util.UUID;

import austeretony.alternateui.screen.button.GUIButton;

public class PlayerGUIButton extends GUIButton {

    public final UUID playerUUID;

    public PlayerGUIButton(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }
}
