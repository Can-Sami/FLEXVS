package flexscript.features.breakfailsafe;

import flexscript.Main;
import flexscript.features.cobblestone.NewCobblestoneMacro;
import flexscript.utils.ChatUtils;
import flexscript.utils.InventoryUtils;
import flexscript.utils.Sleep;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import java.util.TimerTask;

public class BreakFailsafeCobble extends TimerTask {

    private int lastCount;

    @Override
    public void run() {

        if (!Main.blockMacro) return;
        if (Minecraft.getMinecraft().theWorld == null) return;
        if (Minecraft.getMinecraft().thePlayer == null) return;
        if (InventoryUtils.getAmountInAllSlots("Enchanted Cobblestone") == lastCount) {
            ChatUtils.sendMessage("§f Block breaking is being prevented. You will be logged out and log back in.");
            NewCobblestoneMacro.stopCobble();
            Sleep.sleep(5000);
            Main.mc.setIngameFocus();
            Sleep.sleep(500);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
            Sleep.sleep(500);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), true);
            KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindAttack.getKeyCode(), false);
            Sleep.sleep(500);
            NewCobblestoneMacro.startCobble();
            lastCount = -1;
        }
        System.out.println(lastCount + " " + InventoryUtils.getAmountInAllSlots("Enchanted Cobblestone"));
        lastCount = InventoryUtils.getAmountInAllSlots("Enchanted Cobblestone");
    }
}
