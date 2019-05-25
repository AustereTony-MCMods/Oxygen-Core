package austeretony.oxygen.client;

import austeretony.oxygen.common.api.process.AbstractPersistentProcess;

public class NotificationsProcess extends AbstractPersistentProcess {

    @Override
    public boolean hasDelay() {
        return false;
    }

    @Override
    public int getExecutionDelay() {
        return 0;
    }

    @Override
    public void execute() {
        if (OxygenManagerClient.instance().getNotificationsManager() != null)
            OxygenManagerClient.instance().getNotificationsManager().processNotifications();
    }
}
