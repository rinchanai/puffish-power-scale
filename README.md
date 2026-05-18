# Puffish Power Scale

Puffish Power Scale adds a Puffish Skills level function to Silent's Power Scale expressions.

It does **not** calculate mob difficulty itself. Power Scale remains responsible for all formulas, caps, dimension values, player weighting, and attribute scaling.

## Expression function

This addon registers one Power Scale expression function:

```text
PUFFISH_SKILL_LEVEL(radius)
```

In Power Scale local-difficulty expressions, it returns the highest configured Puffish Skills experience level among players within the given horizontal radius of the evaluated mob position.

Example use inside `config/powerscale-common.toml`:

```toml
[difficulty.local_difficulty.parts]
from_players = "PUFFISH_SKILL_LEVEL(256)"
```

The number returned by the function is the raw Puffish Skills experience level. If a pack wants to divide, cap, blend with dimension baselines, or combine it with Power Scale's existing functions, put that formula in Power Scale's config.

When a Power Scale expression is evaluated with a `player` variable, `PUFFISH_SKILL_LEVEL()` returns that player's configured Puffish Skills experience level.

## Config

The addon config is `config/puffish_power_scale-common.toml`.

- `enabled`: enables or disables the expression function.
- `categoryIds`: Puffish Skills category IDs to read. The highest level wins. Leave it empty to scan all Puffish Skills categories that have experience.

Default category:

```text
puffish_skills:wmf_arts
```

## Requirements

- Minecraft 1.21.1
- NeoForge
- Puffish Skills
- Silent's Power Scale
- Silent Lib
