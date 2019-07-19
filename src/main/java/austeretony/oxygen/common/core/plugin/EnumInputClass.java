package austeretony.oxygen.common.core.plugin;

import java.util.Iterator;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public enum EnumInputClass {

    //Client

    MC_GUI_PLAYER_TAB_OVERLAY("Minecraft", "GuiPlayerTabOverlay", 0, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);

    private static final String HOOKS_CLASS = "austeretony/oxygen/common/core/plugin/OxygenHooks";

    public final String domain, clazz;

    public final int readerFlags, writerFlags;

    EnumInputClass(String domain, String clazz, int readerFlags, int writerFlags) {
        this.domain = domain;
        this.clazz = clazz;
        this.readerFlags = readerFlags;
        this.writerFlags = writerFlags;
    }

    public boolean patch(ClassNode classNode) {
        switch (this) {
        case MC_GUI_PLAYER_TAB_OVERLAY:
            return this.pathcMCGuiPlayerTabOverlay(classNode);
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
}
