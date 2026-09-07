package baritone.utils;

import baritone.api.BaritoneAPI;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class BaritoneControlScreen extends Screen {
    private EditBox x, y, z, search; private final List<Button> results = new ArrayList<>();
    public BaritoneControlScreen() { super(Component.literal("Baritone Control")); }
    @Override public boolean isPauseScreen() { return false; }
    @Override protected void init() {
        int c=width/2; x=addRenderableWidget(new EditBox(font,c-150,42,90,20,Component.literal("X"))); y=addRenderableWidget(new EditBox(font,c-50,42,90,20,Component.literal("Y"))); z=addRenderableWidget(new EditBox(font,c+50,42,90,20,Component.literal("Z")));
        x.setValue("0"); y.setValue("64"); z.setValue("0");
        addRenderableWidget(Button.builder(Component.literal("Goto"),b->cmd("goto "+x.getValue()+" "+y.getValue()+" "+z.getValue())).bounds(c-150,68,90,20).build());
        addRenderableWidget(Button.builder(Component.literal("Stop"),b->cmd("stop")).bounds(c-50,68,90,20).build());
        addRenderableWidget(Button.builder(Component.literal("World pick"),b->mc.setScreen(new GuiClick())).bounds(c+50,68,90,20).build());
        search=addRenderableWidget(new EditBox(font,c-150,106,300,20,Component.literal("中文/English/ID"))); search.setResponder(v->refresh()); refresh();
    }
    private void refresh(){ results.forEach(this::removeWidget); results.clear(); String q=search==null?"":search.getValue().toLowerCase(Locale.ROOT); int i=0,c=width/2; for(Block b:BuiltInRegistries.BLOCK){ Identifier id=BuiltInRegistries.BLOCK.getKey(b); String n=b.getName().getString(); if(!q.isEmpty()&&!id.toString().toLowerCase(Locale.ROOT).contains(q)&&!n.toLowerCase(Locale.ROOT).contains(q))continue; results.add(addRenderableWidget(Button.builder(Component.literal(n),v->cmd("mine "+id)).bounds(c-150+(i%2)*155,136+(i/2)*22,150,20).build())); if(++i>=12)break; } }
    private void cmd(String s){ BaritoneAPI.getProvider().getPrimaryBaritone().getCommandManager().execute(s); }
    @Override public void extractRenderState(GuiGraphicsExtractor g,int mx,int my,float pt){ super.extractRenderState(g,mx,my,pt); g.drawCenteredString(font,title,width/2,15,0xFFFFFF); g.drawString(font,"Coordinates",width/2-150,31,0xA0A0A0); g.drawString(font,"Search localized block name or registry ID",width/2-150,96,0xA0A0A0); }
}
