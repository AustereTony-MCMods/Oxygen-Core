package austeretony.oxygen_core.client.gui.base.background;

import austeretony.oxygen_core.client.gui.base.Alignment;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.core.Callback;
import austeretony.oxygen_core.client.gui.base.core.Section;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.settings.CoreSettings;
import net.minecraft.client.gui.ScaledResolution;

import javax.annotation.Nonnull;

public abstract class Background extends Widget<Background> {

    public static final int BUTTONS_PANEL_HEIGHT = 11;
    @Nonnull
    protected ScaledResolution sr;

    protected boolean isCallback;

    public Background(int width, int height) {
        setPosition(0, 0);
        setSize(width, height);
        sr = GUIUtils.getScaledResolution();

        setVisible(true);
    }

    public Background(@Nonnull Section section) {
        this(section.getWidth(), section.getHeight());
    }

    public Background(@Nonnull Callback callback) {
        this(callback.getWidth(), callback.getHeight());
        this.isCallback = true;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY, float partialTicks) {
        int color = isCallback ? CoreSettings.COLOR_CALLBACK_BACKGROUND_FILL.asInt()
                : CoreSettings.COLOR_SCREEN_BACKGROUND_FILL.asInt();
        int x = -getScreen().getWorkspace().getX();
        int y = -getScreen().getWorkspace().getY();
        GUIUtils.drawRect(x, y, sr.getScaledWidth(), sr.getScaledHeight(), color);
        drawBackgroundAdditions(mouseX, mouseY, partialTicks);
    }

    public abstract void drawBackgroundAdditions(int mouseX, int mouseY, float partialTicks);

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int color = CoreSettings.COLOR_BACKGROUND_BASE.asInt();
        GUIUtils.drawRect(0.0, 0.0, getWidth(), getHeight(), color);
        if (!isCallback && CoreSettings.ENABLE_VERTICAL_GRADIENT.asBoolean()) {
            GUIUtils.drawGradientRect(0.0, -getScreen().getWorkspace().getY(), getWidth(), 0.0,
                    0x00000000, color, Alignment.BOTTOM);
            GUIUtils.drawGradientRect(0.0, getHeight(), getWidth(), getHeight() + getScreen().getWorkspace().getY(),
                    0x00000000, color, Alignment.TOP);
        }
        if (isCallback) {
            GUIUtils.drawFrame(0, 0, getWidth(), getHeight());
        }
        drawAdditions(mouseX, mouseY, partialTicks);

        GUIUtils.popMatrix();
    }

    public abstract void drawAdditions(int mouseX, int mouseY, float partialTicks);

    public static class Default extends Background {

        public Default(int width, int height) {
            super(width, height);
        }

        public Default(@Nonnull Section section) {
            super(section);
        }

        public Default(@Nonnull Callback callback) {
            super(callback);
        }

        @Override
        public void drawBackgroundAdditions(int mouseX, int mouseY, float partialTicks) {}

        @Override
        public void drawAdditions(int mouseX, int mouseY, float partialTicks) {}
    }

    public static class UnderlinedTitle extends Background {

        public UnderlinedTitle(int width, int height) {
            super(width, height);
        }

        public UnderlinedTitle(@Nonnull Section section) {
            super(section);
        }

        public UnderlinedTitle(@Nonnull Callback callback) {
            super(callback);
        }

        @Override
        public void drawBackgroundAdditions(int mouseX, int mouseY, float partialTicks) {}

        @Override
        public void drawAdditions(int mouseX, int mouseY, float partialTicks) {
            GUIUtils.drawRect(4.0, 13.0, getWidth() - 4.0, 13.0 + FRAME_WIDTH,
                    CoreSettings.COLOR_BACKGROUND_ADDITIONAL.asInt());
        }
    }

    public static class UnderlinedTitleBottom extends Background {

        public UnderlinedTitleBottom(int width, int height) {
            super(width, height);
        }

        public UnderlinedTitleBottom(@Nonnull Section section) {
            super(section);
        }

        public UnderlinedTitleBottom(@Nonnull Callback callback) {
            super(callback);
        }

        @Override
        public void drawBackgroundAdditions(int mouseX, int mouseY, float partialTicks) {}

        @Override
        public void drawAdditions(int mouseX, int mouseY, float partialTicks) {
            GUIUtils.drawRect(4.0, 13.0, getWidth() - 4.0, 13.0 + FRAME_WIDTH,
                    CoreSettings.COLOR_BACKGROUND_ADDITIONAL.asInt());
            GUIUtils.drawRect(4.0, getHeight() - 12.0 - FRAME_WIDTH, getWidth() - 4.0, getHeight() - 12.0,
                    CoreSettings.COLOR_BACKGROUND_ADDITIONAL.asInt());
        }
    }

    public static class UnderlinedTitleButtons extends Background {

        public UnderlinedTitleButtons(int width, int height) {
            super(width, height);
        }

        public UnderlinedTitleButtons(@Nonnull Section section) {
            super(section);
        }

        public UnderlinedTitleButtons(@Nonnull Callback callback) {
            super(callback);
        }

        @Override
        public void drawBackgroundAdditions(int mouseX, int mouseY, float partialTicks) {
            int x = -getScreen().getWorkspace().getX();
            int y = -getScreen().getWorkspace().getY();
            GUIUtils.drawRect(x, y + sr.getScaledHeight() - BUTTONS_PANEL_HEIGHT,
                    sr.getScaledWidth(), sr.getScaledHeight(), CoreSettings.COLOR_BACKGROUND_BASE.asInt());
        }

        @Override
        public void drawAdditions(int mouseX, int mouseY, float partialTicks) {
            GUIUtils.drawRect(4.0, 13.0, getWidth() - 4.0, 13.0 + FRAME_WIDTH,
                    CoreSettings.COLOR_BACKGROUND_ADDITIONAL.asInt());
        }
    }

    public static class UnderlinedTitleBottomButtons extends Background {

        public UnderlinedTitleBottomButtons(int width, int height) {
            super(width, height);
        }

        public UnderlinedTitleBottomButtons(@Nonnull Section section) {
            super(section);
        }

        public UnderlinedTitleBottomButtons(@Nonnull Callback callback) {
            super(callback);
        }

        @Override
        public void drawBackgroundAdditions(int mouseX, int mouseY, float partialTicks) {
            int x = -getScreen().getWorkspace().getX();
            int y = -getScreen().getWorkspace().getY();
            GUIUtils.drawRect(x, y + sr.getScaledHeight() - BUTTONS_PANEL_HEIGHT,
                    sr.getScaledWidth(), sr.getScaledHeight(), CoreSettings.COLOR_BACKGROUND_BASE.asInt());
        }

        @Override
        public void drawAdditions(int mouseX, int mouseY, float partialTicks) {
            GUIUtils.drawRect(4.0, 13.0, getWidth() - 4.0, 13.0 + FRAME_WIDTH,
                    CoreSettings.COLOR_BACKGROUND_ADDITIONAL.asInt());
            GUIUtils.drawRect(4.0, getHeight() - 12.0 - FRAME_WIDTH, getWidth() - 4.0, getHeight() - 12.0,
                    CoreSettings.COLOR_BACKGROUND_ADDITIONAL.asInt());
        }
    }
}
