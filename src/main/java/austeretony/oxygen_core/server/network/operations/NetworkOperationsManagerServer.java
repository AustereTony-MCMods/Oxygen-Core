package austeretony.oxygen_core.server.network.operations;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.operation.NetworkOperationsHandler;
import austeretony.oxygen_core.common.network.packets.client.CPPerformOperation;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class NetworkOperationsManagerServer {

    private final Map<Integer, NetworkOperationsHandler> handlersMap = new HashMap<>();

    public void registerHandler(NetworkOperationsHandler handler) {
        handlersMap.put(handler.getId(), handler);
        OxygenServer.registerNetworkRequest(handler.getId() + 21500, 500);
    }

    public void sendToClient(EntityPlayerMP playerMP, int handlerId, int operation, Consumer<ByteBuf> dataConsumer) {
        ByteBuf buffer = null;
        try {
            buffer = Unpooled.buffer();
            dataConsumer.accept(buffer);

            byte[] dataRaw = new byte[buffer.readableBytes()];
            buffer.getBytes(0, dataRaw);
            OxygenMain.network().sendTo(new CPPerformOperation(handlerId, operation, dataRaw), playerMP);
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }

    public void processOperation(EntityPlayerMP playerMP, int handlerId, int operation, byte[] dataRaw) {
        NetworkOperationsHandler handler = handlersMap.get(handlerId);
        if (handler == null) return;

        ByteBuf buffer = null;
        try {
            buffer = Unpooled.wrappedBuffer(dataRaw);
            handler.process(playerMP, operation, buffer);
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }
}
