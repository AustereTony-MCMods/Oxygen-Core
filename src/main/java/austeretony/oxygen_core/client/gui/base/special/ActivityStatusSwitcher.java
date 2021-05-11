package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.gui.base.*;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.common.ListEntry;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.list.DropDownList;
import austeretony.oxygen_core.client.gui.base.listener.ListEntryClickListener;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.server.SPChangeActivityStatus;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import austeretony.oxygen_core.common.sound.SoundEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ActivityStatusSwitcher extends Widget<ActivityStatusSwitcher> {

    @Nonnull
    protected Fill fill;
    @Nonnull
    protected Text text;
    @Nonnull
    protected Texture activityTexture, checkTexture;
    protected boolean changeStatusOnClick;

    @Nullable
    protected ListEntryClickListener<ActivityStatus> clickListener;

    protected ActivityStatus status = ActivityStatus.OFFLINE;
    protected boolean opened;

    public ActivityStatusSwitcher(int x, int y, boolean changeStatusOnClick) {
        setPosition(x, y);
        fill = Fills.element();
        text = Texts.panel(MinecraftClient.getClientPlayer().getName());
        int nicknameWidth = (int) Math.ceil(text.getWidth()) + 9 + 9;
        setSize(Math.max(nicknameWidth, 65), LIST_HEIGHT);
        activityTexture = Texture.builder()
                .texture(Textures.STATUS_ICONS)
                .size(3, 3)
                .imageSize(12, 3)
                .build();
        checkTexture = Texture.builder()
                .texture(Textures.SORT_DOWN_ICONS)
                .size(3, 3)
                .imageSize(9, 3)
                .build();
        this.changeStatusOnClick = changeStatusOnClick;

        setEnabled(true);
        setVisible(true);
    }

    public ActivityStatusSwitcher setMouseClickListener(ListEntryClickListener<ActivityStatus> listener) {
        clickListener = listener;
        return this;
    }

    public void updateStatus() {
        updateStatus(OxygenClient.getClientPlayerSharedData());
    }

    public void updateStatus(PlayerSharedData sharedData) {
        status = OxygenClient.getPlayerActivityStatus(sharedData);
    }

    @Override
    public void init() {
        ActivityStatus[] statuses = ActivityStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            Entry entry;
            addWidget(entry = new Entry(statuses[i]));
            entry.setPosition(0, DropDownList.Y_OFFSET + getHeight() * (i + 1));
            entry.setSize(getWidth(), LIST_HEIGHT);
        }
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int statusIconU = activityTexture.getU() + status.ordinal() * activityTexture.getWidth();
        GUIUtils.colorDef();
        GUIUtils.drawTexturedRect(2F, (getHeight() - activityTexture.getHeight()) / 2F,
                activityTexture.getWidth(), activityTexture.getHeight(), activityTexture.getTexture(),
                statusIconU, activityTexture.getV(), activityTexture.getImageWidth(), activityTexture.getImageHeight());

        GUIUtils.pushMatrix();
        GUIUtils.translate(7, (getHeight() - GUIUtils.getTextHeight(text.getScale())) / 2 + .5F);
        GUIUtils.scale(text.getScale(), text.getScale());

        int color = text.getColorEnabled();
        int iconU = activityTexture.getU();
        if (!isEnabled()) {
            color = text.getColorDisabled();
            iconU += activityTexture.getWidth();
        } else if (isMouseOver() && !opened) {
            color = text.getColorMouseOver();
            iconU += activityTexture.getWidth() * 2;
        }

        GUIUtils.drawString(text.getText(), 0, 0, color, text.isShadowEnabled());
        GUIUtils.popMatrix();

        GUIUtils.colorDef();
        GUIUtils.drawTexturedRect(getWidth() - 5F, (getHeight() - checkTexture.getHeight()) / 2F,
                checkTexture.getWidth(), checkTexture.getHeight(), checkTexture.getTexture(),
                iconU, checkTexture.getV(), checkTexture.getImageWidth(), checkTexture.getImageHeight());

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
               Entry entry = (Entry) widget;
                if (entry.mouseClicked(mouseX, mouseY, mouseButton)) {
                    MinecraftClient.playUISound(SoundEffects.uiContextClose);
                    opened = false;
                    status = entry.getEntry();
                    if (changeStatusOnClick) {
                        OxygenMain.network().sendToServer(new SPChangeActivityStatus(entry.getEntry()));
                        PlayerSharedData sharedData = OxygenClient.getClientPlayerSharedData();
                        if (sharedData != null) {
                            sharedData.setValue(OxygenMain.SHARED_ACTIVITY_STATUS, entry.getEntry().ordinal());
                        }
                    }
                    if (clickListener != null) {
                        clickListener.click(null, entry, mouseX, mouseY, mouseButton);
                    }
                    return true;
                }
            }
        }
        opened = false;
        if (isMouseOver() && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiDropDownListOpen);
            opened = true;
            return true;
        }
        return false;
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public void setFill(@Nonnull Fill fill) {
        this.fill = fill;
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    public void setText(@Nonnull Text text) {
        this.text = text;
    }

    @Nonnull
    public Texture getActivityTexture() {
        return activityTexture;
    }

    public void setActivityTexture(@Nonnull Texture activityTexture) {
        this.activityTexture = activityTexture;
    }

    @Override
    public String toString() {
        return "ActivityStatusSwitcher[" +
                "x= " + getX() + ", " +
                "y= " + getY() + "" +
                "]";
    }

    private static class Entry extends ListEntry<ActivityStatus> {

        @Nonnull
        protected Texture activityTexture;

        public Entry(@Nonnull ActivityStatus entry) {
            super(entry.getDisplayName(), entry);
            activityTexture = Texture.builder()
                    .texture(Textures.STATUS_ICONS)
                    .size(3, 3)
                    .imageSize(12, 3)
                    .build();
        }

        @Override
        public void draw(int mouseX, int mouseY, float partialTicks) {
            if (!isVisible()) return;
            GUIUtils.pushMatrix();
            GUIUtils.translate(getX(), getY());

            GUIUtils.drawRect(0, 0, getWidth(), getHeight(), getColorFromState(getFill()));

            int statusIconU = activityTexture.getU() + entry.ordinal() * activityTexture.getWidth();
            GUIUtils.colorDef();
            GUIUtils.drawTexturedRect(2F, (getHeight() - activityTexture.getHeight()) / 2F,
                    activityTexture.getWidth(), activityTexture.getHeight(), activityTexture.getTexture(),
                    statusIconU, activityTexture.getV(), activityTexture.getImageWidth(), activityTexture.getImageHeight());

            GUIUtils.pushMatrix();
            GUIUtils.translate(7F, (getHeight() - GUIUtils.getTextHeight(text.getScale())) / 2 + .5F);
            GUIUtils.scale(text.getScale(), text.getScale());

            int color = text.getColorEnabled();
            if (!isEnabled())
                color = text.getColorDisabled();
            else if (isMouseOver() || selected)
                color = text.getColorMouseOver();

            GUIUtils.drawString(text.getText(), 0, 0, color, text.isShadowEnabled());
            GUIUtils.popMatrix();

            GUIUtils.popMatrix();
        }
    }
}
