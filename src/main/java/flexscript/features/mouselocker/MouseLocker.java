package flexscript.features.mouselocker;

import net.minecraft.client.Minecraft;
import net.minecraft.util.MouseHelper;
import org.lwjgl.input.Mouse;

public class MouseLocker {

    public static boolean locked = false;
    private static MouseHelper oldMouseHelper;
    private static boolean doesGameWantUngrabbed;

    public static void lockMouse() {
        Minecraft m = Minecraft.getMinecraft();
        if (locked) return;
        m.gameSettings.pauseOnLostFocus = false;
        if (oldMouseHelper == null) oldMouseHelper = m.mouseHelper;
        doesGameWantUngrabbed = !Mouse.isGrabbed();
        oldMouseHelper.ungrabMouseCursor();
        m.inGameHasFocus = true;
        m.mouseHelper = new MouseHelper() {
            @Override
            public void mouseXYChange() {
            }
            @Override
            public void grabMouseCursor() {
                doesGameWantUngrabbed = false;
            }
            @Override
            public void ungrabMouseCursor() {
                doesGameWantUngrabbed = true;
            }
        };
        locked = true;
    }

    /**
     * This function performs all the steps required to regrab the mouse.
     */
    public static void unLockMouse() {
        if (!locked) return;
        Minecraft m = Minecraft.getMinecraft();
        m.mouseHelper = oldMouseHelper;
        if (!doesGameWantUngrabbed) m.mouseHelper.grabMouseCursor();
        oldMouseHelper = null;
        locked = false;
    }
}
