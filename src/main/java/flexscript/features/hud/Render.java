package flexscript.features.hud;

import flexscript.Config;
import flexscript.Main;
import flexscript.features.profitcalculator.ProfitCalculator;
import flexscript.utils.ChatUtils;
import gg.essential.universal.UGraphics;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import gg.essential.elementa.components.Window;
import gg.essential.universal.UGraphics;

public class Render {
    private final Minecraft mc = Minecraft.getMinecraft();
    private final Window window;
    private final StatusHUD statusHUD;

    public Render() {
        window = new Window();
        statusHUD = new StatusHUD(window);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void render(RenderGameOverlayEvent.Post event) {
        if (mc.theWorld != null && mc.thePlayer != null && (Config.INSTANCE.HUD) && event.type == RenderGameOverlayEvent.ElementType.TEXT) {
            window.removeChild(statusHUD);
            if (Config.INSTANCE.HUD) {
                ProfitCalculator.calculateProfit();
                window.addChild(statusHUD);
            }
            UGraphics.enableAlpha();
            window.draw();
            UGraphics.disableAlpha();
        }
    }
}
