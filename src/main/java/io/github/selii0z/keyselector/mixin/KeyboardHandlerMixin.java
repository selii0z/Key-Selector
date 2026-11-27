package io.github.selii0z.keyselector.mixin;

import io.github.selii0z.keyselector.client.KeySelectorClient;
import net.minecraft.client.KeyboardHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeyboardHandler.class)
public abstract class KeyboardHandlerMixin {
    @Inject(method = "keyPress", at = @At("HEAD"), cancellable = true)
    private void keyselector$intercept(long window, int key, int scanCode, int action, int modifiers, CallbackInfo ci) {
        if (KeySelectorClient.keyEvent(key, scanCode, action)) ci.cancel();
    }
}
