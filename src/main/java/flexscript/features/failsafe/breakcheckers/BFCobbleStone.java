package flexscript.features.failsafe.breakcheckers;

import flexscript.Main;
import flexscript.config.Config;
import flexscript.features.cobblestone.NewCobblestoneMacro;
import flexscript.utils.ChatUtils;
import flexscript.utils.InventoryUtils;
import flexscript.utils.Sleep;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.TimerTask;

public class BFCobbleStone {

    private int ticks = 0;
    private int lastCount;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(!Main.cobbleMacro) return;
        if(!Config.INSTANCE.resync) return;
        if(Main.mc.theWorld == null) return;
        if(Main.mc.thePlayer == null) return;
        ticks++;
        if (ticks == 600) {
            ticks = 0;
            if (lastCount == InventoryUtils.getAmountInAllSlots("Enchanted Cobblestone")) {
                Main.mc.thePlayer.sendChatMessage("/hub");
                lastCount = -1;
            }
            lastCount = InventoryUtils.getAmountInAllSlots("Enchanted Cobblestone");
        }
    }
}
