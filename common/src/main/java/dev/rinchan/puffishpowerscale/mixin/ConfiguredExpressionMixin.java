package dev.rinchan.puffishpowerscale.mixin;

import dev.rinchan.puffishpowerscale.evalex.PuffishSkillLevelFunction;
import java.util.Map;
import net.silentchaos512.powerscale.config.ConfiguredExpression;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import shadow.powerscale.evalex.config.ExpressionConfiguration;

@Mixin(ConfiguredExpression.class)
public abstract class ConfiguredExpressionMixin {
    @Inject(method = "getDefaultExpressionConfiguration", at = @At("RETURN"), cancellable = true, remap = false)
    private static void addPuffishSkillLevelFunction(CallbackInfoReturnable<ExpressionConfiguration> cir) {
        cir.setReturnValue(cir.getReturnValue().withAdditionalFunctions(
            Map.entry("PUFFISH_SKILL_LEVEL", new PuffishSkillLevelFunction())
        ));
    }
}
