package io.prplz.memoryfix;

public class VersionUtil {

    private static final String minecraftVersion;

    static {
        String mcVer;
        try {
            mcVer = (String) Class.forName("net.minecraftforge.common.ForgeVersion").getField("mcVersion").get(null);
        } catch (NoSuchFieldException ex) {
            mcVer = "1.8";
        }
        catch (Exception ex) {
            System.out.println("Failed to detect forge version:");
            ex.printStackTrace();
            mcVer = "unknown";
        }
        minecraftVersion = mcVer;
    }

    public static String getMinecraftVersion() {
        return minecraftVersion;
    }

    public static boolean is1_8_X() {
        return minecraftVersion.equals("1.8") || minecraftVersion.startsWith("1.8.");
    }
}
