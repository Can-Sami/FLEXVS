package flexscript.features.sugarcane;

import flexscript.Main;
import flexscript.features.farming.NewFarmingMacro;
import flexscript.features.mouselocker.MouseLocker;
import flexscript.utils.BlockUtils;
import flexscript.utils.ChatUtils;
import flexscript.utils.InventoryUtils;
import flexscript.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static flexscript.utils.BlockUtils.getRelativeBlock;

public class NewSugarCaneMacro {
    private static boolean left = true;
    private static boolean forward = false;
    private static boolean sneak = false;
    private static boolean goingForward = false;
    private Block block;

    private Thread thread;

    public static int startCounter = 0;

    private static long currentTimeMillisSetSpawn = Utils.currentTimeMillis();

    private static long currentTimeMillisSwitch = Utils.currentTimeMillis();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Main.sugarCaneMacro) {
            if (BlockUtils.isWalkable(BlockUtils.getFrontBlock(1)) &&
                    BlockUtils.isWalkable(BlockUtils.getFrontBlock(2)) &&
                    BlockUtils.isWalkable(BlockUtils.getFrontBlock(3)) &&
                    !goingForward) {
                go3BlocksForward();
            }
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
            //KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), left);
            //KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), !left);
            //Sneak and forward need to work together every time.
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), sneak);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), sneak);


        }
    }


    private void go3BlocksForward() {
        goingForward = true;
        BlockPos blockPos = Main.mc.thePlayer.getPosition().add(0, 0, 3);
        thread = new Thread(() -> {
            while (Math.round(Main.mc.thePlayer.posZ) != blockPos.getZ()) {
                sneak = true;
            }
            sneak = false;
            goingForward = false;
            Main.sugarCaneMacro = false;
            ChatUtils.sendMessage(sneak + " " + goingForward + " "  + Main.sugarCaneMacro);
        }, "thread");
        thread.start();
    }


    public static void startFarming() {
        Main.sugarCaneMacro = true;
        MouseLocker.lockMouse();

        Main.startTime = System.currentTimeMillis();
        startCounter = InventoryUtils.getCounter();
    }

    public static void stopFarming() {
        Main.sugarCaneMacro = false;
        MouseLocker.unLockMouse();
        goingForward = false;

        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindSneak.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
        KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
    }

}
