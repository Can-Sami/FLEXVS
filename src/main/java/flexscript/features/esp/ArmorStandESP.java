package flexscript.features.esp;

import flexscript.Config;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import flexscript.Main;
import flexscript.events.RenderLivingEntityEvent;
import flexscript.utils.RenderUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ArmorStandESP {
    private static final HashMap<Entity, String> highlightedEntities = new HashMap<>();
    private static final HashSet<Entity> checked = new HashSet<>();

    private static void highlightEntity(Entity entity, String name) {
        highlightedEntities.put(entity, name);
    }

    private static boolean checkName(String name) {
        return name.contains("§8[§7Lv");
    }

    @SubscribeEvent
    public void onRenderEntityLiving(RenderLivingEntityEvent event) {
        if (checked.contains(event.entity)) return;
        if (!Main.configFile.mobEsp) return;
        if (event.entity.hasCustomName() && checkName(event.entity.getCustomNameTag())) {
            if (event.entity.getDistanceToEntity(Main.mc.thePlayer) > Config.INSTANCE.mobEspRange)
                return;
            if (event.entity instanceof EntityArmorStand) {
                List<Entity> possibleEntities = event.entity.getEntityWorld().getEntitiesInAABBexcluding(event.entity, event.entity.getEntityBoundingBox().offset(0, -1, 0), entity -> (!(entity instanceof EntityArmorStand) && entity != Main.mc.thePlayer));
                if (!possibleEntities.isEmpty()) {
                    highlightEntity(possibleEntities.get(0), event.entity.getCustomNameTag());
                } else {
                    highlightEntity(event.entity, event.entity.getCustomNameTag());

                }
            } else {
                highlightEntity(event.entity, event.entity.getCustomNameTag());
            }
            checked.add(event.entity);
        }
    }

    @SubscribeEvent
    public void onRenderWorld(RenderWorldLastEvent event) {
        if (!Main.configFile.mobEsp) return;
        Main.mc.theWorld.loadedEntityList.forEach(entity -> {
            if (highlightedEntities.containsKey(entity)) {
                String name = highlightedEntities.get(entity);
                RenderUtils.drawEntityBox(entity, Config.espColor, 3, event.partialTicks);
                RenderUtils.renderWaypointText(name, entity.posX, entity.posY + entity.height, entity.posZ, event.partialTicks, Config.INSTANCE.showNameTag, Config.INSTANCE.showDistance);
            }
        });
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (Main.mc.thePlayer == null) return;
        if (!Main.configFile.mobEsp) return;
        if (Main.mc.thePlayer.ticksExisted % 40 == 0) {
            checked.clear();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        highlightedEntities.clear();
        checked.clear();
    }
}