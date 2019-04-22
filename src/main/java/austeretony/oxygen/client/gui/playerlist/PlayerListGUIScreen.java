package austeretony.oxygen.client.gui.playerlist;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

public class PlayerListGUIScreen extends AbstractGUIScreen {

    public static final ResourceLocation 
    BACKGROUND_TEXTURE = new ResourceLocation(OxygenMain.MODID, "textures/gui/players/background.png"),
    PING_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/players/ping_icons.png");

    protected PlayerListGUISection mainSection;

    private boolean initialized;

    @Override
    protected GUIWorkspace initWorkspace() {
        return new GUIWorkspace(this, 210, 199);
    }

    @Override
    protected void initSections() {
        this.mainSection = new PlayerListGUISection(this);
        this.getWorkspace().initSection(this.mainSection);        
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.mainSection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }

    @Override
    public void updateScreen() {    
        super.updateScreen();
        if (!this.initialized//reduce map calls
                && OxygenGUIHelper.isNeedSync(OxygenMain.PLAYER_LIST_SCREEN_ID)
                && OxygenGUIHelper.isScreenInitialized(OxygenMain.PLAYER_LIST_SCREEN_ID)
                && OxygenGUIHelper.isDataRecieved(OxygenMain.PLAYER_LIST_SCREEN_ID)) {
            this.initialized = true;
            OxygenGUIHelper.resetNeedSync(OxygenMain.PLAYER_LIST_SCREEN_ID);
            this.mainSection.sortPlayers(0);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        OxygenGUIHelper.resetNeedSync(OxygenMain.PLAYER_LIST_SCREEN_ID);
        OxygenGUIHelper.resetScreenInitialized(OxygenMain.PLAYER_LIST_SCREEN_ID);
        OxygenGUIHelper.resetDataRecieved(OxygenMain.PLAYER_LIST_SCREEN_ID);
    }
}
