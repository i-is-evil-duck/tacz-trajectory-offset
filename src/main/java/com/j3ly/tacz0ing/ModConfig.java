package com.j3ly.tacz0ing;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ModConfig {
    public static final ForgeConfigSpec COMMON_SPEC;
    public static final Common COMMON;

    static {
        Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    public static class Common {
        public final ForgeConfigSpec.IntValue lockKey;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.push("tacz0ing");
            lockKey = builder
                .comment("GLFW key code for the lock key (default 76 = L)")
                .defineInRange("lockKey", 76, 32, 349);
            builder.pop();
        }
    }

    private static boolean hudVisible = true;

    public static boolean isHudVisible() {
        return hudVisible;
    }

    public static void toggleHud() {
        hudVisible = !hudVisible;
    }

    public static int getLockKeyCode() {
        return COMMON.lockKey.get();
    }
}
