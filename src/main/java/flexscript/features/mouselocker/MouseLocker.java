package flexscript.features.mouselocker;

import flexscript.Config;
import flexscript.utils.ChatUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class MouseLocker {

    public static boolean locked = false;

    public static float defSens = Minecraft.getMinecraft().gameSettings.mouseSensitivity;

    public static void lockMouse(){
        Minecraft.getMinecraft().gameSettings.mouseSensitivity = -1F/3F;
        locked = true;

    }

    public static void unLockMouse(){
        Minecraft.getMinecraft().gameSettings.mouseSensitivity = defSens;
        locked = false;
    }

    @SubscribeEvent
    public void mouseEvent(MouseEvent event) {
        if(!Config.INSTANCE.mouseLock) return;
        if (!locked) return;
        event.setCanceled(true);
    }


}
