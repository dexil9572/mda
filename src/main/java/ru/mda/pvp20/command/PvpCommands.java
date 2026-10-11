package ru.mda.pvp20.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import ru.mda.pvp20.item.PvpItems;
import ru.mda.pvp20.structure.PvpStashStructure;

/** Operator commands for receiving the PvP kit and placing a hidden stash bunker. */
public final class PvpCommands {
    private PvpCommands() {
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, buildContext, selection) ->
                dispatcher.register(Commands.literal("pvp20")
                        .requires(source -> source.permissions().hasPermission(Permissions.COMMANDS_MODERATOR))
                        .executes(context -> showHelp(context.getSource()))
                        .then(Commands.literal("give")
                                .then(Commands.literal("all").executes(PvpCommands::giveAll))
                                .then(Commands.argument("item", StringArgumentType.word())
                                        .suggests(PvpCommands::suggestItems)
                                        .executes(PvpCommands::giveOne)))
                        .then(Commands.literal("stash").executes(PvpCommands::spawnStash))));
    }

    private static int showHelp(CommandSourceStack source) {
        source.sendSuccess(() -> Component.literal(
                "PVP20: /pvp20 give all, /pvp20 give <item_id>, /pvp20 stash"
        ), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int giveAll(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        for (Item item : PvpItems.ALL) {
            give(player, item);
        }

        context.getSource().sendSuccess(
                () -> Component.literal("Выдан полный PvP-набор: " + PvpItems.ALL.size() + " шт."),
                true
        );
        return PvpItems.ALL.size();
    }

    private static int giveOne(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        String itemId = StringArgumentType.getString(context, "item");
        Item item = PvpItems.BY_ID.get(itemId);

        if (item == null) {
            source.sendFailure(Component.literal("Неизвестный предмет: " + itemId));
            return 0;
        }

        give(player, item);
        source.sendSuccess(() -> Component.literal("Выдан предмет pvp20:" + itemId), true);
        return Command.SINGLE_SUCCESS;
    }

    private static CompletableFuture<Suggestions> suggestItems(
            CommandContext<CommandSourceStack> context,
            SuggestionsBuilder builder
    ) {
        PvpItems.BY_ID.keySet().stream().sorted().forEach(itemId -> builder.suggest(itemId));
        return builder.buildFuture();
    }

    private static int spawnStash(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CommandSourceStack source = context.getSource();
        ServerPlayer player = source.getPlayerOrException();
        BlockPos hatch = PvpStashStructure.place(source.getLevel(), player);

        if (hatch == null) {
            source.sendFailure(Component.literal("Не удалось разместить тайник здесь: слишком близко к нижней границе мира."));
            return 0;
        }

        source.sendSuccess(() -> Component.literal(
                "Подземный тайник построен. Входной люк: "
                        + hatch.getX() + " " + hatch.getY() + " " + hatch.getZ()
        ), true);
        return Command.SINGLE_SUCCESS;
    }

    private static void give(ServerPlayer player, Item item) {
        player.getInventory().placeItemBackInInventory(new ItemStack(item));
    }
}
