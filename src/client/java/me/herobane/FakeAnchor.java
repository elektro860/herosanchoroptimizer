package me.herobane;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;

public class FakeAnchor extends Block {

    public FakeAnchor (Settings settings) {
        super(settings);
    }
    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return 1.0F;  // Make sure it doesn't block light
    }
}