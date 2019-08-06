package austeretony.oxygen.common;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.process.IPersistentProcess;
import austeretony.oxygen.common.process.ITemporaryProcess;
import net.minecraft.entity.player.EntityPlayer;

public class ProcessesManagerServer {

    private final Map<Long, ITemporaryProcess> globalTemporaryProcesses = new ConcurrentHashMap<Long, ITemporaryProcess>();

    private final Set<IPersistentProcess> serviceProcesses = new HashSet<IPersistentProcess>(5);

    private volatile boolean globalTemporaryProcessesExist;

    public void addPersistentServiceProcess(IPersistentProcess process) {
        this.serviceProcesses.add(process);
    }

    private void runServiceProcesses() {
        for (IPersistentProcess process : this.serviceProcesses)
            process.run();
    }

    public void addGlobalTemporaryProcess(ITemporaryProcess process) {
        this.globalTemporaryProcesses.put(process.getId(), process);
        this.globalTemporaryProcessesExist = true;
    }

    public boolean globalTemporaryProcessExist(long processId) {
        return this.globalTemporaryProcesses.containsKey(processId);
    }

    public ITemporaryProcess getGlobalTemporaryProcess(long processId) {
        return this.globalTemporaryProcesses.get(processId);
    }

    private void runGlobalTemporaryProcesses() {
        if (this.globalTemporaryProcessesExist) {
            Iterator<ITemporaryProcess> iterator = this.globalTemporaryProcesses.values().iterator();
            while (iterator.hasNext()) {
                if (iterator.next().isExpired()) {
                    iterator.remove();
                    this.globalTemporaryProcessesExist = !this.globalTemporaryProcesses.isEmpty();
                }
            }
        }
    }

    public void addPlayerTemporaryProcess(EntityPlayer player, ITemporaryProcess process) {
        OxygenManagerServer.instance().getPlayerData(CommonReference.getPersistentUUID(player)).addTemporaryProcess(process);
    }

    private void runPlayersTemporaryProcesses() {
        for (OxygenPlayerData profile : OxygenManagerServer.instance().getPlayersData())
            profile.runTemporaryProcesses();
    }

    public void runProcesses() {
        this.runServiceProcesses();
        this.runGlobalTemporaryProcesses();
        this.runPlayersTemporaryProcesses();
    }

    public void reset() {
        this.globalTemporaryProcesses.clear();
    }
}
