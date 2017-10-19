package io.prplz.memoryfix;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

public class FMLLoadingPlugin implements IFMLLoadingPlugin {

    @Override
    public String[] getASMTransformerClass() {
        if (!VersionUtil.is1_8_X()) {
            System.out.println("***********************************");
            System.out.println("");
            System.out.println("MemoryFix only supports 1.8.x! Bye!");
            System.out.println("");
            System.out.println("***********************************");
            return null;
        }
        return new String[]{
                ClassTransformer.class.getName()
        };
    }

    @Override
    public String getModContainerClass() {
        if (!VersionUtil.is1_8_X()) {
            return null;
        }
        return MemoryFix.class.getName();
    }

    @Override
    public String getSetupClass() {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data) {
    }

    @Override
    public String getAccessTransformerClass() {
        return null;
    }
}
