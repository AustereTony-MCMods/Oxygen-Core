package austeretony.oxygen_core.client.notification;

public interface NotificationProviderClient {

    void acceptLatestRequest();

    void rejectLatestRequest();
}
