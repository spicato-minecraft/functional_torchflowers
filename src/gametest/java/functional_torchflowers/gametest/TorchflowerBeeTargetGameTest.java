package functional_torchflowers.gametest;

import functional_torchflowers.TorchflowerBeeSupport;
import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchflowerCropBlock;

public class TorchflowerBeeTargetGameTest {

	@GameTest(maxTicks = 40)
	public void nightTargetsOnlyQualifyingTorchflowers(GameTestHelper context) {
		BlockPos origin = new BlockPos(1, 1, 1);
		context.setBlock(origin, Blocks.STONE);
		var level = context.getLevel();
		level.setWeatherParameters(99999, 0, false, false);
		level.setRainLevel(0.0F);
		context.assertTrue(
				TorchflowerBeeSupport.attractsBee(level, Blocks.TORCHFLOWER.defaultBlockState(), true, true),
				"torchflower should attract at night"
		);
		context.assertTrue(
				TorchflowerBeeSupport.attractsBee(
						level,
						Blocks.TORCHFLOWER_CROP.defaultBlockState().setValue(TorchflowerCropBlock.AGE, 1),
						false,
						true
				),
				"fully grown crop should attract at night even if vanilla ignores the crop"
		);
		context.assertFalse(
				TorchflowerBeeSupport.attractsBee(level, Blocks.DANDELION.defaultBlockState(), true, true),
				"dandelion should not attract at night"
		);
		context.assertFalse(
				TorchflowerBeeSupport.attractsBee(level, Blocks.POPPY.defaultBlockState(), true, true),
				"poppy should not attract at night"
		);
		context.succeed();
	}

	@GameTest(maxTicks = 40)
	public void daytimeKeepsVanillaFlowerTargets(GameTestHelper context) {
		BlockPos origin = new BlockPos(1, 1, 1);
		context.setBlock(origin, Blocks.STONE);
		var level = context.getLevel();
		level.setWeatherParameters(99999, 0, false, false);
		level.setRainLevel(0.0F);
		boolean dandelionVanilla = Bee.attractsBees(Blocks.DANDELION.defaultBlockState());
		context.assertTrue(dandelionVanilla, "dandelion is a vanilla bee flower");
		context.assertTrue(
				TorchflowerBeeSupport.attractsBee(level, Blocks.DANDELION.defaultBlockState(), dandelionVanilla, false),
				"daytime should still attract dandelions"
		);
		boolean cropVanilla = Bee.attractsBees(Blocks.TORCHFLOWER_CROP.defaultBlockState().setValue(TorchflowerCropBlock.AGE, 1));
		boolean cropActual = TorchflowerBeeSupport.attractsBee(
				level,
				Blocks.TORCHFLOWER_CROP.defaultBlockState().setValue(TorchflowerCropBlock.AGE, 1),
				cropVanilla,
				false
		);
		context.assertTrue(cropActual == cropVanilla, "daytime crop targeting should match vanilla");
		context.succeed();
	}
}
