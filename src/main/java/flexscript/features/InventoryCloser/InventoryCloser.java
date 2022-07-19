package flexscript.features.InventoryCloser;

import flexscript.Main;
import flexscript.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InventoryCloser {
    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event) {
        if (Main.farmingMacro || Main.cobbleMacro || Main.sugarCaneMacro) {
            if (Minecraft.getMinecraft().currentScreen == null) {
                event.setCanceled(true);
                ChatUtils.sendMessage("§fYou can not open menu while the bot is running.");
            }
        }
    }
}