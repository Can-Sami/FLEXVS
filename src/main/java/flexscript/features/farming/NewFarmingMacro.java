package flexscript.features.farming;

import flexscript.config.Config;
import flexscript.Main;
import flexscript.features.mouselocker.MouseLocker;
import flexscript.utils.InventoryUtils;
import flexscript.utils.ShadyRotation;
import flexscript.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NewFarmingMacro {

    private static boolean left = true;
    private static boolean forward = false;

    public static int startCounter = 0;

    private static long currentTimeMillisSetSpawn = Utils.currentTimeMillis();

    private static long currentTimeMillisSwitch = Utils.currentTimeMillis();

    @SubscribeEvent
    public void farmRow(@NotNull TickEvent.ClientTickEvent event) {
        if (Main.farmingMacro) {
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), left);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), !left);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), forward);
            if (Main.mc.thePlayer.motionX <= 0.001 && Main.mc.thePlayer.motionX >= -0.001 && Main.mc.thePlayer.motionZ <= 0.001 && Main.mc.thePlayer.motionZ >= -0.001) {
                int randomDelay = getRandomDelay();
                if (Config.INSTANCE.autoSetHome && Utils.currentTimeMillis() - currentTimeMillisSetSpawn >= randomDelay) {
                    Main.mc.thePlayer.sendChatMessage("/setspawn");
                    currentTimeMillisSetSpawn = Utils.currentTimeMillis();
                }
                if (Utils.currentTimeMillis() - currentTimeMillisSwitch >= randomDelay) {
                    forward = !forward;
                    left = !left;

                    currentTimeMillisSwitch = Utils.currentTimeMillis();
                }
                if (Utils.currentTimeMillis() - currentTimeMillisSwitch <= (randomDelay) && Utils.currentTimeMillis() - currentTimeMillisSwitch >= 200 && !AntiStuck.antistuckOn && Config.INSTANCE.antiStuck) {
                    AntiStuck.antistuckOn = true;
                }
            }
            flip180Check();
        }
    }

    public final int getRandomDelay() {
        return 1000 + (new Random().nextInt() % (2000 - 1000 + 1));
    }

    public static void startFarming(){
            Main.farmingMacro = true;
            NewFarmingMacro.left = true;
            NewFarmingMacro.forward = false;
            MouseLocker.lockMouse();

            Main.startTime = System.currentTimeMillis();
            startCounter = InventoryUtils.getCounter();
    }

    public static void stopFarming(){
        Main.farmingMacro = false;
        MouseLocker.unLockMouse();

        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
    }

    private static long currentTimeMillis180 = Utils.currentTimeMillis();

    public static void flip180Check() {
        if (Main.mc.thePlayer.posY != 0 && Main.mc.thePlayer.motionY <= -0.3 && Config.INSTANCE.drop180 && Utils.currentTimeMillis() - currentTimeMillis180 >= 15000) {
            currentTimeMillis180 = Utils.currentTimeMillis();
            float yaw = Main.mc.thePlayer.rotationYaw;
            float pitch = Main.mc.thePlayer.rotationPitch;
            ShadyRotation.Rotation rotation = new ShadyRotation.Rotation(pitch, yaw - 180);
            ShadyRotation.smoothLook(rotation, Main.configFile.smoothLookVelocity, () -> {});
        }
    }
}
