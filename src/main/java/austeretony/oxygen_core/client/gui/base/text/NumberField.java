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
import org.lwjgl.input.Keyboard;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class NumberField extends Widget<NumberField> {

    public static final String CURSOR = "_";

    @Nonnull
    protected Fill fill;
    @Nonnull
    protected final Text text;
    protected final int decimalDigitsAmount;
    protected final boolean decimal;
    protected int maxSymbols;
    protected long minNumber, maxNumber;

    @Nullable
    protected MouseClickListener clickListener;
    @Nullable
    protected KeyPressListener keyPressListener;
    @Nullable
    protected TextFiledStateChangeListener stateChangeListener;

    protected int cursorCounter;
    protected boolean focused, selected;

    public NumberField(int x, int y, int width, @Nonnull Fill fill, @Nonnull Text text, long minNumber, long maxNumber,
                       boolean decimal, int decimalDigitsAmount) {
        setPosition(x, y);
        setSize(width, 8);
        this.fill = fill;
        this.text = text;

        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
        this.decimal = decimal;
        this.decimalDigitsAmount = decimalDigitsAmount;
        maxSymbols = String.valueOf(Math.max(minNumber, maxNumber)).length() + (decimal ? decimalDigitsAmount + 1 : 0);

        setEnabled(true);
        setVisible(true);
    }

    public NumberField(int x, int y, int width, long minNumber, long maxNumber, boolean decimal, int decimalDigitsAmount) {
        this(x, y, width, Fills.textField(), Texts.additional(""), minNumber, maxNumber, decimal, decimalDigitsAmount);
    }

    public NumberField(int x, int y, int width, long minNumber, long maxNumber) {
        this(x, y, width, Fills.textField(), Texts.additional(""), minNumber, maxNumber, false, 0);
    }

    public void setValueRange(long minNumber, long maxNumber) {
        this.minNumber = minNumber;
        this.maxNumber = maxNumber;
        maxSymbols = String.valueOf(Math.max(minNumber, maxNumber)).length() + (decimal ? decimalDigitsAmount + 1 : 0);
    }

    public <T extends NumberField> T setMouseClickListener(MouseClickListener listener) {
        clickListener = listener;
        return (T) this;
    }

    public <T extends NumberField> T setKeyPressListener(KeyPressListener listener) {
        keyPressListener = listener;
        return (T) this;
    }

    public <T extends NumberField> T setStateChangeListener(TextFiledStateChangeListener listener) {
        stateChangeListener = listener;
        return (T) this;
    }

    @Override
    public void update() {
        cursorCounter++;
        if (cursorCounter == Integer.MAX_VALUE) {
            cursorCounter = 0;
        }
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

        GUIUtils.pushMatrix();
        GUIUtils.translate(1, (getHeight() - GUIUtils.getTextHeight(text.getScale())) / 2 + .5F);
        GUIUtils.scale(text.getScale(), text.getScale());

        color = text.getColorEnabled();
        if (!isEnabled())
            color = text.getColorDisabled();
        else if (isMouseOver() || focused)
            color = text.getColorMouseOver();

        GUIUtils.drawString(text.getText(), 0, 0, color, text.isShadowEnabled());
        if (focused && cursorCounter / 6 % 2 == 0) {
            float offsetX = GUIUtils.getTextWidth(text.getText(), 1F);
            GUIUtils.drawString(CURSOR, offsetX, 0, color, text.isShadowEnabled());
        }
        GUIUtils.popMatrix();

        if (selected) {
            GUIUtils.drawSelectionBox(1, 1,
                    (int) Math.ceil(GUIUtils.getTextWidth(text.getText(), text.getScale())), getHeight() - 1);
        }

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (isMouseOver() && mouseButton == MouseButtons.LEFT_BUTTON) {
            setFocused(true);
            if (clickListener != null) {
                clickListener.click(mouseX, mouseY, mouseButton);
            }
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

    public boolean processKey(char typedChar, int keyCode) {
        if (!isEnabled()) return false;
        if (focused) {
            if (keyCode == Keyboard.KEY_ESCAPE) {
                setFocused(false);
                return false;
            }

            if (keyPressListener != null) {
                keyPressListener.press(typedChar, keyCode);
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
                    setText(filterAllowedCharacters(GuiScreen.getClipboardString()));
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
                } else {
                    String symbol = Character.toString(typedChar);
                    symbol = filterAllowedCharacters(symbol);
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

    private String filterAllowedCharacters(String input) {
        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (isAllowedCharacter(c)) {
                if (c == '-') {
                    if (minNumber >= 0) break;
                    if (!getTypedText().isEmpty()) break;
                } else if (c == '.') {
                    if (!decimal) break;
                    if (getTypedText().isEmpty()) break;
                    if (getTypedText().contains(".")) break;
                } else {
                    if (getTypedText().contains(".")) {
                        String[] array = getTypedText().split("[.]");
                        if (array.length == 2 && array[1].length() == decimalDigitsAmount) break;
                    } else if (getTypedText().startsWith("0")) break;
                }
                builder.append(c);
            }
        }

        String str = getTypedText() + builder.toString();
        if (str.isEmpty()) {
            str = "0";
        }
        if (str.length() == 1 && str.startsWith("-")) {
            str = "0";
        }
        if (decimal) {
            float value = 0.0F;
            try {
                value = Float.parseFloat(str);
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
            return (maxNumber == Long.MAX_VALUE || value <= maxNumber) ? builder.toString() : "";
        } else {
            long value = 0L;
            try {
                value = Long.parseLong(str);
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
            return (maxNumber == Long.MAX_VALUE || value <= maxNumber) ? builder.toString() : "";
        }
    }

    private boolean isAllowedCharacter(char c) {
        return Character.isDigit(c) || c == '-' || c == '.';
    }

    private void ensureNotExceedSymbolsLimit() {
        String typed = text.getText();
        if (typed.length() >= maxSymbols) {
            text.setText(typed.substring(0, maxSymbols));
        }
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    @Nonnull
    public String getTypedText() {
        return text.getText();
    }

    public void setText(@Nonnull String str) {
        str = ensureValueLimits(str);
        text.setText(str);
        ensureNotExceedSymbolsLimit();
    }

    public void reset() {
        setText(String.valueOf(minNumber));
    }

    private String ensureValueLimits(@Nonnull String text) {
        if (text.isEmpty()) return text;
        long value = (long) Double.parseDouble(text);
        if (value < minNumber) return String.valueOf(minNumber);
        if (value > maxNumber) return String.valueOf(maxNumber);
        return text;
    }

    public long getTypedNumberAsLong() {
        String text = getTypedText();
        if (text.isEmpty()) return 0L;
        if (text.equals("-")) return 0L;
        return Long.parseLong(text);
    }

    public double getTypedNumberAsDouble() {
        String text = getTypedText();
        if (text.isEmpty()) return 0D;
        return Double.parseDouble(text);
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public <T extends NumberField> T setFill(@Nonnull Fill fill) {
        this.fill = fill;
        return (T) this;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean flag) {
        focused = flag;
        if (!flag) {
            String str = getTypedText();
            if (str.isEmpty()) {
                str = "";
            }
            text.setText(ensureValueLimits(str));
        }
        if (stateChangeListener != null) {
            stateChangeListener.stateChanged(flag);
        }
    }

    @Override
    public String toString() {
        return "NumberField[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", fill= " + fill.toString() + ", text= " + text + "]";
    }
}
