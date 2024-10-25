package me.herobane.block;

import me.herobane.HerosAnchorOptimizer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.block.ShapeContext;

public class ModBlocks {
    // Existing block
    public static final Block FAKE_ANCHOR = registerBlock("fake_anchor",
            new Block(AbstractBlock.Settings.create()
                    .nonOpaque() // Makes the block translucent
                    .noCollision() // Walk-through block like a fern
                    .strength(-1.0f) // Can't Break
                    .dropsNothing() // Does not drop items when broken
                    .replaceable() // Replaceable like a fern
                    .sounds(BlockSoundGroup.POWDER_SNOW)));

    // New block - Fake Air with full pass-through and no interaction
    public static final Block FAKE_AIR = registerBlock("fake_air",
            new FakeAirBlock(AbstractBlock.Settings.create()
                    .nonOpaque() // Block is transparent, allowing light through
                    .noCollision() // Walk-through block with no collision
                    .strength(-1.0f) // Unbreakable and explosion resistant
                    .dropsNothing() // Does not drop any items
                    .sounds(BlockSoundGroup.GLASS))); // Optional: set to sound like glass

    // Helper methods for adding blocks
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(HerosAnchorOptimizer.MOD_ID, name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(HerosAnchorOptimizer.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    //public static void registerModBlocks() {
    //    HerosAnchorOptimizer.LOGGER.info("Registering Mod Blocks for " + HerosAnchorOptimizer.MOD_ID);
    //}

    // Custom FakeAirBlock class to override interaction methods
    public static class FakeAirBlock extends Block {
        private static final VoxelShape EMPTY_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

        public FakeAirBlock(AbstractBlock.Settings settings) {
            super(settings);
        }

        @Override
        public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            return EMPTY_SHAPE;
        }

        @Override
        public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            return EMPTY_SHAPE;
        }
    }
}
