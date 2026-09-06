package functional_torchflowers.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import functional_torchflowers.TorchflowerBeeSupport;
import net.minecraft.core.BlockPos;
import net.minecraft.world.attribute.EnvironmentAttribute;
import net.minecraft.world.attribute.EnvironmentAttributeSystem;
import net.minecraft.world.entity.animal.bee.Bee;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.jspecify.annotations.Nullable;

@Mixin(Bee.class)
public abstract class BeeStayInHiveMixin {

	@Shadow
	@Nullable
	BlockPos hivePos;

	@WrapOperation(
			method = "wantsToEnterHive",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/attribute/EnvironmentAttributeSystem;getValue(Lnet/minecraft/world/attribute/EnvironmentAttribute;Lnet/minecraft/world/phys/Vec3;)Ljava/lang/Object;"
			)
	)
	private Object functionalTorchflowers$nightTorchflowerStay(
			EnvironmentAttributeSystem reader,
			EnvironmentAttribute<?> attribute,
			Vec3 pos,
			Operation<Object> original
	) {
		Object vanilla = original.call(reader, attribute, pos);
		Bee bee = (Bee) (Object) this;
		boolean vanillaStay = Boolean.TRUE.equals(vanilla);
		return TorchflowerBeeSupport.beesStayInHiveForBee(bee.level(), this.hivePos, bee.blockPosition(), vanillaStay);
	}
}
