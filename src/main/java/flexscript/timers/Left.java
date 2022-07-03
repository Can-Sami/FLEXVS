package flexscript.timers;


import java.util.TimerTask;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.breakfailsafe.BreakFailsafeCrops;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class Left extends TimerTask {

    @Override
    public void run() {
        int left = Minecraft.getMinecraft().gameSettings.keyBindLeft.getKeyCode();

        long oldTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - oldTime < Config.INSTANCE.sideTimer * 1000L && Main.farmingMacro) {
                KeyBinding.setKeyBindState(left, true);

        }
        KeyBinding.setKeyBindState(left, false);

        try {
            BreakFailsafeCrops.BreakFailsafe();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
