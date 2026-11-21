package functional_torchflowers.mixin;

import functional_torchflowers.TorchflowerLight;
import net.minecraft.world.level.lighting.BlockLightEngine;
import net.minecraft.world.level.lighting.LightEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightEngine.class)
public class LightEngineUpdateMixin {

	@Inject(method = "runLightUpdates", at = @At("HEAD"))
	private void functionalTorchflowers$drainDeferred(CallbackInfoReturnable<Integer> cir) {
		if ((Object) this instanceof BlockLightEngine engine) {
			TorchflowerLight.drain(engine);
		}
	}
}
