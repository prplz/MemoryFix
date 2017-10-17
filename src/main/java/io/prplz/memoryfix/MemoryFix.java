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

import java.util.Collections;

public class MemoryFix extends DummyModContainer {

    public static final String MOD_ID = "memoryfix";
    public static final String MOD_NAME = "MemoryFix";
    public static final String VERSION = "0.1";
    private int messageDelay = 0;
    private IChatComponent updateMessage;

    public MemoryFix() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = MOD_ID;
        meta.name = MOD_NAME;
        meta.version = VERSION;
        meta.authorList = Collections.singletonList("prplz");
        meta.url = "https://prplz.io/memoryfix";
    }

    public void setUpdateMessage(IChatComponent updateMessage) {
        this.updateMessage = updateMessage;
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        bus.register(this);
        return true;
    }

    @Subscribe
    public void init(FMLInitializationEvent event) {
        // register the old event bus too, some versions use this (thanks sem)
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);

        new UpdateChecker(this).start();
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
