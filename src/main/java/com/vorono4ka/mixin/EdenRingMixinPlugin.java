package com.vorono4ka.mixin;

import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static com.vorono4ka.NoMoreDimensionsMod.LOGGER;


public class EdenRingMixinPlugin implements IMixinConfigPlugin {
    public static final String EDEN_RING_MOD_ID = "edenring";

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        boolean modLoaded = FabricLoader.getInstance().isModLoaded(EDEN_RING_MOD_ID);
        LOGGER.info("Eden ring mixins loaded: {}", modLoaded);
        return modLoaded;
    }

    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
