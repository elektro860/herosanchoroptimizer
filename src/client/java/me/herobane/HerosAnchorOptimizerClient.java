package me.herobane;
import me.herobane.block.ModBlocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ClientModInitializer;

public class HerosAnchorOptimizerClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("heros_anchor_optimizer");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello HerobaneNair's Anchor Optimizer");

		ModBlocks.registerModBlocks();
	}
}