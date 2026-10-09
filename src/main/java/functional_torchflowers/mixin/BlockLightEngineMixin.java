package functional_torchflowers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import functional_torchflowers.TorchflowerBeeSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.BlockLightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockLightEngine.class)
public class BlockLightEngineMixin {

	@WrapOperation(
		method = "getEmission(JLnet/minecraft/world/level/block/state/BlockState;)I",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"
		)
	)
	private static int functionalTorchflowers$emissionForEngine(
		BlockState state,
		Operation<Integer> original,
		long packedPos
	) {
		int mod = TorchflowerBeeSupport.modLightEmissionOrNeg1(state, BlockPos.of(packedPos));
		return mod >= 0 ? mod : original.call(state);
	}

	@WrapOperation(
		method = "lambda$propagateLightSources$0(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/block/state/BlockState;getLightEmission()I"
		)
	)
	private static int functionalTorchflowers$emissionForPropagation(
		BlockState state,
		Operation<Integer> original,
		BlockPos pos
	) {
		int mod = TorchflowerBeeSupport.modLightEmissionOrNeg1(state, pos);
		return mod >= 0 ? mod : original.call(state);
	}
}
