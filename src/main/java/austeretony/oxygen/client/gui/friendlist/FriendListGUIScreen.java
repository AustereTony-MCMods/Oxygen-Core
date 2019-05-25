package austeretony.oxygen.client.gui.friendlist;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

public class FriendListGUIScreen extends AbstractGUIScreen {

    public static final ResourceLocation FRIEND_LIST_BACKGROUND_TEXTURE = new ResourceLocation(OxygenMain.MODID, "textures/gui/friends/friend_list_background.png");

    private FriendListGUISection friendListSection;

    private IgnoreListGUISection ignoreListSection;

    private boolean initialized;

    @Override
    protected GUIWorkspace initWorkspace() {
        return new GUIWorkspace(this, 217, 202).setAlignment(EnumGUIAlignment.RIGHT, - 10, 0);
    }

    @Override
    protected void initSections() {
        this.friendListSection = new FriendListGUISection(this);
        this.getWorkspace().initSection(this.friendListSection);     
        this.ignoreListSection = new IgnoreListGUISection(this);  
        this.getWorkspace().initSection(this.ignoreListSection);     
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.friendListSection;
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
                && OxygenGUIHelper.isNeedSync(OxygenMain.FRIEND_LIST_SCREEN_ID)
                && OxygenGUIHelper.isScreenInitialized(OxygenMain.FRIEND_LIST_SCREEN_ID)
                && OxygenGUIHelper.isDataRecieved(OxygenMain.FRIEND_LIST_SCREEN_ID)) {
            this.initialized = true;
            OxygenGUIHelper.resetNeedSync(OxygenMain.FRIEND_LIST_SCREEN_ID);
            this.friendListSection.sortPlayers(0);
            this.ignoreListSection.sortPlayers(0);
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        OxygenGUIHelper.resetNeedSync(OxygenMain.FRIEND_LIST_SCREEN_ID);
        OxygenGUIHelper.resetScreenInitialized(OxygenMain.FRIEND_LIST_SCREEN_ID);
        OxygenGUIHelper.resetDataRecieved(OxygenMain.FRIEND_LIST_SCREEN_ID);
        OxygenHelperClient.savePlayerDataDelegated(OxygenHelperClient.getPlayerData());
    }

    public FriendListGUISection getFriendListSection() {
        return this.friendListSection;
    }

    public IgnoreListGUISection getIgnoreListSection() {
        return this.ignoreListSection;
    }
}