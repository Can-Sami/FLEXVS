package flexscript.features.cobblestone;

import flexscript.Main;
import flexscript.config.Config;
import flexscript.events.MillisecondEvent;
import flexscript.events.PlayerMoveEvent;
import flexscript.events.SecondEvent;
import flexscript.utils.*;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;

public class CobblestoneMacroNuker {
    public static boolean enabled;
    private final ArrayList<BlockPos> broken = new ArrayList<>();
    public static BlockPos blockPos;
    private long lastBroken = 0;
    private BlockPos current;
    private final ArrayList<BlockPos> blocksInRange = new ArrayList<>();

    @SubscribeEvent
    public void onSecond(SecondEvent event) {
        if (Main.mc.thePlayer == null) return;
        if (!isEnabled()) return;
        if (broken.size() > 0) broken.clear();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) return;
        if (!isEnabled()) {
            if (current != null && Main.mc.thePlayer != null) {
                Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.ABORT_DESTROY_BLOCK,
                        blockPos,
                        EnumFacing.DOWN)
                );
            }
            blockPos = null;
            current = null;
            return;
        }
        blocksInRange.clear();
        EntityPlayerSP player = Main.mc.thePlayer;
        BlockPos playerPos = new BlockPos((int) Math.floor(player.posX), (int) Math.floor(player.posY) + 1, (int) Math.floor(player.posZ));
        Vec3i vec3Top = new Vec3i(1, 5, 1);
        Vec3i vec3Bottom = new Vec3i(1, 0, 1);

        for (BlockPos blockPos : BlockPos.getAllInBox(playerPos.subtract(vec3Bottom), playerPos.add(vec3Top))) {
            Vec3 target = new Vec3(blockPos.getX() + 0.5, blockPos.getY() + 0.5, blockPos.getZ() + 0.5);
            if (Math.abs(RotationUtils.wrapAngleTo180(RotationUtils.fovToVec3(target) - RotationUtils.wrapAngleTo180(Main.mc.thePlayer.rotationYaw))) < (float) 180 / 2)
                blocksInRange.add(blockPos);
        }
        if (current != null) PlayerUtils.swingHand(null);
    }

    @SubscribeEvent
    public void onMillisecond(MillisecondEvent event) {
        if (!isEnabled()) {
            current = null;
            if (broken.size() > 0) broken.clear();
            return;
        }

        if (event.timestamp - lastBroken > 1000f / Config.INSTANCE.cobbleSpeed) {
            lastBroken = event.timestamp;
            if (Config.INSTANCE.nukerShape == 1) {
                if (broken.size() > 6) broken.clear();
            } else {
                if (broken.size() > 10) broken.clear();
            }

            if (current == null) {
                blockPos = NukerBlockUtils.getClosestBlock(1, 5, 0, this::canMine);
            }

            if (blockPos != null) {
                if (current != null && current.compareTo(blockPos) != 0) {
                    current = null;
                }
                if (isSlow(getBlockState(blockPos))) {
                    if (current == null) {
                        mineBlock(blockPos);
                    }
                } else {
                    pinglessMineBlock(blockPos);
                    current = null;
                }
            } else {
                current = null;
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (!isEnabled()) return;
        //RenderUtils.drawBlockBox(blockPos, Color.GRAY, 1, event.partialTicks);
        //RenderUtils.drawBlockBox(blockPos, Color.BLUE, 1, event.partialTicks);
    }

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public void onUpdatePre(PlayerMoveEvent.Pre pre) {
        if (!isEnabled()) return;
        if (!Config.INSTANCE.cobbleServerRotation) return;
        if (blockPos != null) {
            RotationUtils.look(RotationUtils.getRotation(blockPos));
        }
        if (current != null) {
            RotationUtils.look(RotationUtils.getRotation(current));
        }
    }

    private void mineBlock(BlockPos blockPos) {
        breakBlock(blockPos);
        current = blockPos;
    }

    private void pinglessMineBlock(BlockPos blockPos) {
        PlayerUtils.swingHand(null);
        breakBlock(blockPos);
        broken.add(blockPos);
    }

    private void breakBlock(BlockPos blockPos) {
        MovingObjectPosition objectMouseOver = Main.mc.objectMouseOver;
        if (objectMouseOver != null) {
            objectMouseOver.hitVec = new Vec3(blockPos);
            if (objectMouseOver.sideHit != null) {
                Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                        C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                        blockPos,
                        objectMouseOver.sideHit
                ));
            }
        } else {
            Main.mc.thePlayer.sendQueue.addToSendQueue(new C07PacketPlayerDigging(
                    C07PacketPlayerDigging.Action.START_DESTROY_BLOCK,
                    blockPos,
                    EnumFacing.UP
            ));
        }
    }

    public static boolean isEnabled() {
        return (Main.cobbleMacro && Main.mc.thePlayer != null && Main.mc.theWorld != null);
    }

    private BlockPos blockInFront() {
        EntityPlayerSP player = Main.mc.thePlayer;
        BlockPos playerPos = new BlockPos((int) Math.floor(player.posX), (int) Math.floor(player.posY), (int) Math.floor(player.posZ));
        Vec3i axisVector = player.getHorizontalFacing().getDirectionVec();

        if (getBlockState(playerPos).getBlock() != Blocks.air && !(getBlockState(playerPos).getBlock() instanceof BlockLiquid) &&
                getBlockState(playerPos).getBlock() != Blocks.bedrock && !broken.contains(playerPos)) {
            return playerPos;
        }
        if (getBlockState(playerPos.add(new Vec3i(0, 1, 0))).getBlock() != Blocks.air && !(getBlockState(playerPos).getBlock() instanceof BlockLiquid) &&
                getBlockState(playerPos).getBlock() != Blocks.bedrock && !broken.contains(playerPos.add(new Vec3i(0, 1, 0)))) {
            return playerPos.add(new Vec3i(0, 1, 0));
        }
        if (getBlockState(playerPos.add(axisVector)).getBlock() != Blocks.air && !(getBlockState(playerPos).getBlock() instanceof BlockLiquid) &&
                getBlockState(playerPos).getBlock() != Blocks.bedrock && !broken.contains(playerPos.add(axisVector))) {
            return playerPos.add(axisVector);
        }
        if (getBlockState(playerPos.add(axisVector).add(new Vec3i(0, 1, 0))).getBlock() != Blocks.air && !(getBlockState(playerPos).getBlock() instanceof BlockLiquid) &&
                getBlockState(playerPos).getBlock() != Blocks.bedrock && !broken.contains(playerPos.add(axisVector).add(new Vec3i(0, 1, 0)))) {
            return playerPos.add(axisVector).add(new Vec3i(0, 1, 0));
        }
        return null;
    }

    private boolean canMine(BlockPos blockPos) {
        if (canMineBlockType(blockPos) && !broken.contains(blockPos) && blocksInRange.contains(blockPos)) {
            EntityPlayerSP player = Main.mc.thePlayer;
            EnumFacing axis = player.getHorizontalFacing();

            Vec3i ray = VectorUtils.addVector(VectorUtils.addVector(new Vec3i((int) Math.floor(player.posX), 0, (int) Math.floor(player.posZ)), VectorUtils.scaleVec(axis.getDirectionVec(), -4)), VectorUtils.scaleVec(axis.rotateY().getDirectionVec(), -4));

            return true;
        }

        return false;
    }

    private boolean canMineBlockType(BlockPos bp) {
        IBlockState blockState = getBlockState(bp);
        Block block = blockState.getBlock();
        return (block == Blocks.stone || block == Blocks.cobblestone);
    }

    private boolean isSlow(IBlockState blockState) {
        Block block = blockState.getBlock();
        return block == Blocks.prismarine || block == Blocks.wool || block == Blocks.stained_hardened_clay ||
                block == Blocks.gold_block || block == Blocks.stained_glass_pane || block == Blocks.stained_glass ||
                block == Blocks.glowstone || block == Blocks.chest ||
                (block == Blocks.stone && blockState.getValue(BlockStone.VARIANT) == BlockStone.EnumType.DIORITE_SMOOTH) ||
                block == Blocks.obsidian;
    }

    private IBlockState getBlockState(BlockPos blockPos) {
        return Main.mc.theWorld.getBlockState(blockPos);
    }
}