package austeretony.oxygen_core.server.item;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.JsonUtils;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public final class ItemsBlackList {

    private static final List<ItemsBlackList> BLACKLISTS = new ArrayList<>(5);

    private final String domain;

    private final Set<ResourceLocation> items = new HashSet<>();

    private ItemsBlackList(String domain) {
        this.domain = domain;
        BLACKLISTS.add(this);
    }

    public static ItemsBlackList create(String domain) {
        return new ItemsBlackList(domain);
    }

    public static void loadBlackLists() {
        OxygenHelperServer.addIOTask(()->{
            for (ItemsBlackList blackList : BLACKLISTS)
                blackList.load();
        });
    }

    public void load() {
        String pathStr = CommonReference.getGameFolder() + "/config/oxygen/data/server/" + this.domain + "/items_blacklist.json";
        Path path = Paths.get(pathStr); 
        if (Files.exists(path)) {
            try {      
                JsonArray jsonArray = JsonUtils.getExternalJsonData(pathStr).getAsJsonArray();
                for (JsonElement element : jsonArray)
                    this.items.add(new ResourceLocation(element.getAsString()));
                OxygenMain.LOGGER.info("Loaded items blacklist for <{}>.", this.domain);
            } catch (IOException exception) {  
                OxygenMain.LOGGER.info("Items blacklist for <{}> damaged!", this.domain);
                exception.printStackTrace();
            }       
        } else {                
            try {               
                Files.createDirectories(path.getParent());             
                try (PrintStream printStream = new PrintStream(new File(pathStr))) {
                    printStream.print("[]");
                } 
                OxygenMain.LOGGER.info("Created empty items blacklist file for <{}>.", this.domain);
            } catch (IOException exception) {      
                exception.printStackTrace();
            }                     
        }
    }

    public boolean isBlackListed(ResourceLocation registryName) {
        return this.items.contains(registryName);
    }

    public boolean isBlackListed(Item item) {
        return this.isBlackListed(item.getRegistryName());
    }

    public boolean isBlackListed(ItemStack itemStack) {
        return this.isBlackListed(itemStack.getItem().getRegistryName());
    }
}