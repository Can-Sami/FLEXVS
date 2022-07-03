package flexscript.features.breakfailsafe;

import flexscript.Config;
import flexscript.Main;
import flexscript.utils.ChatUtils;
import flexscript.utils.InventoryUtils;
import net.minecraft.client.Minecraft;

import java.util.TimerTask;

public class BreakFailsafeCrops {
    private static int lastCount;

    public static void BreakFailsafe() throws InterruptedException {
        if(!Main.farmingMacro) return;
        if(!Config.INSTANCE.resetCheatDetection) return;
        if(Minecraft.getMinecraft().theWorld == null) return;
        if(Minecraft.getMinecraft().thePlayer == null) return;
        if(InventoryUtils.getCounter() == lastCount){
            ChatUtils.sendMessage("§f Block breaking is being prevented ");
            Thread.sleep(3000);
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/hub");
            lastCount = 0;
        }
        lastCount = InventoryUtils.getCounter();

    }

}
