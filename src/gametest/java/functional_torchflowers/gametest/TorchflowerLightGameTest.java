package functional_torchflowers.gametest;

import functional_torchflowers.TorchflowerBeeSupport;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchflowerCropBlock;
import net.minecraft.world.level.block.state.BlockState;

public class TorchflowerLightGameTest {

	@GameTest(maxTicks = 100)
	public void placedTorchflowerEmitsFourToEight(GameTestHelper context) {
		BlockPos pos = new BlockPos(1, 1, 1);
		context.setBlock(pos, Blocks.TORCHFLOWER);
		assertPlacedTorchflowerLight(context, pos);
		context.succeed();
	}

	@GameTest(maxTicks = 100)
	public void fullyGrownCropEmitsEight(GameTestHelper context) {
		BlockPos pos = new BlockPos(1, 1, 1);
		context.setBlock(pos, Blocks.FARMLAND);
		BlockState crop = Blocks.TORCHFLOWER_CROP.defaultBlockState().setValue(TorchflowerCropBlock.AGE, 1);
		context.setBlock(pos.above(), crop);
		assertLight(context, pos.above(), 8);
		context.succeed();
	}

	@GameTest(maxTicks = 100)
	public void immatureCropAndPotStayDark(GameTestHelper context) {
		context.setBlock(new BlockPos(1, 1, 1), Blocks.FARMLAND);
		context.setBlock(
				new BlockPos(1, 2, 1),
				Blocks.TORCHFLOWER_CROP.defaultBlockState().setValue(TorchflowerCropBlock.AGE, 0)
		);
		context.setBlock(new BlockPos(2, 1, 1), Blocks.POTTED_TORCHFLOWER);
		assertLight(context, new BlockPos(1, 2, 1), 0);
		assertLight(context, new BlockPos(2, 1, 1), 0);
		context.succeed();
	}

	private static void assertLight(GameTestHelper context, BlockPos relative, int expected) {
		int actual = context.getLevel().getLightEmission(context.absolutePos(relative));
		context.assertTrue(
				actual == expected,
				"light at " + relative + " expected " + expected + " but was " + actual
						+ " (support=" + TorchflowerBeeSupport.isQualifyingTorchflower(context.getBlockState(relative)) + ")"
		);
	}

	private static void assertPlacedTorchflowerLight(GameTestHelper context, BlockPos relative) {
		BlockPos absolute = context.absolutePos(relative);
		int actual = context.getLevel().getLightEmission(absolute);
		int expected = TorchflowerBeeSupport.placedTorchflowerLight(absolute);
		context.assertTrue(
				actual >= TorchflowerBeeSupport.PLACED_TORCHFLOWER_LIGHT_MIN
						&& actual <= TorchflowerBeeSupport.PLACED_TORCHFLOWER_LIGHT_MAX,
				"placed torchflower light at " + relative + " was " + actual
		);
		context.assertTrue(
				actual == expected,
				"placed torchflower light at " + relative + " expected " + expected + " but was " + actual
		);
	}
}
