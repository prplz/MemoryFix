package io.prplz.memoryfix;

public class VersionUtil {

    public static String getMinecraftVersion() {
        try {
            return (String) Class.forName("net.minecraftforge.common.MinecraftForge").getField("MC_VERSION").get(null);
        } catch (Exception ex) {
            System.out.println("Failed to detect forge version:");
            ex.printStackTrace();
            return "unknown";
        }
    }

    public static boolean is1_8_X() {
        String ver = getMinecraftVersion();
        return ver.equals("1.8") || ver.startsWith("1.8.");
    }
}
