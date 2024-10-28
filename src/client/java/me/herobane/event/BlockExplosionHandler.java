//Currently unused, ignore for now - just having a place to save it



package me.herobane.event;

import me.herobane.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockExplosionHandler {

    // https://minecraft.wiki/w/Explosion#Calculating_which_blocks_to_destroy
    private static final float EXPLOSION_STRENGTH = 5.0F; // Respawn anchor explosion strength
    private static final int RADIUS = 2; // Radius within which to check for blocks

    // Paths defined from your provided data (full data should be populated)
    private static final int[][][] EXPLOSION_PATHS = {
            {{-2, -2, -2}, {-1, -1, -1}},
            {{-2, -2, -1}, {-1, -1, -1}, {-1, -1, 0}},
            {{-2, -2, 0}, {-1, -1, 0}},
            {{-2, -2, 1}, {-1, -1, 1}, {-1, -1, 0}},
            {{-2, -2, 2}, {-1, -1, 1}},
            {{-2, -1, -2}, {-1, -1, -1}, {-1, 0, -1}},
            {{-2, -1, -1}, {-1, -1, -1}, {-1, 0, 0}},
            {{-2, -1, 0}, {-1, -1, 0}, {-1, 0, 0}},
            {{-2, -1, 1}, {-1, -1, 1}, {-1, 0, 0}},
            {{-2, -1, 2}, {-1, -1, 1}, {-1, 0, 1}},
            {{-2, 0, -2}, {-1, 0, -1}},
            {{-2, 0, -1}, {-1, 0, -1}, {-1, 0, 0}},
            {{-2, 0, 0}, {-1, 0, 0}},
            {{-2, 0, 1}, {-1, 0, 1}, {-1, 0, 0}},
            {{-2, 0, 2}, {-1, 0, 1}},
            {{-2, 1, -2}, {-1, 1, -1}, {-1, 0, -1}},
            {{-2, 1, -1}, {-1, 1, -1}, {-1, 0, 0}},
            {{-2, 1, 0}, {-1, 1, 0}, {-1, 0, 0}},
            {{-2, 1, 1}, {-1, 1, 1}, {-1, 0, 0}},
            {{-2, 1, 2}, {-1, 1, 1}, {-1, 0, 1}},
            {{-2, 2, -2}, {-1, 1, -1}},
            {{-2, 2, -1}, {-1, 1, -1}, {-1, 1, 0}},
            {{-2, 2, 0}, {-1, 1, 0}},
            {{-2, 2, 1}, {-1, 1, 1}, {-1, 1, 0}},
            {{-2, 2, 2}, {-1, 1, 1}},
            {{-1, -2, -2}, {-1, -1, -1}, {0, -1, -1}},
            {{-1, -2, -1}, {-1, -1, -1}, {0, -1, 0}},
            {{-1, -2, 0}, {-1, -1, 0}, {0, -1, 0}},
            {{-1, -2, 1}, {-1, -1, 1}, {0, -1, 0}},
            {{-1, -2, 2}, {-1, -1, 1}, {0, -1, 1}},
            {{-1, -1, -2}, {-1, -1, -1}, {0, 0, -1}},
            {{-1, -1, 2}, {-1, -1, 1}, {0, 0, 1}},
            {{-1, 0, -2}, {-1, 0, -1}, {0, 0, -1}},
            {{-1, 0, 2}, {-1, 0, 1}, {0, 0, 1}},
            {{-1, 1, -2}, {-1, 1, -1}, {0, 0, -1}},
            {{-1, 1, 2}, {-1, 1, 1}, {0, 0, 1}},
            {{-1, 2, -2}, {-1, 1, -1}, {0, 1, -1}},
            {{-1, 2, -1}, {-1, 1, -1}, {0, 1, 0}},
            {{-1, 2, 0}, {-1, 1, 0}, {0, 1, 0}},
            {{-1, 2, 1}, {-1, 1, 1}, {0, 1, 0}},
            {{-1, 2, 2}, {-1, 1, 1}, {0, 1, 1}},
            {{0, -2, -2}, {0, -1, -1}},
            {{0, -2, -1}, {0, -1, -1}, {0, -1, 0}},
            {{0, -2, 0}, {0, -1, 0}},
            {{0, -2, 1}, {0, -1, 1}, {0, -1, 0}},
            {{0, -2, 2}, {0, -1, 1}},
            {{0, -1, -2}, {0, -1, -1}, {0, 0, -1}},
            {{0, -1, 2}, {0, -1, 1}, {0, 0, 1}},
            {{0, 0, -2}, {0, 0, -1}},
            {{0, 0, 2}, {0, 0, 1}},
            {{0, 1, -2}, {0, 1, -1}, {0, 0, -1}},
            {{0, 1, 2}, {0, 1, 1}, {0, 0, 1}},
            {{0, 2, -2}, {0, 1, -1}},
            {{0, 2, -1}, {0, 1, -1}, {0, 1, 0}},
            {{0, 2, 0}, {0, 1, 0}},
            {{0, 2, 1}, {0, 1, 1}, {0, 1, 0}},
            {{0, 2, 2}, {0, 1, 1}},
            {{1, -2, -2}, {1, -1, -1}, {0, -1, -1}},
            {{1, -2, -1}, {1, -1, -1}, {0, -1, 0}},
            {{1, -2, 0}, {1, -1, 0}, {0, -1, 0}},
            {{1, -2, 1}, {1, -1, 1}, {0, -1, 0}},
            {{1, -2, 2}, {1, -1, 1}, {0, -1, 1}},
            {{1, -1, -2}, {1, -1, -1}, {0, 0, -1}},
            {{1, -1, 2}, {1, -1, 1}, {0, 0, 1}},
            {{1, 0, -2}, {1, 0, -1}, {0, 0, -1}},
            {{1, 0, 2}, {1, 0, 1}, {0, 0, 1}},
            {{1, 1, -2}, {1, 1, -1}, {0, 0, -1}},
            {{1, 1, 2}, {1, 1, 1}, {0, 0, 1}},
            {{1, 2, -2}, {1, 1, -1}, {0, 1, -1}},
            {{1, 2, -1}, {1, 1, -1}, {0, 1, 0}},
            {{1, 2, 0}, {1, 1, 0}, {0, 1, 0}},
            {{1, 2, 1}, {1, 1, 1}, {0, 1, 0}},
            {{1, 2, 2}, {1, 1, 1}, {0, 1, 1}},
            {{2, -2, -2}, {1, -1, -1}},
            {{2, -2, -1}, {1, -1, -1}, {1, -1, 0}},
            {{2, -2, 0}, {1, -1, 0}},
            {{2, -2, 1}, {1, -1, 1}, {1, -1, 0}},
            {{2, -2, 2}, {1, -1, 1}},
            {{2, -1, -2}, {1, -1, -1}, {1, 0, -1}},
            {{2, -1, -1}, {1, -1, -1}, {1, 0, 0}},
            {{2, -1, 0}, {1, -1, 0}, {1, 0, 0}},
            {{2, -1, 1}, {1, -1, 1}, {1, 0, 0}},
            {{2, -1, 2}, {1, -1, 1}, {1, 0, 1}},
            {{2, 0, -2}, {1, 0, -1}},
            {{2, 0, -1}, {1, 0, -1}, {1, 0, 0}},
            {{2, 0, 0}, {1, 0, 0}},
            {{2, 0, 1}, {1, 0, 1}, {1, 0, 0}},
            {{2, 0, 2}, {1, 0, 1}},
            {{2, 1, -2}, {1, 1, -1}, {1, 0, -1}},
            {{2, 1, -1}, {1, 1, -1}, {1, 0, 0}},
            {{2, 1, 0}, {1, 1, 0}, {1, 0, 0}},
            {{2, 1, 1}, {1, 1, 1}, {1, 0, 0}},
            {{2, 1, 2}, {1, 1, 1}, {1, 0, 1}},
            {{2, 2, -2}, {1, 1, -1}},
            {{2, 2, -1}, {1, 1, -1}, {1, 1, 0}},
            {{2, 2, 0}, {1, 1, 0}},
            {{2, 2, 1}, {1, 1, 1}, {1, 1, 0}},
            {{2, 2, 2}, {1, 1, 1}}
    };


    public static void handleExplosion(World world, BlockPos anchorPos) {
        double[] explosivity = new double[124];
        BlockState currentBlock;
        int c = 0;

        // First pass: Collect blast resistance values and set to FAKE_AIR if needed
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    if (Math.abs(x) == 1 || Math.abs(y) == 1 || Math.abs(z) == 1) {
                        BlockPos checkPos = anchorPos.add(x, y, z);
                        currentBlock = world.getBlockState(checkPos);
                        double resistance = currentBlock.getBlock().getBlastResistance();
                        explosivity[c] = resistance;

                        // If resistance < 4 and block isn't air, set to FAKE_AIR
                        if (resistance < 4 && currentBlock.getBlock() != Blocks.AIR) {
                            world.setBlockState(checkPos, ModBlocks.FAKE_AIR.getDefaultState());
                        }
                        c++;
                    }
                }
            }
        }

        // Second pass: Check paths for blast resistance >= 4, otherwise continue
        for (int[][] path : EXPLOSION_PATHS) {
            boolean pathValid = true;  // Track if all blocks in the path meet the resistance condition
            double minBlastResistance = Double.MAX_VALUE;

            for (int[] offset : path) {
                BlockPos checkPos = anchorPos.add(offset[0], offset[1], offset[2]);
                currentBlock = world.getBlockState(checkPos);
                double resistance = currentBlock.getBlock().getBlastResistance();

                // Track the minimum blast resistance in the path
                if (resistance < minBlastResistance) {
                    minBlastResistance = resistance;
                }

                // Check if resistance is >= 4
                if (resistance < 4) {
                    pathValid = false;  // Mark path as invalid if resistance < 4
                    break; // No need to check further positions in this path
                }
            }

            if (pathValid) {
                explosivity[c] = -1;
                c++;
                continue; // Proceed to the next path if this path is valid
            }

            // Calculate threshold for setting blocks to FAKE_AIR in this path
            double threshold = 3.7 - minBlastResistance;

            // Apply FAKE_AIR to blocks in path with resistance below threshold
            for (int[] offset : path) {
                BlockPos checkPos = anchorPos.add(offset[0], offset[1], offset[2]);
                currentBlock = world.getBlockState(checkPos);
                double resistance = currentBlock.getBlock().getBlastResistance();

                // Set to FAKE_AIR if resistance is below the calculated threshold and not already air
                if (resistance < threshold && currentBlock.getBlock() != Blocks.AIR) {
                    world.setBlockState(checkPos, ModBlocks.FAKE_AIR.getDefaultState());
                }
            }
        }
    }
}
