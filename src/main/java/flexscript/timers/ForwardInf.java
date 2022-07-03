package flexscript.timers;

import java.util.TimerTask;

import flexscript.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class ForwardInf extends TimerTask{

    
    @Override
    public void run() {
        int left = Minecraft.getMinecraft().gameSettings.keyBindForward.getKeyCode();

        while (Main.blockMacro) {
            KeyBinding.setKeyBindState(left, true);


        }
        KeyBinding.setKeyBindState(left, false);


    }

    
}
