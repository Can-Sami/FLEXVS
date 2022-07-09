package flexscript.features.cobblestone;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.breakfailsafe.BreakFailsafeCobble;
import flexscript.features.mouselocker.MouseLocker;
import flexscript.timers.AntiGhostBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Timer;

public class NewCobblestoneMacro {

    private static boolean shouldSetHome = true;

    private static Timer ghostTimer = new Timer();
    private static Timer breakTimer = new Timer();

    @SubscribeEvent
    public void blockCobble(@NotNull TickEvent.ClientTickEvent event) {
        if (Main.blockMacro) {
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
            if (Config.INSTANCE.shift) {
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
            if (shouldSetHome) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/sethome");
                shouldSetHome = false;
            }
            Main.blockMacro = true;
            Main.startTime = System.currentTimeMillis();

            if (Config.INSTANCE.mouseLock && !MouseLocker.locked) {
                MouseLocker.lockMouse();
            }

        }
    }

    public static void startCobble() {
        Main.blockMacro = true;
        Main.startTime = System.currentTimeMillis();

        mouseLock();
        setupTimers();

    }

    public static void stopCobble() {
        shouldSetHome = true;

        ghostTimer.cancel();
        breakTimer.cancel();

        ghostTimer = new Timer();
        breakTimer = new Timer();

        Main.blockMacro = false;
        MouseLocker.unLockMouse();

        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
    }


    private static void setupTimers(){

        if (Config.INSTANCE.antiGhost) {
            ghostTimer.schedule(new AntiGhostBlock(), Config.INSTANCE.antiGhostPeriod * 60000, 
                Config.INSTANCE.antiGhostPeriod * 60000);
        }

        if (Config.INSTANCE.breakFailsafeCobble) {
            breakTimer.schedule(new BreakFailsafeCobble(), 10000, 10000);
        }

    }

    private static void mouseLock(){
        if (Config.INSTANCE.mouseLock) {
            MouseLocker.lockMouse();
        }
    }

}
