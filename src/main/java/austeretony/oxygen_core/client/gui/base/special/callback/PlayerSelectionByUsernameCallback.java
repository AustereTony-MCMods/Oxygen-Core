package austeretony.oxygen_core.client.gui.base.special.callback;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Keys;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.background.Background;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.base.special.UsernameField;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class PlayerSelectionByUsernameCallback extends Callback {

    private final String title;
    private final List<String> messageLines;
    @Nonnull
    private final Consumer<PlayerSharedData> selectionConsumer;
    @Nullable
    private final Runnable cancelTask;

    private UsernameField usernameField;
    private KeyButton confirmButton;

    public PlayerSelectionByUsernameCallback(@Nonnull String title, @Nonnull String displayMessage,
                                             @Nonnull Consumer<PlayerSharedData> selectionConsumer,
                                             @Nullable Runnable cancelTask) {
        super(0, 0);
        this.title = title;

        int width = 180;
        Text commonText = Texts.common(displayMessage).decrementScale(.05F);
        messageLines = GUIUtils.splitTextToLines(commonText.getText(), commonText.getScale(), width - 6 * 2);
        setSize(width, (int) (47 + (commonText.getHeight() + 3F) * messageLines.size()));

        this.selectionConsumer = selectionConsumer;
        this.cancelTask = cancelTask;
    }

    public PlayerSelectionByUsernameCallback(@Nonnull String title, @Nonnull String displayMessage,
                                             @Nonnull Consumer<PlayerSharedData> selectionConsumer) {
        this(title, displayMessage, selectionConsumer, null);
    }

    @Override
    public void init() {
        addWidget(new Background.UnderlinedTitleBottom(this));
        addWidget(new TextLabel(4, 12, Texts.title(title)));

        int index = 0;
        float lineHeight = 0F;
        for (String line : messageLines) {
            Text lineText = Texts.common(line).decrementScale(.05F);
            lineHeight = lineText.getHeight() + 3F;
            addWidget(new TextLabel(6, 23 + (int) lineHeight * index++, lineText));
        }

        addWidget(new TextLabel(6, 24 + (int) lineHeight * index,
                Texts.commonDark(localize("oxygen_core.gui.label.username")).decrementScale(.05F)));
        addWidget(usernameField = new UsernameField(6, 25 + (int) lineHeight * index, getWidth() - 6 * 2)
                .ignoreClientPlayer(true));
        usernameField.updatePlayers();

        int buttonPosSegment = (int) (getWidth() / 2F);
        addWidget(confirmButton = new KeyButton(0, getHeight() - 10, Keys.CONFIRM_KEY, "oxygen_core.gui.button.confirm")
                .setPressListener(this::confirm).setEnabled(false));
        confirmButton.setX(getX() + (int) ((buttonPosSegment - confirmButton.getText().getWidth()) / 2F));
        KeyButton cancelButton;
        addWidget(cancelButton = new KeyButton(0, getHeight() - 10, Keys.CANCEL_KEY, "oxygen_core.gui.button.cancel")
                .setPressListener(this::close));
        cancelButton.setX(getX() + buttonPosSegment + (int) ((buttonPosSegment - cancelButton.getText().getWidth()) / 2F));
    }

    @Override
    public void update() {
        super.update();
        confirmButton.setEnabled(usernameField.getSelectedPlayer() != null);
    }

    @Override
    public boolean close() {
        if (!usernameField.isFocused()) {
            if (cancelTask != null) {
                cancelTask.run();
            }
            return super.close();
        }
        return false;
    }

    private void confirm() {
        if (!usernameField.isFocused() && usernameField.getSelectedPlayer() != null) {
            selectionConsumer.accept(usernameField.getSelectedPlayer());
            close();
        }
    }
}
