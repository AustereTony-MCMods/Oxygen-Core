package austeretony.alternateui.screen.text;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatAllowedCharacters;

/**
 * @author AustereTony
 */
public class GUITextBoxField extends GUISimpleElement<GUITextBoxField> {

    protected String typedText;

    protected final int maxStringLength;

    protected int cursorCounter, lineOffset;

    protected boolean newLine;

    private final List<String> lines = new ArrayList<String>(5);

    public GUITextBoxField(int xPosition, int yPosition, int width, int height, int maxStringLength) {                 
        this.setPosition(xPosition, yPosition);
        this.setSize(width, height);
        this.maxStringLength = maxStringLength > 1500 ? 1500 : maxStringLength;        
        this.typedText = "";
        this.lineOffset = 2;
        this.enableFull();
    }

    @Override
    public void updateCursorCounter() {         
        this.cursorCounter++;
    }

    public String getTypedText() {      
        return this.typedText;
    }

    public GUITextBoxField setText(String text) {      
        if (text.length() > this.maxStringLength)               
            this.typedText = text.substring(0, this.maxStringLength);    
        else
            this.typedText = text;

        this.lines.clear(); 
        int width = this.getWidth() - 3;
        StringBuilder builder = new StringBuilder();    
        int 
        index = 0, 
        wordStartIndex = 0;
        boolean
        rechedLimit = false,
        wordProcessing = false;
        char prevSymbol = '0';
        String line;
        for (char symbol : this.typedText.toCharArray()) {
            if ((this.textHeight(this.getTextScale()) + this.lineOffset) * this.lines.size() >= this.getHeight())
                break;
            this.newLine = false;
            if (symbol != ' ') {
                wordProcessing = true;
                if (prevSymbol == ' ')
                    wordStartIndex = index;
            }
            if (symbol == '\n') {
                this.lines.add(builder.toString());
                builder.delete(0, builder.length());
                index = 0;
                this.newLine = true;
                continue;
            }
            if (this.textWidth(builder.toString() + String.valueOf(symbol), this.getTextScale()) <= width)
                builder.append(symbol);
            else {
                if (symbol == '.' 
                        || symbol == ',' 
                        || symbol == '!'
                        || symbol == '?')
                    builder.append(symbol);
                if (wordProcessing) {
                    this.lines.add(builder.toString().substring(0, wordStartIndex));
                    builder.delete(0, wordStartIndex);
                } else {
                    this.lines.add(builder.toString());
                    builder.delete(0, builder.length());
                }
                if (symbol != ' ')
                    builder.append(symbol);
                index = builder.length() - 1;
            }
            wordProcessing = false;
            prevSymbol = symbol;
            index++;
        }
        if (builder.length() != 0)
            this.lines.add(builder.toString());

        return this;
    }

    public void reset() {
        this.setText("");
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
    public boolean keyTyped(char keyChar, int keyCode) {
        if (!this.isDragged())
            return false;
        switch (keyCode) {
        case 14:
            String s = this.getTypedText();
            if (!s.isEmpty())
                this.setText(s.substring(0, s.length() - 1));
            return true;
        case 28:
        case 156:
            this.append("\n");
            return true;
        default:
            if (ChatAllowedCharacters.isAllowedCharacter(keyChar))
                this.append(Character.toString(keyChar));
            return true;
        }
    }

    private void append(String string) {
        String s = this.getTypedText();
        String s1 = s + string;
        if (s1.length() < 256)
            this.setText(s1);
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isDynamicBackgroundEnabled()) {
                if (this.isEnabled())
                    drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledBackgroundColor());
                else
                    drawRect(0, 0, this.getWidth(), this.getHeight(), this.getDisabledBackgroundColor());
                if (this.isDragged())
                    drawRect(0, 0, this.getWidth(), this.getHeight(), this.getHoveredBackgroundColor());
            }

            GlStateManager.pushMatrix();           
            GlStateManager.translate(1.0F, 2.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isDragged() 
                    && this.lines.isEmpty() 
                    && (this.cursorCounter / 6 % 2 == 0))
                this.mc.fontRenderer.drawString("_", 0.0F, 0.0F, this.getEnabledTextColor(), this.isTextShadowEnabled());

            int index = 0;//fix for equal lines render issue (if List#indexOf() used)
            for (String line : this.lines) {   
                if (this.isDragged() 
                        && !this.newLine
                        && index == this.lines.size() - 1
                        && (this.cursorCounter / 6 % 2 == 0))
                    this.mc.fontRenderer.drawString(line + "_", 0.0F, (this.mc.fontRenderer.FONT_HEIGHT + this.lineOffset) * index, this.getEnabledTextColor(), this.isTextShadowEnabled());
                else
                    this.mc.fontRenderer.drawString(line, 0.0F, (this.mc.fontRenderer.FONT_HEIGHT + this.lineOffset) * index, this.getEnabledTextColor(), this.isTextShadowEnabled());
                index++;
            }

            if (this.isDragged() 
                    && this.newLine
                    && (this.cursorCounter / 6 % 2 == 0))
                this.mc.fontRenderer.drawString("_", 0.0F, (this.mc.fontRenderer.FONT_HEIGHT + this.lineOffset) * this.lines.size(), this.getEnabledTextColor(), this.isTextShadowEnabled());

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }

    public GUITextBoxField setLineOffset(int offset) {
        this.lineOffset = offset;
        return this;
    }
}