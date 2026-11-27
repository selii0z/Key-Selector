package io.github.selii0z.keyselector.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import java.util.List;

public final class RadialScreen extends Screen {
    private final List<KeyMapping> mappings;
    private final InputConstants.Key input;
    private final boolean holdMode;
    private int selected = -1;

    public RadialScreen(List<KeyMapping> mappings, InputConstants.Key input, boolean holdMode) {
        super(Component.literal("Key Selector"));
        this.mappings = List.copyOf(mappings);
        this.input = input;
        this.holdMode = holdMode;
    }

    @Override public boolean isPauseScreen() { return false; }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        int cx = width / 2;
        int cy = height / 2;
        double dx = mouseX - cx;
        double dy = mouseY - cy;
        double distance = Math.hypot(dx, dy);
        selected = distance < 25 || mappings.isEmpty() ? -1 :
            (int) Math.floor((Math.atan2(dy, dx) + Math.PI) / (Math.PI * 2) * mappings.size()) % mappings.size();
        for (int i = 0; i < mappings.size(); i++) {
            double angle = ((i + 0.5) / mappings.size()) * Math.PI * 2 - Math.PI;
            int x = cx + (int) (Math.cos(angle) * 90);
            int y = cy + (int) (Math.sin(angle) * 70);
            int color = i == selected ? 0xD05B7854 : 0xB0222929;
            graphics.fill(x - 53, y - 13, x + 53, y + 13, color);
            String name = Component.translatable(mappings.get(i).getName()).getString();
            if (name.length() > 19) name = name.substring(0, 18) + "…";
            graphics.drawCenteredString(font, name, x, y - 4, 0xFFFFFF);
        }
        graphics.fill(cx-23,cy-16,cx+23,cy+16,0xCC202428);
        graphics.drawCenteredString(font, input.getDisplayName(), cx, cy-4, 0xFFFFFF);
        graphics.drawCenteredString(font, holdMode ? "Release to select" : "Click to select", cx, cy+110, 0xC8D0D0);
        super.render(graphics,mouseX,mouseY,delta);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!holdMode && button == 0) { commit(); return true; }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 256) { onClose(); return true; }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void finishHold() { if (holdMode) commit(); }

    private void commit() {
        int target = selected;
        onClose();
        if (target >= 0 && target < mappings.size()) KeySelectorClient.activate(mappings.get(target));
    }
}
