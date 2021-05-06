package austeretony.oxygen_core.client.gui.base.text;

import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.KeyPressListener;
import austeretony.oxygen_core.client.gui.base.listener.MouseClickListener;
import austeretony.oxygen_core.client.gui.base.listener.TextFiledStateChangeListener;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class TextBoxField extends Widget<TextBoxField> {

    public static final String CURSOR = "_";

    @Nonnull
    protected Fill fill;
    @Nonnull
    protected final Text text;
    protected final int maxSymbols;

    @Nullable
    protected MouseClickListener clickListener;
    @Nullable
    protected KeyPressListener keyPressListener;
    @Nullable
    protected TextFiledStateChangeListener stateChangeListener;

    protected int cursorCounter;
    protected boolean focused, selected, newLine;

    protected List<String> lines = new ArrayList<>(1);

    public TextBoxField(int x, int y, int width, int height, int maxSymbols, @Nonnull Fill fill, @Nonnull Text text) {
        setPosition(x, y);
        setSize(width, height);
        this.maxSymbols = maxSymbols;
        this.fill = fill;
        this.text = text;

        setEnabled(true);
        setVisible(true);
    }

    public TextBoxField(int x, int y, int width, int height, int maxSymbols) {
        this(x, y, width, height, maxSymbols, Fills.textField(), Texts.additional(""));
    }

    public <T extends TextBoxField> T setMouseClickListener(MouseClickListener listener) {
        clickListener = listener;
        return (T) this;
    }

    public <T extends TextBoxField> T setKeyPressListener(KeyPressListener listener) {
        keyPressListener = listener;
        return (T) this;
    }

    public <T extends TextBoxField> T setStateChangeListener(TextFiledStateChangeListener listener) {
        stateChangeListener = listener;
        return (T) this;
    }

    @Override
    public void update() {
        cursorCounter++;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int color = fill.getColorEnabled();
        if (!isEnabled())
            color = fill.getColorDisabled();
        else if (isMouseOver() || focused)
            color = fill.getColorMouseOver();
        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), color);

        color = text.getColorEnabled();
        if (!isEnabled())
            color = text.getColorDisabled();
        else if (isMouseOver() || focused)
            color = text.getColorMouseOver();

        float lineHeight = text.getHeight() + text.getHeight() / 3F;
        boolean drawCursor = cursorCounter / 6 % 2 == 0;
        if (focused && drawCursor && (lines.isEmpty() || newLine)) {
            GUIUtils.drawString(CURSOR, 2, 2 + lines.size() * lineHeight, text.getScale(), color, text.isShadowEnabled());
        }

        int i;
        for (i = 0; i < lines.size(); i++) {
            if (drawCursor && !newLine && focused && i == lines.size() - 1) {
                GUIUtils.drawString(lines.get(i) + CURSOR, 2, 2 + i * lineHeight, text.getScale(), color, text.isShadowEnabled());
            } else {
                GUIUtils.drawString(lines.get(i), 2, 2 + i * lineHeight, text.getScale(), color, text.isShadowEnabled());
            }
        }

        if (selected) {
            GUIUtils.drawSelectionBox(1, 1, getWidth() - 1, (int) (lines.size() * lineHeight) + 1);
        }

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (isMouseOver() && mouseButton == MouseButtons.LEFT_BUTTON) {
            setFocused(true);
            if (clickListener != null)
                clickListener.click(mouseX, mouseY, mouseButton);
            return true;
        }
        setFocused(false);
        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        boolean flag = processKey(typedChar, keyCode);
        if (flag && keyPressListener != null) {
            keyPressListener.press(typedChar, keyCode);
        }
        return flag;
    }

    private boolean processKey(char typedChar, int keyCode) {
        if (!isEnabled()) return false;
        if (focused) {
            newLine = false;

            if (keyCode == Keyboard.KEY_ESCAPE) {
                setFocused(false);
                return false;
            }

            if (GuiScreen.isKeyComboCtrlA(keyCode)) {
                selected = true;
                return true;
            } else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
                if (selected) {
                    GuiScreen.setClipboardString(text.getText());
                    return true;
                }
                return false;
            } else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
                if (selected) {
                    setText(ChatAllowedCharacters.filterAllowedCharacters(GuiScreen.getClipboardString()));
                } else {
                    setText(text.getText() + GuiScreen.getClipboardString());
                }
                return true;
            } else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
                if (selected) {
                    setText("");
                    selected = false;
                    return true;
                }
                return false;
            } else {
                String typed = text.getText();
                if (keyCode == Keyboard.KEY_BACK) {
                    if (typed.isEmpty()) return false;
                    if (selected) {
                        setText("");
                        selected = false;
                        return true;
                    }
                    typed = typed.substring(0, typed.length() - 1);
                } else if (keyCode == Keyboard.KEY_RETURN || keyCode == Keyboard.KEY_NUMPADENTER) {
                    typed += "\n";
                    newLine = true;
                } else {
                    String symbol = Character.toString(typedChar);
                    symbol = ChatAllowedCharacters.filterAllowedCharacters(symbol);
                    if (symbol.isEmpty()) return false;
                    typed += symbol;
                }
                if (selected) {
                    typed = Character.toString(typedChar);
                    selected = false;
                }
                setText(typed);
                return true;
            }
        }
        return false;
    }

    private void ensureNotExceedSymbolsLimit() {
        String typed = text.getText();
        if (typed.length() >= maxSymbols) {
            text.setText(typed.substring(0, maxSymbols));
        }
        lines = GUIUtils.splitTextToLines(text.getText(), text.getScale(), getWidth());
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    @Nonnull
    public String getTypedText() {
        return text.getText();
    }

    public void setText(@Nonnull String text) {
        this.text.setText(text);
        ensureNotExceedSymbolsLimit();
    }

    public void reset() {
        setText("");
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public <T extends TextBoxField> T setFill(@Nonnull Fill fill) {
        this.fill = fill;
        return (T) this;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean flag) {
        focused = flag;
        if (stateChangeListener != null) {
            stateChangeListener.stateChanged(flag);
        }
    }

    @Override
    public String toString() {
        return "TextBoxField[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", fill= " + fill.toString() + ", text= " + text + "]";
    }
}
