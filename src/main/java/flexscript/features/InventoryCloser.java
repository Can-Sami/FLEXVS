package flexscript.features;

import flexscript.Main;
import flexscript.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InventoryCloser {
    Minecraft mc = Minecraft.getMinecraft();

    @SubscribeEvent
    public void guiOpenEvent(GuiOpenEvent event) {
        if (Main.farmingMacro) {
            Container playerContainer = mc.thePlayer.openContainer;
            if (event.gui instanceof GuiContainer) {
                event.setCanceled(true);
                ChatUtils.sendMessage("§fYou can not open Inventory while the bot is running.");
            }
        }
    }
}