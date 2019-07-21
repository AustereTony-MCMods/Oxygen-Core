package austeretony.alternateui.screen.text;

import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.math.MathHelper;

/**
 * Простое однострочное поле для ввода текста.
 */
public class GUITextField extends GUISimpleElement<GUITextField> {

    protected String typedText;

    protected char cursorSymbol;

    protected final int maxStringLength;

    protected boolean resetTypedText, hasCustomCursor, numberFieldMode;

    protected int lineScrollOffset, cursorCounter, cursorPosition, selectionEnd, maxNumber = - 1;

    /**
     * Текстовое поле.
     * 
     * @param xPosition позиция по x
     * @param yPosition позиция по y
     * @param width ширина
     * @param maxStringLength максимальное кол-во символов, которое можно ввести
     */
    public GUITextField(int xPosition, int yPosition, int width, int maxStringLength) {   	    	
        this.setPosition(xPosition, yPosition);
        this.setSize(width, FONT_HEIGHT);
        this.maxStringLength = maxStringLength > 64 ? 64 : maxStringLength;        
        this.typedText = "";
        this.enableFull();
    }

    public GUITextField enableNumberFieldMode() {
        this.numberFieldMode = true;
        return this;
    }

    public GUITextField enableNumberFieldMode(int maxNumber) {
        this.numberFieldMode = true;
        this.maxNumber = maxNumber;
        return this;
    }

    public int getTypedNumber() {
        if (!this.numberFieldMode || this.typedText.isEmpty())
            return 0;
        return Integer.parseInt(this.typedText);
    }

    @Override
    public void updateCursorCounter() {    	
        this.cursorCounter++;
    }

    public String getTypedText() {  	
        return this.typedText;
    }

    public GUITextField setText(String text) {   	
        if (text.length() > this.maxStringLength)       	
            this.typedText = text.substring(0, this.maxStringLength);    
        else
            this.typedText = text;
        this.setCursorPositionEnd();
        return this;
    }

    public void reset() {
        this.setText("");
    }

    private String getSelectedText() {  	
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;       
        return this.typedText.substring(i, j);
    }

    public void writeText(String text) {    	
        String s1 = "";
        String s2 = this.numberFieldMode ? this.filterAllowedCharacters(text) : ChatAllowedCharacters.filterAllowedCharacters(text);      
        int 
        i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd,
                j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition,
                        k = this.maxStringLength - this.typedText.length() - (i - this.selectionEnd),
                        l;
        if (!this.typedText.isEmpty())
            s1 = s1 + this.typedText.substring(0, i);
        if (k < s2.length()) {       	
            s1 = s1 + s2.substring(0, k);         
            l = k;
        } else {        	
            s1 = s1 + s2;            
            l = s2.length();
        }
        if (!this.typedText.isEmpty() && j < this.typedText.length())
            s1 = s1 + this.typedText.substring(j);
        this.typedText = s1;       
        this.moveCursorBy(i - this.selectionEnd + l);
    }

    private boolean isAllowedCharacter(char character) {
        return character == '0' 
                || character == '1' 
                || character == '2' 
                || character == '3' 
                || character == '4' 
                || character == '5' 
                || character == '6' 
                || character == '7' 
                || character == '8' 
                || character == '9';
    }

    private String filterAllowedCharacters(String input) {
        StringBuilder stringBuilder = new StringBuilder();
        for (char c : input.toCharArray())
            if (this.isAllowedCharacter(c) && !this.typedText.startsWith("0"))
                stringBuilder.append(c);
        String typed = this.typedText + stringBuilder.toString();
        int value = typed.isEmpty() ? 0 : Integer.parseInt(typed);
        return (this.maxNumber == - 1 || value <= this.maxNumber) ? stringBuilder.toString() : "";
    }

    private void deleteWords(int index) {   	
        if (this.typedText.length() != 0) {       	
            if (this.selectionEnd != this.cursorPosition)
                this.writeText("");
            else 
                this.deleteFromCursor(this.getNthWordFromCursor(index) - this.cursorPosition);
        }
    }

    private void deleteFromCursor(int index) {    	
        if (this.typedText.length() != 0) {      	
            if (this.selectionEnd != this.cursorPosition) {          	
                this.writeText("");
            } else {          	
                boolean flag = index < 0;               
                int j = flag ? this.cursorPosition + index : this.cursorPosition;
                int k = flag ? this.cursorPosition : this.cursorPosition + index;                
                String s = "";
                if (j >= 0)
                    s = this.typedText.substring(0, j);
                if (k < this.typedText.length())            
                    s = s + this.typedText.substring(k);
                this.typedText = s;
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
                while (flag && k > 0 && this.typedText.charAt(k - 1) == 32) {
                    k--;
                }
                while (k > 0 && this.typedText.charAt(k - 1) != 32) {
                    k--;
                }
            } else {
                int j1 = this.typedText.length();
                k = this.typedText.indexOf(32, k);
                if (k == - 1) {
                    k = j1;
                } else {
                    while (flag && k < j1 && this.typedText.charAt(k) == 32) {
                        k++;
                    }
                }
            }
        }
        return k;
    }

    protected void moveCursorBy(int offset) {
        this.setCursorPosition(this.selectionEnd + offset);
    }

    private void setCursorPosition(int index) {
        this.cursorPosition = index;
        int i = this.typedText.length();
        this.cursorPosition = MathHelper.clamp(this.cursorPosition, 0, i);
        this.setSelectionPos(this.cursorPosition);
    }

    private void setCursorPositionZero() {    	
        this.setCursorPosition(0);
    }

    protected void setCursorPositionEnd() {   	
        this.setCursorPosition(this.typedText.length());
    }

    @Override
    public boolean keyTyped(char keyChar, int keyCode) {
        if (!this.isDragged())
            return false;
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
                if (this.isEnabled())
                    this.writeText(GuiScreen.getClipboardString());
                return true;
            case 24:
                GuiScreen.setClipboardString(this.getSelectedText());
                if (this.isEnabled())
                    this.writeText("");
                return true;
            default:
                switch (keyCode) {
                case 14:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.isEnabled())
                            this.deleteWords(- 1);
                    } else if (this.isEnabled())
                        this.deleteFromCursor(- 1);
                    return true;
                case 199:
                    if (GuiScreen.isShiftKeyDown())
                        this.setSelectionPos(0);
                    else
                        this.setCursorPositionZero();
                    return true;
                case 203:
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown())
                            this.setSelectionPos(this.getNthWordFromPos(- 1, this.getSelectionEnd()));
                        else
                            this.setSelectionPos(this.getSelectionEnd() - 1);
                    } else if (GuiScreen.isCtrlKeyDown())
                        this.setCursorPosition(this.getNthWordFromCursor(- 1));
                    else
                        this.moveCursorBy(- 1);
                    return true;
                case 205:
                    if (GuiScreen.isShiftKeyDown()) {
                        if (GuiScreen.isCtrlKeyDown())
                            this.setSelectionPos(this.getNthWordFromPos(1, this.getSelectionEnd()));
                        else
                            this.setSelectionPos(this.getSelectionEnd() + 1);
                    } else if (GuiScreen.isCtrlKeyDown())
                        this.setCursorPosition(this.getNthWordFromCursor(1));
                    else
                        this.moveCursorBy(1);
                    return true;
                case 207:
                    if (GuiScreen.isShiftKeyDown())
                        this.setSelectionPos(this.typedText.length());
                    else 
                        this.setCursorPositionEnd();
                    return true;
                case 211:
                    if (GuiScreen.isCtrlKeyDown()) {
                        if (this.isEnabled())
                            this.deleteWords(1);
                    } else if (this.isEnabled())
                        this.deleteFromCursor(1);
                    return true;
                default:
                    if (ChatAllowedCharacters.isAllowedCharacter(keyChar)) {
                        if (this.isEnabled())
                            this.writeText(Character.toString(keyChar));
                        return true;
                    } else
                        return false;
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
                    int l = (int) ((1.0F + this.getScale()) * (float) (mouseX - this.getX()));//why?
                    if (this.isDynamicBackgroundEnabled())
                        l -= 4;                  
                    String s = this.mc.fontRenderer.trimStringToWidth(this.typedText.substring(this.lineScrollOffset), this.getWidth());
                    this.setCursorPosition(this.mc.fontRenderer.trimStringToWidth(s, l).length() + this.lineScrollOffset);
                    this.screen.handleElementClick(this.screen.getWorkspace().getCurrentSection(), this);
                    this.screen.getWorkspace().getCurrentSection().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);
                    if (this.screen.getWorkspace().getCurrentSection().hasCurrentCallback())
                        this.screen.getWorkspace().getCurrentSection().getCurrentCallback().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);
                    return true;
                } else
                    return false;
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
                this.drawRect(0, - 1, this.getWidth(), this.getHeight() + 1, this.getEnabledBackgroundColor());
                if (this.isDragged())
                    this.drawRect(0, - 1, this.getWidth(), this.getHeight() + 1, this.getHoveredBackgroundColor());
            }
            int 
            i = this.isEnabled() ? this.getEnabledTextColor() : this.getDisabledTextColor(),
                    j = this.cursorPosition - this.lineScrollOffset,
                    k = this.selectionEnd - this.lineScrollOffset;           

            String s = this.mc.fontRenderer.trimStringToWidth(this.typedText.substring(this.lineScrollOffset), this.getWidth()); 
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
            if (this.hasDisplayText() && !this.isDragged() && this.getTypedText().isEmpty())
                this.mc.fontRenderer.drawString(this.getDisplayText(), l, i1 + 1, this.getEnabledTextColor(), this.isTextShadowEnabled());
            boolean flag2 = this.cursorPosition < this.typedText.length() || this.typedText.length() >= this.getMaxStringLength();
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

    public int getMaxStringLength() {   	
        return this.maxStringLength;
    }

    public int getCursorPosition() {   	
        return this.cursorPosition;
    }

    @Override
    public GUITextField setDragged(boolean isDragged) {       	
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
        return this.isDynamicBackgroundEnabled() ? super.getWidth() + 8 : super.getWidth();
    }

    private void setSelectionPos(int index) {   	
        int i = this.typedText.length();
        if (index > i)
            index = i;
        if (index < 0)
            index = 0;
        this.selectionEnd = index;
        if (this.mc.fontRenderer != null) {
            if (this.lineScrollOffset > i)
                this.lineScrollOffset = i;
            int j = this.getWidth();//TODO getWidth() called
            String s = this.mc.fontRenderer.trimStringToWidth(this.typedText.substring(this.lineScrollOffset), j);
            int k = s.length() + this.lineScrollOffset;
            if (index == this.lineScrollOffset)
                this.lineScrollOffset -= this.mc.fontRenderer.trimStringToWidth(this.typedText, j, true).length();
            if (index > k)
                this.lineScrollOffset += index - k;
            else if (index <= this.lineScrollOffset)
                this.lineScrollOffset -= this.lineScrollOffset - index;
            this.lineScrollOffset = MathHelper.clamp(this.lineScrollOffset, 0, i);
        }
    }

    public boolean shouldResetOnMisclick() {    	
        return this.resetTypedText;
    }

    public GUITextField resetOnMisclick() {   	
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
     * @return
     */
    public GUITextField setCustomCursor(char symbol) {   	
        this.cursorSymbol = symbol;    	
        this.hasCustomCursor = true;  	
        return this;
    }
}
