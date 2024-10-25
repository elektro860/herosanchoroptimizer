package me.herobane.event;

import me.herobane.block.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockExplosionHandler {

    // https://minecraft.wiki/w/Explosion#Calculating_which_blocks_to_destroy
    private static final float EXPLOSION_STRENGTH = 5.0F; // Respawn anchor explosion strength
    private static final int RADIUS = 3; // Radius within which to check for blocks

    public static void handleExplosion(World world, BlockPos anchorPos) {


        for (int x = -RADIUS; x <= RADIUS; x++) {
            for (int y = -1; y <= RADIUS; y++) {
                for (int z = -RADIUS; z <= RADIUS; z++) {
                    if (x==0 && y==0 && z == 0) continue;

                    BlockPos pos = anchorPos.add(x, y, z);
                    double distanceSquared = pos.getSquaredDistance(anchorPos);

                    // Check if the block is within the explosion radius
                    if (distanceSquared <= RADIUS * RADIUS) {
                        float intensity = (0.7F) * EXPLOSION_STRENGTH; //Remove the [0,0.6] cuz you don't want random
                        float stepSize = 0.3F; // Attenuation step size
                        float attenuation = 0.225F; // Intensity reduction per step

                        // Check each step along the ray
                        for (float step = 0; step < Math.sqrt(distanceSquared); step += stepSize) {
                            BlockPos stepPos = anchorPos.add(
                                    Math.round(x * (step / RADIUS)),
                                    Math.round(y * (step / RADIUS)),
                                    Math.round(z * (step / RADIUS))
                            );

                            BlockState blockState = world.getBlockState(stepPos);

                            // Check if the block is air; if so, skip intensity reduction
                            if (!blockState.isAir()) {
                                float blastResistance = blockState.getBlock().getBlastResistance();

                                // Reduce intensity based on attenuation and block resistance
                                intensity -= attenuation;
                                intensity -= (blastResistance + 0.3F) * 0.3F;
                            }

                            // If intensity is still greater than zero, replace the block
                            if (intensity >= 0 && isThiccEnough(world, stepPos)) {
                                world.setBlockState(stepPos, ModBlocks.FAKE_AIR.getDefaultState());
                            }

                            // If intensity drops to zero or below, stop processing this ray
                            if (intensity <= 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    // Helper method to check if a block is a full cube
    private static boolean isThiccEnough(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        // Check if the block is a full opaque cube and if it's a solid block (walkable)
        return state.isFullCube(world, pos);
    }
}
