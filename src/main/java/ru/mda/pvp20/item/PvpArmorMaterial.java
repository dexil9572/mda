package ru.mda.pvp20.item;

import java.util.Map;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.EquipmentAssets;

/** Armor stats exceed diamond while intentionally pointing at Minecraft's diamond equipment asset. */
public final class PvpArmorMaterial {
    public static final int BASE_DURABILITY = 45;

    private static final TagKey<Item> REPAIR_ITEMS = TagKey.create(
            BuiltInRegistries.ITEM.key(),
            Identifier.fromNamespaceAndPath("minecraft", "repairs_diamond_armor")
    );
    private static final ResourceKey<EquipmentAsset> VANILLA_DIAMOND_ASSET = ResourceKey.create(
            EquipmentAssets.ROOT_ID,
            Identifier.fromNamespaceAndPath("minecraft", "diamond")
    );

    public static final ArmorMaterial REINFORCED_DIAMOND = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    ArmorType.HELMET, 4,
                    ArmorType.CHESTPLATE, 9,
                    ArmorType.LEGGINGS, 7,
                    ArmorType.BOOTS, 4
            ),
            12,
            SoundEvents.ARMOR_EQUIP_DIAMOND,
            3.0F,
            0.1F,
            REPAIR_ITEMS,
            VANILLA_DIAMOND_ASSET
    );

    private PvpArmorMaterial() {
    }
}
