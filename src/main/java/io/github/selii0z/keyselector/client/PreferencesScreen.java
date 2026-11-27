package io.github.selii0z.keyselector.client;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import java.util.*;
public final class PreferencesScreen extends Screen {
    private final Screen parent;
    private final InputConstants.Key key;
    private int page;
    public PreferencesScreen(Screen parent, InputConstants.Key key) {
        super(Component.literal("Key Selector")); this.parent=parent; this.key=key;
    }
    private void refresh(){clearWidgets();init();}
    private void save(){Preferences.INSTANCE.save();refresh();}
    private void button(String text,int x,int y,int w,Runnable action){
        addRenderableWidget(Button.builder(Component.literal(text),b->action.run()).bounds(x,y,w,20).build());
    }
    @Override protected void init(){
        var rule=Preferences.INSTANCE.rule(key.getName());
        var all=new ArrayList<>(ConflictIndex.find(key));
        for(var m:all) if(!rule.order.contains(m.getName())) rule.order.add(m.getName());
        all.sort(Comparator.comparingInt(m->rule.order.indexOf(m.getName())));
        int rows=Math.max(1,(height-85)/24);
        page=Math.max(0,Math.min(page,Math.max(0,(all.size()-1)/rows)));
        int x=Math.max(4,width/2-145);
        for(int i=page*rows;i<Math.min(all.size(),(page+1)*rows);i++){
            var m=all.get(i);String id=m.getName();int y=28+(i-page*rows)*24;
            button(font.plainSubstrByWidth(Component.translatable(id).getString(),115),x,y,120,()->{});
            button(id.equals(rule.priority)?"★":"☆",x+122,y,28,()->{rule.priority=id.equals(rule.priority)?"":id;rule.hidden.remove(id);save();});
            button(rule.hidden.contains(id)?"Show":"Hide",x+152,y,48,()->{if(!rule.hidden.remove(id)){rule.hidden.add(id);if(id.equals(rule.priority))rule.priority="";}save();});
            int index=i;
            button("↑",x+202,y,24,()->{if(index>0){Collections.swap(rule.order,rule.order.indexOf(id),rule.order.indexOf(all.get(index-1).getName()));save();}});
            button("↓",x+228,y,24,()->{if(index+1<all.size()){Collections.swap(rule.order,rule.order.indexOf(id),rule.order.indexOf(all.get(index+1).getName()));save();}});
        }
        button("<",x,height-50,28,()->{page--;refresh();});
        button(">",x+30,height-50,28,()->{page++;refresh();});
        button("- 50 ms",x+60,height-50,65,()->{Preferences.INSTANCE.holdMillis=Math.max(100,Preferences.INSTANCE.holdMillis-50);save();});
        button("+ 50 ms",x+127,height-50,65,()->{Preferences.INSTANCE.holdMillis=Math.min(2000,Preferences.INSTANCE.holdMillis+50);save();});
        button("Done",x+194,height-50,58,this::onClose);
    }
    @Override public void render(net.minecraft.client.gui.GuiGraphics g,int mx,int my,float delta){
        g.fill(0,0,width,height,0xD0182020);
        g.drawCenteredString(font,"Key Selector — "+key.getDisplayName().getString(),width/2,10,0xFFFFFF);
        g.drawCenteredString(font,"Hold: "+Preferences.INSTANCE.holdMillis+" ms",width/2,height-22,0xFFFFFF);
        super.render(g,mx,my,delta);
    }
    @Override public void onClose(){Preferences.INSTANCE.save();minecraft.setScreen(null);}
    @Override public boolean isPauseScreen(){return false;}
}
