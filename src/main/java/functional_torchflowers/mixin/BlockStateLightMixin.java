package functional_torchflowers.mixin;

import functional_torchflowers.TorchflowerBeeSupport;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockStateLightMixin {

	@Inject(method = "getLightEmission", at = @At("HEAD"), cancellable = true)
	private void functionalTorchflowers$qualifyingLight(CallbackInfoReturnable<Integer> cir) {
		BlockState state = (BlockState) (Object) this;
		if (TorchflowerBeeSupport.isQualifyingTorchflower(state)) {
			cir.setReturnValue(TorchflowerBeeSupport.LIGHT_LEVEL);
		}
	}
}
