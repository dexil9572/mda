package ru.mda.pvp20.itemgroup;

import net.fabricmc.fabric.api.creativetab.v1.FabricCreativeModeTab;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import ru.mda.pvp20.Pvp20;
import ru.mda.pvp20.item.PvpItems;

/** One creative tab containing the full 20-item PvP set. */
public final class PvpCreativeTab {
    private static final ResourceKey<CreativeModeTab> KEY = ResourceKey.create(
            BuiltInRegistries.CREATIVE_MODE_TAB.key(),
            Pvp20.id("arsenal")
    );

    private static final CreativeModeTab TAB = FabricCreativeModeTab.builder()
            .icon(() -> new ItemStack(PvpItems.EMBERFANG_BLADE))
            .title(Component.translatable("creativeTab.pvp20.arsenal"))
            .displayItems((parameters, output) -> PvpItems.ALL.forEach(output::accept))
            .build();

    private PvpCreativeTab() {
    }

    public static void initialize() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, KEY, TAB);
    }
}
