package austeretony.oxygen_core.client.gui.privileges.management;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.lwjgl.input.Keyboard;

import austeretony.alternateui.screen.callback.AbstractGUICallback;
import austeretony.alternateui.screen.core.AbstractGUISection;
import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu;
import austeretony.oxygen_core.client.gui.elements.OxygenDefaultBackgroundWithButtonsFiller;
import austeretony.oxygen_core.client.gui.elements.OxygenKeyButton;
import austeretony.oxygen_core.client.gui.elements.OxygenScrollablePanel;
import austeretony.oxygen_core.client.gui.elements.OxygenSectionSwitcher;
import austeretony.oxygen_core.client.gui.elements.OxygenSorter;
import austeretony.oxygen_core.client.gui.elements.OxygenSorter.EnumSorting;
import austeretony.oxygen_core.client.gui.elements.OxygenTextLabel;
import austeretony.oxygen_core.client.gui.privileges.management.privileges.callback.AddDefaultPrivilegeCallback;
import austeretony.oxygen_core.client.gui.privileges.management.privileges.callback.RemoveDefaultPrivilegeCallback;
import austeretony.oxygen_core.client.gui.privileges.management.privileges.context.RemovePrivilegeContextAction;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.util.MathUtils;
import net.minecraft.client.gui.ScaledResolution;

public class DefaultPrivilegesSection extends AbstractGUISection {

    private final PrivilegesManagementScreen screen;

    private OxygenTextLabel privilegesAmountLabel;

    private OxygenKeyButton addDefaultPrivilegeButton;

    private OxygenSorter nameSorter;

    private OxygenScrollablePanel privilegesPanel;

    private AbstractGUICallback addDefaultPrivilegeCallback, removeDefaultPrivilegeCallback;

    //cache

    private PrivilegePanelEntry currPrivilegeEntry;

    public DefaultPrivilegesSection(PrivilegesManagementScreen screen) {
        super(screen);
        this.screen = screen;
        this.setDisplayText(ClientReference.localize("oxygen_core.gui.privileges.management.defaultPrivileges"));
    }

    @Override
    public void init() {
        this.addDefaultPrivilegeCallback = new AddDefaultPrivilegeCallback(this.screen, this, 190, 153).enableDefaultBackground();
        this.removeDefaultPrivilegeCallback = new RemoveDefaultPrivilegeCallback(this.screen, this, 140, 36).enableDefaultBackground();

        this.addElement(new OxygenDefaultBackgroundWithButtonsFiller(0, 0, this.getWidth(), this.getHeight()));
        this.addElement(new OxygenTextLabel(4, 12, ClientReference.localize("oxygen_core.gui.privileges.management.title"), EnumBaseGUISetting.TEXT_TITLE_SCALE.get().asFloat(), EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.privilegesAmountLabel = new OxygenTextLabel(54, this.getHeight() - 4, "", EnumBaseGUISetting.TEXT_SUB_SCALE.get().asFloat() - 0.05F, EnumBaseGUISetting.TEXT_ENABLED_COLOR.get().asInt()));

        this.addElement(this.nameSorter = new OxygenSorter(6, 18, EnumSorting.INACTIVE, ClientReference.localize("oxygen_core.gui.name")));  
        this.nameSorter.setSortingListener((sorting)->this.sortPrivileges(sorting == EnumSorting.DOWN ? 0 : 1));

        this.addElement(this.privilegesPanel = new OxygenScrollablePanel(this.screen, 6, 24, this.getWidth() - 15, 10, 1, 1120, 14, EnumBaseGUISetting.TEXT_PANEL_SCALE.get().asFloat(), true));
        this.privilegesPanel.<PrivilegePanelEntry>setElementClickListener((previous, clicked, mouseX, mouseY, mouseButton)->this.currPrivilegeEntry = clicked);
        this.privilegesPanel.initContextMenu(new OxygenContextMenu(new RemovePrivilegeContextAction()));

        this.addElement(new OxygenSectionSwitcher(this.getWidth() - 4, 4, this, this.screen.getRolesSection(), this.screen.getPlayersSection()));

        this.addElement(this.addDefaultPrivilegeButton = new OxygenKeyButton(0, this.getY() + this.getHeight() + this.screen.guiTop - 8, ClientReference.localize("oxygen_core.gui.privileges.management.button.addDefaultPrivilege"), Keyboard.KEY_E, this.addDefaultPrivilegeCallback::open));     
    }

    private void calculateButtonsHorizontalPosition() {
        ScaledResolution sr = new ScaledResolution(this.mc);
        this.addDefaultPrivilegeButton.setX((sr.getScaledWidth() - (10 + this.textWidth(this.addDefaultPrivilegeButton.getDisplayText(), this.addDefaultPrivilegeButton.getTextScale()))) / 2 - this.screen.guiLeft);
    }

    private void sortPrivileges(int mode) {
        List<Privilege> privileges = new ArrayList<>(OxygenManagerClient.instance().getPrivilegesContainer().getDefaultPrivileges());

        Collections.sort(privileges, (p1, p2)->p1.getId() - p2.getId());

        this.privilegesPanel.reset();
        for (Privilege privilege : privileges)
            this.privilegesPanel.addEntry(new PrivilegePanelEntry(privilege));

        this.privilegesPanel.getScroller().reset();
        this.privilegesPanel.getScroller().updateRowsAmount(MathUtils.clamp(privileges.size(), 14, 1120));
    }

    @Override
    public void handleElementClick(AbstractGUISection section, GUIBaseElement element, int mouseButton) {
        if (mouseButton == 0) {
            if (element == this.addDefaultPrivilegeButton)
                this.addDefaultPrivilegeCallback.open();
        }
    }

    public void privilegesDataReceived() {
        this.sortPrivileges(0);

        this.calculateButtonsHorizontalPosition();
    }

    public void defaultPrivilegeAdded(Privilege privilege) {
        this.sortPrivileges(0);
    }

    public void defaultPrivilegeRemoved(Privilege privilege) {
        this.sortPrivileges(0);
    }

    public PrivilegePanelEntry getCurrentPrivilegeEntry() {
        return this.currPrivilegeEntry;
    }

    public void openAddDefaultPrivilegeCallback() {
        this.addDefaultPrivilegeCallback.open();
    }

    public void openRemoveDefaultPrivilegeCallback() {
        this.removeDefaultPrivilegeCallback.open();
    }
}
