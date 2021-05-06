package austeretony.oxygen_core.client.gui.base.special.callback;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Keys;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.background.Background;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.special.KeyButton;
import austeretony.oxygen_core.client.gui.base.text.TextLabel;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class YesNoCallback extends Callback {

    private final String title;
    private final List<String> messageLines;
    @Nonnull
    private final Runnable confirmTask;
    @Nullable
    private final Runnable cancelTask;

    public YesNoCallback(@Nonnull String title, @Nonnull String displayMessage, @Nonnull Runnable confirmTask,
                         @Nullable Runnable cancelTask) {
        super(0, 0);
        this.title = title;

        int width = 180;
        Text commonText = Texts.common(displayMessage).decrementScale(.05F);
        messageLines = GUIUtils.splitTextToLines(commonText.getText(), commonText.getScale(), width - 6 * 2);
        int messageHeight = (int) ((commonText.getHeight() + 3F) * messageLines.size());
        setSize(width, 30 + messageHeight);

        this.confirmTask = confirmTask;
        this.cancelTask = cancelTask;
    }

    public YesNoCallback(@Nonnull String title, @Nonnull String displayMessage, @Nonnull Runnable confirmTask) {
        this(title, displayMessage, confirmTask, null);
    }

    @Override
    public void init() {
        addWidget(new Background.UnderlinedTitleBottom(this));
        addWidget(new TextLabel(4, 12, Texts.title(title)));

        int index = 0;
        for (String line : messageLines) {
            Text lineText = Texts.common(line).decrementScale(.05F);
            addWidget(new TextLabel(6, 23 + (int) (lineText.getHeight() + 3F) * index++, lineText));
        }

        int buttonPosSegment = (int) (getWidth() / 2F);
        KeyButton confirmButton;
        addWidget(confirmButton = new KeyButton(0, getHeight() - 10, Keys.CONFIRM_KEY, "oxygen_core.gui.button.confirm")
                .setPressListener(this::confirm));
        confirmButton.setX(getX() + (int) ((buttonPosSegment - confirmButton.getText().getWidth()) / 2F));
        KeyButton cancelButton;
        addWidget(cancelButton = new KeyButton(0, getHeight() - 10, Keys.CANCEL_KEY, "oxygen_core.gui.button.cancel")
                .setPressListener(this::close));
        cancelButton.setX(getX() + buttonPosSegment + (int) ((buttonPosSegment - cancelButton.getText().getWidth()) / 2F));
    }

    @Override
    public boolean close() {
        if (cancelTask != null) {
            cancelTask.run();
        }
        return super.close();
    }

    private void confirm() {
        confirmTask.run();
        close();
    }
}
