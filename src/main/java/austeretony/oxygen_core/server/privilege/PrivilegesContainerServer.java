package austeretony.oxygen_core.server.privilege;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncPrivilegesManagementData;
import austeretony.oxygen_core.common.network.client.CPSyncRolesData;
import austeretony.oxygen_core.common.privilege.EnumPrivilegeFileKey;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegeUtils;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.privilege.RoleImpl;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.JsonUtils;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.event.OxygenPrivilegesLoadedEvent;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;

public class PrivilegesContainerServer {

    private final OxygenManagerServer manager;

    //privileges data

    private final Map<Integer, Privilege> defaultPrivileges = new ConcurrentHashMap<>();

    private final Map<Integer, Role> roles = new ConcurrentHashMap<>(3);

    //players data

    private final Map<UUID, Set<Integer>> playersRoles = new ConcurrentHashMap<>();

    private final Map<UUID, Integer> formattingRoles = new ConcurrentHashMap<>();

    //cache

    private ByteBuf compressedRolesData = Unpooled.buffer();

    private volatile boolean changed;

    public PrivilegesContainerServer(OxygenManagerServer manager) {
        this.manager = manager;
    } 

    public Collection<Privilege> getDefaultPrivileges() {
        return this.defaultPrivileges.values();
    }

    @Nullable
    public Privilege getDefaultPrivilege(int privilegeId) {
        return this.defaultPrivileges.get(privilegeId);
    }

    public void addDefaultPrivilege(Privilege privilege) {
        this.defaultPrivileges.put(privilege.getId(), privilege);
    }

    @Nullable
    public Privilege removeDefaultPrivilege(int privilegeId) {
        return this.defaultPrivileges.remove(privilegeId);
    }

    public Set<Integer> getRolesIds() {
        return this.roles.keySet();
    }

    public Collection<Role> getRoles() {
        return this.roles.values();
    }

    @Nullable
    public Role getRole(int roleId) {
        return this.roles.get(roleId);
    }

    public void addRole(Role role) {
        this.roles.put(role.getId(), role);
    }

    @Nullable
    public Role removeRole(int roleId) {
        return this.roles.remove(roleId);
    }

    public Map<UUID, Set<Integer>> getPlayersRoles() {
        return this.playersRoles;
    }

    @Nullable
    public Set<Integer> getPlayerRolesIds(UUID playerUUID) {
        Set<Integer> ids;
        if ((ids = this.playersRoles.get(playerUUID)) != null)
            return ids;
        return null;
    }

    public void setPlayerRolesIds(UUID playerUUID, Set<Integer> ids) {
        this.playersRoles.put(playerUUID, ids);
    }

    public void removePlayerRolesIds(UUID playerUUID) {
        this.playersRoles.remove(playerUUID);
    }

    public void setPlayerChatFormattingRole(UUID playerUUID, int roleId) {
        this.formattingRoles.put(playerUUID, roleId);
    }

    public void removePlayerChatFormattingRole(UUID playerUUID) {
        this.formattingRoles.remove(playerUUID);
    }

    public int getPlayerChatFormattingRole(UUID playerUUID) {
        Integer formattingRole = this.formattingRoles.get(playerUUID);
        return formattingRole == null ? OxygenMain.DEFAULT_ROLE_INDEX : formattingRole;
    }

    public void compressPrivilegesData() {
        synchronized (this.compressedRolesData) {
            this.compressedRolesData.clear();

            this.compressedRolesData.writeByte(this.roles.size());
            for (Role role : this.roles.values()) {
                this.compressedRolesData.writeByte(role.getId());
                ByteBufUtils.writeString(role.getName(), this.compressedRolesData);
                this.compressedRolesData.writeByte(role.getNameColor().ordinal());
            }    
        }
    }

    public void syncPrivilegesData(EntityPlayerMP playerMP) {
        synchronized (this.compressedRolesData) {
            byte[] compressed = new byte[this.compressedRolesData.writerIndex()];
            this.compressedRolesData.getBytes(0, compressed);

            OxygenMain.network().sendTo(new CPSyncRolesData(compressed), playerMP);
        }
    }

    public void syncManagementData(EntityPlayerMP playerMP) {
        if (CommonReference.isPlayerOpped(playerMP)) {
            ByteBuf buffer = null;
            try {
                buffer = Unpooled.buffer();

                buffer.writeShort(this.defaultPrivileges.size());
                for (Privilege privilege : this.defaultPrivileges.values())
                    privilege.write(buffer);

                buffer.writeByte(this.roles.size());
                for (Role role : this.roles.values())
                    role.write(buffer);

                buffer.writeShort(this.playersRoles.size());
                PlayerSharedData sharedData;
                for (UUID playerUUID : this.playersRoles.keySet()) {
                    sharedData = OxygenHelperServer.getPlayerSharedData(playerUUID);
                    if (sharedData != null) {
                        buffer.writeBoolean(true);
                        sharedData.write(buffer);
                    } else
                        buffer.writeBoolean(false);
                }

                byte[] compressed = new byte[buffer.writerIndex()];
                buffer.getBytes(0, compressed);

                OxygenMain.network().sendTo(new CPSyncPrivilegesManagementData(compressed), playerMP);
            } finally {
                if (buffer != null)
                    buffer.release();
            }
        }
    }

    public void worldLoaded() {
        this.loadDefaultPrivileges();
        this.loadRoles();
        this.loadPlayersList();
        this.manager.getPrivilegesManager().addDefaultRoles();
        this.compressPrivilegesData();
        MinecraftForge.EVENT_BUS.post(new OxygenPrivilegesLoadedEvent());
    }

    public Future<?> reload() {
        return OxygenHelperServer.addIOTask(()->{
            this.loadDefaultPrivileges();
            this.loadRoles();
            this.loadPlayersList();
            this.compressPrivilegesData();
        });
    }

    private void loadDefaultPrivileges() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/default_privileges.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray privilegesArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                for (JsonElement privilegeElement : privilegesArray)
                    this.addDefaultPrivilege(PrivilegeUtils.fromJson(privilegeElement.getAsJsonObject()));
                OxygenMain.LOGGER.info("[Core] Default privileges loaded.");
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("[Core] Default privileges loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    private void loadRoles() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/roles.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray rolesArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                for (JsonElement roleElement : rolesArray)
                    this.addRole(RoleImpl.fromJson(roleElement.getAsJsonObject()));
                OxygenMain.LOGGER.info("[Core] Roles loaded.");
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("[Core] Roles loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    private void loadPlayersList() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/players.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray 
                playersArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray(),
                rolesIdsArray;
                JsonObject playerObject;
                UUID playerUUID;
                for (JsonElement playerElement : playersArray) {
                    playerObject = playerElement.getAsJsonObject();
                    playerUUID = UUID.fromString(playerObject.get(EnumPrivilegeFileKey.PLAYER_UUID.name).getAsString());
                    rolesIdsArray = playerObject.get(EnumPrivilegeFileKey.ROLES.name).getAsJsonArray();
                    for (JsonElement roleIdElement : rolesIdsArray)
                        this.manager.getPrivilegesManager().addRoleToPlayerFast(playerUUID, roleIdElement.getAsInt());
                    this.manager.getPrivilegesManager().setPlayerChatFormattingRole(playerUUID, playerObject.get(EnumPrivilegeFileKey.CHAT_FORMATTING_ROLE.name).getAsInt());
                }
                OxygenMain.LOGGER.info("[Core] Loaded privileged players list.");
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("[Core] Privileged players list loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    public Future<?> saveDefaultPrivilegesAsync() {
        return OxygenHelperServer.addIOTask(this::saveDefaultPrivileges);
    }

    public void saveDefaultPrivileges() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/default_privileges.json";
        Path path = Paths.get(folder);    
        try {   
            if (!Files.exists(path))
                Files.createDirectories(path.getParent());              

            JsonArray privilegesArray = new JsonArray();
            this.getDefaultPrivileges()
            .stream()
            .sorted((p1, p2)->p1.getId() - p2.getId())
            .forEach((privilege)->privilegesArray.add(privilege.toJson()));

            JsonUtils.createExternalJsonFile(folder, privilegesArray);
        } catch (IOException exception) {
            OxygenMain.LOGGER.error("[Core] Default privileges saving failed.");
            exception.printStackTrace();
        }       
    }

    public Future<?> saveRolesAsync() {
        return OxygenHelperServer.addIOTask(this::saveRoles);
    }

    public void saveRoles() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/roles.json";
        Path path = Paths.get(folder);    
        try {     
            if (!Files.exists(path))
                Files.createDirectories(path.getParent());

            JsonArray rolesArray = new JsonArray();
            this.getRoles()
            .stream()
            .sorted((r1, r2)->r1.getId() - r2.getId())
            .forEach((role)->rolesArray.add(role.toJson()));

            JsonUtils.createExternalJsonFile(folder, rolesArray);
        } catch (IOException exception) {
            OxygenMain.LOGGER.error("[Core] Roles saving failed.");
            exception.printStackTrace();
        }       
    }

    public Future<?> savePlayersListAsync() {
        return OxygenHelperServer.addIOTask(this::savePlayersList);
    }

    public void savePlayersList() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/players.json";
        Path path = Paths.get(folder);    
        try {      
            if (!Files.exists(path))
                Files.createDirectories(path.getParent());

            JsonArray 
            playersArray = new JsonArray(),
            rolesIdsArray;
            JsonObject playerObject;
            for (Map.Entry<UUID, Set<Integer>> entry : this.playersRoles.entrySet()) {
                playerObject = new JsonObject();
                playerObject.add(EnumPrivilegeFileKey.PLAYER_UUID.name, new JsonPrimitive(entry.getKey().toString()));
                rolesIdsArray = new JsonArray();
                for (int roleId : entry.getValue())
                    rolesIdsArray.add(new JsonPrimitive(roleId));
                playerObject.add(EnumPrivilegeFileKey.ROLES.name, rolesIdsArray);
                playerObject.add(EnumPrivilegeFileKey.CHAT_FORMATTING_ROLE.name, 
                        new JsonPrimitive(this.getPlayerChatFormattingRole(entry.getKey())));
                playersArray.add(playerObject);
            }

            JsonUtils.createExternalJsonFile(folder, playersArray);
        } catch (IOException exception) {
            OxygenMain.LOGGER.error("[Core] Privileged players list saving failed.");
            exception.printStackTrace();
        }       
    }

    public void markChanged() {
        this.changed = true;
    }

    public void save() {
        if (this.changed) {
            this.changed = false;
            this.saveDefaultPrivilegesAsync();
            this.saveRolesAsync();
            this.savePlayersListAsync();
        }
    }
}
