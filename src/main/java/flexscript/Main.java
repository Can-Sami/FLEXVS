package flexscript;

import flexscript.HUD.Counter;
import flexscript.HUD.OverlayHandler;
import flexscript.features.*;
import flexscript.features.BreakFailsafe.BreakMain;
import flexscript.features.FarmingMacro.FarmingMain;
import flexscript.features.discordRPC.DiscordRPCManager;
import flexscript.utils.ChatUtils;
import flexscript.utils.PlayerUtils;
import flexscript.utils.ScoreboardUtils;
import flexscript.utils.ShadyRotation;
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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.io.*;
import java.util.*;

@Mod(modid = "flexpremium", name = "FlexPremium", version = "3.1")

public class Main {
    Thread checkWhitelist = new Thread(new WhitelistHandler());
    public static GuiScreen display = null;
    public static Config configFile = Config.INSTANCE;
    public static KeyBinding[] keyBinds = new KeyBinding[5];
    public static boolean endermanMacro = false;
    public static boolean gemNukeToggle = false;
    public static boolean mithrilNuker = false;
    public static volatile boolean farmingMacro = false;
    public static boolean forageOnIsland = false;
    public static boolean nukeCrops = false;
    public static boolean nukeWood = false;
    public static boolean placeCane = false;
    private static boolean firstLoginThisSession = true;
    private static boolean oldanim = false;
    public static boolean init = false;
    public static boolean mithrilMacro = false;
    private boolean issue = false;
    public static boolean pauseCustom = false;
    public static boolean tested = false;
    public static volatile boolean wasBotOn = false;
    private Thread thread;

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
        DiscordRPCManager.discordRPC();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EntityReach());
        MinecraftForge.EVENT_BUS.register(new CropNuker());
        MinecraftForge.EVENT_BUS.register(new MithrilNuker());
        MinecraftForge.EVENT_BUS.register(new ForagingNuker());
        MinecraftForge.EVENT_BUS.register(new PlayerUtils());
        MinecraftForge.EVENT_BUS.register(new PinglessMining());
        MinecraftForge.EVENT_BUS.register(new MithrilMacro());
        MinecraftForge.EVENT_BUS.register(new ShadyRotation());
        MinecraftForge.EVENT_BUS.register(new ScoreboardUtils());
        MinecraftForge.EVENT_BUS.register(new InventoryCloser());
        MinecraftForge.EVENT_BUS.register(new Counter());
        MinecraftForge.EVENT_BUS.register(new OverlayHandler());
        MinecraftForge.EVENT_BUS.register(new LimboSaver());
        MinecraftForge.EVENT_BUS.register(new BreakMain());
        configFile.initialize();
        ClientCommandHandler.instance.registerCommand(new OpenSettings());

        checkWhitelist.start();

        keyBinds[0] = new KeyBinding("Foraging Island Macro", Keyboard.KEY_NONE, "Flex PREMIUM - Foraging");
        keyBinds[1] = new KeyBinding("Mithril Aura Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Mining");
        keyBinds[2] = new KeyBinding("Cane Placer Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Farming");
        keyBinds[3] = new KeyBinding("Mithril Macro Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Mining");
        keyBinds[4] = new KeyBinding("Farming Macro Toggle", Keyboard.KEY_NONE, "Flex PREMIUM - Farming");

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

    @SubscribeEvent
    public void onWorldChange(WorldEvent.Unload event) {
        if (farmingMacro) {
            ChatUtils.sendMessage("§fFail Safe is triggered. You will be put in your island in few seconds.");
            wasBotOn = true;
            nukeCrops = false;
            farmingMacro = false;
            FarmingMain.stopScript();
            thread = new Thread(() -> {
                try {
                    Thread.sleep(10000);
                    if (ScoreboardUtils.scoreboardContains("hypixel.net/ptl")) {
                        Main.mc.thePlayer.sendChatMessage("/skyblock");
                        Thread.sleep(10000);
                        FarmingMain.farmingMacroStarter();
                    } else if (ScoreboardUtils.scoreboardContains("Village")) {
                        Main.mc.thePlayer.sendChatMessage("/is");
                        Thread.sleep(10000);
                        FarmingMain.farmingMacroStarter();
                    } else if (ScoreboardUtils.scoreboardContains("Rank:")) {
                        Main.mc.thePlayer.sendChatMessage("/Skyblock");
                        Thread.sleep(10000);
                        FarmingMain.farmingMacroStarter();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "FailSafe");
            thread.start();

        }
    }

    @SubscribeEvent
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
    }

    @SubscribeEvent
    public void key(InputEvent.KeyInputEvent event) {
         if(!WhitelistHandler.Raw.contains(Minecraft.getMinecraft().thePlayer.getGameProfile().getId().toString())) 
         return;
        if (keyBinds[0].isPressed()) {
            forageOnIsland = !forageOnIsland;
            String str = forageOnIsland ? "§fYou have successfully §bEnabled §fForaging BOT."
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
                    FarmingMain.farmingMacroStarter();
                } else {
                    FarmingMain.stopScript();
                }
            } else if (!Main.farmingMacro || !Config.INSTANCE.failSafe) {
                ChatUtils.sendMessage("§fYou can not start the bot outside your island.");
            }
        }
    }
}