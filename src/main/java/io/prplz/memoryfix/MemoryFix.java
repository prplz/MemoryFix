package io.prplz.memoryfix;

import com.google.common.eventbus.EventBus;
import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;

import java.util.Collections;

public class MemoryFix extends DummyModContainer {

    public static final String MOD_ID = "memoryfix";
    public static final String MOD_NAME = "MemoryFix";
    public static final String VERSION = "0.1";

    public MemoryFix() {
        super(new ModMetadata());
        ModMetadata meta = getMetadata();
        meta.modId = MOD_ID;
        meta.name = MOD_NAME;
        meta.version = VERSION;
        meta.authorList = Collections.singletonList("prplz");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller) {
        return true;
    }
}
