package flexscript.timers;

import java.util.TimerTask;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.cobblestone.NewCobblestoneMacro;
import flexscript.utils.ChatUtils;
import flexscript.utils.Sleep;
import net.minecraft.client.Minecraft;

public class AntiGhostBlock extends TimerTask {

    /* § */

    @Override
    public void run() {
        if (Main.blockMacro && Config.INSTANCE.antiGhost)

            ChatUtils.sendMessage("§fYou will be sent to Hub for resetting Ghost Blocks!");
        Sleep.sleep(5000);
        Minecraft.getMinecraft().thePlayer.sendChatMessage("/hub");
        NewCobblestoneMacro.shouldGhostBlock = true;
    }
}
