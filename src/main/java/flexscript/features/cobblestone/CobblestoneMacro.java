package flexscript.features.cobblestone;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.mouselocker.MouseLocker;
import flexscript.timers.*;
import flexscript.utils.InventoryUtils;
import flexscript.utils.PlayerUtils;
import flexscript.utils.ScoreboardUtils;
import flexscript.utils.SetHome;

import java.util.Timer;

public class CobblestoneMacro {

    public static int startCobble = 0;
    static Timer timer = new Timer();
    static Timer timer2 = new Timer();   
    static Timer timer3 = new Timer();

    public static void blockMacroStarter(){
        if(ScoreboardUtils.scoreboardContains("Your Island")){
            if(Config.INSTANCE.shift){
                startCobble = InventoryUtils.getCounter();
                Main.blockMacro = true;
                Main.startTime = System.currentTimeMillis();

                PlayerUtils.attackHold();
                timer.schedule(new SetHome(), 100);
                timer.schedule(new ForwardInf(), 1000, 20000);
                timer2.schedule(new ShiftInf(), 500, 20000);
                if(Config.INSTANCE.antiGhost){
                    timer3.schedule(new AntiGhostBlock(), Config.INSTANCE.antiGhostPeriod, Config.INSTANCE.antiGhostPeriod);
                }
                if(Config.INSTANCE.mouseLock){
                    MouseLocker.lockMouse();
                }

            } else if(!Config.INSTANCE.shift){
                startCobble = InventoryUtils.getCounter();
                Main.blockMacro = true;
                Main.startTime = System.currentTimeMillis();
                PlayerUtils.attackHold();
                timer.schedule(new SetHome(), 100);
                timer.schedule(new ForwardInf(), 1000, 20000);
                timer3.schedule(new ShiftBlock(), 400);
                if(Config.INSTANCE.antiGhost){
                    timer2.schedule(new AntiGhostBlock(), Config.INSTANCE.antiGhostPeriod * 60000, Config.INSTANCE.antiGhostPeriod * 60000);
                }
                if(Config.INSTANCE.mouseLock){
                    MouseLocker.lockMouse();
                }
            }
        }
    }

    public static void stopScript(){
        Main.nukeBlocks = false;
        Main.blockMacro = false;
        timer.cancel();
        timer3.cancel();
        timer2.cancel();
        PlayerUtils.stopAttacking();

        timer3 = new Timer();
        timer = new Timer();
        timer2 = new Timer();
        MouseLocker.unLockMouse();
    }

}
