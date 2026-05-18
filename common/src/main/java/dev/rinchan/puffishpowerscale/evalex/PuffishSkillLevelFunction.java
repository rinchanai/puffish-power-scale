package dev.rinchan.puffishpowerscale.evalex;

import dev.rinchan.puffishpowerscale.PuffishPowerScaleConfig;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.puffish.skillsmod.api.Category;
import net.puffish.skillsmod.api.Experience;
import net.puffish.skillsmod.api.SkillsAPI;
import net.silentchaos512.powerscale.core.DifficultyUtil;
import shadow.powerscale.evalex.Expression;
import shadow.powerscale.evalex.EvaluationException;
import shadow.powerscale.evalex.config.ExpressionConfiguration;
import shadow.powerscale.evalex.data.EvaluationValue;
import shadow.powerscale.evalex.functions.AbstractFunction;
import shadow.powerscale.evalex.parser.Token;

public class PuffishSkillLevelFunction extends AbstractFunction {
    @Override
    public EvaluationValue evaluate(Expression expression, Token token, EvaluationValue... parameterValues) throws EvaluationException {
        if (!PuffishPowerScaleConfig.ENABLED.get()) {
            return number(0);
        }

        Player player = getPlayer(expression);
        if (player instanceof ServerPlayer serverPlayer && parameterValues.length == 0) {
            return number(getConfiguredSkillLevel(serverPlayer));
        }

        Level level = getLevel(expression);
        BlockPos pos = getPos(expression);
        if (level == null || pos == null || parameterValues.length == 0) {
            return number(0);
        }

        double radius = Math.max(0.0D, parameterValues[0].getNumberValue().doubleValue());
        double radiusSquared = radius * radius;
        int maxLevel = 0;
        for (Player nearbyPlayer : level.players()) {
            if (!(nearbyPlayer instanceof ServerPlayer serverPlayer)) {
                continue;
            }
            if (DifficultyUtil.horizontalDistanceSqr(pos, serverPlayer) > radiusSquared) {
                continue;
            }
            maxLevel = Math.max(maxLevel, getConfiguredSkillLevel(serverPlayer));
        }
        return number(maxLevel);
    }

    private static int getConfiguredSkillLevel(ServerPlayer player) {
        List<? extends String> categoryIds = PuffishPowerScaleConfig.CATEGORY_IDS.get();
        if (categoryIds.isEmpty()) {
            return SkillsAPI.streamCategories()
                .mapToInt(category -> getCategoryLevel(category, player))
                .max()
                .orElse(0);
        }

        int maxLevel = 0;
        for (String categoryId : categoryIds) {
            ResourceLocation id = ResourceLocation.tryParse(categoryId);
            if (id == null) {
                continue;
            }
            maxLevel = Math.max(maxLevel, SkillsAPI.getCategory(id)
                .map(category -> getCategoryLevel(category, player))
                .orElse(0));
        }
        return maxLevel;
    }

    private static int getCategoryLevel(Category category, ServerPlayer player) {
        return category.getExperience()
            .map(experience -> getExperienceLevel(experience, player))
            .orElse(0);
    }

    private static int getExperienceLevel(Experience experience, ServerPlayer player) {
        return Math.max(0, experience.getLevel(player));
    }

    private static Level getLevel(Expression expression) {
        Object value = getDataValue(expression, "level");
        return value instanceof Level level ? level : null;
    }

    private static BlockPos getPos(Expression expression) {
        Object value = getDataValue(expression, "pos");
        return value instanceof BlockPos pos ? pos : null;
    }

    private static Player getPlayer(Expression expression) {
        Object value = getDataValue(expression, "player");
        return value instanceof Player player ? player : null;
    }

    private static Object getDataValue(Expression expression, String name) {
        EvaluationValue value = expression.getDataAccessor().getData(name);
        return value == null || value.isNullValue() ? null : value.getValue();
    }

    private static EvaluationValue number(int value) {
        return EvaluationValue.of(value, ExpressionConfiguration.defaultConfiguration());
    }
}
