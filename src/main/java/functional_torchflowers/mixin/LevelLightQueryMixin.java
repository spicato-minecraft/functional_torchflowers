package functional_torchflowers.mixin;

import functional_torchflowers.TorchflowerBeeSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockGetter.class)
public interface LevelLightQueryMixin {

	@Inject(method = "getLightEmission", at = @At("HEAD"))
	private void functionalTorchflowers$capturePos(BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		TorchflowerBeeSupport.setLightQueryPos(pos);
	}

	@Inject(method = "getLightEmission", at = @At("RETURN"))
	private void functionalTorchflowers$releasePos(BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		TorchflowerBeeSupport.clearLightQueryPos();
	}
}
