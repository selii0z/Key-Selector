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
        int radius = Math.max(35, Math.min(115, Math.min(width, height) / 2 - 22));
        int inner = Math.min(32, radius / 3);
        selected = sector(mouseX-cx, mouseY-cy, inner, radius);
        for (int y = -radius; y <= radius; y++) {
            int last = -2, from = -radius;
            for (int x = -radius; x <= radius+1; x++) {
                int s = x > radius ? -1 : sector(x,y,inner,radius);
                if (s != last) {
                    if (last >= 0) graphics.fill(cx+from,cy+y,cx+x,cy+y+1,last == selected ? 0xD05B7854 : 0xB0222929);
                    from = x; last = s;
                }
            }
        }
        for (int i=0;i<mappings.size();i++) {
            double angle = i * Math.PI * 2 / mappings.size();
            int x = cx + (int)(Math.sin(angle)*(radius+inner)/2);
            int y = cy - (int)(Math.cos(angle)*(radius+inner)/2);
            String name = Component.translatable(mappings.get(i).getName()).getString();
            graphics.drawCenteredString(font,font.plainSubstrByWidth(name,Math.max(28,radius/2)),x,y-4,0xFFFFFF);
        }
        graphics.drawCenteredString(font,input.getDisplayName(),cx,cy-4,0xFFFFFF);
        graphics.drawCenteredString(font,holdMode ? "Release to select" : "Click to select | P: Settings",cx,cy+radius+8,0xFFFFFF);
        super.render(graphics,mouseX,mouseY,delta);
    }

    private int sector(double x,double y,int inner,int outer) {
        double d=Math.hypot(x,y);
        if(d<inner || d>outer || mappings.isEmpty()) return -1;
        double a=(Math.atan2(x,-y)+Math.PI*2)%(Math.PI*2);
        double n=a*mappings.size()/(Math.PI*2);
        if(mappings.size()>1 && Math.abs(n-Math.floor(n)-0.5)<0.025) return -1;
        return (int)Math.floor(n+0.5)%mappings.size();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!holdMode && button == 0) { commit(); return true; }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == 80 && !holdMode) { minecraft.setScreen(new PreferencesScreen(this,input)); return true; }
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
