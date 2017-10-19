package io.prplz.memoryfix;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Iterator;

public class ClassTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes) {
        if (name.equals("CapeUtils")) {
            // Use our CapeImageBuffer instead of OptiFine's
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);

            RemappingClassAdapter adapter = new RemappingClassAdapter(classWriter, new Remapper() {
                @Override
                public String map(String typeName) {
                    if (typeName.equals("CapeUtils$1")) {
                        return "io.prplz.memoryfix.CapeImageBuffer".replace('.', '/');
                    }
                    return typeName;
                }
            });

            ClassReader classReader = new ClassReader(bytes);
            classReader.accept(adapter, ClassReader.EXPAND_FRAMES);
            return classWriter.toByteArray();
        } else if (name.equals("io.prplz.memoryfix.CapeImageBuffer")) {
            // Redirect our stub calls to optifine
            ClassReader classReader = new ClassReader(bytes);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.EXPAND_FRAMES);

            for (MethodNode method : classNode.methods) {
                Iterator<AbstractInsnNode> iter = method.instructions.iterator();
                while (iter.hasNext()) {
                    AbstractInsnNode insn = iter.next();
                    if (insn instanceof MethodInsnNode) {
                        MethodInsnNode methodInsn = (MethodInsnNode) insn;
                        if (methodInsn.name.equals("parseCape")) {
                            methodInsn.owner = "CapeUtils";
                        } else if (methodInsn.name.equals("setLocationOfCape")) {
                            methodInsn.setOpcode(Opcodes.INVOKEVIRTUAL);
                            methodInsn.owner = "net/minecraft/client/entity/AbstractClientPlayer";
                            methodInsn.desc = "(Lnet/minecraft/util/ResourceLocation;)V";
                        }
                    }
                }
            }

            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        }
        return bytes;
    }
}
