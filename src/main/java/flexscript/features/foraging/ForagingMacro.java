package flexscript.features.foraging;

import flexscript.Main;
import flexscript.config.Config;
import flexscript.features.pathfinding.PathFinder;
import flexscript.utils.PathFindUtils.RenderUtilsPathFinding;
import flexscript.utils.ScoreboardUtils;
import flexscript.utils.VectorUtils;
import net.minecraft.util.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

public class ForagingMacro {
    private int phase = 0;
    private Thread thread;



    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.foragingMacro) return;
        if (Main.mc.currentScreen != null) return;
        if(PathFinder.hasPath()) return;
        if(phase == 0) {
            goTo(new BlockPos(-113, 74, -30));
            phase++;
        }

    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!Main.foragingMacro) return;
        RenderUtilsPathFinding.renderWaypointText("Point 1", new BlockPos(-113, 74, -30), Color.RED.getRGB());
        RenderUtilsPathFinding.renderWaypointText("Point 1", new BlockPos(-117, 74, -32), Color.RED.getRGB());
        RenderUtilsPathFinding.renderWaypointText("Point 1", new BlockPos(-122, 74, -24), Color.RED.getRGB());
    }

    private void goTo(BlockPos goal) {
        thread = new Thread(() -> {
            flexscript.features.pathfinding.PathFinding.initWalk();
            PathFinder.setup(new BlockPos(VectorUtils.floorVec(Main.mc.thePlayer.getPositionVector())), goal, 0.0, 200);
        }, "PathFinding");
        thread.start();
    }

    private void isInForest() {
        if(ScoreboardUtils.scoreboardContains("Forest"))
    }

}
