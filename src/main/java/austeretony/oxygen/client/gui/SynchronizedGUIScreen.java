package austeretony.oxygen.client.gui;

import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.oxygen.client.api.OxygenGUIHelper;

public abstract class SynchronizedGUIScreen extends AbstractGUIScreen {

    public final int screenId;

    private boolean initialized;

    public SynchronizedGUIScreen(int screenId) {
        this.screenId = screenId;
    }

    @Override
    protected void init() {     
        super.init();
        OxygenGUIHelper.screenInitialized(this.screenId);
    }

    /**
     * Called when data received from server. Refer actual data from here.
     */
    public abstract void loadData();

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (!this.initialized
                && OxygenGUIHelper.isNeedSync(this.screenId)
                && OxygenGUIHelper.isScreenInitialized(this.screenId)
                && OxygenGUIHelper.isDataReceived(this.screenId)) {
            this.initialized = true;
            this.loadData();
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        OxygenGUIHelper.resetNeedSync(this.screenId);
        OxygenGUIHelper.resetScreenInitialized(this.screenId);
        OxygenGUIHelper.resetDataReceived(this.screenId);
    }
}
