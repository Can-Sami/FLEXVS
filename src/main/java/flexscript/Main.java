package flexscript;

import flexscript.features.*;
import flexscript.features.InventoryCloser.InventoryCloser;
import flexscript.features.esp.ArmorStandESP;
import flexscript.features.gemstoneaura.GemstoneAura;
import flexscript.features.mithril.MithrilMacro;
import flexscript.features.mithril.MithrilNuker;
import flexscript.features.mithril.PinglessMining;
import flexscript.features.autoreconnect.autoreconnect;
import flexscript.features.autosell.SellCobblestone;
import flexscript.features.cobblestone.CobbleStoneBreaker;
import flexscript.features.cobblestone.CobblestoneMacro;
import flexscript.features.breakfailsafe.BreakFailsafeCrops;
import flexscript.features.farming.FarmingMacro;
import flexscript.features.farming.CropNuker;
import flexscript.features.foraging.ForagingNuker;
import flexscript.features.hud.Render;
import flexscript.features.powdermacro.PowderMacro;
import flexscript.features.profitcalculator.ProfitCalculator;
import flexscript.utils.*;
import flexscript.whitelist.WhitelistHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.*;

@Mod(modid = "flexpremium", name = "FlexPremium", version = "3.1")

public class Main {
    //every 20 tick will make it 1
    public static int tickCount = 0;

    Thread checkWhitelist = new Thread(new WhitelistHandler());
    public static GuiScreen display = null;
    public static Config configFile = Config.INSTANCE;
    public static KeyBinding[] keyBinds = new KeyBinding[9];
    public static boolean endermanMacro = false;
    public static boolean gemNukeToggle = false;
    public static boolean mithrilNuker = false;
    public static volatile boolean farmingMacro = false;
    public static volatile boolean blockMacro = false;
    public static boolean forageOnIsland = false;
    public static boolean nukeCrops = false;
    public static boolean nukeBlocks = false;
    public static boolean nukeWood = false;
    public static boolean placeCane = false;
    private static boolean firstLoginThisSession = true;
    private static boolean oldanim = false;
    public static boolean init = false;
    public static boolean mithrilMacro = false;
    private boolean issue = false;
    public static boolean pauseCustom = false;
    public static boolean tested = false;
    public static volatile boolean wasFarming = false;
    public static volatile boolean wasBlock = false;
    private Thread thread;
    public static volatile double tempProfit = 0;
    public static boolean powderMacro = false;

    public static volatile long startTime = 0;

    public static final Minecraft mc = Minecraft.getMinecraft();

    public static HashMap<String, String> nameCache = new HashMap<>();
    public static HashMap<String, String> rankCache = new HashMap<>();
    public static ArrayList<String> hashedCache = new ArrayList<>();
    public static HashMap<String, String> names = new HashMap<>();
    public static HashMap<String, String> ranks = new HashMap<>();

    @Mod.EventHandler
    public void onFMLInitialization(FMLPreInitializationEvent event) {
        File directory = new File(event.getModConfigurationDirectory(), "flexscript");
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        if (issue)
            return;
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EntityReach());
        MinecraftForge.EVENT_BUS.register(new CropNuker());
        MinecraftForge.EVENT_BUS.register(new CobbleStoneBreaker());
        MinecraftForge.EVENT_BUS.register(new MithrilNuker());
        MinecraftForge.EVENT_BUS.register(new Render());
        MinecraftForge.EVENT_BUS.register(new ForagingNuker());
        MinecraftForge.EVENT_BUS.register(new PlayerUtils());
        MinecraftForge.EVENT_BUS.register(new PinglessMining());
        MinecraftForge.EVENT_BUS.register(new MithrilMacro());
        MinecraftForge.EVENT_BUS.register(new ShadyRotation());
        MinecraftForge.EVENT_BUS.register(new ScoreboardUtils());
        MinecraftForge.EVENT_BUS.register(new InventoryCloser());
        MinecraftForge.EVENT_BUS.register(new BreakFailsafeCrops());
        MinecraftForge.EVENT_BUS.register(new SellCobblestone());
        MinecraftForge.EVENT_BUS.register(new autoreconnect());
        MinecraftForge.EVENT_BUS.register(new ArmorStandESP());
        MinecraftForge.EVENT_BUS.register(new PowderMacro());
        MinecraftForge.EVENT_BUS.register(new GemstoneAura());

        configFile.initialize();
        ClientCommandHandler.instance.registerCommand(new OpenSettings());
        checkWhitelist.start();

        keyBinds[0] = new KeyBinding("Foraging Aura toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Foraging");
        keyBinds[1] = new KeyBinding("Mithril Aura Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Mining");
        keyBinds[2] = new KeyBinding("Cane Placer Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Farming");
        keyBinds[3] = new KeyBinding("Mithril Macro Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Mining");
        keyBinds[4] = new KeyBinding("Farming Macro Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Farming");
        keyBinds[5] = new KeyBinding("Block Breaking Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Blocks");
        keyBinds[6] = new KeyBinding("Crop Aura Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Farming");
        keyBinds[7] = new KeyBinding("Powder Macro", Keyboard.KEY_NONE, "Flex PREMIUM - Mining");
        keyBinds[8] = new KeyBinding("Gemstone Aura", Keyboard.KEY_NONE, "Flex PREMIUM - Mining");


        for (KeyBinding keyBind : keyBinds) {
            ClientRegistry.registerKeyBinding(keyBind);
        }

    }

    @Mod.EventHandler
    public void post(FMLPostInitializationEvent event) {
        Loader.instance().getActiveModList().forEach(modContainer -> {
            if (modContainer.getModId().equals("oldanimations")) {
                oldanim = true;
            }
        });
    }


    //block macro or farming macro failsafe
    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        if (farmingMacro  || blockMacro ) {
            ChatUtils.sendMessage("§fFail Safe is triggered. You will be put in your island in few seconds.");
            wasFarming = farmingMacro;
            wasBlock = blockMacro;


            nukeCrops = false;
            farmingMacro = false;
            FarmingMacro.stopScript();

            blockMacro = false;
            CobblestoneMacro.stopScript();
            nukeBlocks = false;

            thread = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    if (ScoreboardUtils.scoreboardContains("hypixel.net/ptl")) {
                        Main.mc.thePlayer.sendChatMessage("/skyblock");
                        Thread.sleep(10000);
                        if(wasFarming){
                            FarmingMacro.farmingMacroStarter();
                            ChatUtils.sendMessage("farming: " + wasFarming + " block: " + wasBlock);
                        }else if(wasBlock){
                            CobblestoneMacro.blockMacroStarter();
                            ChatUtils.sendMessage("farming: " + wasFarming + " block: " + wasBlock);
                        }
                    } else if (ScoreboardUtils.scoreboardContains("Village")) {
                        Main.mc.thePlayer.sendChatMessage("/is");
                        Thread.sleep(10000);
                        if(wasFarming){
                            FarmingMacro.farmingMacroStarter();
                            ChatUtils.sendMessage("farming: " + wasFarming + " block: " + wasBlock);
                        }else if(wasBlock){
                            CobblestoneMacro.blockMacroStarter();
                            ChatUtils.sendMessage("farming: " + wasFarming + " block: " + wasBlock);
                        }
                    } else if (ScoreboardUtils.scoreboardContains("Rank:")) {
                        Main.mc.thePlayer.sendChatMessage("/Skyblock");
                        if(wasFarming){
                            FarmingMacro.farmingMacroStarter();
                            ChatUtils.sendMessage("farming: " + wasFarming + " block: " + wasBlock);
                        }else if(wasBlock){
                            CobblestoneMacro.blockMacroStarter();
                            ChatUtils.sendMessage("farming: " + wasFarming + " block: " + wasBlock);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "FailSafe");
            thread.start();

        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START)
            return;
        if (mc.thePlayer == null || mc.theWorld == null)
            return;
        if (mc.gameSettings.limitFramerate == 1) {
            mc.gameSettings.setOptionFloatValue(GameSettings.Options.FRAMERATE_LIMIT, 260.0F);
        }
        if (display != null) {
            try {
                mc.displayGuiScreen(display);
            } catch (Exception e) {
                e.printStackTrace();
            }
            display = null;
        }

        tickCount += 1;
        tickCount %= 20;

    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent event) {
         if(!WhitelistHandler.Raw.contains(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString())) 
         return;
        if (keyBinds[0].isPressed()) {
            nukeWood = !nukeWood;
            String str = nukeWood ? "§fYou have successfully §bEnabled §fForaging BOT."
                    : "§fYou have successfully §cDisabled §fForaging BOT.";
            ChatUtils.sendMessage(str);
        } else if (keyBinds[1].isPressed()) {
            mithrilNuker = !mithrilNuker;
            String str = mithrilNuker ? "§fYou have successfully §bEnabled §fMithril Aura."
                    : "§fYou have successfully §cDisabled §fMithril Aura.";
            ChatUtils.sendMessage(str);
        } else if (keyBinds[2].isPressed()) {
            placeCane = !placeCane;
            String str = placeCane ? "§fYou have successfully §bEnabled §fSugarCane Placer."
                    : "§fYou have successfully §cDisabled §fSugarCane Placer.";
            ChatUtils.sendMessage(str);
        } else if (keyBinds[7].isPressed()) {
            powderMacro = !powderMacro;
            String str = powderMacro ? "§fYou have successfully §bEnabled §fPowder Macro."
                    : "§fYou have successfully §cDisabled §fPowder Macro.";
            ChatUtils.sendMessage(str);
        } else if (keyBinds[8].isPressed()) {
            gemNukeToggle = !gemNukeToggle;
            String str = gemNukeToggle ? "§fYou have successfully §bEnabled §fGemstone Aura."
                    : "§fYou have successfully §cDisabled §fGemstone Aura.";
            ChatUtils.sendMessage(str);
        } else if(keyBinds[6].isPressed()){
            nukeCrops = !nukeCrops;
            String str = nukeCrops ? "§fYou have successfully §bEnabled §fCrop Aura."
                    : "§fYou have successfully §cDisabled §fCrop Aura.";
            ChatUtils.sendMessage(str);
                FarmingMacro.startCounter = InventoryUtils.getCounter();
                if(!nukeCrops){
                    tempProfit = ProfitCalculator.totalProfit;
                }
                if(nukeCrops) Main.startTime = System.currentTimeMillis();

        } else if (keyBinds[3].isPressed()) {
            mithrilMacro = !mithrilMacro;
            String str = mithrilMacro ? "§fYou have successfully §bEnabled §fMithril BOT."
                    : "§fYou have successfully §cDisabled §fMithril BOT.";
            ChatUtils.sendMessage(str);
        } else if (keyBinds[4].isPressed()) {
            if (ScoreboardUtils.scoreboardContains("Your Island")) {
                farmingMacro = !farmingMacro;
                String str = farmingMacro ? "§fYou have successfully §bEnabled §fFarming BOT."
                        : "§fYou have successfully §cDisabled §fFarming BOT.";
                ChatUtils.sendMessage(str);
                if (farmingMacro) {
                    FarmingMacro.farmingMacroStarter();
                } else {
                    FarmingMacro.stopScript();
                }
            } else if (!Main.farmingMacro || !Config.INSTANCE.failSafe) {
                ChatUtils.sendMessage("§fYou can not start the bot outside your island.");
            }
        } else if (keyBinds[5].isPressed()) {
            if (ScoreboardUtils.scoreboardContains("Your Island")) {
                blockMacro = !blockMacro;
                String str = blockMacro ? "§fYou have successfully §bEnabled §fBlock Breaking BOT."
                        : "§fYou have successfully §cDisabled §fBlock Breaking BOT.";
                ChatUtils.sendMessage(str);
                if (blockMacro) {
                    CobblestoneMacro.blockMacroStarter();
                } else {
                    CobblestoneMacro.stopScript();
                    wasBlock = false;
                }
            } else if (!Main.blockMacro || !Config.INSTANCE.failSafe) {
                ChatUtils.sendMessage("§fYou can not start the bot outside your island.");
            }
        }
    }
}