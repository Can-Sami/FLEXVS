package flexscript.features.breakfailsafe;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.cobblestone.CobblestoneMacro;
import flexscript.utils.ChatUtils;
import flexscript.utils.InventoryUtils;
import net.minecraft.client.Minecraft;

public class BreakFailsafeCobble {

    private static int lastCount;

    public static void BreakFailsafe() throws InterruptedException {
        if(!Main.blockMacro) return;
        if(Minecraft.getMinecraft().theWorld == null) return;
        if(Minecraft.getMinecraft().thePlayer == null) return;
        if(InventoryUtils.getCounter() == lastCount){
            ChatUtils.sendMessage("§f Block breaking is being prevented ");
            Thread.sleep(3000);
            CobblestoneMacro.stopScript();

            lastCount = 0;
        }
        lastCount = InventoryUtils.getCounter();

    }
}
