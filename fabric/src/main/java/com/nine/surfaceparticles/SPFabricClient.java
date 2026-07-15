package com.nine.surfaceparticles;

import net.fabricmc.api.ClientModInitializer;

public class SPFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        SPCommon.init();
    }

}
