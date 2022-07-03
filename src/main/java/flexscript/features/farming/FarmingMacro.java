package flexscript.features.farming;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.breakfailsafe.BreakFailsafeCrops;
import flexscript.features.mouselocker.MouseLocker;
import flexscript.timers.*;
import flexscript.utils.*;

import java.util.Timer;

public class FarmingMacro {
    static Timer timer = new Timer();
    static Timer timer2 = new Timer();
    private static float playerYaw = 0;

    public static int startCounter = 0;


    public static void farmingMacroStarter(){
        if(ScoreboardUtils.scoreboardContains("Your Island")){
            if(!Config.INSTANCE.closeFly){
                Main.startTime = System.currentTimeMillis();
                startCounter = InventoryUtils.getCounter();
                PlayerUtils.attackHold(true);
                MouseLocker.lockMouse();
                Main.farmingMacro = true;

                timer.schedule(new SetHome(), 0, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);
                timer.schedule(new Left(), 0, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);
                timer.schedule(new Forward(), Config.INSTANCE.sideTimer, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);
                timer.schedule(new Right(), Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);
                timer.schedule(new Forward(), Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer, Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer + Config.INSTANCE.sideTimer + Config.INSTANCE.forwardTimer);

            }else if(Config.INSTANCE.closeFly){
                Main.startTime = System.currentTimeMillis();
                startCounter = InventoryUtils.getCounter();
                MouseLocker.lockMouse();
                Main.farmingMacro = true;
                PlayerUtils.attackHold(true);
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
        MouseLocker.unLockMouse();
        Main.farmingMacro = false;
        timer.cancel();
        timer2.cancel();
        PlayerUtils.attackHold(false);
        timer = new Timer();
        timer2 = new Timer();
    }

}
