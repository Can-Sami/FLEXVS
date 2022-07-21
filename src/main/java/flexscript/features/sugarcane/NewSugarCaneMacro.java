package flexscript.features.sugarcane;

import flexscript.Main;
import flexscript.config.Config;
import flexscript.features.mouselocker.MouseLocker;
import flexscript.utils.BlockUtils;
import flexscript.utils.InventoryUtils;
import flexscript.utils.ShadyRotation;
import flexscript.utils.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Random;


public class NewSugarCaneMacro {
    private static boolean left = true;
    private static boolean sneak = false;
    private static boolean right = false;

    private static int homeCounter = 0;

    private static boolean shouldGoInLane = false;
    private static boolean shouldSetSwitchTime = true;

    public static int startCounter = 0;

    private static long currentTimeMillisSetSpawn = Utils.currentTimeMillis();

    private static long currentTimeMillisSwitch = Utils.currentTimeMillis();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Main.sugarCaneMacro) {
            setupKeys();
            lineSwitcher();
            flip180Check();
        }
    }


    public static void startMacro() {
        Main.sugarCaneMacro = true;
        MouseLocker.lockMouse();

        Main.startTime = System.currentTimeMillis();
        startCounter = InventoryUtils.getCounter();
    }

    public static void stopMacro() {
        left = true;
        sneak = false;
        right = false;
        shouldGoInLane = false;
        shouldSetSwitchTime = true;

        Main.sugarCaneMacro = false;
        MouseLocker.unLockMouse();

        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
    }

    private void setupKeys() {
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), sneak);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), sneak);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), left);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), right);
    }

    private void checkIfAlligned() {
        if (!BlockUtils.isWalkable(BlockUtils.getLeftBlock())) {
            if (BlockUtils.getRightTopBlock() == Blocks.reeds && shouldSetSwitchTime) {
                currentTimeMillisSwitch = Utils.currentTimeMillis();
                shouldSetSwitchTime = false;
            }
            if (BlockUtils.getRightTopBlock() == Blocks.reeds && Utils.currentTimeMillis() - currentTimeMillisSwitch >= 10) {
                setHome();
                shouldSetSwitchTime = true;
                shouldGoInLane = true;

                right = true;
                left = false;
                sneak = false;
            }

        }
        if (!BlockUtils.isWalkable(BlockUtils.getRightBlock())) {
            if (BlockUtils.getLeftTopBlock() == Blocks.reeds && shouldSetSwitchTime) {
                currentTimeMillisSwitch = Utils.currentTimeMillis();
                shouldSetSwitchTime = false;
            }
            if (BlockUtils.getLeftTopBlock() == Blocks.reeds && Utils.currentTimeMillis() - currentTimeMillisSwitch >= 10) {
                setHome();
                shouldSetSwitchTime = true;
                shouldGoInLane = true;

                right = false;
                left = true;
                sneak = false;
            }

        }


    }

    public void lineSwitcher() {
        if (!BlockUtils.isWalkable(BlockUtils.getLeftBlock()) && !shouldGoInLane) {
            sneak = true;
            left = false;
            right = false;
            checkIfAlligned();
        }
        if (!BlockUtils.isWalkable(BlockUtils.getRightBlock()) && !shouldGoInLane) {
            sneak = true;
            left = false;
            right = false;
            checkIfAlligned();
        }
        if (BlockUtils.isWalkable(BlockUtils.getRightBlock()) && BlockUtils.isWalkable(BlockUtils.getLeftBlock())) {
            shouldGoInLane = false;
        }
    }

    public void setHome() {
        homeCounter++;
        if (homeCounter != 15) return;
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

