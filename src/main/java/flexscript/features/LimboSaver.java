package flexscript.features;

import flexscript.Main;
import flexscript.timers.ShiftLimbo;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Timer;

public class LimboSaver{
 /*   static Timer timer = new Timer();
 // You are AFK.

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        String msg = event.message.getUnformattedText();
        if(Main.wasBotOn){
            if(msg.contains("You are AFK.") || msg.contains("/limbo for more information.") || msg.contains("You were spawned in Limbo.")){
                timer.schedule(new ShiftLimbo(), 1000);

                (new Thread(() -> {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Main.wasBotOn = false;
                  })).start();

            }
        }
    }
    */
}
