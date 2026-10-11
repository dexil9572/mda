package ru.mda.pvp20;

import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.mda.pvp20.command.PvpCommands;
import ru.mda.pvp20.item.PvpItems;
import ru.mda.pvp20.itemgroup.PvpCreativeTab;

public final class Pvp20 implements ModInitializer {
    public static final String MOD_ID = "pvp20";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        PvpItems.initialize();
        PvpCreativeTab.initialize();
        PvpCommands.register();
        LOGGER.info("Registered the PvP arsenal, commands, and stash builder.");
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
