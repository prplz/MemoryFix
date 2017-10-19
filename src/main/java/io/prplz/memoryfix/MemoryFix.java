package io.prplz.memoryfix;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import net.minecraft.client.Minecraft;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Properties;

public class MemoryFix extends DummyModContainer {

    private final String modId = "memoryfix";
    private final String modName = "MemoryFix";
    private final String version;
    private final String updateUrl;
    private int messageDelay = 0;
    private IChatComponent updateMessage;

    public MemoryFix() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = modId;
        meta.name = modName;
        Properties properties = new Properties();
        try (InputStream in = MemoryFix.class.getClassLoader().getResourceAsStream("version.properties")) {
            properties.load(in);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        version = properties.getProperty("version");
        updateUrl = properties.getProperty("updateUrl") + modName + "/" + version;
        meta.version = version;
        meta.authorList = Collections.singletonList("prplz");
        meta.url = "https://prplz.io/memoryfix";
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @SuppressWarnings("deprecation")
    @Subscribe
    public void init(FMLInitializationEvent event) {
        // register the old event bus too, some versions use this (thanks sem)
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

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
