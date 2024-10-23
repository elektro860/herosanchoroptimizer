package me.herobane;

import me.herobane.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HerosAnchorOptimizerClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("heros_anchor_optimizer");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello HerobaneNair's Anchor Optimizer");

		// Register the Fake Anchor block for translucent rendering
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FAKE_ANCHOR, RenderLayer.getTranslucent());
	}
}
