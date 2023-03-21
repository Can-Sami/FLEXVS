package flexscript.features.farming;

import flexscript.config.Config;
import flexscript.Main;
import flexscript.features.mouselocker.MouseLocker;
import flexscript.utils.*;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class NewFarmingMacro {
    private static boolean sneak = false;

    private static boolean left = false;
    private static boolean right = true;
    private static boolean forward = false;

    private static int homeCounter = 0;

    public static int startCounter = 0;

    private static long currentTimeMillisSetSpawn = Utils.currentTimeMillis();

    private static long currentTimeMillisSwitch = Utils.currentTimeMillis();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Main.farmingMacro) {
            setKeybinds();
            setConditions();
            flip180Check();
        }
    }

    private void setConditions(){
        //if left is blocked but right is not go right
        if(!BlockUtils.isWalkable(BlockUtils.getLeftBlock()) && BlockUtils.isWalkable(BlockUtils.getRightBlock())){
            right = true;
            left = false;
            forward = false;
        }
        //if right is blocked but left is not go left
        if(!BlockUtils.isWalkable(BlockUtils.getRightBlock()) && BlockUtils.isWalkable(BlockUtils.getLeftBlock())){
            left = true;
            right = false;
            forward = false;
        }
        //if right and back is blocked but forward is not go forward
        if(!BlockUtils.isWalkable(BlockUtils.getRightBlock()) && !BlockUtils.isWalkable(BlockUtils.getBackBlock()) && BlockUtils.isWalkable(BlockUtils.getFrontBlock(1))){
            forward = true;
            left = false;
            right = false;
            setHome();
        }
        //if left and back is blocked  but forward is not go forward
        if(!BlockUtils.isWalkable(BlockUtils.getLeftBlock()) && !BlockUtils.isWalkable(BlockUtils.getBackBlock()) && BlockUtils.isWalkable(BlockUtils.getFrontBlock(1))){
            forward = true;
            left = false;
            right = false;
            setHome();
        }
    }

    public void setKeybinds(){
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), forward);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), left);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), right);
    }

    public static void startMacro() {
        Main.farmingMacro = true;
        MouseLocker.lockMouse();

        Main.startTime = System.currentTimeMillis();
        startCounter = InventoryUtils.getCounter();
    }

    public static void stopMacro() {
        left = false;
        right = true;
        forward = false;
        Main.farmingMacro = false;
        MouseLocker.unLockMouse();

        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
    }

    public void setHome() {
        homeCounter++;
        if (homeCounter != 4) return;
        homeCounter = 0;
        int randomDelay = getRandomDelay();
        if (Config.INSTANCE.autoSetHome && Utils.currentTimeMillis() - currentTimeMillisSetSpawn >= randomDelay) {
            Main.mc.thePlayer.sendChatMessage("/setspawn");
            currentTimeMillisSetSpawn = Utils.currentTimeMillis();
        }
    }

    public final int getRandomDelay() {
        return 1000 + (new Random().nextInt() % (2000 - 1000 + 1));
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
