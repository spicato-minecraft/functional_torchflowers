package functional_torchflowers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import functional_torchflowers.TorchflowerBeeSupport;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.world.entity.animal.bee.Bee$ValidateFlowerGoal")
public abstract class BeeValidateFlowerGoalMixin {

	@Shadow
	@Final
	Bee field_52460;

	@WrapOperation(
			method = "isFlower",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/animal/bee/Bee;attractsBees(Lnet/minecraft/world/level/block/state/BlockState;)Z"
			)
	)
	private boolean functionalTorchflowers$nightValidate(BlockState state, Operation<Boolean> original) {
		return TorchflowerBeeSupport.attractsBee(this.field_52460.level(), this.field_52460.blockPosition(), state, original.call(state));
	}
}
