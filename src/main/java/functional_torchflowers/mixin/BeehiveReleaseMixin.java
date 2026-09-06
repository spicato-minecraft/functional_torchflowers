package functional_torchflowers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import functional_torchflowers.TorchflowerBeeSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BeehiveBlockEntity.class)
public abstract class BeehiveReleaseMixin {

	@WrapOperation(
			method = "releaseOccupant",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/core/BlockPos;)Ljava/lang/Object;"
			)
	)
	private static Object functionalTorchflowers$nightTorchflowerRelease(
			EnvironmentAttributeSystem reader,
			EnvironmentAttribute<?> attribute,
			BlockPos hivePos,
			Operation<Object> original,
			@Local(argsOnly = true) Level level
	) {
		Object vanilla = original.call(reader, attribute, hivePos);
		boolean vanillaStay = Boolean.TRUE.equals(vanilla);
		return TorchflowerBeeSupport.beesStayInHive(level, hivePos, vanillaStay);
	}
}
