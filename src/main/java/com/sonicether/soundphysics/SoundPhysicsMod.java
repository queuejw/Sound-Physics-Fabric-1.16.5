package com.sonicether.soundphysics;

import com.sonicether.soundphysics.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Pair;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class SoundPhysicsMod implements ModInitializer {
    public static Map<BlockSoundGroup, Pair<String, String>> blockSoundGroups;
    @Override
    public void onInitialize()
    {
        blockSoundGroups = Arrays.stream(BlockSoundGroup.class.getDeclaredFields())
                .filter((f) -> {
                    try {
                        return Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && f.get(null) instanceof BlockSoundGroup;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .collect(Collectors.toMap(
                        (f) -> {
                            try {
                                return (BlockSoundGroup)f.get(null);
                            } catch (IllegalAccessException | ClassCastException e) {
                                e.printStackTrace();
                            }
                            return null;
                        },
                        (f) -> {
                             try {
                                 return new Pair<>(f.getName(), (f.get(null) instanceof BlockSoundGroup g ? g.getBreakSound().getId().getPath() : ""));
                             } catch (IllegalAccessException e) {
                                 e.printStackTrace();
                             }
                             return new Pair<>("", "");
                         }));

        ConfigManager.registerAutoConfig();
    }
}
