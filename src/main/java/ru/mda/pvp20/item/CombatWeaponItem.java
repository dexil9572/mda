package ru.mda.pvp20.item;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** A durable melee weapon with one server-side PvP hit effect. */
public final class CombatWeaponItem extends Item {
    private final HitEffect hitEffect;

    public CombatWeaponItem(Properties properties, HitEffect hitEffect) {
        super(properties);
        this.hitEffect = hitEffect;
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (!attacker.level().isClientSide()) {
            switch (hitEffect) {
                case SCORCH -> target.igniteForSeconds(2.5F);
                case FROST -> apply(target, MobEffects.SLOWNESS, 80, 1);
                case VENOM -> apply(target, MobEffects.POISON, 100, 0);
                case SIPHON -> attacker.heal(2.0F);
                case STORM -> apply(target, MobEffects.LEVITATION, 24, 0);
                case RIFT -> apply(target, MobEffects.WEAKNESS, 80, 0);
                case BLOOD_RUSH -> apply(attacker, MobEffects.STRENGTH, 60, 0);
                case ANCHOR -> {
                    apply(target, MobEffects.SLOWNESS, 60, 0);
                    apply(target, MobEffects.WEAKNESS, 60, 0);
                }
            }
        }

        // Returning true tells vanilla to consume weapon durability for this hit.
        return true;
    }

    private static void apply(LivingEntity entity, Holder<MobEffect> effect, int duration, int amplifier) {
        entity.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }

    public enum HitEffect {
        SCORCH,
        FROST,
        VENOM,
        SIPHON,
        STORM,
        RIFT,
        BLOOD_RUSH,
        ANCHOR
    }
}
