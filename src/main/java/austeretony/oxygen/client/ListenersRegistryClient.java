package austeretony.oxygen.client;

import java.util.LinkedHashSet;
import java.util.Set;

import austeretony.oxygen.common.core.api.listeners.client.IChatMessageInfoListener;
import austeretony.oxygen.common.core.api.listeners.client.ICientInitListener;

public class ListenersRegistryClient {

    private static ListenersRegistryClient instance;

    private Set<IChatMessageInfoListener> chatMessagesListeners;

    private Set<ICientInitListener> clientInitListeners;

    private ListenersRegistryClient() {}

    public static void create() {
        instance = new ListenersRegistryClient();
    }

    public static ListenersRegistryClient instance() {
        return instance;
    }

    public void addChatMessagesInfoListener(IChatMessageInfoListener listener) {
        if (this.chatMessagesListeners == null)
            this.chatMessagesListeners = new LinkedHashSet<IChatMessageInfoListener>(3);
        this.chatMessagesListeners.add(listener);
    }

    public void notifyChatMessageInfoListeners(int mod, int message, String... args) {
        if (this.chatMessagesListeners != null)
            for (IChatMessageInfoListener listener : this.chatMessagesListeners)
                listener.onChatMessage(mod, message, args);
    }

    public void addClientInitListener(ICientInitListener listener) {
        if (this.clientInitListeners == null)
            this.clientInitListeners = new LinkedHashSet<ICientInitListener>(3);
        this.clientInitListeners.add(listener);
    }

    public void notifyClientInitListeners() {
        if (this.clientInitListeners != null)
            for (ICientInitListener listener : this.clientInitListeners)
                listener.onClientInit();
    }
}
