package flexscript.timers;

import java.util.TimerTask;

import flexscript.Config;
import flexscript.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class Forward extends TimerTask {


    @Override
    public void run() {
        int left = Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode();

        long oldTime = System.currentTimeMillis();

        while (System.currentTimeMillis() - oldTime < Config.INSTANCE.forwardTimer * 1000L && Main.farmingMacro) {
            KeyBinding.setKeyBindState(left, true);


        }
        KeyBinding.setKeyBindState(left, false);


    }

}
