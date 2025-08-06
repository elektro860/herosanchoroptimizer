package me.herobane.block;

import java.util.function.Function;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModBlocks {
    // Existing block
    public static final Block FAKE_ANCHOR = register("fake_anchor", Block::new,
            Block.Settings.create().nonOpaque().strength(-1.0f)); // can't Break

    // Helper methods for adding blocks
    // copied from https://wiki.fabricmc.net/tutorial:blocks
    private static Block register(String path, Function<AbstractBlock.Settings, Block> factory,
            AbstractBlock.Settings settings) {
        final Identifier identifier = Identifier.of("tutorial", path);
        final RegistryKey<Block> registryKey = RegistryKey.of(RegistryKeys.BLOCK, identifier);

        final Block block = Blocks.register(registryKey, factory, settings);
        Items.register(block);
        return block;
    }

    // Force loads the class
    public static void registerModBlocks() {
    }
}
