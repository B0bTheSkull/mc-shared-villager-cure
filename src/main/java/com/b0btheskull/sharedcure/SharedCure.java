package com.b0btheskull.sharedcure;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SharedCure implements ModInitializer {
    private static final Logger LOGGER = LoggerFactory.getLogger("shared-villager-cure");

    @Override
    public void onInitialize() {
        LOGGER.info("Shared Villager Cure loaded — villager cure discounts are now shared by all players.");
    }
}
