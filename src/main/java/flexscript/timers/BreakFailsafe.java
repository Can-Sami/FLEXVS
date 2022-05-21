package flexscript.timers;

import java.util.TimerTask;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.BreakFailsafe.BreakMain;
import flexscript.utils.ChatUtils;
import flexscript.utils.Sleep;
import net.minecraft.client.Minecraft;

public class BreakFailsafe extends TimerTask {
    
    /* § */

    int wartCountLast = 0;

    @Override
    public void run() {
        if(Main.farmingMacro && Config.INSTANCE.resetCheatDetection)
        wartCountLast = BreakMain.getWartCount();
        Sleep.sleep(120000);
        if (BreakMain.getWartCount() == wartCountLast) {
            if(!Main.farmingMacro) return;
            ChatUtils.sendMessage("§fBlock break is being prevented! FailSafe is starting now.");
            Sleep.sleep(5000);
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/hub");
        }
    }
}
