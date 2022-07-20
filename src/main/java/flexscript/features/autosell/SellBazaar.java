package flexscript.features.autosell;

import flexscript.config.Config;
import flexscript.Main;
import flexscript.features.cobblestone.NewCobblestoneMacro;
import flexscript.features.farming.NewFarmingMacro;
import flexscript.features.sugarcane.NewSugarCaneMacro;
import flexscript.utils.ChatUtils;
import flexscript.utils.InventoryUtils;
import flexscript.utils.Sleep;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

//§
public class SellBazaar {
    private static Thread thread;
    private static boolean cobble;
    private static boolean sugar;
    private static boolean crops;
    private static boolean selling = false;

    @SubscribeEvent
    public final void tick(TickEvent.ClientTickEvent event) {
        if(selling) return;
        if(Main.tickCount != 1) return;
        if(!Config.INSTANCE.sellCobble && !Config.INSTANCE.sellFarming ) return;
        if(!Main.cobbleMacro && !Main.farmingMacro && !Main.sugarCaneMacro) return;
        if(Main.mc.thePlayer.inventory.getFirstEmptyStack() == -1){
            ChatUtils.sendMessage("§f Auto Sell is starting in a moment...");
            cobble = Main.cobbleMacro;
            sugar = Main.sugarCaneMacro;
            crops = Main.farmingMacro;
            selling = true;
            thread = new Thread(() -> {
                try {
                    if (Minecraft.getMinecraft().currentScreen == null) {
                        Sleep.sleep(3000);
                        stopMacros();
                        Sleep.sleep(3000);
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
                                    Thread.sleep(3000);
                                    startMacros();
                                    selling = false;
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
    }


    private static void stopMacros(){
        if(cobble) {
            NewCobblestoneMacro.stopMacro();
        } else if(sugar) {
            NewSugarCaneMacro.stopMacro();
        } else if(crops) {
            NewFarmingMacro.stopMacro();
        }
    }

    private static void startMacros(){
        if(cobble) {
            NewCobblestoneMacro.startMacro();
        } else if(sugar) {
            NewSugarCaneMacro.startMacro();
        } else if(crops) {
            NewFarmingMacro.startMacro();
        }
    }

}
