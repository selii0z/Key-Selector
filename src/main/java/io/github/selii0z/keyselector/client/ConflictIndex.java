package io.github.selii0z.keyselector.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ConflictIndex {
    private ConflictIndex() {}
    public static List<KeyMapping> find(InputConstants.Key input) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null || input.equals(InputConstants.UNKNOWN)) return List.of();
        List<KeyMapping> out = new ArrayList<>();
        for (KeyMapping m : mc.options.keyMappings) if (m.getKey().equals(input)) out.add(m);
        out.sort(Comparator.comparing(KeyMapping::getName));
        return out;
    }
}
