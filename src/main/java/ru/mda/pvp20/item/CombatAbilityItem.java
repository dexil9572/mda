package ru.mda.pvp20.item;

import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** A reusable right-click ability balanced with a per-item cooldown. */
public final class CombatAbilityItem extends Item {
    private final Ability ability;
    private final int cooldownTicks;

    public CombatAbilityItem(Properties properties, Ability ability, int cooldownTicks) {
        super(properties);
        this.ability = ability;
        this.cooldownTicks = cooldownTicks;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (player.getCooldowns().isOnCooldown(this)) {
            return InteractionResult.FAIL;
        }

        if (!level.isClientSide()) {
            activate(level, player);
        }

        player.getCooldowns().addCooldown(this, cooldownTicks);
        return InteractionResult.SUCCESS;
    }

    private void activate(Level level, Player player) {
        switch (ability) {
            case WINDSTEP -> {
                Vec3 look = player.getLookAngle();
                player.push(look.x * 0.95, 0.28, look.z * 0.95);
                apply(player, MobEffects.SPEED, 100, 1);
            }
            case GUARDIAN -> apply(player, MobEffects.RESISTANCE, 120, 1);
            case BERSERKER -> apply(player, MobEffects.STRENGTH, 100, 1);
            case VITALITY -> {
                player.heal(6.0F);
                apply(player, MobEffects.REGENERATION, 100, 0);
            }
            case SMOKE_BOMB -> forEnemies(level, player, 5.0, enemy -> {
                apply(enemy, MobEffects.BLINDNESS, 50, 0);
                apply(enemy, MobEffects.SLOWNESS, 60, 0);
            });
            case FROST_NOVA -> forEnemies(level, player, 6.0, enemy -> {
                apply(enemy, MobEffects.SLOWNESS, 100, 1);
                apply(enemy, MobEffects.WEAKNESS, 60, 0);
            });
            case REPULSION -> forEnemies(level, player, 4.5, enemy -> pushAway(player, enemy, 1.35, 0.45));
            case HUNTERS_FLARE -> forEnemies(level, player, 14.0, enemy -> apply(enemy, MobEffects.GLOWING, 160, 0));
            case BLINK -> {
                Vec3 look = player.getLookAngle();
                player.push(-look.x * 1.25, 0.22, -look.z * 1.25);
                apply(player, MobEffects.INVISIBILITY, 30, 0);
                apply(player, MobEffects.SPEED, 50, 0);
            }
            case ADRENALINE -> {
                apply(player, MobEffects.SPEED, 100, 1);
                apply(player, MobEffects.STRENGTH, 60, 0);
            }
            case BINDING_SIGIL -> forEnemies(level, player, 5.0, enemy -> {
                apply(enemy, MobEffects.SLOWNESS, 140, 2);
                apply(enemy, MobEffects.WEAKNESS, 100, 0);
            });
            case ECHO_HORN -> {
                forEnemies(level, player, 7.0, enemy -> pushAway(player, enemy, 1.7, 0.55));
                apply(player, MobEffects.RESISTANCE, 60, 0);
            }
        }
    }

    private static void forEnemies(Level level, Player player, double radius, Consumer<LivingEntity> action) {
        AABB area = player.getBoundingBox().inflate(radius);
        double radiusSquared = radius * radius;

        for (LivingEntity enemy : level.getEntitiesOfClass(
                LivingEntity.class,
                area,
                entity -> entity != player && entity.isAlive() && !player.isAlliedTo(entity)
        )) {
            if (enemy.distanceToSqr(player) <= radiusSquared) {
                action.accept(enemy);
            }
        }
    }

    private static void pushAway(Player player, LivingEntity target, double horizontalStrength, double verticalStrength) {
        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        double distance = Math.max(Math.sqrt(dx * dx + dz * dz), 0.01);
        target.push(dx / distance * horizontalStrength, verticalStrength, dz / distance * horizontalStrength);
    }

    private static void apply(LivingEntity entity, Holder<MobEffect> effect, int duration, int amplifier) {
        entity.addEffect(new MobEffectInstance(effect, duration, amplifier));
    }

    public enum Ability {
        WINDSTEP,
        GUARDIAN,
        BERSERKER,
        VITALITY,
        SMOKE_BOMB,
        FROST_NOVA,
        REPULSION,
        HUNTERS_FLARE,
        BLINK,
        ADRENALINE,
        BINDING_SIGIL,
        ECHO_HORN
    }
}
