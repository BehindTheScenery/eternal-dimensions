package com.vorono4ka;

import com.vorono4ka.config.ModConfig;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;

import static net.minecraft.server.command.CommandManager.literal;

public class CommandManager {
    public static void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("no-more-dimensions:reload")
            .requires(source -> source.hasPermissionLevel(4))
            .executes(context -> {
                ModConfig.loadConfig();
                context.getSource().sendMessage(Text.translatable("commands.no_more_dimensions.reload.success"));

                return 1;
            })));
    }
}
