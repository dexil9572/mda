package ru.mda.pvp20.item;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.component.ItemLore;

/** Registry of all items in the duel arsenal. */
public final class PvpItems {
    public static final CombatWeaponItem EMBERFANG_BLADE = weapon(
            "emberfang_blade", CombatWeaponItem.HitEffect.SCORCH, 2.8F, -2.2F
    );
    public static final CombatWeaponItem GLACIAL_RAPIER = weapon(
            "glacial_rapier", CombatWeaponItem.HitEffect.FROST, 2.0F, -1.7F
    );
    public static final CombatWeaponItem VENOM_STILETTO = weapon(
            "venom_stiletto", CombatWeaponItem.HitEffect.VENOM, 1.6F, -1.5F
    );
    public static final CombatWeaponItem SIPHON_SCYTHE = weapon(
            "siphon_scythe", CombatWeaponItem.HitEffect.SIPHON, 2.4F, -2.5F
    );
    public static final CombatWeaponItem STORMBREAKER_HAMMER = weapon(
            "stormbreaker_hammer", CombatWeaponItem.HitEffect.STORM, 4.0F, -3.0F
    );
    public static final CombatWeaponItem RIFT_CLEAVER = weapon(
            "rift_cleaver", CombatWeaponItem.HitEffect.RIFT, 3.2F, -2.5F
    );
    public static final CombatWeaponItem BLOODRUSH_KATANA = weapon(
            "bloodrush_katana", CombatWeaponItem.HitEffect.BLOOD_RUSH, 2.3F, -1.9F
    );
    public static final CombatWeaponItem ANCHOR_MAUL = weapon(
            "anchor_maul", CombatWeaponItem.HitEffect.ANCHOR, 4.4F, -3.2F
    );

    public static final Item REINFORCED_DIAMOND_HELMET = armor(
            "reinforced_diamond_helmet", ArmorType.HELMET
    );
    public static final Item REINFORCED_DIAMOND_CHESTPLATE = armor(
            "reinforced_diamond_chestplate", ArmorType.CHESTPLATE
    );
    public static final Item REINFORCED_DIAMOND_LEGGINGS = armor(
            "reinforced_diamond_leggings", ArmorType.LEGGINGS
    );
    public static final Item REINFORCED_DIAMOND_BOOTS = armor(
            "reinforced_diamond_boots", ArmorType.BOOTS
    );

    public static final CombatAbilityItem WINDSTEP_CHARM = ability(
            "windstep_charm", CombatAbilityItem.Ability.WINDSTEP, 200
    );
    public static final CombatAbilityItem GUARDIAN_BREW = ability(
            "guardian_brew", CombatAbilityItem.Ability.GUARDIAN, 500
    );
    public static final CombatAbilityItem BERSERKER_SERUM = ability(
            "berserker_serum", CombatAbilityItem.Ability.BERSERKER, 600
    );
    public static final CombatAbilityItem VITALITY_ELIXIR = ability(
            "vitality_elixir", CombatAbilityItem.Ability.VITALITY, 400
    );
    public static final CombatAbilityItem SMOKE_BOMB = ability(
            "smoke_bomb", CombatAbilityItem.Ability.SMOKE_BOMB, 300
    );
    public static final CombatAbilityItem FROST_NOVA = ability(
            "frost_nova", CombatAbilityItem.Ability.FROST_NOVA, 400
    );
    public static final CombatAbilityItem REPULSION_ORB = ability(
            "repulsion_orb", CombatAbilityItem.Ability.REPULSION, 280
    );
    public static final CombatAbilityItem HUNTERS_FLARE = ability(
            "hunters_flare", CombatAbilityItem.Ability.HUNTERS_FLARE, 240
    );
    public static final CombatAbilityItem BLINK_RUNE = ability(
            "blink_rune", CombatAbilityItem.Ability.BLINK, 260
    );
    public static final CombatAbilityItem ADRENALINE_SHOT = ability(
            "adrenaline_shot", CombatAbilityItem.Ability.ADRENALINE, 450
    );
    public static final CombatAbilityItem BINDING_SIGIL = ability(
            "binding_sigil", CombatAbilityItem.Ability.BINDING_SIGIL, 320
    );
    public static final CombatAbilityItem ECHO_HORN = ability(
            "echo_horn", CombatAbilityItem.Ability.ECHO_HORN, 500
    );

    public static final List<Item> ALL = List.of(
            EMBERFANG_BLADE,
            GLACIAL_RAPIER,
            VENOM_STILETTO,
            SIPHON_SCYTHE,
            STORMBREAKER_HAMMER,
            RIFT_CLEAVER,
            BLOODRUSH_KATANA,
            ANCHOR_MAUL,
            REINFORCED_DIAMOND_HELMET,
            REINFORCED_DIAMOND_CHESTPLATE,
            REINFORCED_DIAMOND_LEGGINGS,
            REINFORCED_DIAMOND_BOOTS,
            WINDSTEP_CHARM,
            GUARDIAN_BREW,
            BERSERKER_SERUM,
            VITALITY_ELIXIR,
            SMOKE_BOMB,
            FROST_NOVA,
            REPULSION_ORB,
            HUNTERS_FLARE,
            BLINK_RUNE,
            ADRENALINE_SHOT,
            BINDING_SIGIL,
            ECHO_HORN
    );

    public static final Map<String, Item> BY_ID = Map.ofEntries(
            Map.entry("emberfang_blade", EMBERFANG_BLADE),
            Map.entry("glacial_rapier", GLACIAL_RAPIER),
            Map.entry("venom_stiletto", VENOM_STILETTO),
            Map.entry("siphon_scythe", SIPHON_SCYTHE),
            Map.entry("stormbreaker_hammer", STORMBREAKER_HAMMER),
            Map.entry("rift_cleaver", RIFT_CLEAVER),
            Map.entry("bloodrush_katana", BLOODRUSH_KATANA),
            Map.entry("anchor_maul", ANCHOR_MAUL),
            Map.entry("reinforced_diamond_helmet", REINFORCED_DIAMOND_HELMET),
            Map.entry("reinforced_diamond_chestplate", REINFORCED_DIAMOND_CHESTPLATE),
            Map.entry("reinforced_diamond_leggings", REINFORCED_DIAMOND_LEGGINGS),
            Map.entry("reinforced_diamond_boots", REINFORCED_DIAMOND_BOOTS),
            Map.entry("windstep_charm", WINDSTEP_CHARM),
            Map.entry("guardian_brew", GUARDIAN_BREW),
            Map.entry("berserker_serum", BERSERKER_SERUM),
            Map.entry("vitality_elixir", VITALITY_ELIXIR),
            Map.entry("smoke_bomb", SMOKE_BOMB),
            Map.entry("frost_nova", FROST_NOVA),
            Map.entry("repulsion_orb", REPULSION_ORB),
            Map.entry("hunters_flare", HUNTERS_FLARE),
            Map.entry("blink_rune", BLINK_RUNE),
            Map.entry("adrenaline_shot", ADRENALINE_SHOT),
            Map.entry("binding_sigil", BINDING_SIGIL),
            Map.entry("echo_horn", ECHO_HORN)
    );

    private PvpItems() {
    }

    public static void initialize() {
        // Calling this method forces class initialization before the creative tab is built.
    }

    private static CombatWeaponItem weapon(String id, CombatWeaponItem.HitEffect effect, float damage, float speed) {
        return register(
                id,
                properties -> new CombatWeaponItem(properties, effect),
                withLore(id, new Item.Properties().sword(ToolMaterial.DIAMOND, damage, speed))
        );
    }

    private static CombatAbilityItem ability(String id, CombatAbilityItem.Ability ability, int cooldownTicks) {
        return register(
                id,
                properties -> new CombatAbilityItem(properties, ability, cooldownTicks),
                withLore(id, new Item.Properties().stacksTo(1))
        );
    }

    private static Item armor(String id, ArmorType type) {
        return register(
                id,
                Item::new,
                withLore(
                        id,
                        new Item.Properties()
                                .humanoidArmor(PvpArmorMaterial.REINFORCED_DIAMOND, type)
                                .durability(type.getDurability(PvpArmorMaterial.BASE_DURABILITY))
                )
        );
    }

    private static Item.Properties withLore(String id, Item.Properties properties) {
        return properties.component(
                DataComponents.LORE,
                new ItemLore(List.of(Component.translatable("tooltip.pvp20." + id)))
        );
    }

    private static <T extends Item> T register(
            String name,
            Function<Item.Properties, T> itemFactory,
            Item.Properties properties
    ) {
        ResourceKey<Item> itemKey = ModItemIds.create(name);
        T item = itemFactory.apply(properties.setId(itemKey));
        Registry.register(BuiltInRegistries.ITEM, itemKey, item);
        return item;
    }
}
