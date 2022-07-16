package flexscript.utils;


import flexscript.config.Config;
import flexscript.Main;
import net.minecraft.client.Minecraft;

import java.util.TimerTask;

public class SetHome extends TimerTask {

    public void run() {

        if(Main.farmingMacro && Config.INSTANCE.autoSetHome) {


            Minecraft.getMinecraft().thePlayer.sendChatMessage("/sethome");
        }

        if(Main.blockMacro) {
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/sethome");
        }

    }
}
