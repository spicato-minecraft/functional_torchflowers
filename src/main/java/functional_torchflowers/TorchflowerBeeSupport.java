package functional_torchflowers;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchflowerCropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

/**
 * Shared predicates for light and bee AI. Range matches the product lock (~22 from hive).
 */
public final class TorchflowerBeeSupport {

	public static final int LIGHT_LEVEL = 8;
	public static final int PLACED_TORCHFLOWER_LIGHT_MIN = 4;
	public static final int PLACED_TORCHFLOWER_LIGHT_MAX = 8;
	/** Product-locked hive search radius (vanilla wander from a known hive is 24). */
	public static final int HIVE_NECTAR_RANGE = 22;
	private static final int HIVE_NECTAR_VERTICAL = 8;
	private static final ThreadLocal<BlockPos> LIGHT_QUERY_POS = new ThreadLocal<>();

	private TorchflowerBeeSupport() {
	}

	public static void setLightQueryPos(BlockPos pos) {
		LIGHT_QUERY_POS.set(pos);
	}

	public static void clearLightQueryPos() {
		LIGHT_QUERY_POS.remove();
	}

	/** Stable per-block light for placed {@code torchflower} (not crops). */
	public static int placedTorchflowerLight(BlockPos pos) {
		long mix = pos.asLong() * 1103515245L + 12345L;
		int span = PLACED_TORCHFLOWER_LIGHT_MAX - PLACED_TORCHFLOWER_LIGHT_MIN + 1;
		return PLACED_TORCHFLOWER_LIGHT_MIN + Math.floorMod(Long.hashCode(mix), span);
	}

	/**
	 * Mod light for this block, or {@code -1} when vanilla emission should apply.
	 */
	public static int modLightEmissionOrNeg1(BlockState state, @Nullable BlockPos pos) {
		if (state.is(Blocks.TORCHFLOWER)) {
			BlockPos at = pos != null ? pos : LIGHT_QUERY_POS.get();
			if (at == null) {
				return PLACED_TORCHFLOWER_LIGHT_MAX;
			}
			return placedTorchflowerLight(at);
		}
		if (state.is(Blocks.TORCHFLOWER_CROP) && state.getValue(TorchflowerCropBlock.AGE) >= 1) {
			return LIGHT_LEVEL;
		}
		return -1;
	}

	public static boolean isQualifyingTorchflower(BlockState state) {
		if (state.is(Blocks.TORCHFLOWER)) {
			return true;
		}
		if (state.is(Blocks.TORCHFLOWER_CROP)) {
			return state.getValue(TorchflowerCropBlock.AGE) >= 1;
		}
		return false;
	}

	public static boolean isWeatherKeepingBeesInside(Level level) {
		return level.isRaining();
	}

	public static boolean hasQualifyingTorchflowerInRange(Level level, BlockPos origin) {
		return hasQualifyingTorchflowerInRange(level, origin, HIVE_NECTAR_RANGE, HIVE_NECTAR_VERTICAL);
	}

	public static boolean hasQualifyingTorchflowerInRange(Level level, BlockPos origin, int horizontalRange, int verticalRange) {
		return BlockPos.findClosestMatch(
				origin,
				horizontalRange,
				verticalRange,
				pos -> isQualifyingTorchflower(level.getBlockState(pos))
		).isPresent();
	}

	public static boolean beesStayInHive(Level level, BlockPos searchOrigin, boolean vanillaStay) {
		return beesStayInHive(level, searchOrigin, vanillaStay, HIVE_NECTAR_RANGE, HIVE_NECTAR_VERTICAL);
	}

	public static boolean beesStayInHive(
			Level level,
			BlockPos searchOrigin,
			boolean vanillaStay,
			int horizontalRange,
			int verticalRange
	) {
		if (!vanillaStay) {
			return false;
		}
		if (isWeatherKeepingBeesInside(level)) {
			return true;
		}
		return !hasQualifyingTorchflowerInRange(level, searchOrigin, horizontalRange, verticalRange);
	}

	/** Homeless bees keep vanilla night stay (no hive to work from). */
	public static boolean beesStayInHiveForBee(Level level, @Nullable BlockPos hivePos, BlockPos beePos, boolean vanillaStay) {
		if (hivePos == null) {
			return vanillaStay;
		}
		return beesStayInHive(level, hivePos, vanillaStay);
	}

	/** Night-only nectar targets: qualifying torchflowers, never other flowers. */
	public static boolean attractsBee(Level level, BlockPos beePos, BlockState state, boolean vanillaAttracts) {
		boolean vanillaStay = Boolean.TRUE.equals(
				level.environmentAttributes().getValue(net.minecraft.world.attribute.EnvironmentAttributes.BEES_STAY_IN_HIVE, beePos)
		);
		return attractsBee(level, state, vanillaAttracts, vanillaStay);
	}

	public static boolean attractsBee(Level level, BlockState state, boolean vanillaAttracts, boolean vanillaStay) {
		boolean nightWorkWindow = vanillaStay && !isWeatherKeepingBeesInside(level);
		if (nightWorkWindow) {
			return isQualifyingTorchflower(state);
		}
		return vanillaAttracts;
	}
}
