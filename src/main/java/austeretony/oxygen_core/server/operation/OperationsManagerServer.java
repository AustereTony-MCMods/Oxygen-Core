package austeretony.oxygen_core.server.operation;

import austeretony.oxygen_core.common.util.MinecraftCommon;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.*;

public class OperationsManagerServer implements Runnable {

    private final Map<UUID, PlayerEntry> playersMap = new HashMap<>();

    public OperationsManagerServer() {
        Thread thread = new Thread(this);
        thread.setName("Oxygen Operations");
        thread.setDaemon(true);
        thread.start();
    }

    void addOperation(EntityPlayerMP playerMP, Operation operation) {
        synchronized (playersMap) {
            playersMap
                    .computeIfAbsent(MinecraftCommon.getEntityUUID(playerMP), playerUUID -> new PlayerEntry())
                    .addTransaction(operation);
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (playersMap) {
                for (PlayerEntry entry : playersMap.values()) {
                    entry.update();
                }
            }
            try {
                Thread.sleep(1L);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    private static class PlayerEntry {

        private final Deque<Operation> queue = new ArrayDeque<>();
        private Operation current;

        void addTransaction(Operation operation) {
            queue.offer(operation);
        }

        void update() {
            if (current == null) {
                current = queue.poll();
            } else {
                if (current.update()) {
                    current = null;
                }
            }
        }
    }
}
