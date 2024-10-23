package me.herobane.block;

        import me.herobane.HerosAnchorOptimizer;
        import net.minecraft.block.AbstractBlock;
        import net.minecraft.block.Block;
        import net.minecraft.item.BlockItem;
        import net.minecraft.item.Item;
        import net.minecraft.registry.Registries;
        import net.minecraft.registry.Registry;
        import net.minecraft.sound.BlockSoundGroup;
        import net.minecraft.util.Identifier;


public class ModBlocks {
    public static final Block FAKE_ANCHOR = registerBlock("fake_anchor",
            new Block(AbstractBlock.Settings.create()
                    .nonOpaque() // Makes the block translucent
                    .noCollision() // Walk-through block like a fern
                    .strength(-1.0f) // Can't Break
                    .dropsNothing() // Does not drop items when broken
                    .replaceable() // Replaceable like a fern
                    .sounds(BlockSoundGroup.POWDER_SNOW)));
    //Helper methods for adding blocks
    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(HerosAnchorOptimizer.MOD_ID, name), block);
    }
    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(HerosAnchorOptimizer.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }
    public static void registerModBlocks() {
        HerosAnchorOptimizer.LOGGER.info("Registering Mod Blocks for " + HerosAnchorOptimizer.MOD_ID);
    }
}

