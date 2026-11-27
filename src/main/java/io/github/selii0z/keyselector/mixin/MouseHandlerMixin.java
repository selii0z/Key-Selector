package io.github.selii0z.keyselector.mixin;

import io.github.selii0z.keyselector.client.KeySelectorClient;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MouseHandler.class)
public abstract class MouseHandlerMixin {
    @Inject(method = "onPress", at = @At("HEAD"), cancellable = true)
    private void keyselector$intercept(long window, int button, int action, int modifiers, CallbackInfo ci) {
        if (KeySelectorClient.mouseEvent(button, action)) ci.cancel();
    }
}
