package flexscript.features.FarmingMacro;

import flexscript.Config;
import flexscript.Main;
import flexscript.timers.BreakFailsafe;
import flexscript.timers.Forward;
import flexscript.timers.Left;
import flexscript.timers.Right;
import flexscript.timers.Shift;
import flexscript.utils.ScoreboardUtils;
import flexscript.utils.SetHome;

import java.util.Timer;

public class FarmingMain {
    static Timer timer = new Timer();
    static Timer timer2 = new Timer();

    public static void farmingMacroStarter(){
        if(ScoreboardUtils.scoreboardContains("Your Island")){
            if(!Config.INSTANCE.closeFly){
                Main.nukeCrops = true;
                Main.farmingMacro = true;
                if(Config.INSTANCE.resetCheatDetection){
                    timer2.schedule(new BreakFailsafe(), 1000, 125000);
                }
                timer.schedule(new SetHome(), 0, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);
                timer.schedule(new Left(), 0, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);
                timer.schedule(new Forward(), Config.INSTANCE.sideTimer, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);
                timer.schedule(new Right(), Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);
                timer.schedule(new Forward(), Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);

            }else if(Config.INSTANCE.closeFly){
                Main.nukeCrops = true;
                Main.farmingMacro = true;
                if(Config.INSTANCE.resetCheatDetection){
                    timer2.schedule(new BreakFailsafe(), 1000, 125000);
                }
                timer.schedule(new SetHome(), 0, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + 1000);
                timer.schedule(new Shift(), 0);
                timer.schedule(new Left(), 1000, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + 1000);
                timer.schedule(new Forward(), Config.INSTANCE.sideTimer + 1000, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + 1000);
                timer.schedule(new Right(), Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + 1000, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + 1000);
                timer.schedule(new Forward(), Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + 1000, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + 1000);
            
            } 

        }
    }

    public static void stopScript(){
        Main.nukeCrops = false;
        Main.farmingMacro = false;
        timer.cancel();
        timer2.cancel();
        timer = new Timer();
        timer2 = new Timer();
    }
}
