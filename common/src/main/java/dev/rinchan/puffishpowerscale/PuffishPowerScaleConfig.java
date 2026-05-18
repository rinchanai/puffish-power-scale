package dev.rinchan.puffishpowerscale;

import java.util.List;
import net.neoforged.neoforge.common.ModConfigSpec;

public final class PuffishPowerScaleConfig {
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.BooleanValue ENABLED;
    public static final ModConfigSpec.ConfigValue<List<? extends String>> CATEGORY_IDS;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        ENABLED = builder
            .comment("Enable Puffish Skills level access from Power Scale expressions.")
            .define("enabled", true);

        CATEGORY_IDS = builder
            .comment(
                "Puffish Skills category IDs used as the level source.",
                "The highest experience level among these categories is returned.",
                "Leave empty to scan all Puffish Skills categories that have experience."
            )
            .defineListAllowEmpty("categoryIds", List.of("puffish_skills:wmf_arts"), () -> "puffish_skills:wmf_arts", value -> value instanceof String string && !string.isBlank());

        SPEC = builder.build();
    }

    private PuffishPowerScaleConfig() {
    }
}
