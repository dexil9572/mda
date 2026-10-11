package ru.mda.pvp20.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LadderBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import ru.mda.pvp20.item.PvpItems;

/** Builds a small concealed underground bunker with a ladder hatch and a stocked chest. */
public final class PvpStashStructure {
    private static final int ROOM_RADIUS = 4;
    private static final int ROOM_DEPTH = 10;
    private static final int HATCH_DISTANCE = 5;

    private PvpStashStructure() {
    }

    /**
     * Builds the stash a few blocks in front of the player and returns its surface hatch.
     * Returns {@code null} if there is not enough vertical space below the chosen surface.
     */
    public static BlockPos place(ServerLevel level, ServerPlayer player) {
        BlockPos column = player.blockPosition().relative(player.getDirection(), HATCH_DISTANCE);
        int hatchY = findSurfaceBlockY(level, column);
        if (hatchY < level.getMinY() + ROOM_DEPTH + 2) {
            return null;
        }

        int centerX = column.getX();
        int centerZ = column.getZ();
        int floorY = hatchY - ROOM_DEPTH;
        buildRoom(level, centerX, centerZ, floorY);
        buildLadderShaft(level, centerX, centerZ, floorY, hatchY);
        BlockPos hatchPos = new BlockPos(centerX, hatchY, centerZ);
        level.setBlockAndUpdate(
                hatchPos,
                Blocks.OAK_TRAPDOOR.defaultBlockState().setValue(TrapDoorBlock.FACING, Direction.NORTH)
        );
        addLighting(level, centerX, centerZ, floorY);
        addStockedChest(level, centerX - 2, centerZ - 2, floorY + 1);
        return hatchPos;
    }

    private static int findSurfaceBlockY(ServerLevel level, BlockPos column) {
        int y = column.getY();
        int minY = level.getMinY() + ROOM_DEPTH + 2;

        while (y > minY) {
            BlockPos checkPos = new BlockPos(column.getX(), y, column.getZ());
            if (level.getBlockState(checkPos).blocksMotion()) {
                return y;
            }
            y--;
        }

        return Integer.MIN_VALUE;
    }

    private static void buildRoom(ServerLevel level, int centerX, int centerZ, int floorY) {
        for (int dx = -ROOM_RADIUS; dx <= ROOM_RADIUS; dx++) {
            for (int dz = -ROOM_RADIUS; dz <= ROOM_RADIUS; dz++) {
                boolean boundary = Math.abs(dx) == ROOM_RADIUS || Math.abs(dz) == ROOM_RADIUS;
                set(level, centerX + dx, floorY, centerZ + dz, Blocks.DEEPSLATE_TILES.defaultBlockState());

                for (int dy = 1; dy <= 3; dy++) {
                    set(
                            level,
                            centerX + dx,
                            floorY + dy,
                            centerZ + dz,
                            boundary ? Blocks.DEEPSLATE_BRICKS.defaultBlockState() : Blocks.AIR.defaultBlockState()
                    );
                }

                // Keep a one-block opening in the center of the roof for the ladder shaft.
                if (dx != 0 || dz != 0) {
                    set(level, centerX + dx, floorY + 4, centerZ + dz, Blocks.DEEPSLATE_BRICKS.defaultBlockState());
                }
            }
        }
    }

    private static void buildLadderShaft(ServerLevel level, int centerX, int centerZ, int floorY, int hatchY) {
        var shaftWall = Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState();
        var ladder = Blocks.LADDER.defaultBlockState().setValue(LadderBlock.FACING, Direction.NORTH);

        for (int y = floorY + 1; y < hatchY; y++) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dz = -1; dz <= 1; dz++) {
                    if (Math.max(Math.abs(dx), Math.abs(dz)) != 1) {
                        continue;
                    }

                    // Open the north side at room level so the ladder can be entered from inside.
                    boolean roomEntrance = y <= floorY + 3 && dx == 0 && dz == -1;
                    if (!roomEntrance) {
                        set(level, centerX + dx, y, centerZ + dz, shaftWall);
                    }
                }
            }

            set(level, centerX, y, centerZ, ladder);
        }
    }

    private static void addLighting(ServerLevel level, int centerX, int centerZ, int floorY) {
        var lantern = Blocks.SOUL_LANTERN.defaultBlockState();
        set(level, centerX - 3, floorY + 1, centerZ - 3, lantern);
        set(level, centerX + 3, floorY + 1, centerZ - 3, lantern);
        set(level, centerX - 3, floorY + 1, centerZ + 3, lantern);
        set(level, centerX + 3, floorY + 1, centerZ + 3, lantern);
    }

    private static void addStockedChest(ServerLevel level, int x, int z, int y) {
        BlockPos chestPos = new BlockPos(x, y, z);
        level.setBlockAndUpdate(chestPos, Blocks.CHEST.defaultBlockState());

        if (level.getBlockEntity(chestPos) instanceof ChestBlockEntity chest) {
            for (int slot = 0; slot < PvpItems.ALL.size(); slot++) {
                chest.setItem(slot, new ItemStack(PvpItems.ALL.get(slot)));
            }
            chest.setChanged();
        }
    }

    private static void set(ServerLevel level, int x, int y, int z, BlockState state) {
        level.setBlockAndUpdate(new BlockPos(x, y, z), state);
    }
}
