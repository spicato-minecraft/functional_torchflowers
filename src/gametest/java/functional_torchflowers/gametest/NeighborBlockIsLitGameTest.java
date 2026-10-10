package functional_torchflowers.gametest;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;

/**
 * Torchflower light must reach a neighboring empty cell through the block light engine, then leave when the flower is removed.
 */
public class NeighborBlockIsLitGameTest {

	@GameTest(maxTicks = 100)
	public void neighborBlockIsLit(GameTestHelper context) {
		BlockPos flower = new BlockPos(1, 1, 1);
		BlockPos neighbor = new BlockPos(2, 1, 1);
		context.setBlock(flower, Blocks.TORCHFLOWER);
		context.startSequence()
				.thenWaitUntil(() -> assertNeighborLit(context, neighbor))
				.thenExecute(() -> context.destroyBlock(flower))
				.thenWaitUntil(() -> assertNeighborDark(context, neighbor))
				.thenSucceed();
	}

	private static void assertNeighborLit(GameTestHelper context, BlockPos neighbor) {
		int emission = context.getLevel().getLightEmission(context.absolutePos(neighbor));
		context.assertTrue(
				emission == 0,
				"neighbor at " + neighbor + " must be an empty cell (emission 0) but was " + emission
		);
		int lit = blockLight(context, neighbor);
		context.assertTrue(
				lit > 0,
				"neighbor block light at " + neighbor + " expected > 0 but was " + lit
		);
	}

	private static void assertNeighborDark(GameTestHelper context, BlockPos neighbor) {
		int dark = blockLight(context, neighbor);
		context.assertTrue(
				dark == 0,
				"neighbor block light at " + neighbor + " expected 0 after removal but was " + dark
		);
	}

	private static int blockLight(GameTestHelper context, BlockPos relative) {
		return context.getLevel().getBrightness(LightLayer.BLOCK, context.absolutePos(relative));
	}
}
