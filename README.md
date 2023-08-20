# no-more-dimensions

A mod for Minecraft that allows you to disable portal creation and teleportation.

## How to configurate?

You must use the Mod Menu with Cloth Config to be able to configurate the mod from the game menu.

If you doesn't have the Mod Menu in your mod list, then just modify the `no_more_dimensions.toml`.

Example of a file with disabled standard another dimensions:
```toml
[common]
listDelimiter = ","
blockedDimensions = "minecraft:the_nether,minecraft:the_end"
```

## About mod

### Dependencies

- [Cloth Config](https://github.com/shedaniel/cloth-config) 