package austeretony.oxygen_core.client.gui.base.core;

import austeretony.oxygen_core.client.gui.base.event.OxygenScreenInitEvent;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import javax.annotation.Nullable;
import java.io.IOException;

public abstract class OxygenScreen extends GuiScreen {

    @Nullable
    protected Workspace workspace;
    protected boolean wasKeyboardRepeatEventsEnabled;

    @Override
    public void initGui() {
        wasKeyboardRepeatEventsEnabled = Keyboard.areRepeatEventsEnabled();
        Keyboard.enableRepeatEvents(true);

        workspace = createWorkspace();
        MinecraftCommon.postEvent(new OxygenScreenInitEvent.Pre(this));
        init();
        workspace.setCurrentSection(getDefaultSection());
        MinecraftCommon.postEvent(new OxygenScreenInitEvent.Post(this));
    }

    protected void init() {
        addSections();
        for (Section section : getWorkspace().getSections()) {
            section.init();
        }
    }

    public abstract int getScreenId();

    public abstract Workspace createWorkspace();

    public abstract void addSections();

    public abstract Section getDefaultSection();

    @Nullable
    public Workspace getWorkspace() {
        return this.workspace;
    }

    @Override
    public void drawDefaultBackground() {}

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        if (getWorkspace() != null) {
            getWorkspace().drawScreen(mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        if (getWorkspace() != null) {
            getWorkspace().handleMouseInput();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (getWorkspace() != null) {
            getWorkspace().mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if (getWorkspace() != null) {
            getWorkspace().mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        if (getWorkspace() != null) {
            getWorkspace().mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (getWorkspace() != null) {
            getWorkspace().keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {
        if (getWorkspace() != null) {
            getWorkspace().updateScreen();
        }
    }

    @Override
    public void onResize(Minecraft mc, int width, int height) {
        if (getWorkspace() != null) {
            getWorkspace().clearSections();
        }
        super.onResize(mc, width, height);
        if (getWorkspace() != null) {
            getWorkspace().resized(mc, width, height);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        if (getWorkspace() != null) {
            getWorkspace().closed();
        }
        Keyboard.enableRepeatEvents(wasKeyboardRepeatEventsEnabled);
    }

    public void drawToolTipVanilla(ItemStack itemStack, int x, int y) {
        renderToolTip(itemStack, x, y);
    }

    public void close() {
        mc.displayGuiScreen(null);
        if (mc.currentScreen == null) {
            mc.setIngameFocus();
        }
    }

    protected static String localize(String key, Object... args) {
        return MinecraftClient.localize(key, args);
    }
}

