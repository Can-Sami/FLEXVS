package flexscript.features.cobblestone;


import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import flexscript.Main;
import flexscript.utils.PlayerUtils;

import java.util.ArrayList;

public class CobbleStoneBreaker {
    private static BlockPos blocks;
    private ArrayList<BlockPos> broken = new ArrayList<>();

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (!Main.nukeBlocks || Main.mc.thePlayer == null || !Main.mc.thePlayer.onGround) {
            broken.clear();
            return;
        }
        blocks = closestBlock();
        if (blocks != null) {
            if(broken.size() > 80) {
                broken.clear();
            }
            Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.START_DESTROY_BLOCK, blocks, EnumFacing.DOWN));
            PlayerUtils.swingItem();
            broken.add(blocks);
        }

    }

    private BlockPos closestBlock() {
        int r = 7;
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> warts = new ArrayList<>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
                if (blockState.getBlock() == Blocks.cobblestone || blockState.getBlock() == Blocks.sand) {
                    if (!broken.contains(blockPos)) {
                        warts.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (Vec3 wart : warts) {
            double dist = wart.distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = wart;
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }
    
}
