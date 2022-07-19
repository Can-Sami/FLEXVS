package flexscript.features.failsafe;

import flexscript.Main;
import flexscript.features.sugarcane.NewSugarCaneMacro;
import flexscript.utils.ChatUtils;
import flexscript.utils.ScoreboardUtils;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static flexscript.Main.cobbleMacro;
import static flexscript.Main.sugarCaneMacro;

public class FSSugarCane {
    private Thread thread;

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        if (sugarCaneMacro) {
            ChatUtils.sendMessage("§fFail Safe is triggered. You will be put in your island in few seconds.");
            Main.wasScane = sugarCaneMacro;
            sugarCaneMacro = false;
            NewSugarCaneMacro.stopMacro();

            thread = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    if (ScoreboardUtils.scoreboardContains("hypixel.net/ptl")) {
                        Main.mc.thePlayer.sendChatMessage("/skyblock");
                        Thread.sleep(10000);
                        startIfShould();
                    } else if (ScoreboardUtils.scoreboardContains("Village")) {
                        Main.mc.thePlayer.sendChatMessage("/is");
                        Thread.sleep(10000);
                        startIfShould();
                    } else if (ScoreboardUtils.scoreboardContains("Rank:")) {
                        Main.mc.thePlayer.sendChatMessage("/skyblock");
                        Thread.sleep(10000);
                        startIfShould();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "FailSafe");
            thread.start();

        }
    }

    private void startIfShould() {
        if (Main.wasScane) {
            NewSugarCaneMacro.startMacro();
            Main.wasScane = false;
        }
    }
}
