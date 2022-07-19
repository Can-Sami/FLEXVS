package flexscript.features.autoreconnect;

import flexscript.config.Config;
import flexscript.Main;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class autoreconnect {
    private static final Minecraft mc = Minecraft.getMinecraft();
    private static final ServerData hypixelServerData = new ServerData("foo", "mc.hypixel.net", false);

    @SubscribeEvent
    public final void tick(TickEvent.RenderTickEvent event) {
        if (!Main.wasBlock || !Main.wasScane || !Main.wasFarming) return;
        if(!Config.INSTANCE.reconnect) return;
        if((mc.currentScreen instanceof GuiDisconnected || mc.currentScreen instanceof GuiMultiplayer)){
            FMLClientHandler.instance().connectToServer(mc.currentScreen instanceof GuiMultiplayer ? mc.currentScreen : new GuiMultiplayer(new GuiMainMenu()), hypixelServerData);

        }

    }
}
