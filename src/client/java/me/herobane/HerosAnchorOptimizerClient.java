package me.herobane;

import me.herobane.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HerosAnchorOptimizerClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("heros_anchor_optimizer");

	@Override
	public void onInitializeClient() {
		LOGGER.info("Hello HerobaneNair's Anchor Optimizer");

		// Register the Fake Anchor block for translucent rendering
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FAKE_ANCHOR, RenderLayer.getTranslucent());

		// Register an event for right-clicking a respawn anchor
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			// Check if the player is right-clicking a respawn anchor
			if (world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
				// Get charge level of the respawn anchor
				int charge = world.getBlockState(hitResult.getBlockPos()).get(RespawnAnchorBlock.CHARGES);
				boolean holdingGlowstone = player.getStackInHand(hand).isOf(Items.GLOWSTONE);

				// Check if anchor would explode in this dimension (not in Nether)
				String dimension = world.getRegistryKey().getValue().toString();
				boolean wouldExplode = !dimension.contains("_nether");

				// Check if the player is crouching (shift) or charging
				if (!player.isSneaking() && !holdingGlowstone) {
					// Charge is 0-3, not holding glowstone, and would explode in this dimension so like normal anchor
					if (charge >= 0 && charge <= 3 && wouldExplode) {
						placeClientSideFakeAnchor(world, hitResult);
						return ActionResult.SUCCESS;
					}

					// Charge is 4 (anchor fully charged) and would explode in the dimension (to fix edge case of if fully charged).
					if (charge == 4 && wouldExplode) {
						placeClientSideFakeAnchor(world, hitResult);
						return ActionResult.SUCCESS;
					}
				}

				return ActionResult.PASS; // Pass to allow default behavior (charging or exploding from server)
			}

			return ActionResult.PASS;
		});
	}

	private void placeClientSideFakeAnchor(World world, BlockHitResult hitResult) {
		BlockPos pos = hitResult.getBlockPos();

		// Replace the respawn anchor with a fake anchor block client-side
		world.setBlockState(pos, ModBlocks.FAKE_ANCHOR.getDefaultState());
	}
}
