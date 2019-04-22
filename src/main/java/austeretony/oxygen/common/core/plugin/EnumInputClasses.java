package austeretony.oxygen.common.core.plugin;

import java.util.Iterator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public enum EnumInputClasses {

    //Client

    MC_MINECRAFT("Minecraft", "Minecraft", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_LOCALE("Minecraft", "Locale", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_GUI_PLAYER_TAB_OVERLAY("Minecraft", "GuiPlayerTabOverlay", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_GUI_INGAME("Minecraft", "GuiIngame", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_GUI_INGAME_FORGE("Minecraft", "GuiIngameForge", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),

    //Server

    MC_MINECRAFT_SERVER("Minecraft", "MinecraftServer", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_PLAYER_LIST("Minecraft", "PlayerList", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_NET_HANDLER_PLAY_SERVER("Minecraft", "NetHandlerPlayServer", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

    private static final String HOOKS_CLASS = "austeretony/oxygen/common/core/plugin/OxygenHooks";

    public final String domain, clazz;

    public final int readerFlags, writerFlags;

    EnumInputClasses(String domain, String clazz, int readerFlags, int writerFlags) {
        this.domain = domain;
        this.clazz = clazz;
        this.readerFlags = readerFlags;
        this.writerFlags = writerFlags;
    }

    public boolean patch(ClassNode classNode) {
        switch (this) {
        case MC_MINECRAFT:
            return this.pathcMCMinecraft(classNode);
        case MC_LOCALE:
            return this.pathcMCLocale(classNode);
        case MC_GUI_PLAYER_TAB_OVERLAY:
            return this.pathcMCGuiPlayerTabOverlay(classNode);
        case MC_GUI_INGAME:
            return this.pathcMCGuiIngame(classNode);
        case MC_GUI_INGAME_FORGE:
            return this.pathcMCGuiIngameForge(classNode);

        case MC_MINECRAFT_SERVER:
            return this.patchMCMinecraftServer(classNode);
        case MC_PLAYER_LIST:
            return this.patchMCPlayerList(classNode);
        case MC_NET_HANDLER_PLAY_SERVER:
            return this.patchMCNetHandlerPlayServer(classNode);
        }
        return false;
    }

    private boolean pathcMCMinecraft(ClassNode classNode) {
        String runTickMethodName = OxygenCorePlugin.isObfuscated() ? "t" : "runTick";
        boolean isSuccessful = false;   
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(runTickMethodName) && methodNode.desc.equals("()V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.ALOAD) {    
                        methodNode.instructions.insertBefore(currentInsn, new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "onClientTick", "()V", false)); 
                        isSuccessful = true;                        
                        break;
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }

    private boolean pathcMCLocale(ClassNode classNode) {
        String
        propertiesFieldName = OxygenCorePlugin.isObfuscated() ? "a" : "properties",
                loadLocaleDataFilesMethodName = OxygenCorePlugin.isObfuscated() ? "a" : "loadLocaleDataFiles",
                        localeClassName = OxygenCorePlugin.isObfuscated() ? "cfb" : "net/minecraft/client/resources/Locale",
                                iResourceManagerClassName = OxygenCorePlugin.isObfuscated() ? "cep" : "net/minecraft/client/resources/IResourceManager",
                                        listClassName = "java/util/List",
                                        mapClassName = "java/util/Map";
        boolean isSuccessful = false;   
        int invokespecialCount = 0;
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(loadLocaleDataFilesMethodName) && methodNode.desc.equals("(L" + iResourceManagerClassName + ";L" + listClassName + ";)V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.INVOKESPECIAL) {    
                        invokespecialCount++;
                        if (invokespecialCount == 3) {
                            InsnList nodesList = new InsnList();   
                            nodesList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            nodesList.add(new FieldInsnNode(Opcodes.GETFIELD, localeClassName, propertiesFieldName, "L" + mapClassName + ";"));
                            nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "loadCustomLocalization", "(L" + listClassName + ";L" + mapClassName + ";)V", false));
                            methodNode.instructions.insertBefore(currentInsn.getPrevious(), nodesList); 
                            isSuccessful = true;                        
                            break;
                        }
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }

    private boolean pathcMCGuiPlayerTabOverlay(ClassNode classNode) {
        String
        renderPlayerlistMethodName = OxygenCorePlugin.isObfuscated() ? "a" : "renderPlayerlist",
                scoreboardClassName = OxygenCorePlugin.isObfuscated() ? "bhk" : "net/minecraft/scoreboard/Scoreboard",
                        scoreObjectiveClassName = OxygenCorePlugin.isObfuscated() ? "bhg" : "net/minecraft/scoreboard/ScoreObjective",
                                listClassName = "java/util/List";
        boolean isSuccessful = false;   
        int astoreCount = 0;
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(renderPlayerlistMethodName) && methodNode.desc.equals("(IL" + scoreboardClassName + ";L" + scoreObjectiveClassName + ";)V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.ASTORE) {    
                        astoreCount++;
                        if (astoreCount == 2) {
                            InsnList nodesList = new InsnList();   
                            nodesList.add(new VarInsnNode(Opcodes.ALOAD, 5));
                            nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "verifyPlayersList", "(L" + listClassName + ";)V", false));
                            methodNode.instructions.insert(currentInsn, nodesList); 
                            isSuccessful = true;                        
                            break;
                        }
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }

    private boolean pathcMCGuiIngame(ClassNode classNode) {
        String renderGameOverlayMethodName = OxygenCorePlugin.isObfuscated() ? "a" : "renderGameOverlay";
        boolean isSuccessful = false;   
        int ifeqCount = 0;
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(renderGameOverlayMethodName) && methodNode.desc.equals("(F)V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.IFEQ) {    
                        ifeqCount++;
                        if (ifeqCount == 11) {
                            InsnList nodesList = new InsnList();   
                            nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "renderTabOverlay", "()Z", false));
                            nodesList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) currentInsn).label));
                            methodNode.instructions.insertBefore(currentInsn.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious(), nodesList); 
                            isSuccessful = true;                        
                            break;
                        }
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }

    private boolean pathcMCGuiIngameForge(ClassNode classNode) {
        String renderPlayerListMethodName = "renderPlayerList";
        boolean isSuccessful = false;   
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(renderPlayerListMethodName) && methodNode.desc.equals("(II)V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.IFEQ) {    
                        InsnList nodesList = new InsnList();   
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "renderTabOverlay", "()Z", false));
                        nodesList.add(new JumpInsnNode(Opcodes.IFEQ, ((JumpInsnNode) currentInsn).label));
                        methodNode.instructions.insertBefore(currentInsn.getPrevious().getPrevious().getPrevious().getPrevious().getPrevious(), nodesList); 
                        isSuccessful = true;                        
                        break;
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }

    private boolean patchMCMinecraftServer(ClassNode classNode) {
        String 
        tickMethodName = OxygenCorePlugin.isObfuscated() ? "C" : "tick";       
        boolean isSuccessful = false; 
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {       
            if (methodNode.name.equals(tickMethodName) && methodNode.desc.equals("()V")) {                               
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();                  
                while (insnIterator.hasNext()) {                    
                    currentInsn = insnIterator.next();                      
                    if (currentInsn.getOpcode() == Opcodes.INVOKESTATIC) {   
                        methodNode.instructions.insertBefore(currentInsn, new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "onServerTick", "()V", false)); 
                        isSuccessful = true;                        
                        break;
                    }
                }                                       
                break;
            }
        }
        return isSuccessful;
    }

    private boolean patchMCPlayerList(ClassNode classNode) {
        String 
        initializeConnectionToPlayerMethodName = "initializeConnectionToPlayer",
        playerLoggedOutMethodName = OxygenCorePlugin.isObfuscated() ? "e" : "playerLoggedOut",
                transferPlayerToDimensionMethodName = "transferPlayerToDimension",
                networkManagerClassName = OxygenCorePlugin.isObfuscated() ? "gw" : "net/minecraft/network/NetworkManager",
                        entityPlayerMPClassName = OxygenCorePlugin.isObfuscated() ? "oq" : "net/minecraft/entity/player/EntityPlayerMP",
                                iTeleportaerClassName = "net/minecraftforge/common/util/ITeleporter",//forge
                                netHandlerPlayServer = OxygenCorePlugin.isObfuscated() ? "pa" : "net/minecraft/network/NetHandlerPlayServer";                      
        boolean 
        isSuccessful0 = false,
        isSuccessful1 = false,
        isSuccessful2 = false; 
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {       
            if (methodNode.name.equals(initializeConnectionToPlayerMethodName) && methodNode.desc.equals("(L" + networkManagerClassName + ";L" + entityPlayerMPClassName + ";L" + netHandlerPlayServer + ";)V")) {                               
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();                  
                while (insnIterator.hasNext()) {                    
                    currentInsn = insnIterator.next();                      
                    if (currentInsn.getOpcode() == Opcodes.RETURN) {   
                        InsnList nodesList = new InsnList();             
                        nodesList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "onPlayerLogIn", "(L" + entityPlayerMPClassName + ";)V", false));
                        methodNode.instructions.insertBefore(currentInsn, nodesList); 
                        isSuccessful0 = true;           
                        break;
                    }
                }                                       
            } else if (methodNode.name.equals(playerLoggedOutMethodName) && methodNode.desc.equals("(L" + entityPlayerMPClassName + ";)V")) {                               
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();                  
                while (insnIterator.hasNext()) {                    
                    currentInsn = insnIterator.next();                      
                    if (currentInsn.getOpcode() == Opcodes.ALOAD) {   
                        InsnList nodesList = new InsnList();             
                        nodesList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "onPlayerLogOut", "(L" + entityPlayerMPClassName + ";)V", false));
                        methodNode.instructions.insertBefore(currentInsn.getPrevious(), nodesList); 
                        isSuccessful1 = true;                     
                        break;
                    }
                }                                       
            } else if (methodNode.name.equals(transferPlayerToDimensionMethodName) && methodNode.desc.equals("(L" + entityPlayerMPClassName + ";IL" + iTeleportaerClassName + ";)V")) {                               
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();                  
                while (insnIterator.hasNext()) {                    
                    currentInsn = insnIterator.next();                      
                    if (currentInsn.getOpcode() == Opcodes.RETURN) {   
                        InsnList nodesList = new InsnList();             
                        nodesList.add(new VarInsnNode(Opcodes.ALOAD, 1));
                        nodesList.add(new VarInsnNode(Opcodes.ILOAD, 4));
                        nodesList.add(new VarInsnNode(Opcodes.ILOAD, 2));
                        nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "onPlayerChangedDimension", "(L" + entityPlayerMPClassName + ";II)V", false));
                        methodNode.instructions.insertBefore(currentInsn, nodesList); 
                        isSuccessful2 = true;                        
                        break;
                    }
                }                                       
                break;
            }
        }
        return isSuccessful0 && isSuccessful1 && isSuccessful2;
    }

    private boolean patchMCNetHandlerPlayServer(ClassNode classNode) {
        String
        playerFieldName = OxygenCorePlugin.isObfuscated() ? "b" : "player",
                processChatMessageMethodName = OxygenCorePlugin.isObfuscated() ? "a" : "processChatMessage",
                        netHandlerPlayServerClassName = OxygenCorePlugin.isObfuscated() ? "pa" : "net/minecraft/network/NetHandlerPlayServer",
                                entityPlayerMPClassName = OxygenCorePlugin.isObfuscated() ? "oq" : "net/minecraft/entity/player/EntityPlayerMP",
                                        cPacketChatMessageClassName = OxygenCorePlugin.isObfuscated() ? "la" : "net/minecraft/network/play/client/CPacketChatMessage",
                                                iTextComponentClassName = OxygenCorePlugin.isObfuscated() ? "hh" : "net/minecraft/util/text/ITextComponent",
                                                        stringClassName = "java/lang/String";
        boolean isSuccessful = false;   
        int iconstCount = 0;
        AbstractInsnNode currentInsn;

        for (MethodNode methodNode : classNode.methods) {               
            if (methodNode.name.equals(processChatMessageMethodName) && methodNode.desc.equals("(L" + cPacketChatMessageClassName + ";)V")) {                         
                Iterator<AbstractInsnNode> insnIterator = methodNode.instructions.iterator();              
                while (insnIterator.hasNext()) {                        
                    currentInsn = insnIterator.next();                  
                    if (currentInsn.getOpcode() == Opcodes.ICONST_0) {    
                        iconstCount++;
                        if (iconstCount == 5) {
                            InsnList nodesList = new InsnList();   
                            nodesList.add(new VarInsnNode(Opcodes.ALOAD, 0));
                            nodesList.add(new FieldInsnNode(Opcodes.GETFIELD, netHandlerPlayServerClassName, playerFieldName, "L" + entityPlayerMPClassName + ";"));
                            nodesList.add(new VarInsnNode(Opcodes.ALOAD, 2));
                            nodesList.add(new VarInsnNode(Opcodes.ALOAD, 3));
                            nodesList.add(new MethodInsnNode(Opcodes.INVOKESTATIC, HOOKS_CLASS, "processChatMessage", "(L" + entityPlayerMPClassName + ";L" + stringClassName + ";L" + iTextComponentClassName + ";)V", false));
                            methodNode.instructions.insertBefore(currentInsn.getPrevious().getPrevious().getPrevious().getPrevious(), nodesList); 
                            methodNode.instructions.remove(currentInsn.getPrevious().getPrevious().getPrevious().getPrevious());
                            methodNode.instructions.remove(currentInsn.getPrevious().getPrevious().getPrevious());
                            methodNode.instructions.remove(currentInsn.getPrevious().getPrevious());
                            methodNode.instructions.remove(currentInsn.getPrevious());
                            methodNode.instructions.remove(currentInsn.getNext());
                            methodNode.instructions.remove(currentInsn);
                            isSuccessful = true;                        
                            break;
                        }
                    }
                }    
                break;
            }
        }
        return isSuccessful;
    }
}
