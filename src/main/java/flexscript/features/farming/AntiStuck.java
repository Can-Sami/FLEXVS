package flexscript.features.farming;

import flexscript.config.Config;
import flexscript.Main;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.jetbrains.annotations.NotNull;

public class AntiStuck {
    public static boolean antistuckOn = false;

    private static int tick = 0;

    private static int direction = 0;

    @SubscribeEvent
    public void AntiStuckTick(@NotNull TickEvent.ClientTickEvent event) {
        if (antistuckOn && Main.mc.theWorld != null && Main.mc.thePlayer != null) {
            if (tick >= 20) {

                if (Config.INSTANCE.antiStuckJump) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), true);
                }

                if (direction == 0) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), true);

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);

                } else if (direction == 1) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), true);

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                } else if (direction == 2) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), true);

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                } else if (direction == 3) {
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), true);

                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                } else if (direction == 4) {
                    direction = 0;
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindLeft.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindForward.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindRight.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindBack.getKeyCode(), false);
                    KeyBinding.setKeyBindState(Main.mc.gameSettings.keyBindJump.getKeyCode(), false);
                    antistuckOn = false;
                }


                direction++;
                tick = 0;
            }
            tick++;
        }
    }
}
