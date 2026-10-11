package ru.mda.pvp20.item;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import ru.mda.pvp20.Pvp20;

/** Resource keys are kept separate from item instances, following the 26.2 registry pattern. */
public final class ModItemIds {
    private ModItemIds() {
    }

    public static ResourceKey<Item> create(String name) {
        return ResourceKey.create(Registries.ITEM, Pvp20.id(name));
    }
}
