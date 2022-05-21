package flexscript.timers;

import java.util.TimerTask;


import flexscript.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class RightClick extends TimerTask {

    @Override
    public void run() {
            int rc = Minecraft.getMinecraft().gameSettings.keyBindUseItem.getKeyCode();

        
                long oldTime = System.currentTimeMillis();
        
                while (System.currentTimeMillis() - oldTime < 10 && Main.farmingMacro) {
                    KeyBinding.setKeyBindState(rc, true);
                }
                KeyBinding.setKeyBindState(rc, false);
        

    }

}
