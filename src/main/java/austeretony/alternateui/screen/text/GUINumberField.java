package austeretony.alternateui.screen.text;

import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;

/**
 * Простое однострочное поле для ввода чисел.
 */
public class GUINumberField extends GUISimpleElement<GUINumberField> {

    protected char cursorSymbol;

    protected final int maxNumber;

    protected boolean resetTypedText, hasCustomCursor;

    protected String typedNumber;

    protected int lineScrollOffset, cursorCounter, cursorPosition, selectionEnd;

    /**
     * Поле для ввода числа.
     * 
     * @param xPosition позиция по x
     * @param yPosition позиция по y
     * @param width ширина поля
     * @param maxNumber максимальное число, которое можно ввести
     */
    public GUINumberField(int xPosition, int yPosition, int width, int maxNumber) {
        this.setPosition(xPosition, yPosition);
        this.setSize(this.textWidth(String.valueOf(maxNumber), 1.0F), FONT_HEIGHT);        
        this.maxNumber = maxNumber;
        this.typedNumber = "";
        this.enableFull();		
    }

    public void setNumber(int value) {
        this.setText(String.valueOf(value));
    }

    private void setText(String text) {
        int value = 0;
        boolean empty = false;
        try {
            if (!text.equals(""))
                value = Integer.valueOf(text);
            else
                empty = true;
        } catch(NumberFormatException exception) {}
        if (value > this.getMaxNumber())
            this.typedNumber = String.valueOf(this.getMaxNumber());
        else
            this.typedNumber = empty ? "" : String.valueOf(value);
        this.setCursorPositionEnd();
    }

    public void reset() {
        this.setText("");
    }

    private String getSelectedText() {
        int 
        i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd,
                j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;
        return this.typedNumber.substring(i, j);
    }

    private void writeText(String text) {
        String 
        s1 = "",
        s2 = this.filterAllowedCharacters(text);
        int 
        i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd,
                j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition,
                        k = String.valueOf(this.maxNumber).length() - this.typedNumber.length() - (i - this.selectionEnd);
        boolean flag = false;
        if (this.typedNumber.length() > 0)
            s1 = s1 + this.typedNumber.substring(0, i);
        int l;
        if (k < s2.length()) {
            s1 = s1 + s2.substring(0, k);
            l = k;
        } else {
            s1 = s1 + s2;
            l = s2.length();
        }
        if (this.typedNumber.length() > 0 && j < this.typedNumber.length())
            s1 = s1 + this.typedNumber.substring(j);
        this.typedNumber = s1;
        this.moveCursorBy(i - this.selectionEnd + l);
    }

    private boolean isAllowedCharacter(char character) {
        return character == '0' || character == '1' || character == '2' || character == '3' || character == '4' ||
                character == '5' || character == '6' || character == '7' || character == '8' || character == '9';
    }

    private String filterAllowedCharacters(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : input.toCharArray())
            if (this.isAllowedCharacter(c))
                stringBuilder.append(c);
        int value = 0;
        try {
            value = Integer.valueOf(this.typedNumber + stringBuilder.toString());
        } catch(NumberFormatException exception) {}
        return value <= this.getMaxNumber() ? stringBuilder.toString() : "";
    }

    private void deleteWords(int index) {
        if (this.typedNumber.length() != 0) {
            if (this.selectionEnd != this.cursorPosition)
                this.writeText("");
            else
                this.deleteFromCursor(this.getNthWordFromCursor(index) - this.cursorPosition);          
        }
    }

    private void deleteFromCursor(int index) {
        if (this.typedNumber.length() != 0) {
            if (this.selectionEnd != this.cursorPosition)
                this.writeText("");
            else {
                boolean flag = index < 0;
                int 
                j = flag ? this.cursorPosition + index : this.cursorPosition,
                        k = flag ? this.cursorPosition : this.cursorPosition + index;
                String s = "";
                if (j >= 0)
                    s = this.typedNumber.substring(0, j);

                if (k < this.typedNumber.length())
                    s = s + this.typedNumber.substring(k);
                this.typedNumber = s;
                if (flag)
                    this.moveCursorBy(index);
            }
        }
    }

    private int getNthWordFromCursor(int index) {
        return this.getNthWordFromPos(index, this.getCursorPosition());
    }

    private int getNthWordFromPos(int index1, int index2) {
        return this.getWord(index1, this.getCursorPosition(), true);
    }

    private int getWord(int index1, int index2, boolean flag) {

        int k = index2;

        boolean flag1 = index1 < 0;

        int l = Math.abs(index1);

        for (int i1 = 0; i1 < l; ++i1) {

            if (flag1) {

                while (flag && k > 0 && this.typedNumber.charAt(k - 1) == 32) {

                    k--;
                }

                while (k > 0 && this.typedNumber.charAt(k - 1) != 32) {

                    k--;
                }
            }

            else {

                int j1 = this.typedNumber.length();

                k = this.typedNumber.indexOf(32, k);

                if (k == - 1) {

                    k = j1;
                }

                else {

                    while (flag && k < j1 && this.typedNumber.charAt(k) == 32) {

                        k++;
                    }
                }
            }
        }

        return k;
    }

    public void moveCursorBy(int offset) {

        this.setCursorPosition(this.selectionEnd + offset);
    }

    private void setCursorPosition(int index) {

        this.cursorPosition = index;

        int j = this.typedNumber.length();

        if (this.cursorPosition < 0) {

            this.cursorPosition = 0;
        }

        if (this.cursorPosition > j) {

            this.cursorPosition = j;
        }

        this.setSelectionPos(this.cursorPosition);
    }

    public void setCursorPositionZero() {

        this.setCursorPosition(0);
    }

    public void setCursorPositionEnd() {

        this.setCursorPosition(this.typedNumber.length());
    }

    @Override
    public boolean keyTyped(char keyChar, int keyCode) {

        if (!this.isDragged()) {

            return false;
        }

        else {

            switch (keyChar) {

            case 1:

                this.setCursorPositionEnd();
                this.setSelectionPos(0);

                return true;

            case 3:

                GuiScreen.setClipboardString(this.getSelectedText());

                return true;

            case 22:

                if (this.isEnabled()) {

                    this.writeText(GuiScreen.getClipboardString());
                }

                return true;

            case 24:

                GuiScreen.setClipboardString(this.getSelectedText());

                if (this.isEnabled()) {

                    this.writeText("");
                }

                return true;

            default:

                switch (keyCode) {

                case 14:

                    if (GuiScreen.isCtrlKeyDown()) {

                        if (this.isEnabled()) {

                            this.deleteWords(- 1);
                        }
                    }

                    else if (this.isEnabled()) {

                        this.deleteFromCursor(- 1);
                    }

                    return true;

                case 199:

                    if (GuiScreen.isShiftKeyDown()) {

                        this.setSelectionPos(0);
                    }

                    else {

                        this.setCursorPositionZero();
                    }

                    return true;

                case 203:

                    if (GuiScreen.isShiftKeyDown()) {

                        if (GuiScreen.isCtrlKeyDown()) {

                            this.setSelectionPos(this.getNthWordFromPos(-1, this.getSelectionEnd()));
                        }

                        else {

                            this.setSelectionPos(this.getSelectionEnd() - 1);
                        }
                    }

                    else if (GuiScreen.isCtrlKeyDown()) {

                        this.setCursorPosition(this.getNthWordFromCursor(-1));
                    }

                    else {

                        this.moveCursorBy(-1);
                    }

                    return true;

                case 205:

                    if (GuiScreen.isShiftKeyDown()) {

                        if (GuiScreen.isCtrlKeyDown()) {

                            this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                        }

                        else {

                            this.setSelectionPos(this.getSelectionEnd() + 1);
                        }
                    }

                    else if (GuiScreen.isCtrlKeyDown()) {

                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    }

                    else {

                        this.moveCursorBy(1);
                    }

                    return true;

                case 207:

                    if (GuiScreen.isShiftKeyDown()) {

                        this.setSelectionPos(this.typedNumber.length());
                    }

                    else {

                        this.setCursorPositionEnd();
                    }

                    return true;

                case 211:

                    if (GuiScreen.isCtrlKeyDown()) {

                        if (this.isEnabled()) {

                            this.deleteWords(1);
                        }
                    }

                    else if (this.isEnabled()) {

                        this.deleteFromCursor(1);
                    }

                    return true;

                default:

                    if (ChatAllowedCharacters.isAllowedCharacter(keyChar)) {

                        if (this.isEnabled()) {

                            this.writeText(Character.toString(keyChar));
                        }

                        return true;
                    }

                    else {

                        return false;
                    }
                }
            }
        }
    }

    //TODO mouseClicked()
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        boolean flag = false;
        if (this.isDoubleClickRequired()) {
            if (this.isClickedLately()) {
                flag = true;
                this.setLastClickTime(0L);
            } else
                this.setLastClickTime(Minecraft.getSystemTime());
        } else
            flag = true;
        if (flag) {
            if (this.isEnabled()) {
                this.setDragged(this.isHovered());	
                if (this.isDragged() && this.shouldCancelDraggedLogic())
                    resetDragged();
                if (!this.isHovered() && this.shouldResetOnMisclick())
                    this.setText("");	   	       
                if (this.isDragged() && this.isHovered()) {
                    int l = mouseX - this.getX();
                    if (this.isDynamicBackgroundEnabled())
                        l -= 4;
                    String s = this.mc.fontRenderer.trimStringToWidth(this.typedNumber.substring(this.lineScrollOffset), (int) ((float) this.getWidth() * this.getScale()));
                    this.setCursorPosition(this.mc.fontRenderer.trimStringToWidth(s, l).length() + this.lineScrollOffset);
                }
                this.screen.handleElementClick(this.screen.getWorkspace().getCurrentSection(), this);
                this.screen.getWorkspace().getCurrentSection().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);
                if (this.screen.getWorkspace().getCurrentSection().hasCurrentCallback())
                    this.screen.getWorkspace().getCurrentSection().getCurrentCallback().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);
                return true;
            }
        }
        return false;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            if (this.isDynamicBackgroundEnabled()) {
                this.drawRect(0, 0, this.getWidth(), this.getHeight() + 1, this.getEnabledBackgroundColor());
                if (this.isDragged())
                    this.drawRect(0, 0, this.getWidth(), this.getHeight() + 1, this.getHoveredBackgroundColor());   
            }
            int 
            i = this.isEnabled() ? this.getEnabledTextColor() : this.getDisabledTextColor(),
                    j = this.cursorPosition - this.lineScrollOffset,
                    k = this.selectionEnd - this.lineScrollOffset;           
            String s = this.mc.fontRenderer.trimStringToWidth(this.typedNumber.substring(this.lineScrollOffset), this.getWidth()); 
            boolean 
            flag = j >= 0 && j <= s.length(),
            flag1 = this.isDragged() && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.isDynamicBackgroundEnabled() ? 2 : 0, i1 = this.isDynamicBackgroundEnabled() ? (this.getHeight() - this.textHeight(this.getTextScale())) / 2 : 0, j1 = l;
            if (k > s.length())
                k = s.length();
            if (s.length() > 0) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = this.mc.fontRenderer.drawString(s1, l, i1 + 1, i, this.isTextShadowEnabled());
            }
            if (this.hasDisplayText() && !this.isDragged() && this.typedNumber.length() == 0)
                this.mc.fontRenderer.drawString(this.getDisplayText(), l, i1 + 1, i, this.isTextShadowEnabled());
            boolean flag2 = this.cursorPosition < this.typedNumber.length() || this.typedNumber.length() >= String.valueOf(this.maxNumber).length();
            int k1 = j1;
            if (!flag)
                k1 = j > 0 ? l + this.getWidth() : l;
                else if (flag2) {
                    k1 = j1 - 1;
                    j1++;
                }
            if (s.length() > 0 && flag && j < s.length())
                this.mc.fontRenderer.drawString(s.substring(j), j1, i1 + 1, i, this.isTextShadowEnabled());
            if (flag1) {
                if (!this.hasCustomCursor()) {
                    if (flag2)
                        this.drawRect(k1, i1, k1 + 1, i1 + FONT_HEIGHT, - 3092272);
                    else
                        this.drawRect(k1, i1, k1 + 1, i1 + FONT_HEIGHT, - 3092272);
                } else
                    this.mc.fontRenderer.drawString(String.valueOf(this.getCursorSymbol()), k1, i1 + 1, this.getEnabledTextColor(), this.isTextShadowEnabled());
            }
            if (k != j) {
                int l1 = l + this.mc.fontRenderer.getStringWidth(s.substring(0, k));
                if (!this.hasCustomCursor())
                    this.drawCursorVertical(k1, i1, l1 - 1, i1 + 1 + FONT_HEIGHT);
                else
                    this.mc.fontRenderer.drawString(String.valueOf(this.getCursorSymbol()), k1, i1 + 1, this.getEnabledTextColor(), this.isTextShadowEnabled());
            }
            GlStateManager.popMatrix();
        }
    }

    private void drawCursorVertical(int xStart, int yStart, int xEnd, int yEnd) {
        int i1;
        if (xStart < xEnd) {
            i1 = xStart;
            xStart = xEnd;
            xEnd = i1;
        }
        if (yStart < yEnd) {
            i1 = yStart;
            yStart = yEnd;
            yEnd = i1;
        }
        if (xEnd > this.getX() + this.getWidth())
            xEnd = this.getX() + this.getWidth();
        if (xStart > this.getX() + this.getWidth())
            xStart = this.getX() + this.getWidth();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
        GlStateManager.disableTexture2D();
        GlStateManager.enableColorLogic();
        GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
        bufferbuilder.pos((double)xStart, (double)yEnd, 0.0D).endVertex();
        bufferbuilder.pos((double)xEnd, (double)yEnd, 0.0D).endVertex();
        bufferbuilder.pos((double)xEnd, (double)yStart, 0.0D).endVertex();
        bufferbuilder.pos((double)xStart, (double)yStart, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.disableColorLogic();
        GlStateManager.enableTexture2D();
    }

    public int getCursorPosition() {
        return this.cursorPosition;
    }

    @Override
    public GUINumberField setDragged(boolean isDragged) {
        super.setDragged(isDragged);
        if (!this.isCanNotBeDragged())
            this.cursorCounter = 0;
        return this;
    }

    public int getSelectionEnd() {
        return this.selectionEnd;
    }

    @Override
    public int getWidth() {
        return this.isDynamicBackgroundEnabled() ? super.getWidth() + 3 : super.getWidth();
    }

    private void setSelectionPos(int index) {
        int j = this.typedNumber.length();
        if (index > j)
            index = j;
        if (index < 0)
            index = 0;
        this.selectionEnd = index;
        if (this.mc.fontRenderer != null) {
            if (this.lineScrollOffset > j)
                this.lineScrollOffset = j;
            int k = this.getWidth();
            String s = this.mc.fontRenderer.trimStringToWidth(this.typedNumber.substring(this.lineScrollOffset), k);
            int l = s.length() + this.lineScrollOffset;
            if (index == this.lineScrollOffset)
                this.lineScrollOffset -= this.mc.fontRenderer.trimStringToWidth(this.typedNumber, k, true).length();
            if (index > l)
                this.lineScrollOffset += index - l;
            else if (index <= this.lineScrollOffset) 
                this.lineScrollOffset -= this.lineScrollOffset - index;
            if (this.lineScrollOffset < 0)
                this.lineScrollOffset = 0;
            if (this.lineScrollOffset > j)
                this.lineScrollOffset = j;
        }
    }

    public boolean shouldResetOnMisclick() {
        return this.resetTypedText;
    }

    public GUINumberField resetOnMisclick() {
        this.resetTypedText = true;
        return this;
    }

    public boolean hasCustomCursor() {
        return this.hasCustomCursor;
    }

    public char getCursorSymbol() {
        return this.cursorSymbol;
    }

    /**
     * Использует указанный символ как символ курсора.
     * 
     * @param symbol
     * 
     * @return вызывающий элемент
     */
    public GUINumberField setCustomCursor(char symbol) {
        this.cursorSymbol = symbol;
        this.hasCustomCursor = true;
        return this;
    }

    public int getTypedNumber() {
        int value = 0;
        if (!this.typedNumber.isEmpty())
            value = Integer.valueOf(this.typedNumber);
        else
            value = this.getDefaultNumber();
        return value;
    }

    public int getDefaultNumber() {
        int value = 0;
        if (!this.getDisplayText().isEmpty())
            value = Integer.valueOf(this.getDisplayText());
        return value;
    }

    public GUINumberField setDefaultNumber(int value) {
        if (value < this.getMaxNumber())
            this.setDisplayText(String.valueOf(value));
        return this;
    }

    public int getMaxNumber() {
        return this.maxNumber;
    }

    @Override
    public void updateCursorCounter() {
        this.cursorCounter++;
    }
}
