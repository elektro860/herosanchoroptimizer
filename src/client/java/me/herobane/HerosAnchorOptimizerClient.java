package me.herobane;

//import me.herobane.event.BlockExplosionHandler;
import me.herobane.block.ModBlocks;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.Blocks;
import net.minecraft.block.RespawnAnchorBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class HerosAnchorOptimizerClient implements ClientModInitializer {
	public static final Logger LOGGER = LoggerFactory.getLogger("heros_anchor_optimizer");
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	@Override
	public void onInitializeClient() {

        LOGGER.info("Hello HerobaneNair's Anchor Optimizer");

		// Set block render layers for custom blocks
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FAKE_ANCHOR, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.GHOST_ANCHOR, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.FAKE_AIR, RenderLayer.getTranslucent());

		// Register an event for right-clicking a respawn anchor
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			if (world.isClient() && world.getBlockState(hitResult.getBlockPos()).isOf(Blocks.RESPAWN_ANCHOR)) {


				boolean isSingleplayer = MinecraftClient.getInstance().isInSingleplayer();
				int charge = world.getBlockState(hitResult.getBlockPos()).get(RespawnAnchorBlock.CHARGES);
				boolean holdingGlowstoneMainHand = player.getStackInHand(hand).isOf(Items.GLOWSTONE);
				boolean holdingGlowstoneOffHand = player.getOffHandStack().isOf(Items.GLOWSTONE);
				String dimension = world.getRegistryKey().getValue().toString();
				boolean wouldExplode = !dimension.contains("_nether");

				if (!player.isSneaking() && !holdingGlowstoneMainHand && !holdingGlowstoneOffHand && !isSingleplayer) {
					int ping;
					MinecraftClient client = MinecraftClient.getInstance();
					//MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(MinecraftClient.getInstance().player.getUuid()).getLatency();
					if (client.player != null) {
						PlayerListEntry entry = Objects.requireNonNull(client.getNetworkHandler()).getPlayerListEntry(client.player.getUuid());
						// If entry is null, set clientPing to 100, else get the latency
						ping = (entry != null) ? entry.getLatency() : 100;
					} else {
						ping = 100;
					}
					if ((charge >= 1 && charge <= 3 && wouldExplode) || (charge == 4 && wouldExplode)) {
						placeClientSideFakeAnchor(world, hitResult);
						//BlockExplosionHandler.handleExplosion(world, hitResult.getBlockPos());

						// Retrieve the player's ping from PlayerListEntry
						PlayerListEntry playerListEntry = Objects.requireNonNull(MinecraftClient.getInstance().getNetworkHandler()).getPlayerListEntry(player.getUuid());
						if (playerListEntry != null) {

                            // Schedule the delayed replacement of the fake anchor with red stained glas
							BlockPos pos = hitResult.getBlockPos();
							scheduler.schedule(() -> replaceWithGhostAnchor(world, pos), (ping), TimeUnit.MILLISECONDS);
						}

						return ActionResult.SUCCESS;
					}
				}
				return ActionResult.PASS;
			}
			return ActionResult.PASS;
		});
	}

	// Place a fake anchor client-side
	private void placeClientSideFakeAnchor(World world, BlockHitResult hitResult) {
		BlockPos pos = hitResult.getBlockPos();
		world.setBlockState(pos, ModBlocks.FAKE_ANCHOR.getDefaultState());
	}

	// Replace the fake anchor with red stained glass if it's still present
	private void replaceWithGhostAnchor(World world, BlockPos pos) {
		// Place a ghost anchor client-side
		if (world.getBlockState(pos).isOf(ModBlocks.FAKE_ANCHOR)) {
			world.setBlockState(pos, ModBlocks.GHOST_ANCHOR.getDefaultState());
		}
	}
}
