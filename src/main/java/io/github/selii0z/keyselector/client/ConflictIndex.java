package io.github.selii0z.keyselector.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import com.mojang.blaze3d.platform.InputConstants;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ConflictIndex {
    private ConflictIndex() {}
    public static List<KeyMapping> visible(InputConstants.Key input, List<KeyMapping> mappings) {
        var rule = Preferences.INSTANCE.rule(input.getName());
        return mappings.stream().filter(m -> !rule.hidden.contains(m.getName()))
            .sorted(Comparator.comparingInt(m -> {
                int i = rule.order.indexOf(m.getName());
                return i < 0 ? Integer.MAX_VALUE : i;
            })).toList();
    }
    public static List<KeyMapping> find(InputConstants.Key input) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.options == null || input.equals(InputConstants.UNKNOWN)) return List.of();
        // Never intercept essential vanilla gameplay controls, including remapped AZERTY keys.
        for (KeyMapping m : mc.options.keyMappings) {
            if (m.getKey().equals(input) && java.util.Set.of(
                "key.forward", "key.back", "key.left", "key.right", "key.jump",
                "key.sneak", "key.sprint", "key.attack", "key.use", "key.pickItem",
                "key.drop", "key.inventory", "key.swapOffhand"
            ).contains(m.getName())) return List.of();
        }
        List<KeyMapping> out = new ArrayList<>();
        for (KeyMapping m : mc.options.keyMappings) if (m.getKey().equals(input)) out.add(m);
        out.sort(Comparator.comparing(KeyMapping::getName));
        return out;
    }
}
