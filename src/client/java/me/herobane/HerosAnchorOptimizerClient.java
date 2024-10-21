package me.herobane;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.api.ClientModInitializer;

public class HerosAnchorOptimizerClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("heros_anchor_optimizer");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello HerobaneNair's Anchor Optimizer");
		AbstractBlock.Settings settings = AbstractBlock.Settings.create().noCollision().breakInstantly();
		FakeAnchor temp1 = new FakeAnchor(settings);
	}
}