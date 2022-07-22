package flexscript.features.profitcalculator;

import flexscript.Main;
import flexscript.features.farming.NewFarmingMacro;
import flexscript.utils.InventoryUtils;
import flexscript.utils.Utils;
import gg.essential.elementa.state.BasicState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;


import java.util.concurrent.TimeUnit;

import static flexscript.Main.mc;
import static flexscript.Main.tempProfit;

public class ProfitCalculator {
    public static  int lastCounter = 0;
    public volatile static BasicState<String> realProfit = new BasicState<>("§cNo Profit Made");
    public volatile static BasicState<String> profitHour = new BasicState<>("§cNo Profit Made");
    public volatile static BasicState<String> MacroTime = new BasicState<>("§cMacro Timer: not running");
    public static  double wartProfit = 0;
    public static  double wheatProfit = 0;
    public static  double caneProfit = 0;
    public static  double carrotProfit = 0;
    public static  double potatoProfit = 0;
    public volatile static double totalProfit = 0;


    public static int getCropDiff() {
        if(NewFarmingMacro.startCounter == 0) return 0;
        lastCounter = InventoryUtils.getCounter();
        return lastCounter - NewFarmingMacro.startCounter;

    }

    public static void calculateProfit() {
        if(!Main.farmingMacro && !Main.sugarCaneMacro) return;
        if(mc.theWorld == null) return;
        if(Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() == null) return;
        final ItemStack held = mc.thePlayer.getHeldItem();
        if (held.getDisplayName().contains("Newton")) {
            wartProfit = getCropDiff() * 3;

        } else if (held.getDisplayName().contains("Pythagorean")) {
            potatoProfit = getCropDiff() * 2.4;

        }else if (held.getDisplayName().contains("Euclid's")) {
            wheatProfit = getCropDiff() * 2;

        } else if (held.getDisplayName().contains("Gauss")) {
            carrotProfit = getCropDiff() * 1.6;

        } else if (held.getDisplayName().contains("Turing")) {
            caneProfit = getCropDiff() * 2;

        }
        totalProfit = wartProfit + wheatProfit + caneProfit + carrotProfit + potatoProfit + tempProfit;
        realProfit.set("Total Profit Made: §a$" + Utils.formatNumber(Math.round(totalProfit)));
        profitHour.set("Profit per Hour: §a$" + Utils.formatNumber(Math.round(profitHour(totalProfit))));
        MacroTime.set("Macro Time: §e" + Utils.formatTime(macroTime()));

    }

    public static double profitHour(double total){
        if(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Main.startTime) > 0){
            return 3600f * total / TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Main.startTime);
        }
        return 0;
    }

    public static long macroTime(){
        if(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - Main.startTime) > 0){
            return System.currentTimeMillis() - Main.startTime;
        }

        return 0;
    }
}
