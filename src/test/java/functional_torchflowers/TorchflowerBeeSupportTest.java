package functional_torchflowers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchflowerCropBlock;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TorchflowerBeeSupportTest {

	@BeforeAll
	static void bootstrapMinecraft() {
		MinecraftTestBootstrap.init();
	}

	@Test
	void modMainClassLoads() {
		assertNotNull(FunctionalTorchflowers.MOD_ID);
	}

	@Test
	void matureFlowerAndGrownCropQualify() {
		assertTrue(TorchflowerBeeSupport.isQualifyingTorchflower(Blocks.TORCHFLOWER.defaultBlockState()));
		assertTrue(TorchflowerBeeSupport.isQualifyingTorchflower(
				Blocks.TORCHFLOWER_CROP.defaultBlockState().setValue(TorchflowerCropBlock.AGE, 1)
		));
	}

	@Test
	void immatureCropPotAndOtherFlowersDoNotQualify() {
		assertFalse(TorchflowerBeeSupport.isQualifyingTorchflower(
				Blocks.TORCHFLOWER_CROP.defaultBlockState().setValue(TorchflowerCropBlock.AGE, 0)
		));
		assertFalse(TorchflowerBeeSupport.isQualifyingTorchflower(Blocks.POTTED_TORCHFLOWER.defaultBlockState()));
		assertFalse(TorchflowerBeeSupport.isQualifyingTorchflower(Blocks.DANDELION.defaultBlockState()));
		assertFalse(TorchflowerBeeSupport.isQualifyingTorchflower(Blocks.POPPY.defaultBlockState()));
	}

	@Test
	void placedTorchflowerLightIsStableAndInRange() {
		BlockPos pos = new BlockPos(12, 64, -7);
		int first = TorchflowerBeeSupport.placedTorchflowerLight(pos);
		int second = TorchflowerBeeSupport.placedTorchflowerLight(pos);
		assertTrue(first >= TorchflowerBeeSupport.PLACED_TORCHFLOWER_LIGHT_MIN);
		assertTrue(first <= TorchflowerBeeSupport.PLACED_TORCHFLOWER_LIGHT_MAX);
		assertTrue(second == first);
	}

	@Test
	void cropLightStaysEight() {
		BlockPos pos = new BlockPos(0, 0, 0);
		var crop = Blocks.TORCHFLOWER_CROP.defaultBlockState().setValue(TorchflowerCropBlock.AGE, 1);
		assertTrue(TorchflowerBeeSupport.modLightEmissionOrNeg1(crop, pos) == TorchflowerBeeSupport.LIGHT_LEVEL);
	}
}
