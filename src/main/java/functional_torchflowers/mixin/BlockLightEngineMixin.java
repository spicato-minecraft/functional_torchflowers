package functional_torchflowers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import functional_torchflowers.TorchflowerBeeSupport;
import functional_torchflowers.TorchflowerLight;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.BlockLightEngine;
import net.minecraft.world.level.lighting.BlockLightSectionStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockLightEngine.class)
public class BlockLightEngineMixin {
	@Unique
	private long functionalTorchflowers$checking;

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

	/**
	 * Vanilla returns 0 from {@code getEmission} until the column is marked light-enabled.
	 * That check runs once. A torchflower placed in that window kept emission 0 in the
	 * stored light layer even after the chunk finished lighting.
	 */
	@Inject(method = "checkNode(J)V", at = @At("HEAD"))
	private void functionalTorchflowers$captureNode(long packedPos, CallbackInfo ci) {
		this.functionalTorchflowers$checking = packedPos;
	}

	@WrapOperation(
		method = "checkNode(J)V",
		at = @At(
			value = "INVOKE",
			target = "Lnet/minecraft/world/level/lighting/BlockLightSectionStorage;storingLightForSection(J)Z"
		)
	)
	private boolean functionalTorchflowers$deferUntilStored(
		BlockLightSectionStorage storage,
		long sectionPos,
		Operation<Boolean> original
	) {
		boolean storing = original.call(storage, sectionPos);
		if (!storing) {
			TorchflowerLight.defer((BlockLightEngine) (Object) this, this.functionalTorchflowers$checking);
		}
		return storing;
	}

	@ModifyReturnValue(method = "getEmission(JLnet/minecraft/world/level/block/state/BlockState;)I", at = @At("RETURN"))
	private int functionalTorchflowers$keepEmission(int vanilla, long packedPos, BlockState state) {
		if (vanilla > 0) {
			return vanilla;
		}
		int mod = TorchflowerBeeSupport.modLightEmissionOrNeg1(state, BlockPos.of(packedPos));
		return mod > 0 ? mod : vanilla;
	}
}
