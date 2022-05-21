package flexscript.features.BreakFailsafe;

import flexscript.Config;
import flexscript.Main;
import flexscript.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreakMain {

    private static volatile int recentCount = 0;
    private int tempCount = 0;

    @SubscribeEvent
    public void onPlayerTickEvent(PlayerInteractEvent event) {
        if(Main.farmingMacro && Config.INSTANCE.resetCheatDetection){
            EntityPlayer player = event.entityPlayer;
                for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                    if (player.inventory.getStackInSlot(i) != null) {
                        String slotItem = player.inventory.getStackInSlot(i).getDisplayName();
                        Integer slotItemCount = player.inventory.getStackInSlot(i).stackSize;
    
                        if (slotItem.contains("Nether Wart")) {
                            tempCount += slotItemCount;
                        }
                    }
                }

                if(recentCount == tempCount){
                    tempCount = 0;
                    recentCount = 0;
                    Minecraft.getMinecraft().thePlayer.sendChatMessage("/hub");
                }
                recentCount = tempCount;
                tempCount = 0;
                ChatUtils.sendMessage("Recent: " + recentCount);
            }
        }


        public static int getWartCount(){
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            int count = 0;
            for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
                if (player.inventory.getStackInSlot(i) != null) {
                    String slotItem = player.inventory.getStackInSlot(i).getDisplayName();
                    Integer slotItemCount = player.inventory.getStackInSlot(i).stackSize;
    
                    if (slotItem.contains("Nether Wart")) {
                        count += slotItemCount;
                    }
                }
            }
            return count;
        }

}
