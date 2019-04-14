package austeretony.oxygen.common;

import java.util.LinkedHashSet;
import java.util.Set;

import austeretony.oxygen.common.core.api.listeners.server.IPlayerLogInListener;
import austeretony.oxygen.common.core.api.listeners.server.IPlayerLogOutListener;
import net.minecraft.entity.player.EntityPlayerMP;

public class ListenersRegistryServer {

    private static ListenersRegistryServer instance;

    private Set<IPlayerLogInListener> logInListeners;

    private Set<IPlayerLogOutListener> logOutListeners;

    private ListenersRegistryServer() {}

    public static void create() {
        instance = new ListenersRegistryServer();
    }

    public static ListenersRegistryServer instance() {
        return instance;
    }

    public void addPlayerLogInListener(IPlayerLogInListener listener) {
        if (this.logInListeners == null)
            this.logInListeners = new LinkedHashSet<IPlayerLogInListener>(3);
        this.logInListeners.add(listener);
    }

    public void notifyPlayerLogInListeners(EntityPlayerMP playerMP) {
        if (this.logInListeners != null)
            for (IPlayerLogInListener listener : this.logInListeners)
                listener.onPlayerLogIn(playerMP);
    }

    public void addPlayerLogOutListener(IPlayerLogOutListener listener) {
        if (this.logOutListeners == null)
            this.logOutListeners = new LinkedHashSet<IPlayerLogOutListener>(3);
        this.logOutListeners.add(listener);
    }

    public void notifyPlayerLogOutListeners(EntityPlayerMP playerMP) {
        if (this.logOutListeners != null)
            for (IPlayerLogOutListener listener : this.logOutListeners)
                listener.onPlayerLogOut(playerMP);
    }
}
