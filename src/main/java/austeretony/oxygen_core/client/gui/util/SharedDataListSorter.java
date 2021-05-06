package austeretony.oxygen_core.client.gui.util;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import java.util.*;
import java.util.function.Consumer;

public enum SharedDataListSorter {

    BY_USERNAME(list -> list.sort(Comparator.comparing(PlayerSharedData::getUsername))),
    BY_USERNAME_REVERSED(list -> list.sort(Comparator.comparing(PlayerSharedData::getUsername).reversed())),
    BY_ACTIVITY_STATUS(list -> {
        Multimap<Integer, PlayerSharedData> multimap = HashMultimap.create();
        for (PlayerSharedData sharedData : list) {
            ActivityStatus status = OxygenClient.getPlayerActivityStatus(sharedData);
            multimap.put(status.ordinal(), sharedData);
        }
        list.clear();

        Set<Integer> sortedStatuses = new TreeSet<>(multimap.keySet());
        Comparator<PlayerSharedData> comparator = Comparator.comparing(PlayerSharedData::getUsername);
        for (int statusOrdinal : sortedStatuses) {
            List<PlayerSharedData> players = new ArrayList<>(multimap.get(statusOrdinal));
            players.sort(comparator);
            list.addAll(players);
        }
    }),
    BY_ACTIVITY_STATUS_REVERSED(list -> {
        Multimap<Integer, PlayerSharedData> multimap = HashMultimap.create();
        for (PlayerSharedData sharedData : list) {
            ActivityStatus status = OxygenClient.getPlayerActivityStatus(sharedData);
            multimap.put(status.ordinal(), sharedData);
        }
        list.clear();

        List<Integer> sortedStatuses = new ArrayList<>(multimap.keySet());
        sortedStatuses.sort(Comparator.reverseOrder());

        Comparator<PlayerSharedData> comparator = Comparator.comparing(PlayerSharedData::getUsername);
        for (int statusOrdinal : sortedStatuses) {
            List<PlayerSharedData> players = new ArrayList<>(multimap.get(statusOrdinal));
            players.sort(comparator);
            list.addAll(players);
        }
    }),
    BY_DIMENSION_NAME(list -> {
        Multimap<String, PlayerSharedData> multimap = HashMultimap.create();
        for (PlayerSharedData sharedData : list) {
            String dimensionName = OxygenClient.getPlayerDimensionName(sharedData);
            multimap.put(dimensionName, sharedData);
        }
        list.clear();

        Set<String> sortedDimensionNames = new TreeSet<>(multimap.keySet());
        Comparator<PlayerSharedData> comparator = Comparator.comparing(PlayerSharedData::getUsername);
        for (String dimensionName : sortedDimensionNames) {
            List<PlayerSharedData> players = new ArrayList<>(multimap.get(dimensionName));
            players.sort(comparator);
            list.addAll(players);
        }
    }),
    BY_DIMENSION_NAME_REVERSED(list -> {
        Multimap<String, PlayerSharedData> multimap = HashMultimap.create();
        for (PlayerSharedData sharedData : list) {
            String dimensionName = OxygenClient.getPlayerDimensionName(sharedData);
            multimap.put(dimensionName, sharedData);
        }
        list.clear();

        List<String> sortedDimensionNames = new ArrayList<>(multimap.keySet());
        sortedDimensionNames.sort(Comparator.reverseOrder());

        Comparator<PlayerSharedData> comparator = Comparator.comparing(PlayerSharedData::getUsername);
        for (String dimensionName : sortedDimensionNames) {
            List<PlayerSharedData> players = new ArrayList<>(multimap.get(dimensionName));
            players.sort(comparator);
            list.addAll(players);
        }
    });

    private final Consumer<List<PlayerSharedData>> consumer;

    SharedDataListSorter(Consumer<List<PlayerSharedData>> consumer) {
        this.consumer = consumer;
    }

    public Consumer<List<PlayerSharedData>> getConsumer() {
        return consumer;
    }
}
