package com.mrcrayfish.controllable.client;

import com.jab125.event.impl.TickEvent;
import com.jab125.thonkutil.api.annotations.SubscribeEvent;
import com.mrcrayfish.controllable.Config;
import com.mrcrayfish.controllable.Controllable;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.item.UseAnim;
import com.jab125.api.LivingEntityUseItemEvent;

/**
 * Author: MrCrayfish
 */
public class ControllerEvents
{
    private float prevHealth = -1;

    @SubscribeEvent
    public void onPlayerUsingItem(LivingEntityUseItemEvent.Tick event)
    {
        if(event.getEntity() != Minecraft.getInstance().player)
        {
            return;
        }

        if(!Config.CLIENT.options.forceFeedback.get())
        {
            return;
        }

        /* Stops vibration from running because controller is not in use */
        if(Controllable.getInput().getLastUse() <= 0)
        {
            return;
        }

        Controller controller = Controllable.getController();
        if(controller != null)
        {
            float magnitudeFactor = 0.5F;
            UseAnim action = event.getItem().getUseAnimation();
            switch(action)
            {
                case BLOCK:
                    magnitudeFactor = 0.25F;
                    break;
                case SPEAR:
                    magnitudeFactor = Mth.clamp((event.getItem().getUseDuration() - event.getDuration()) / 20F, 0.0F, 0.25F) / 0.25F;
                    break;
                case BOW:
                    magnitudeFactor = Mth.clamp((event.getItem().getUseDuration() - event.getDuration()) / 20F, 0.0F, 1.0F) / 1.0F;
                    break;
                case CROSSBOW:
                    magnitudeFactor = Mth.clamp((event.getItem().getUseDuration() - event.getDuration()) / 20F, 0.0F, 1.5F) / 1.5F;
                    break;
            }
            //controller.getGamepadState().rumble(0.5F * magnitudeFactor, 0.5F * magnitudeFactor, 50); //50ms is one tick
        }
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event)
    {
        if(event.phase != TickEvent.Phase.START)
        {
            return;
        }

        Controller controller = Controllable.getController();
        if(controller == null)
        {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        if(mc.level != null && Config.CLIENT.options.forceFeedback.get())
        {
            if(this.prevHealth == -1)
            {
                this.prevHealth = mc.player.getHealth();
            }
            else if(this.prevHealth > mc.player.getHealth())
            {
                float difference = this.prevHealth - mc.player.getHealth();
                float magnitude = difference / mc.player.getMaxHealth();
                //controller.getGamepadState().rumble(1.0F, 1.0F, (int) (800 * magnitude));
                this.prevHealth = mc.player.getHealth();
            }
            else
            {
                this.prevHealth = mc.player.getHealth();
            }
        }
        else if(this.prevHealth != -1)
        {
            this.prevHealth = -1;
        }
    }
}
