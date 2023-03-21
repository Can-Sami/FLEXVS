package flexscript.features.killaura;

import flexscript.Main;
import flexscript.config.Config;
import flexscript.events.ClickEvent;
import flexscript.utils.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.apache.commons.lang3.RandomUtils;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import scala.tools.nsc.doc.model.Public;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class ClickAura {

    private static boolean isAttacking;
    private static List<String> killed = new ArrayList<String>();
    private static Thread thread;
    private static boolean isRunning = false;
    private static int runTime = 0;
    private static boolean canAttack = false;
    private static Entity target;


    @SubscribeEvent
    public void onUseItem(ClickEvent.Right event) {
        if (isAttacking) return;
        if(!canAttack) event.setCanceled(true);
        if (runTime > 3) {
            killed.clear();
            runTime = 0;
        }
        attackEnderman();
        ChatUtils.sendMessage("Right Clicked");

        // check if player is facing an enderman if doesn't don't attack

    }

    @SubscribeEvent
    public void renderWorld(RenderWorldLastEvent event) {
        if (!Config.INSTANCE.killAura) return;
        if (target == null) return;
        RenderUtils.drawEntityBox(target, Color.ORANGE, Main.configFile.lineWidth, event.partialTicks);
    }

    private static void attackEnderman() {

        double closest = 9999.0;
        Entity[] mobs = Main.mc.theWorld.loadedEntityList.toArray(new Entity[0]);
        for (Entity entity1 : mobs) {
            if (entity1 instanceof EntityEnderman && !(((EntityEnderman) entity1).getHealth() == 0)) {
                if (!((EntityEnderman) entity1).canEntityBeSeen(Main.mc.thePlayer)) continue;
                double dist = entity1.getDistance(Main.mc.thePlayer.posX, Main.mc.thePlayer.posY, Main.mc.thePlayer.posZ);
                if (dist < closest) {
                    if (dist < 200 && !killed.contains(entity1.getUniqueID().toString())) {
                        target = entity1;
                        closest = dist;
                        isAttacking = true;
                        ShadyRotation.smoothLook(ShadyRotation.vec3ToRotation(entity1.getPositionEyes(3).addVector(0, -0.3, 0)), Main.configFile.smoothLookVelocity, () -> {
                            thread = new Thread(() -> {
                                Sleep.sleep(getRandomDelay(100, 50, 100));
                                canAttack = true;
                                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindUseItem.getKeyCode(), true);
                                Sleep.sleep(getRandomDelay(50, 0, 40));
                                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                                canAttack = false;
                                killed.add(entity1.getUniqueID().toString());
                                isAttacking = false;
                                runTime++;
                            }, "Test");
                            thread.start();
                        });
                    }
                }
            }
        }

    }

    public static int getRandomDelay(int baseDelay, int randomMin, int randomMax) {
        return baseDelay + RandomUtils.nextInt(randomMin, randomMax);
    }

}


