package austeretony.oxygen_core.server.api;

import java.util.UUID;

import austeretony.oxygen_core.server.OxygenManagerServer;

public class RequestsFilterHelper {

    public static void registerNetworkRequest(int requestId, int expireTimeSeconds) {
        OxygenManagerServer.instance().getRequestsFilter().registerRequest(requestId, expireTimeSeconds);
    } 

    public static boolean getLock(UUID playerUUID, int requestId) {
        return OxygenManagerServer.instance().getRequestsFilter().getLock(playerUUID, requestId);
    }
}