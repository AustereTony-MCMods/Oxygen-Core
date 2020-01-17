package austeretony.alternateui.screen.text;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.math.MathHelper;

public class GUINumberField extends GUISimpleElement<GUINumberField> {

    protected String typedText;

    protected final boolean decimal, positive;

    protected int decimalDigitsAmount, maxStringLength, lineScrollOffset, cursorCounter, cursorPosition, selectionEnd;

    protected long maxNumber;

    public GUINumberField(int xPosition, int yPosition, int width, int height, long maxNumber, boolean decimal, int decimalDigitsAmount, boolean positive) {                 
        this.setPosition(xPosition, yPosition);
        this.setSize(width, height);
        this.typedText = "";
        this.maxNumber = maxNumber;
        this.decimal = decimal;
        this.decimalDigitsAmount = decimalDigitsAmount;
        this.positive = positive;
        if (positive)
            this.maxStringLength = String.valueOf(maxNumber).length() + (decimal ? decimalDigitsAmount + 1 : 0);
        else
            this.maxStringLength = String.valueOf(Long.MIN_VALUE).length() + (decimal ? decimalDigitsAmount + 1 : 0);
        this.enableFull();
    }

    public void setMaxNumber(long value) {
        this.maxNumber = value;
        if (this.positive)
            this.maxStringLength = String.valueOf(value).length() + (this.decimal ? this.decimalDigitsAmount + 1 : 0);
        else
            this.maxStringLength = String.valueOf(Long.MIN_VALUE).length() + (this.decimal ? this.decimalDigitsAmount + 1 : 0);
    }

    public String getTypedText() {      
        return this.typedText;
    }

    public long getTypedNumberAsLong() {
        if (this.typedText.isEmpty())
            return 0L;
        return Long.parseLong(this.typedText);
    }

    public float getTypedNumberAsFloat() {
        if (this.typedText.isEmpty())
            return 0.0F;
        return Float.parseFloat(this.typedText);
    }

    public void setText(String text) {          
        this.typedText = text;
        this.setCursorPositionEnd();
    }

    public void reset() {
        this.setText("");
    }

    @Override
    public void updateCursorCounter() {         
        this.cursorCounter++;
    }

    private String getSelectedText() {          
        int i = this.cursorPosition < this.selectionEnd ? this.cursorPosition : this.selectionEnd;
        int j = this.cursorPosition < this.selectionEnd ? this.selectionEnd : this.cursorPosition;       
        return this.typedText.substring(i, j);
    }

    public void writeText(String text) {        
        String s1 = "";
        String s2 = this.filterAllowedCharacters(text);      
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
        return character == '-'
                || character == '0' 
                || character == '1' 
                || character == '2' 
                || character == '3' 
                || character == '4' 
                || character == '5' 
                || character == '6' 
                || character == '7' 
                || character == '8' 
                || character == '9'
                || character == '.';
    }

    private String filterAllowedCharacters(String input) {
        StringBuilder builder = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (this.isAllowedCharacter(c)) {
                if (c == '-') {                 
                    if (this.positive)
                        break;
                    if (!this.typedText.isEmpty())
                        break;
                } else if (c == '.') {
                    if (!this.decimal)
                        break;
                    if (this.typedText.isEmpty())
                        break;
                    if (this.typedText.contains("."))
                        break;
                } else {
                    if (this.typedText.contains(".")) {
                        String[] array = this.typedText.split("[.]");
                        if (array.length == 2 && array[1].length() == this.decimalDigitsAmount)
                            break;
                    } else if (this.typedText.startsWith("0"))
                        break;
                }
                builder.append(c);
            }
        }

        String str = this.typedText + builder.toString();    
        if (str.isEmpty())
            str = "0";
        if (str.length() == 1 && str.startsWith("-"))
            str = "0";
        if (this.decimal) {
            float value = 0.0F;
            try {
                value = Float.parseFloat(str);
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
            return (this.maxNumber == Long.MAX_VALUE || value <= this.maxNumber) ? builder.toString() : "";
        } else {
            long value = 0L;
            try {
                value = Long.parseLong(str);
            } catch (NumberFormatException exception) {
                exception.printStackTrace();
            }
            return (this.maxNumber == Long.MAX_VALUE || value <= this.maxNumber) ? builder.toString() : "";
        }
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
        if (GuiScreen.isKeyComboCtrlA(keyCode)) {
            this.setCursorPositionEnd();
            this.setSelectionPos(0);
            return true;
        } else if (GuiScreen.isKeyComboCtrlC(keyCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            return true;
        } else if (GuiScreen.isKeyComboCtrlV(keyCode)) {
            if (this.isEnabled())
                this.writeText(GuiScreen.getClipboardString());
            return true;
        } else if (GuiScreen.isKeyComboCtrlX(keyCode)) {
            GuiScreen.setClipboardString(this.getSelectedText());
            if (this.isEnabled())
                this.writeText("");
            return true;
        } else {
            switch (keyCode) {
            case Keyboard.KEY_BACK:
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled())
                        this.deleteWords(- 1);
                } else if (this.isEnabled())
                    this.deleteFromCursor(- 1);
                return true;
            case Keyboard.KEY_HOME:
                if (GuiScreen.isShiftKeyDown())
                    this.setSelectionPos(0);
                else
                    this.setCursorPositionZero();
                return true;
            case Keyboard.KEY_LEFT:
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
            case Keyboard.KEY_RIGHT:
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
            case Keyboard.KEY_END:
                if (GuiScreen.isShiftKeyDown())
                    this.setSelectionPos(this.typedText.length());
                else 
                    this.setCursorPositionEnd();
                return true;
            case Keyboard.KEY_DELETE:
                if (GuiScreen.isCtrlKeyDown()) {
                    if (this.isEnabled())
                        this.deleteWords(1);
                } else if (this.isEnabled())
                    this.deleteFromCursor(1);
                return true;
            default:
                if (this.isAllowedCharacter(keyChar)) {
                    if (this.isEnabled())
                        this.writeText(Character.toString(keyChar));
                    return true;
                } else
                    return false;
            }
        }
    }

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
                if (this.isDragged() && this.isHovered()) {
                    int l = (int) ((1.0F + this.getScale()) * (float) (mouseX - this.getX())) - 2;//why?      
                    String s = this.mc.fontRenderer.trimStringToWidth(this.typedText.substring(this.lineScrollOffset), this.getWidth() - 6);
                    this.setCursorPosition(this.mc.fontRenderer.trimStringToWidth(s, l).length() + this.lineScrollOffset);
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
            if (this.isDynamicBackgroundEnabled()) {
                if (this.isEnabled())
                    drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());
                else
                    drawRect(0, 0, this.getWidth(), this.getHeight(), this.getDisabledBackgroundColor());
                if (this.isDragged())
                    drawRect(0, 0, this.getWidth(), this.getHeight(), this.getHoveredBackgroundColor());
            }
            int 
            i = this.isEnabled() ? this.getEnabledTextColor() : this.getDisabledTextColor(),
                    j = this.cursorPosition - this.lineScrollOffset,
                    k = this.selectionEnd - this.lineScrollOffset;           

            GlStateManager.pushMatrix();           
            GlStateManager.translate(0.0F, this.getHeight() - this.textHeight(this.getTextScale()) - 1.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int o = (int) ((float) this.getWidth() * (1.0F + this.getTextScale()));

            String s = this.mc.fontRenderer.trimStringToWidth(this.typedText.substring(this.lineScrollOffset), o); 
            boolean 
            flag = j >= 0 && j <= s.length(),
            flag1 = this.isDragged() && this.cursorCounter / 6 % 2 == 0 && flag;
            int l = this.isDynamicBackgroundEnabled() ? 2 : 0, i1 = this.isDynamicBackgroundEnabled() ? (this.getHeight() - this.textHeight(this.getTextScale())) / 2 : 0, j1 = l;
            if (k > s.length())
                k = s.length();
            if (s.length() > 0) {
                String s1 = flag ? s.substring(0, j) : s;
                j1 = this.mc.fontRenderer.drawString(s1, l, i1, i, this.isTextShadowEnabled());
            }
            if (this.hasDisplayText() && !this.isDragged() && this.getTypedText().isEmpty())
                this.mc.fontRenderer.drawString(this.getDisplayText(), l, i1, this.getEnabledTextColor(), this.isTextShadowEnabled());
            boolean flag2 = this.cursorPosition < this.typedText.length() || this.typedText.length() >= this.maxStringLength;
            int k1 = j1;
            if (!flag)
                k1 = j > 0 ? l + this.getWidth() : l;
                else if (flag2) {
                    k1 = j1 - 1;
                    j1++;
                }
            if (s.length() > 0 && flag && j < s.length())
                this.mc.fontRenderer.drawString(s.substring(j), j1, i1, i, this.isTextShadowEnabled());

            //cursor
            if (flag1) {
                if (flag2)
                    drawRect(k1, i1 - 2, k1 + 1, i1 + this.mc.fontRenderer.FONT_HEIGHT - 1, - 3092272);
                else
                    drawRect(k1, i1 - 2, k1 + 1, i1 + this.mc.fontRenderer.FONT_HEIGHT - 1, - 3092272);
            }

            if (k != j) {
                int l1 = l + this.mc.fontRenderer.getStringWidth(s.substring(0, k));
                this.drawSelectionBox(k1, i1 - 2, l1 - 2, i1 + this.mc.fontRenderer.FONT_HEIGHT - 1);
            }
            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }

    private void drawSelectionBox(int xStart, int yStart, int xEnd, int yEnd) {
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

        int j = (int) ((float) this.getWidth() * (1.0F + this.getTextScale()));//TODO

        if (xEnd > this.getX() + j)
            xEnd = this.getX() + j;
        if (xStart > this.getX() + j)
            xStart = this.getX() + j;
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
            int j = (int) ((float) this.getWidth() * (1.0F + this.getTextScale()));//TODO
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
}
