package austeretony.oxygen_core.client.gui.elements;

import javax.annotation.Nullable;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;

public class OxygenTextFormattingColorPicker extends GUISimpleElement<OxygenTextFormattingColorPicker> {

    private static final int[] HEX_COLORS = new int[16];

    static {
        int i, j, k, l, m;
        for (i = 0; i < 16; i++) {
            j = (i >> 3 & 1) * 85;
            k = (i >> 2 & 1) * 170 + j;
            l = (i >> 1 & 1) * 170 + j;
            m = (i >> 0 & 1) * 170 + j;
            if (i == 6)
                k += 85;
            HEX_COLORS[i] = 0xFF << 24 | (k & 0xFF) << 16 | (l & 0xFF) << 8 | m & 0xFF;
        }
    }

    @Nullable
    private ColorPickListener colorPickListener;

    private int pickedIndex = - 1;

    public OxygenTextFormattingColorPicker(int xPosition, int yPosition) {
        this.setPosition(xPosition, yPosition);
        this.setSize(16 * 7, 7);
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.getSoundEvent());
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt());
        this.enableFull();
    }

    public void setColorPickListener(ColorPickListener listener) {
        this.colorPickListener = listener;
    }

    @Override
    public void draw(int mouseX, int mouseY) {          
        if (this.isVisible()) { 
            GlStateManager.pushMatrix();    
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            for (int i = 0; i < 16; i++)
                drawRect(i * 7, 0, i * 7 + 7, 7, HEX_COLORS[i]);

            if (this.pickedIndex != - 1) {
                int frameX = this.pickedIndex * 7;

                drawRect(frameX - 1, - 1, frameX, this.getHeight() + 1, this.getStaticBackgroundColor());
                drawRect(frameX + 7, - 1, frameX + 7 + 1, this.getHeight() + 1, this.getStaticBackgroundColor());
                drawRect(frameX, - 1, frameX + 7, 0, this.getStaticBackgroundColor());
                drawRect(frameX, this.getHeight(), frameX + 7, this.getHeight() + 1, this.getStaticBackgroundColor());
            }

            if (this.isHovered()) {
                int hoveredIndex = (mouseX - this.getX()) / 7;

                hoveredIndex *= 7;

                drawRect(hoveredIndex - 1, - 1, hoveredIndex, this.getHeight() + 1, this.getStaticBackgroundColor());
                drawRect(hoveredIndex + 7, - 1, hoveredIndex + 7 + 1, this.getHeight() + 1, this.getStaticBackgroundColor());
                drawRect(hoveredIndex, - 1, hoveredIndex + 7, 0, this.getStaticBackgroundColor());
                drawRect(hoveredIndex, this.getHeight(), hoveredIndex + 7, this.getHeight() + 1, this.getStaticBackgroundColor());
            }

            GlStateManager.popMatrix();          
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);         
        if (flag) {
            this.pickedIndex = (mouseX - this.getX()) / 7;
            if (this.colorPickListener != null)
                this.colorPickListener.pick(this.pickedIndex);      
        }
        return flag;
    }

    public int getPickedColorIndex() {
        return this.pickedIndex;
    }

    public void setPickedColorIndex(int index) {
        this.pickedIndex = index;
    }

    @FunctionalInterface
    public static interface ColorPickListener {

        void pick(int colorIndex);
    }
}
