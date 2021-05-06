package austeretony.oxygen_core.client.gui.base.special;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import austeretony.oxygen_core.client.gui.base.*;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.core.Section;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.list.DropDownList;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;

import java.util.List;

public class SectionSwitcher extends Widget<SectionSwitcher> {

    @Nonnull
    protected final List<Section> sections;
    @Nonnull
    protected Text text;
    @Nonnull
    protected Texture texture;
    
    @Nullable
    protected SectionSwitchListener switchListener;

    protected boolean opened;

    public SectionSwitcher(@Nonnull Section section) {
        sections = section.getScreen().getWorkspace().getSections();
        text = Texts.panel(section.getName());

        float maxWidth = 0;
        for (Section s : sections) {
            float nameWidth = GUIUtils.getTextWidth(s.getName(), text.getScale());
            if (nameWidth > maxWidth) {
                maxWidth = nameWidth;
            }
        }

        setSize((int) maxWidth + 8, LIST_HEIGHT);
        setPosition(section.getWidth() - 4 - getWidth(), 4);
        texture = Texture.builder()
                .texture(Textures.SORT_DOWN_ICONS)
                .size(3, 3)
                .imageSize(9, 3)
                .build();

        setEnabled(true);
        setVisible(true);
    }
    
    @Override
    public void init() {
        for (int i = 0; i < sections.size(); i++) {
            ListEntry<Section> entry;
            addWidget(entry = new ListEntry<>(sections.get(i).getName(), sections.get(i)));
            entry.setPosition(0, DropDownList.Y_OFFSET + getHeight() * (i + 1));
            entry.setSize(getWidth(), LIST_HEIGHT);
            entry.setEnabled(sections.get(i).isEnabled());
        }
    }
    
    public SectionSwitcher setSectionSwitchListener(SectionSwitchListener listener) {
        switchListener = listener;
        return this;
    }
    
    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());
        
        GUIUtils.pushMatrix();
        GUIUtils.translate(2, getHeight() - GUIUtils.getTextHeight(text.getScale()));
        GUIUtils.scale(text.getScale(), text.getScale());

        int color = text.getColorEnabled();
        int iconU = texture.getU();
        if (!isEnabled()) {
            color = text.getColorDisabled();
            iconU += texture.getWidth();
        } else if (isMouseOver()) {
            color = text.getColorMouseOver();
            iconU += texture.getWidth() * 2;
        }

        GUIUtils.drawString(text.getText(), 0, 0, color, text.isShadowEnabled());
        GUIUtils.popMatrix();

        GUIUtils.colorDef();
        GUIUtils.drawTexturedRect(GUIUtils.getTextWidth(text.getText(), text.getScale()) + 4, (getHeight() - texture.getHeight()) / 2F + 1F,
                texture.getWidth(), texture.getHeight(), texture.getTexture(), iconU, texture.getV(),
                texture.getImageWidth(), texture.getImageHeight());

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        if (opened) {
            int size = getWidgets().size();
            GUIUtils.drawRect(0, getHeight() + DropDownList.Y_OFFSET,
                    getWidth(), DropDownList.Y_OFFSET + getHeight() * (size + 1), CoreSettings.COLOR_BACKGROUND_BASE.asInt());
            GUIUtils.drawFrame(0, getHeight() + DropDownList.Y_OFFSET, getWidth(), getHeight() * size);
        }

        if (opened) {
            for (Widget widget : getWidgets()) {
                widget.draw(mouseX, mouseY, partialTicks);
            }
        }

        GUIUtils.popMatrix();
    }
    
    @Override
    public boolean mouseOver(int mouseX, int mouseY) {  
        if (opened) {
            for (Widget widget : getWidgets()) {
                widget.mouseOver(mouseX - getX(), mouseY - getY());
            }
        }
        if (getLayer() == Layer.MIDDLE) {
            mouseX -= getScreen().getWorkspace().getX();
            mouseY -= getScreen().getWorkspace().getY();
        }
        return mouseOver = mouseX >= getX() && mouseY >= getY() && mouseX < getX() + getWidth() && mouseY < getY() + getHeight();
    }
    
    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (opened) {
            for (Widget widget : getWidgets()) {
                ListEntry<Section> entry = (ListEntry) widget;
                if (entry.mouseClicked(mouseX, mouseY, mouseButton)) {
                    MinecraftClient.playUISound(SoundEffects.uiContextClose);
                    opened = false;

                    Section section = getScreen().getWorkspace().getCurrentSection();
                    if (section != entry.getEntry()) {
                        entry.getEntry().open();
                    }
                    if (switchListener != null) {
                        switchListener.changed(section, entry.getEntry());
                    }
                    return true;
                }
            }
        }
        opened = false;
        if (isMouseOver() && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiDropDownListOpen);
            opened = true;
        }
        return false;
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    public SectionSwitcher setText(@Nonnull Text text) {
        this.text = text;
        return this;
    }

    @Nonnull
    public Texture getTexture() {
        return texture;
    }

    public SectionSwitcher setTexture(@Nonnull Texture texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public String toString() {
        return "SectionSwitcher[" +
                "x= " + getX() + ", " +
                "y= " + getY() +
                "]";
    }
    
    @FunctionalInterface
    public interface SectionSwitchListener {
        
        void changed(@Nonnull Section from, @Nonnull Section to);
    }
}
