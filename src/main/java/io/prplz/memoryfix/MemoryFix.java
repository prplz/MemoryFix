package io.prplz.memoryfix;

import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static net.minecraftforge.fml.common.Mod.EventHandler;

@Mod(modid = "memoryfix", useMetadata = true)
public class MemoryFix {

    private int messageDelay = 0;
    private IChatComponent updateMessage;

    @EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        String updateUrl = System.getProperty("memoryfix.updateurl", "%%UPDATE_URL%%");
        UpdateChecker updater = new UpdateChecker(updateUrl, res -> updateMessage = res.getUpdateMessage());
        updater.start();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (updateMessage != null && Minecraft.getMinecraft().thePlayer != null) {
            if (++messageDelay == 80) {
                Minecraft.getMinecraft().thePlayer.addChatMessage(updateMessage);
                updateMessage = null;
            }
        }
    }
}
