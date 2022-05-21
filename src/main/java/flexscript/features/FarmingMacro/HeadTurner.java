package flexscript.features.FarmingMacro;

import java.util.ArrayList;

import flexscript.Main;
import flexscript.utils.ShadyRotation;
import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class HeadTurner {
    
    public static void turn180(){
        ShadyRotation.smoothLook(ShadyRotation.getRotationToBlock(closestMithril()), Main.configFile.smoothLookVelocity, () -> {});
    }

    private static BlockPos closestMithril() {
        int r = 6;
        if (Main.mc.thePlayer == null || Main.mc.theWorld == null) return null;
        BlockPos playerPos = Main.mc.thePlayer.getPosition();
        playerPos = playerPos.add(0, 1, 0);
        Vec3 playerVec = Main.mc.thePlayer.getPositionVector();
        Vec3i vec3i = new Vec3i(r, r, r);
        ArrayList<Vec3> chests = new ArrayList<Vec3>();
        if (playerPos != null) {
            for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.add(vec3i), playerPos.subtract(vec3i))) {
                IBlockState blockState = Main.mc.theWorld.getBlockState(blockPos);
                if (isMithril(blockState)) {
                    chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                }
                if(Main.configFile.includeOres) {
                    if (blockState.getBlock() == Blocks.coal_ore || blockState.getBlock() == Blocks.diamond_ore || blockState.getBlock() == Blocks.gold_ore || blockState.getBlock() == Blocks.redstone_ore || blockState.getBlock() == Blocks.iron_ore || blockState.getBlock() == Blocks.lapis_ore || blockState.getBlock() == Blocks.emerald_ore || blockState.getBlock() == Blocks.netherrack ) {
                        chests.add(new Vec3(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5));
                    }
                }
            }
        }
        double smallest = 9999;
        Vec3 closest = null;
        for (int i = 0; i < chests.size(); i++) {
            double dist = chests.get(i).distanceTo(playerVec);
            if (dist < smallest) {
                smallest = dist;
                closest = chests.get(i);
            }
        }
        if (closest != null && smallest < 5) {
            return new BlockPos(closest.xCoord, closest.yCoord, closest.zCoord);
        }
        return null;
    }


    private static boolean isMithril(IBlockState blockState) {
        if(blockState.getBlock() == Blocks.stone) {
            return true;
        } else if(blockState.getBlock() == Blocks.wool) {
            return true;
        } else if(blockState.getBlock() == Blocks.stained_hardened_clay) {
            return true;
        } else if(!Main.configFile.ignoreTitanium && blockState.getBlock() == Blocks.stone && blockState.getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE_SMOOTH) {
            return true;
        } else if(blockState.getBlock() == Blocks.gold_block) {
            return true;
        }
        return false;
    }

    
}