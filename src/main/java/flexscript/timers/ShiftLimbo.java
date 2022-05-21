package flexscript.timers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import java.util.TimerTask;

public class ShiftLimbo extends TimerTask {

    @Override
    public void run() {
        int left = Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode();

        long oldTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - oldTime < 500) {
            KeyBinding.setKeyBindState(left, true);
        }
        KeyBinding.setKeyBindState(left, false);


    }
}
