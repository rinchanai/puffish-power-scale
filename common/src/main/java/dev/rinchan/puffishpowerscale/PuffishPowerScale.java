package dev.rinchan.puffishpowerscale;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;

@Mod(PuffishPowerScale.MOD_ID)
public class PuffishPowerScale {
    public static final String MOD_ID = "puffish_power_scale";

    public PuffishPowerScale(IEventBus modBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, PuffishPowerScaleConfig.SPEC);
    }
}
