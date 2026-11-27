package io.github.selii0z.keyselector.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.lwjgl.glfw.GLFW;
import java.util.List;

public final class KeySelectorClient {
    private static InputConstants.Key held;
    private static long pressedAt;
    private static boolean radialOpened;
    private static List<KeyMapping> choices = List.of();
    private KeySelectorClient() {}

    public static boolean keyEvent(int key, int scanCode, int action) {
        if (key == GLFW.GLFW_KEY_UNKNOWN) return false;
        if (action == GLFW.GLFW_REPEAT) return InputConstants.getKey(key, scanCode).equals(held);
        return handleInput(InputConstants.getKey(key, scanCode), action);
    }

    public static boolean mouseEvent(int button, int action) {
        return handleInput(InputConstants.Type.MOUSE.getOrCreate(button), action);
    }

    private static boolean handleInput(InputConstants.Key input, int action) {
        Minecraft mc = Minecraft.getInstance();
        if (action == GLFW.GLFW_RELEASE && input.equals(held)) {
            if (!radialOpened && mc.screen == null && mc.player != null) mc.setScreen(new RadialScreen(choices, input, false));
            else if (mc.screen instanceof RadialScreen screen) screen.finishHold();
            held = null;
            choices = List.of();
            radialOpened = false;
            return true;
        }
        if (mc.screen != null || mc.player == null || held != null) return false;
        if (action == GLFW.GLFW_PRESS) {
            List<KeyMapping> matches = ConflictIndex.find(input);
            if (matches.size() < 2) return false;
            held = input;
            pressedAt = System.nanoTime();
            radialOpened = false;
            choices = matches;
            return true;
        }
        return false;
    }

    @SubscribeEvent
    public static void onTick(ClientTickEvent.Post event) {
        if (held != null && !radialOpened && Minecraft.getInstance().screen == null &&
            System.nanoTime() - pressedAt >= 300_000_000L) {
            radialOpened = true;
            Minecraft.getInstance().setScreen(new RadialScreen(choices, held, true));
        }
    }

    public static void activate(KeyMapping mapping) {
        // Only click-based KeyMappings can be emulated. Direct GLFW and key-down handlers
        // require special compatibility support.
        var access = (io.github.selii0z.keyselector.mixin.KeyMappingAccessor) (Object) mapping;
        access.keyselector$setClickCount(access.keyselector$getClickCount() + 1);
    }
}
