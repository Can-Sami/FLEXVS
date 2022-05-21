package flexscript.HUD;

import flexscript.Config;
import flexscript.Main;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OverlayHandler {

    
    
    int xPos;
    int yPos;

    public static String integerFormatter(long counter){
        if(counter * 3 <= 1000000){

            String str = counter * 3 + " Coins";
            return str;


        }
        else{
            String str = counter * 3 /1000000  + " Million Coins";

            return str;

        }

    }


    @SubscribeEvent
    public void Render(RenderGameOverlayEvent.Post event){

        xPos = event.resolution.getScaledWidth();
        yPos = event.resolution.getScaledHeight();
        if (event.type == RenderGameOverlayEvent.ElementType.TEXT && Config.INSTANCE.HUD) {


            Minecraft.getMinecraft().ingameGUI.drawRect(xPos - 40 /*X*/, yPos - 20 /*Y*/, xPos - 200 /*X1*/, yPos - 80 /*Y1*/, 0x80000000 /*COLOUR*/);


            Minecraft.getMinecraft().fontRendererObj.drawString("Netherwart Bot: " + Main.farmingMacro, xPos - 180, yPos - 70, 0x3fff00, false);
            Minecraft.getMinecraft().fontRendererObj.drawString("Counter: " + Counter.MutantCounter, xPos - 180, yPos - 60, 0xffd700, false);
            Minecraft.getMinecraft().fontRendererObj.drawString("Profit (NPC): " + integerFormatter(Counter.MutantCounter), xPos - 180, yPos - 50, 0xffd700, false);
            }
    }
    
}
