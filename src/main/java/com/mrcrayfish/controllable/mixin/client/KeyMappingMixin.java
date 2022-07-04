package com.mrcrayfish.controllable.mixin.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mrcrayfish.controllable.client.BindingRegistry;
import com.mrcrayfish.controllable.client.KeyAdapterBinding;
import net.minecraft.client.KeyMapping;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

/**
 * Author: MrCrayfish
 */
@Mixin(KeyMapping.class)
public abstract class KeyMappingMixin
{
    @Shadow
    public abstract String getCategory();

//    @Override
//    public boolean isActiveAndMatches(InputConstants.Key keyCode)
//    {
//        String customKey = this.getCategory() + ".custom";
//        KeyAdapterBinding adapter = BindingRegistry.getInstance().getKeyAdapters().get(customKey);
//        if(adapter != null && adapter.isButtonDown())
//        {
//            return true;
//        }
//        return keyCode != InputConstants.UNKNOWN && keyCode.equals(getKey()) && getKeyConflictContext().isActive() && getKeyModifier().isActive(getKeyConflictContext());
//    }
}
