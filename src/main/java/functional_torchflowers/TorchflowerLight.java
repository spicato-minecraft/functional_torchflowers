package functional_torchflowers;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Map;
import java.util.WeakHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.lighting.BlockLightEngine;

public final class TorchflowerLight {

	private static final Map<BlockLightEngine, LongOpenHashSet> DEFERRED = new WeakHashMap<>();

	private TorchflowerLight() {
	}

	/** The section has no light layer yet, so this check would be dropped. */
	public static void defer(BlockLightEngine engine, long packedPos) {
		synchronized (DEFERRED) {
			DEFERRED.computeIfAbsent(engine, ignored -> new LongOpenHashSet()).add(packedPos);
		}
	}

	/** Re-queue checks once a light layer may exist. Runs at the start of each light update. */
	public static void drain(BlockLightEngine engine) {
		long[] packed;
		synchronized (DEFERRED) {
			LongOpenHashSet pending = DEFERRED.get(engine);
			if (pending == null || pending.isEmpty()) {
				return;
			}
			packed = pending.toLongArray();
			pending.clear();
		}
		for (long pos : packed) {
			engine.checkBlock(BlockPos.of(pos));
		}
	}

	/** Queue a block-light check for every torchflower already stored in this chunk. */
	public static void relight(Level level, ChunkAccess chunk) {
		var engine = level.getChunkSource().getLightEngine();
		chunk.findBlocks(
				state -> TorchflowerBeeSupport.modLightEmissionOrNeg1(state, null) >= 0,
				(pos, state) -> engine.checkBlock(pos)
		);
	}
}
