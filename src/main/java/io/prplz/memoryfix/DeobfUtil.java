package io.prplz.memoryfix;

import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class DeobfUtil {

    public static boolean matchMethod(ClassNode clazz, MethodNode method, String srg, String mcp, String desc) {
        String mappedName = FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(clazz.name, method.name, method.desc);
        String mappedDesc = FMLDeobfuscatingRemapper.INSTANCE.mapMethodDesc(method.desc);
        return (mappedName.equals(srg) || mappedName.equals(mcp)) && mappedDesc.equals(desc);
    }
}
