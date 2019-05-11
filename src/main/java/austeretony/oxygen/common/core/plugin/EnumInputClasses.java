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

    MC_GUI_PLAYER_TAB_OVERLAY("Minecraft", "GuiPlayerTabOverlay", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_GUI_INGAME("Minecraft", "GuiIngame", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),
    MC_GUI_INGAME_FORGE("Minecraft", "GuiIngameForge", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES),

    //Server

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
        case MC_GUI_PLAYER_TAB_OVERLAY:
            return this.pathcMCGuiPlayerTabOverlay(classNode);
        case MC_GUI_INGAME:
            return this.pathcMCGuiIngame(classNode);
        case MC_GUI_INGAME_FORGE:
            return this.pathcMCGuiIngameForge(classNode);

        case MC_NET_HANDLER_PLAY_SERVER:
            return this.patchMCNetHandlerPlayServer(classNode);
        }
        return false;
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
