package austeretony.oxygen_core.server;

import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.main.OxygenMain;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Pattern;

public class ItemsBlacklistsManagerServer {

    private static final String
            REGEX_HASH = Pattern.quote("#"),
            REGEX_COMMA = Pattern.quote(",");

    private final Map<String, ItemsBlacklist> blacklistsMap = new HashMap<>();

    public void registerBlacklist(String name) {
        blacklistsMap.put(name, new ItemsBlacklist(name));
    }

    public void loadBlacklists() {
        OxygenMain.logInfo(2, "[Core] Loading items blacklists...");
        String folder = OxygenCommon.getConfigFolder() + "/data/server/blacklists/items/";
        for (ItemsBlacklist blacklist : blacklistsMap.values()) {
            String pathStr = folder + blacklist.name + ".txt";
            File file = new File(pathStr);
            if (file.exists())
                loadFromFile(blacklist, file);
            else {
                Path path = Paths.get(pathStr);

                List<String> tipLines = new ArrayList<>(9);
                tipLines.add("# This is a commentary");
                tipLines.add("#");
                tipLines.add("# Syntax:");
                tipLines.add("# <registry name>#<meta>,<meta>,...");
                tipLines.add("#");
                tipLines.add("# Examples:");
                tipLines.add("# 1. Blacklist item with all subtypes");
                tipLines.add("# minecraft:dye");
                tipLines.add("# 2. Blacklist item subtype(s) 0 - 2");
                tipLines.add("# minecraft:dye#0,1,2");

                try {
                    Files.createDirectories(path.getParent());

                    Files.write(path, tipLines);
                    OxygenMain.logInfo(1, "[Core] Created empty blacklist: <{}>", blacklist.name);
                } catch (IOException exception) {
                    OxygenMain.logError(1, "[Core] Failed to create empty blacklist file: <{}>",
                            blacklist.name);
                    exception.printStackTrace();
                }
            }
        }
        OxygenMain.logInfo(1, "[Core] Blacklists loaded");
    }

    private void loadFromFile(ItemsBlacklist blacklist, File file) {
        try {
            List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
            for (String l : lines) {
                String line = l.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                String[] data = line.split(REGEX_HASH);
                if (data.length > 1) {
                    String[] meta = data[1].split(REGEX_COMMA);
                    for (String metaStr : meta) {
                        blacklist.itemsMetaMap.put(data[0], Integer.parseInt(metaStr));
                    }
                } else {
                    blacklist.itemsMetaMap.put(data[0], ItemsBlacklist.DEFAULT_META);
                }
            }
            OxygenMain.logInfo(1, "[Core] Loaded items blacklist: <{}>", blacklist.name);
        } catch (Exception exception) {
            OxygenMain.logError(1, "[Core] Failed to load items blacklist: <{}>", blacklist.name);
            exception.printStackTrace();
        }
    }

    public boolean isItemBlacklisted(String blacklistName, ItemStackWrapper stackWrapper) {
        return isItemBlacklisted(blacklistName, stackWrapper.getRegistryName(), stackWrapper.getItemDamage());
    }

    public boolean isItemBlacklisted(String blacklistName, ItemStack stack) {
        return isItemBlacklisted(blacklistName, stack.getItem().getRegistryName().toString(), stack.getMetadata());
    }

    public boolean isItemBlacklisted(String blacklistName, String registryName, int meta) {
        ItemsBlacklist blacklist = blacklistsMap.get(blacklistName);
        if (blacklist != null) {
            return blacklist.isItemBlacklisted(registryName, meta);
        }
        return false;
    }

    static class ItemsBlacklist {

        static final int DEFAULT_META = -1;

        final String name;
        final Multimap<String, Integer> itemsMetaMap = HashMultimap.create();

        ItemsBlacklist(String name) {
            this.name = name;
        }

        public boolean isItemBlacklisted(String registryName, int meta) {
            Collection<Integer> metaCollection = itemsMetaMap.get(registryName);
            return metaCollection.contains(DEFAULT_META) || metaCollection.contains(meta);
        }
    }
}
