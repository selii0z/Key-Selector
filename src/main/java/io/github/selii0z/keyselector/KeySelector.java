package io.github.selii0z.keyselector;

import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import io.github.selii0z.keyselector.client.KeySelectorClient;

@Mod(KeySelector.MOD_ID)
public final class KeySelector {
    public static final String MOD_ID = "keyselector";
    public KeySelector() {
        NeoForge.EVENT_BUS.register(KeySelectorClient.class);
    }
}
