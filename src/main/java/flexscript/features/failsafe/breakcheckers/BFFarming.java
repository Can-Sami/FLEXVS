package flexscript.features.failsafe.breakcheckers;

import flexscript.Main;
import flexscript.config.Config;
import flexscript.utils.InventoryUtils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class BFFarming {

    private int ticks = 0;
    private int lastCount;

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if(!Main.farmingMacro && !Main.sugarCaneMacro) return;
        if(!Config.INSTANCE.resync) return;
        if(Main.mc.theWorld == null) return;
        if(Main.mc.thePlayer == null) return;
        ticks++;
        if (ticks == 600) {
            ticks = 0;
            if (lastCount == InventoryUtils.getCounter()) {
                Main.mc.thePlayer.sendChatMessage("/hub");
            }
            lastCount = InventoryUtils.getCounter();
        }
    }
}
