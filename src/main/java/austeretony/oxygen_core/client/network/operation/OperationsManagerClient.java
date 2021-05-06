package austeretony.oxygen_core.client.network.operation;

import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.operation.NetworkOperationsHandler;
import austeretony.oxygen_core.common.network.packets.server.SPPerformOperation;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class OperationsManagerClient {

    private final Map<Integer, NetworkOperationsHandler> handlersMap = new HashMap<>();

    public void registerHandler(NetworkOperationsHandler handler) {
        handlersMap.put(handler.getId(), handler);
    }

    public void sendToServer(int handlerId, int operation, Consumer<ByteBuf> dataConsumer) {
        ByteBuf buffer = null;
        try {
            buffer = Unpooled.buffer();
            dataConsumer.accept(buffer);

            byte[] dataRaw = new byte[buffer.readableBytes()];
            buffer.getBytes(0, dataRaw);
            OxygenMain.network().sendToServer(new SPPerformOperation(handlerId, operation, dataRaw));
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }

    public void processOperation(int handlerId, int operation, byte[] dataRaw) {
        NetworkOperationsHandler handler = handlersMap.get(handlerId);
        if (handler == null) return;

        ByteBuf buffer = null;
        try {
            buffer = Unpooled.wrappedBuffer(dataRaw);
            handler.process(MinecraftClient.getPlayer(), operation, buffer);
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }
}
