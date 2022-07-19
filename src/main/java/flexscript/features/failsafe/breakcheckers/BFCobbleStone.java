package flexscript.features.failsafe.breakcheckers;

import flexscript.Main;
import flexscript.features.cobblestone.NewCobblestoneMacro;
import flexscript.utils.ChatUtils;
import flexscript.utils.InventoryUtils;
import flexscript.utils.Sleep;
import net.minecraft.client.Minecraft;

import java.util.TimerTask;

public class BFCobbleStone extends TimerTask {

    private int lastCount;

    @Override
    public void run() {

        if (!Main.cobbleMacro) return;
        if (Minecraft.getMinecraft().theWorld == null) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (InventoryUtils.getAmountInAllSlots("Enchanted Cobblestone") == lastCount) {
            ChatUtils.sendMessage("§f Block breaking is being prevented. You will be logged out and log back in.");
            Sleep.sleep(5000);
            Main.mc.setIngameFocus();
            Sleep.sleep(5000);
            NewCobblestoneMacro.startMacro();
            lastCount = -1;
        }
        System.out.println(lastCount + " " + InventoryUtils.getAmountInAllSlots("Enchanted Cobblestone"));
        lastCount = InventoryUtils.getAmountInAllSlots("Enchanted Cobblestone");
    }
}
