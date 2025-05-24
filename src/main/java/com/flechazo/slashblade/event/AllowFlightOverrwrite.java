package com.flechazo.slashblade.event;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;

public class AllowFlightOverrwrite {

    private static final class SingletonHolder {
        private static final AllowFlightOverrwrite instance = new AllowFlightOverrwrite();
    }

    public static AllowFlightOverrwrite getInstance () {
        return AllowFlightOverrwrite.SingletonHolder.instance;
    }

    private AllowFlightOverrwrite () {
    }

    public void register () {
        ServerLifecycleEvents.SERVER_STARTING.register(this::enableFlight);
    }
    private void enableFlight (MinecraftServer server) {
        server.setFlightAllowed(true);
    }
}
