	package me.herobane;
	import me.herobane.event.BlockExplosionHandler;
	import me.herobane.block.ModBlocks;
	import net.fabricmc.api.ClientModInitializer;
	import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
	import net.fabricmc.fabric.api.event.player.UseBlockCallback;
	import net.minecraft.block.Blocks;
	import net.minecraft.block.RespawnAnchorBlock;
	import net.minecraft.client.MinecraftClient;
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
		public void onInitializeClient() { // Ensure this method name matches ClientModInitializer ok mr fabric
			LOGGER.info("Hello HerobaneNair's Anchor Optimizer");

			// Register the Fake Anchor block for translucent rendering
			BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FAKE_ANCHOR, RenderLayer.getTranslucent());

			// Register an event for right-clicking a respawn anchor
			UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
				// Check if the player is right-clicking a respawn anchor
				if (world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {
					boolean isSingleplayer = MinecraftClient.getInstance().isInSingleplayer();
					// Get the charge level of the respawn anchor
					int charge = world.getBlockState(hitResult.getBlockPos()).get(RespawnAnchorBlock.CHARGES);
					boolean holdingGlowstone = player.getStackInHand(hand).isOf(Items.GLOWSTONE);

					// Check if the anchor would explode in this dimension (not in Nether)
					String dimension = world.getRegistryKey().getValue().toString();
					boolean wouldExplode = !dimension.contains("_nether");
					// Check if the player is crouching (shift) or charging
					if (!player.isSneaking() && !holdingGlowstone && !isSingleplayer) {
						// Charge is 1-3, not holding glowstone, and would explode in this dimension
						if (charge >= 1 && charge <= 3 && wouldExplode) {
							placeClientSideFakeAnchor(world, hitResult);
							BlockExplosionHandler.handleExplosion(world, hitResult.getBlockPos());
							return ActionResult.SUCCESS;
						}

						// Charge is 4 (anchor full), and would explode in this dimension
						if (charge == 4 && wouldExplode) {
							placeClientSideFakeAnchor(world, hitResult);
							BlockExplosionHandler.handleExplosion(world, hitResult.getBlockPos());
							return ActionResult.SUCCESS;
						}
					}

					return ActionResult.PASS; // Pass to allow default behavior (charging or exploding)
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
