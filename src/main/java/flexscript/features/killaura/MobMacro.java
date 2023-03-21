package flexscript.features.killaura;

import com.sun.org.apache.xpath.internal.operations.Bool;
import flexscript.config.Config;
import flexscript.Main;
import flexscript.utils.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.RandomUtils;
import tv.twitch.chat.Chat;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

// TODO: when useItem triggers change target to next entity
public class KillAura {
    public static Entity target;
    private static Thread thread;

    static List<String> killed = new ArrayList<String>();

    private static Boolean isAttacking = false;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.killAura || !Main.configFile.killAura || Main.mc.thePlayer == null || Main.mc.theWorld == null)
            return;
        attackEnderman();
    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Config.INSTANCE.killAura) return;
        if (target == null) return;
        RenderUtils.drawEntityBox(target, Color.YELLOW, Main.configFile.lineWidth, event.partialTicks);
    }


    private static void attackEnderman() {
        double closest = 9999.0;
        if (isAttacking) return;
        Entity[] mobs = Main.mc.theWorld.loadedEntityList.toArray(new Entity[0]);
        for (Entity entity1 : mobs) {
            if (entity1 instanceof EntityEnderman && !(((EntityEnderman) entity1).getHealth() == 0)) {
                if(!((EntityEnderman) entity1).canEntityBeSeen(Main.mc.thePlayer)) continue;
                double dist = entity1.getDistance(Main.mc.thePlayer.posX, Main.mc.thePlayer.posY, Main.mc.thePlayer.posZ);
                if (dist < closest) {
                    if (dist < 200 && !killed.contains(entity1.getUniqueID().toString())) {
                        isAttacking = true;
                        closest = dist;
                        int randomdelay = getRandomDelay();
                        ShadyRotation.smoothLook(ShadyRotation.vec3ToRotation(entity1.getPositionEyes(3).addVector(0,-0.5,0)), Main.configFile.smoothLookVelocity, () -> {});
                        thread = new Thread(() -> {
                            Sleep.sleep(randomdelay);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                            Sleep.sleep(10);
                            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                            killed.add(entity1.getUniqueID().toString());
                            isAttacking = false;

                        }, "Test");
                        thread.start();
                    }
                }
            }
        }
    }

    public static int getRandomDelay() {
        return 300 + RandomUtils.nextInt(100, 500);
    }

}
