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

@Mixin(targets = "net.minecraft.world.entity.animal.bee.Bee$BeePollinateGoal")
public abstract class BeePollinateGoalMixin {

	@Shadow
	@Final
	Bee field_20377;

	@WrapOperation(
			method = "findNearbyFlower",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/animal/bee/Bee;attractsBees(Lnet/minecraft/world/level/block/state/BlockState;)Z"
			)
	)
	private boolean functionalTorchflowers$nightTargets(BlockState state, Operation<Boolean> original) {
		return TorchflowerBeeSupport.attractsBee(this.field_20377.level(), this.field_20377.blockPosition(), state, original.call(state));
	}
}
