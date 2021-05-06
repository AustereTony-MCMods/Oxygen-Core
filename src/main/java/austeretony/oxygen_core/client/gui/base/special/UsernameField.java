package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.api.PrivilegesClient;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.ListEntryClickListener;
import austeretony.oxygen_core.client.gui.base.text.TextField;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.common.main.CorePrivileges;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public class UsernameField extends TextField {

    private static final int MAX_ENTRIES = 5;

    private final Map<String, PlayerSharedData> playersMap = new HashMap<>();
    private boolean ignoreClientPlayer;
    @Nullable
    private ListEntryClickListener<PlayerSharedData> listener;

    @Nullable
    private PlayerSharedData selectedPlayer;

    public UsernameField(int x, int y, int width) {
        super(x, y, width, OxygenMain.USERNAME_FIELD_LENGTH);
    }

    public <T extends UsernameField> T ignoreClientPlayer(boolean flag) {
        ignoreClientPlayer = flag;
        return (T) this;
    }

    public <T extends UsernameField> T setUsernameSelectionListener(ListEntryClickListener<PlayerSharedData> listener) {
        this.listener = listener;
        return (T) this;
    }

    public void updatePlayers() {
        playersMap.clear();
        OxygenClient.getOnlinePlayersSharedData()
                .stream()
                .filter(e -> OxygenClient.getPlayerActivityStatus(e) != ActivityStatus.OFFLINE
                        || PrivilegesClient.getBoolean(CorePrivileges.EXPOSE_OFFLINE_PLAYERS.getId(), false))
                .forEach(e -> playersMap.put(e.getUsername().toLowerCase(Locale.ROOT), e));

        if (ignoreClientPlayer) {
            PlayerSharedData sharedData = OxygenClient.getPlayerSharedData(OxygenClient.getClientPlayerUUID());
            if (sharedData != null) {
                playersMap.remove(sharedData.getUsername());
            }
        }
    }

    @Override
    public void reset() {
        selectedPlayer = null;
        setText("");
    }

    @Nullable
    public PlayerSharedData getSelectedPlayer() {
        return selectedPlayer;
    }

    private void showMatchedUsernamesList() {
        String typedText = getTypedText().toLowerCase(Locale.ROOT);
        clear();

        selectedPlayer = null;
        if (typedText.isEmpty()) return;

        List<PlayerSharedData> matchedPlayers = playersMap.keySet()
                .stream()
                .filter(e -> e.startsWith(typedText))
                .sorted()
                .map(playersMap::get)
                .collect(Collectors.toList());

        int index = 0;
        for (PlayerSharedData sharedData : matchedPlayers) {
            if (index == MAX_ENTRIES) break;

            ListEntry<PlayerSharedData> listEntry;
            addWidget(listEntry = new ListEntry<>(Texts.additional(sharedData.getUsername()), sharedData));
            listEntry.setPosition(0, getHeight() + getHeight() * index++);
            listEntry.setSize(getWidth(), getHeight());

            if (sharedData.getUsername().equals(typedText)) {
                selectedPlayer = sharedData;
                if (listener != null) {
                    listener.click(null, listEntry, 0, 0, 0);
                }
            }
        }
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible() || getWidgets().isEmpty()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int yStart = getHeight();
        int height = getWidgets().size() * getHeight();
        GUIUtils.drawRect(0, yStart, getWidth(), yStart + height,
                CoreSettings.COLOR_BACKGROUND_BASE.asInt());
        GUIUtils.drawFrame(0, yStart, getWidth(), height);

        for (Widget widget : getWidgets()) {
            widget.draw(mouseX, mouseY, partialTicks);
        }

        GUIUtils.popMatrix();
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        boolean flag = super.keyTyped(typedChar, keyCode);
        if (flag) {
            showMatchedUsernamesList();
        }
        return flag;
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        for (Widget widget : getWidgets()) {
            ListEntry<PlayerSharedData> entry = (ListEntry) widget;
            if (entry.mouseClicked(mouseX, mouseY, mouseButton)) {
                selectedPlayer = entry.getEntry();
                setText(selectedPlayer.getUsername());
                if (listener != null) {
                    listener.click(null, entry, mouseX, mouseY, mouseButton);
                }
            }
        }
        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void setFocused(boolean flag) {
        super.setFocused(flag);
        if (flag) {
            showMatchedUsernamesList();
        } else {
            clear();
        }
    }
}
