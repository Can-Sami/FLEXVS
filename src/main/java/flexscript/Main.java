package flexscript;

import flexscript.config.Config;
import flexscript.config.OpenSettings;
import flexscript.features.InventoryCloser.InventoryCloser;
import flexscript.features.cobblestone.NewCobblestoneMacro;
import flexscript.features.esp.ArmorStandESP;
import flexscript.features.failsafe.FSSugarCane;
import flexscript.features.failsafe.breakcheckers.BFCobbleStone;
import flexscript.features.failsafe.breakcheckers.BFFarming;
import flexscript.features.failsafe.FSCrops;
import flexscript.features.failsafe.FSCobbleStone;
import flexscript.features.farming.AntiStuck;
import flexscript.features.farming.NewFarmingMacro;
import flexscript.features.gemstoneaura.GemstoneAura;
import flexscript.features.killaura.KillAura;
import flexscript.features.mithril.MithrilMacro;
import flexscript.features.mithril.MithrilNuker;
import flexscript.features.mithril.PinglessMining;
import flexscript.features.autoreconnect.autoreconnect;
import flexscript.features.autosell.SellBazaar;
import flexscript.features.farming.CropNuker;
import flexscript.features.foraging.ForagingNuker;
import flexscript.features.hud.Render;
import flexscript.features.mouselocker.MouseLocker;
import flexscript.features.powdermacro.PowderMacro;
import flexscript.features.profitcalculator.ProfitCalculator;
import flexscript.features.sugarcane.NewSugarCaneMacro;
import flexscript.utils.*;
import flexscript.whitelist.WhitelistHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
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
    public static KeyBinding[] keyBinds = new KeyBinding[10];
    public static boolean endermanMacro = false;
    public static boolean gemNukeToggle = false;
    public static boolean mithrilNuker = false;
    public static volatile boolean farmingMacro = false;
    public static volatile boolean sugarCaneMacro = false;
    public static volatile boolean cobbleMacro = false;
    public static boolean forageOnIsland = false;
    public static boolean nukeCrops = false;
    public static boolean nukeBlocks = false;
    public static boolean nukeWood = false;
    public static boolean placeCane = false;
    private static boolean oldanim = false;
    public static boolean init = false;
    public static boolean mithrilMacro = false;
    private boolean issue = false;
    public static boolean pauseCustom = false;
    public static boolean tested = false;
    public static volatile boolean wasFarming = false;
    public static volatile boolean wasBlock = false;
    public static volatile boolean wasScane = false;
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
        MinecraftForge.EVENT_BUS.register(new BFFarming());
        MinecraftForge.EVENT_BUS.register(new BFCobbleStone());
        MinecraftForge.EVENT_BUS.register(new CropNuker());
        MinecraftForge.EVENT_BUS.register(new MithrilNuker());
        MinecraftForge.EVENT_BUS.register(new Render());
        MinecraftForge.EVENT_BUS.register(new ForagingNuker());
        MinecraftForge.EVENT_BUS.register(new PlayerUtils());
        MinecraftForge.EVENT_BUS.register(new PinglessMining());
        MinecraftForge.EVENT_BUS.register(new MithrilMacro());
        MinecraftForge.EVENT_BUS.register(new ShadyRotation());
        MinecraftForge.EVENT_BUS.register(new ScoreboardUtils());
        MinecraftForge.EVENT_BUS.register(new InventoryCloser());
        MinecraftForge.EVENT_BUS.register(new FSSugarCane());
        MinecraftForge.EVENT_BUS.register(new SellBazaar());
        MinecraftForge.EVENT_BUS.register(new autoreconnect());
        MinecraftForge.EVENT_BUS.register(new ArmorStandESP());
        MinecraftForge.EVENT_BUS.register(new PowderMacro());
        MinecraftForge.EVENT_BUS.register(new GemstoneAura());
        MinecraftForge.EVENT_BUS.register(new KillAura());
        MinecraftForge.EVENT_BUS.register(new NewFarmingMacro());
        MinecraftForge.EVENT_BUS.register(new AntiStuck());
        MinecraftForge.EVENT_BUS.register(new NewCobblestoneMacro());
        MinecraftForge.EVENT_BUS.register(new FSCobbleStone());
        MinecraftForge.EVENT_BUS.register(new FSCrops());
        MinecraftForge.EVENT_BUS.register(new NewSugarCaneMacro());


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
        keyBinds[9] = new KeyBinding("SugarCane Macro", Keyboard.KEY_NONE, "Flex PREMIUM - Farming");


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
    public void keyStroke(InputEvent.KeyInputEvent event) {
        if (!WhitelistHandler.Raw.contains(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString()))
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
        } else if (keyBinds[6].isPressed()) {
            nukeCrops = !nukeCrops;
            String str = nukeCrops ? "§fYou have successfully §bEnabled §fCrop Aura."
                    : "§fYou have successfully §cDisabled §fCrop Aura.";
            ChatUtils.sendMessage(str);
            NewFarmingMacro.startCounter = InventoryUtils.getCounter();
            if (!nukeCrops) {
                tempProfit = ProfitCalculator.totalProfit;
            }
            if (nukeCrops) Main.startTime = System.currentTimeMillis();

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
                    NewFarmingMacro.startMacro();
                } else {
                    NewFarmingMacro.stopMacro();
                }
            } else if (!Main.farmingMacro || !Config.INSTANCE.failSafe) {
                ChatUtils.sendMessage("§fYou can not start the bot outside your island.");
            }
        } else if (keyBinds[5].isPressed()) {
            if (ScoreboardUtils.scoreboardContains("Your Island")) {
                cobbleMacro = !cobbleMacro;
                String str = cobbleMacro ? "§fYou have successfully §bEnabled §fCobble Stone BOT."
                        : "§fYou have successfully §cDisabled §fCobble Stone BOT.";
                ChatUtils.sendMessage(str);
                if (cobbleMacro) {
                    NewCobblestoneMacro.startMacro();
                } else {
                    NewCobblestoneMacro.stopMacro();
                    wasBlock = false;
                }
            } else if (!Main.cobbleMacro || !Config.INSTANCE.failSafe) {
                ChatUtils.sendMessage("§fYou can not start the bot outside your island.");
            }
        } else if (keyBinds[9].isPressed()) {
            if (ScoreboardUtils.scoreboardContains("Your Island")) {
                sugarCaneMacro = !sugarCaneMacro;
                String str = sugarCaneMacro ? "§fYou have successfully §bEnabled §fSugarCane BOT."
                        : "§fYou have successfully §cDisabled §fSugarCane BOT.";
                ChatUtils.sendMessage(str);
                if (sugarCaneMacro) {
                    NewSugarCaneMacro.startMacro();
                } else {
                    NewSugarCaneMacro.stopMacro();
                    wasScane = false;
                }
            } else if (!Main.sugarCaneMacro || !Config.INSTANCE.failSafe) {
                ChatUtils.sendMessage("§fYou can not start the bot outside your island.");
            }
        }
    }
}