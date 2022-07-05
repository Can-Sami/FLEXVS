package flexscript.features.killaura;

import flexscript.Config;
import flexscript.Main;
import flexscript.utils.ChatUtils;
import flexscript.utils.RenderUtils;
import flexscript.utils.RotationUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class KillAura {
    public static Entity target;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Config.INSTANCE.killAura || !Main.configFile.killAura || Main.mc.thePlayer == null || Main.mc.theWorld == null)
            return;
        //target = getEntity();
        target = getClosestCreeper();
        if(target != null){
            RotationUtils.faceEntity(target);
        }
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Config.INSTANCE.killAura) return;
        if (target == null) return;
        RenderUtils.drawEntityBox(target, Color.GREEN, Main.configFile.lineWidth, event.partialTicks);
    }

    private static Entity getClosestCreeper() {
        Entity eman = null;
        double closest = 9999.0;
        if(Main.mc.theWorld == null) return null;
        for (Entity entity1 : (Main.mc.theWorld.loadedEntityList)) {
            if (entity1 instanceof EntityCreeper && !(((EntityCreeper) entity1).getHealth() == 0)) {
                double dist = entity1.getDistance(Main.mc.thePlayer.posX, Main.mc.thePlayer.posY, Main.mc.thePlayer.posZ);
                if (dist < closest) {
                    if(dist < 200) {
                        closest = dist;
                        eman = entity1;
                    }
                }
            }
        }
        if(eman != null){
            ChatUtils.sendMessage(eman.getName());
            ChatUtils.sendMessage(String.valueOf(eman.getDistanceToEntity(Main.mc.thePlayer)));
            return eman;

        }else {
            return null;
        }
    }

}
