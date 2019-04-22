package austeretony.oxygen.common;

import java.util.LinkedHashSet;
import java.util.Set;

import austeretony.oxygen.common.core.api.listeners.server.IPlayerChangedDimensionListener;
import austeretony.oxygen.common.core.api.listeners.server.IPlayerLogInListener;
import austeretony.oxygen.common.core.api.listeners.server.IPlayerLogOutListener;
import austeretony.oxygen.common.core.api.listeners.server.IServerTickListener;
import net.minecraft.entity.player.EntityPlayerMP;

public class ListenersRegistryServer {

    private static ListenersRegistryServer instance;

    private Set<IPlayerLogInListener> logInListeners;

    private Set<IPlayerLogOutListener> logOutListeners;

    private Set<IPlayerChangedDimensionListener> changedDimensionListeners;

    private Set<IServerTickListener> serverTickListeners;

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

    public void addPlayerChangedDimensionListener(IPlayerChangedDimensionListener listener) {
        if (this.changedDimensionListeners == null)
            this.changedDimensionListeners = new LinkedHashSet<IPlayerChangedDimensionListener>(3);
        this.changedDimensionListeners.add(listener);
    }

    public void notifyPlayerChangedDimensionListeners(EntityPlayerMP playerMP, int fromDim, int toDim) {
        if (this.changedDimensionListeners != null)
            for (IPlayerChangedDimensionListener listener : this.changedDimensionListeners)
                listener.onPlayerChangedDimension(playerMP, fromDim, toDim);
    }

    public void addServerTickListener(IServerTickListener listener) {
        if (this.serverTickListeners == null)
            this.serverTickListeners = new LinkedHashSet<IServerTickListener>(3);
        this.serverTickListeners.add(listener);
    }

    public void notifyServerTickListeners() {
        if (this.serverTickListeners != null)
            for (IServerTickListener listener : this.serverTickListeners)
                listener.onServerTick();
    }
}
