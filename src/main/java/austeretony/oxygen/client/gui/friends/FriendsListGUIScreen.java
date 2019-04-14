package austeretony.oxygen.client.gui.friends;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.screen.core.GUIWorkspace;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

public class FriendsListGUIScreen extends AbstractGUIScreen {

    public static final ResourceLocation 
    FRIENDS_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/friends/friends_icons.png"),
    IGNORED_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/friends/ignored_icons.png"),
    DOWNLOAD_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/download_icons.png"),
    STATUS_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/status_icons.png"),
    SEARCH_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/search_icons.png"),
    REFRESH_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/refresh_icons.png"),
    SORT_UP_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/sort_up_icons.png"),
    SORT_DOWN_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/sort_down_icons.png"),
    NOTE_ICONS = new ResourceLocation(OxygenMain.MODID, "textures/gui/note_icons.png");

    private FriendsGUISection friendsSection;

    private IgnoredGUISection ignoredSection;

    @Override
    protected GUIWorkspace initWorkspace() {
        return new GUIWorkspace(this, 217, 205).setAlignment(EnumGUIAlignment.RIGHT, - 10, 0);
    }

    @Override
    protected void initSections() {
        this.friendsSection = new FriendsGUISection(this);
        this.getWorkspace().initSection(this.friendsSection);     
        this.ignoredSection = new IgnoredGUISection(this);  
        this.getWorkspace().initSection(this.ignoredSection);     
    }

    @Override
    protected AbstractGUISection getDefaultSection() {
        return this.friendsSection;
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element) {}

    @Override
    protected boolean doesGUIPauseGame() {
        return false;
    }

    public FriendsGUISection getFriendsSection() {
        return this.friendsSection;
    }

    public IgnoredGUISection getIgnoredSection() {
        return this.ignoredSection;
    }
}