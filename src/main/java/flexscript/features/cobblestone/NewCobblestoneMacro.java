package flexscript.features.cobblestone;

import flexscript.config.Config;
import flexscript.Main;
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
    public static boolean shouldGhostBlock = true;
    private Timer timer = new Timer();

    @SubscribeEvent
    public void blockCobble(@NotNull TickEvent.ClientTickEvent event) {
        if (Main.blockMacro) {
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);
            if(Config.INSTANCE.shift){
                KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), true);
            }
            if (shouldSetHome) {
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/sethome");
                shouldSetHome = false;
            }
            Main.blockMacro = true;
            Main.startTime = System.currentTimeMillis();
            if (shouldGhostBlock) {
                timer.schedule(new AntiGhostBlock(), Config.INSTANCE.antiGhostPeriod * 60000, Config.INSTANCE.antiGhostPeriod * 60000);
                shouldGhostBlock = false;
            }
            if (Config.INSTANCE.mouseLock && !MouseLocker.locked) {
                MouseLocker.lockMouse();
            }

        }
    }


    public static void startCobble(){
        Main.blockMacro = true;
        if(Config.INSTANCE.mouseLock){
            MouseLocker.lockMouse();
        }
        Main.startTime = System.currentTimeMillis();
    }

    public static void stopCobble(){
        shouldGhostBlock = true;
        shouldSetHome = true;
        Main.farmingMacro = false;
        MouseLocker.unLockMouse();

        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
    }
}
