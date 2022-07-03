package flexscript.features.mouselocker;

import net.minecraft.client.Minecraft;

public class MouseLocker {

    public static float defSens = Minecraft.getMinecraft().gameSettings.mouseSensitivity;

    public static void lockMouse(){
        Minecraft.getMinecraft().gameSettings.mouseSensitivity = -1F/3F;
    }

    public static void unLockMouse(){
        Minecraft.getMinecraft().gameSettings.mouseSensitivity = defSens;
    }

}
