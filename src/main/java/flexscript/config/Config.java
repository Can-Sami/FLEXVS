package flexscript.config;

import gg.essential.vigilance.Vigilant;
import gg.essential.vigilance.data.*;
import net.minecraft.client.Minecraft;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.File;
import java.util.Comparator;


public class Config extends Vigilant {
    public static Config INSTANCE = new Config();


    public boolean AutoReady = false;


    public int lineWidth = 3;
    
    @Property(type = PropertyType.SWITCH, name = "&bStatues HUD", description = "Shows some information in HUD.",
    category = "General", subcategory = "HUD Overlay")
    public boolean HUD = true;

    @Property(type = PropertyType.SWITCH, name = "&bMob ESP", description = "Highlights the mobs around you.",
            category = "General", subcategory = "Mob ESP")
    public boolean mobEsp = false;

    @Property(type = PropertyType.SLIDER, name = "&bESP Range", description = "Esp blocks distance.",
            category = "General", subcategory = "Mob ESP", min = 0, max = 64)
    public int mobEspRange = 64;

    @Property(type = PropertyType.SWITCH, name = "&bShow NameTag", description = "Shows the Name Tag of the entity.",
            category = "General", subcategory = "Mob ESP")
    public boolean showNameTag = true;

    @Property(type = PropertyType.COLOR, name = "Mob ESP Color", description = "Sets the color for the ESP box.", category = "General", subcategory = "Mob ESP")
    public static Color espColor = Color.MAGENTA;

    @Property(type = PropertyType.SWITCH, name = "&bShow Distance", description = "Shows your distance from the entity.",
            category = "General", subcategory = "Mob ESP")
    public boolean showDistance = true;

    @Property(type = PropertyType.SWITCH, name = "&bAuto SetHome", description = "Automatically sets home in one period. **NEEDED FOR THE FAILSAFE**",
            category = "General", subcategory = "Fail Safe")
    public boolean autoSetHome = true;

    @Property(type = PropertyType.SWITCH, name = "&bFail Safe", description = "Gets you back to your island if something happens.",
            category = "General", subcategory = "Fail Safe")
    public boolean failSafe = true;


    @Property(type = PropertyType.SWITCH, name = "&bAnti Stuck", description = "If you somehow get stuck this will fix it.",
            category = "Farming", subcategory = "General")
    public volatile boolean antiStuck = true;

    @Property(type = PropertyType.SWITCH, name = "&bTurn 180 on drop", description = "Turns your head 180 degree on Y level change.",
            category = "Farming", subcategory = "General")
    public volatile boolean drop180 = true;

    @Property(type = PropertyType.SWITCH, name = "&bResync", description = "Detects when you stop breaking crops and fixes.",
            category = "Farming", subcategory = "General")
    public volatile boolean resync = true;

    @Property(type = PropertyType.SWITCH, name = "&bAnti Stuck Jump", description = "Adds additional Jumping motion to Anti Stuck.",
            category = "Farming", subcategory = "General")
    public volatile boolean antiStuckJump = false;


    @Property(type = PropertyType.SLIDER, name = "&bRotation Sharpness", description = "How fast your head will turn. (1 is fast 40 is slow)",
            category = "General", subcategory = "General", min = 1, max = 40)
    public int smoothLookVelocity = 3;

    @Property(type = PropertyType.SWITCH, name = "&bEntity Reach", description = "Interact with entities from far away. (NOT RECOMMENDED)",
            category = "General", subcategory = "General")
    public boolean entityReach = false;

    public boolean sticky = false;

    @Property(type = PropertyType.SWITCH, name = "&bFast Break", description = "Break blocks / crops much faster than usual. (ALSO WORKS WITH MACROS)",
            category = "General", subcategory = "General")
    public boolean fastBreak = true;

    @Property(type = PropertyType.SWITCH, name = "&bAuto Reconnect", description = "Reconnects you if you get disconnected while Macroing.",
            category = "General", subcategory = "General")
    public boolean reconnect = true;

    @Property(type = PropertyType.SWITCH, name = "&bHold Shift", description = "Holds Shift while you are moving.",
    category = "Cobblestone Macro", subcategory = "General")
    public boolean shift = false;

    @Property(type = PropertyType.SWITCH, name = "&bBlock Break Checker", description = "If mod detects that you are not breaking blocks it will go hub and come back.",
            category = "Cobblestone Macro", subcategory = "General")
    public boolean breakFailsafeCobble = false;

    @Property(type = PropertyType.SWITCH, name = "&bMouse Locker", description = "Prevents your mouse movements while macros are working.",
            category = "General", subcategory = "General")
    public boolean mouseLock = false;

    @Property(type = PropertyType.SWITCH, name = "&bAnti Ghost Blocks", description = "You will go to Hub and come back in a period.",
    category = "Cobblestone Macro", subcategory = "General")
    public boolean antiGhost = true;

    @Property(type = PropertyType.SWITCH, name = "&bAuto Sell Cobble Stone", description = "Sells your Cobble Stone to Bazaar on full inventory. (REQUIRES COOKIE)",
            category = "Cobblestone Macro", subcategory = "General")
    public boolean sellCobble = true;

    @Property(type = PropertyType.SLIDER, name = "&bAnti Ghost Blocks Period", description = "in every how much Minutes you will go to Hub and come back.",
    category = "Cobblestone Macro", subcategory = "General", max = 60 , min = 1)
    public int antiGhostPeriod = 30;


    public int alchsleep = 300;

    public int farmNukeIndex = 0;

    @Property(type = PropertyType.SELECTOR, name = "&bCrop Aura Speed", description = "Choose how many blocks per second Crop Aura will break",
            category = "Farming", subcategory = "Crop Nuker", options = {"Looking at", "Closest Block"})
    public int farmShapeIndex = 1;

    @Property(type = PropertyType.SELECTOR, name = "&bCrop Aura Speed", description = "Choose how many blocks per second Crop Aura will break",
            category = "Farming", subcategory = "Crop Nuker", options = {"40 BPS", "80 BPS"})
    public int farmSpeedIndex = 0;


    @Property(type = PropertyType.SWITCH, name = "&bRandomized actions", description = "This will make u look less sus",
            category = "Mining", subcategory = "General")
    public boolean mithrilLook = false;

    @Property(type = PropertyType.SWITCH, name = "&bSkip Titanium", description = "Mithril nuker will ignore titanium",
            category = "Mining", subcategory = "Mithril")
    public boolean ignoreTitanium = false;

    public boolean includeOres = false;


    public boolean onlyOres = false;

    @Property(type = PropertyType.SELECTOR, name = "&bMithril Macro Priority", description = "Determine the order the macro will breaks blocks in",
            category = "Mining", subcategory = "Mithril", options = {"Highest value to lowest", "Lowest value to highest","Any"})
    public int mithrilMacroPrio = 0;

    @Property(type = PropertyType.SWITCH, name = "&bPingless Mining", description = "Mines the next block before the previous block breaks, instabreak only",
            category = "Mining", subcategory = "General")
    public boolean pinglessMining = false;


    @Property(type = PropertyType.SELECTOR, name = "&bPingless Mining Speed", description = "Determine how long to wait before mining the next block",
            category = "Mining", subcategory = "General", options = {"20 BPS (Legit)", "40 BPS", "80 BPS"})
    public int pinglessSpeed = 0;

    @Property(type = PropertyType.SWITCH, name = "Prioritize Gemstone Blocks", description = "Will first search for full blocks, then panes",
            category = "Mining", subcategory = "Gemstone")
    public boolean prioblocks = false;

    @Property(type = PropertyType.SWITCH, name = "Kill Aura", description = "Hits mobs automatically.",
            category = "Kill Aura", subcategory = "Gemstone")
    public boolean killAura = false;

    @Property(type = PropertyType.SELECTOR, name = "Kill Aura Class",
            category = "Kill Aura", subcategory = "Kill Aura", options = {"Melee", "Archer", "Mage", "Shaman"})
    public int killAuraType = 0;

    @Property(type = PropertyType.BUTTON, name = "&bALT TAB", description = "Mines the next block before the previous block breaks, instabreak only",
            category = "General", subcategory = "General")
    public void click(){
        Minecraft.getMinecraft().gameSettings.pauseOnLostFocus = false;
        
    }

    public int nameRenderType = 0;

    public int skiblock = 3000;

    public boolean wydsi = true;




    public Config() {
        super(new File("./config/flexscript/config.toml"), "Flex Scripts Premium", new JVMAnnotationPropertyCollector(), new ConfigSorting());
        initialize();
    }

    public static class ConfigSorting extends SortingBehavior {
        @NotNull
        @Override
        public Comparator<Category> getCategoryComparator() {
            return (o1, o2) -> {
                if(o1.getName().equals("FlexScriptPremium")) {
                    return -1;
                } else if(o2.getName().equals("FlexScriptPremium")) {
                    return 1;
                } else {
                    return o1.getName().compareTo(o2.getName());
                }
            };
        }
    }
}   