package flexscript.features.autosell;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.cobblestone.NewCobblestoneMacro;
import flexscript.utils.ChatUtils;
import flexscript.utils.InventoryUtils;
import flexscript.utils.Sleep;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

//§
public class SellCobblestone {
    private static Thread thread;
    private static Thread threadStart;

    public static void sellCobble() {
        thread = new Thread(() -> {
            try {
                ChatUtils.sendMessage("§f AutoSell is starting...");

                if (Minecraft.getMinecraft().currentScreen == null) {
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/bz");
                    Thread.sleep(1000);
                    if (InventoryUtils.inventoryNameContains("Bazaar")) {
                        InventoryUtils.clickOpenContainerSlot(9);
                        Thread.sleep(1000);
                        InventoryUtils.clickOpenContainerSlot(38);
                        Thread.sleep(1000);
                        if (InventoryUtils.inventoryNameContains("Are you sure")) {
                            InventoryUtils.clickOpenContainerSlot(11);
                            Thread.sleep(1000);
                            InventoryUtils.clickOpenContainerSlot(11);
                            Thread.sleep(1000);
                            if (InventoryUtils.inventoryNameContains("Are you sure")) {
                                Minecraft.getMinecraft().thePlayer.closeScreen();
                                Thread.sleep(5000);
                                NewCobblestoneMacro.startCobble();
                            }
                        }

                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Test");
        thread.start();
    }

    @SubscribeEvent
    public void onMessageReceived(ClientChatReceivedEvent event) {
        if (!Main.blockMacro) return;
        if (!event.message.getUnformattedText().contains("is full!")) return;
        if (!Config.INSTANCE.sellCobble) return;
        threadStart = new Thread(() -> {
            
                Sleep.sleep(3000);
                NewCobblestoneMacro.stopCobble();
                Sleep.sleep(3000);
                sellCobble();
        }, "start");
        threadStart.start();
    }
}
