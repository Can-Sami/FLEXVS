package flexscript.timers;

import flexscript.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import java.util.TimerTask;

public class ShiftInf extends TimerTask {

    @Override
    public void run() {
        int left = Minecraft.getMinecraft().gameSettings.keyBindSneak.getKeyCode();


        while (Main.cobbleMacro) {
            KeyBinding.setKeyBindState(left, true);
        }
        KeyBinding.setKeyBindState(left, false);

    }
}
