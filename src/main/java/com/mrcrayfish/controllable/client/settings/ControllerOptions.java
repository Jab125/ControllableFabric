package com.mrcrayfish.controllable.client.settings;

import com.mrcrayfish.controllable.Config;
import com.mrcrayfish.controllable.client.*;
import com.mrcrayfish.controllable.mixin.ConfigValueAccessor;
import com.mrcrayfish.controllable.mixin.RangeAccessor;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;

/**
 * Author: MrCrayfish
 */
public class ControllerOptions
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.#");

    //public static final ControllerSetting<Boolean> FORCE_FEEDBACK = createBoolean("controllable.options.forceFeedback", Config.CLIENT.options.forceFeedback);

    /*public static final CycleOption<Boolean> FORCE_FEEDBACK = createOnOff(, options -> {
        return Config.CLIENT.options.forceFeedback.get();
    }, (options, option, value) -> {
        Config.CLIENT.options.forceFeedback.set(value);
        Config.save();
    });*/

    public static final ControllerSetting<Boolean> AUTO_SELECT = createToggleSetting("controllable.options.autoSelect", Config.CLIENT.options.autoSelect);
    public static final ControllerSetting<Boolean> RENDER_MINI_PLAYER = createToggleSetting("controllable.options.renderMiniPlayer", Config.CLIENT.options.renderMiniPlayer);
    public static final ControllerSetting<Boolean> VIRTUAL_MOUSE = createToggleSetting("controllable.options.virtualMouse", Config.CLIENT.options.virtualMouse);
    public static final ControllerSetting<Boolean> CONSOLE_HOTBAR = createToggleSetting("controllable.options.consoleHotbar", Config.CLIENT.options.consoleHotbar);
    public static final ControllerSetting<CursorType> CURSOR_TYPE = createValuesSetting("controllable.options.cursorType", CursorType.class, Config.CLIENT.options.cursorType);
    public static final ControllerSetting<ControllerIcons> CONTROLLER_ICONS = createValuesSetting("controllable.options.controllerIcons", ControllerIcons.class, Config.CLIENT.options.controllerIcons);
    public static final ControllerSetting<Boolean> INVERT_LOOK = createToggleSetting("controllable.options.invertLook", Config.CLIENT.options.invertLook);
    public static final ControllerSetting<Double> DEAD_ZONE = createSliderSetting("controllable.options.deadZone", Config.CLIENT.options.deadZone, 0.01);
    public static final ControllerSetting<Double> ROTATION_SPEED = createSliderSetting("controllable.options.rotationSpeed", Config.CLIENT.options.rotationSpeed, 1.0);
    public static final ControllerSetting<Double> MOUSE_SPEED = createSliderSetting("controllable.options.mouseSpeed", Config.CLIENT.options.mouseSpeed, 1.0);
    public static final ControllerSetting<Boolean> QUICK_CRAFT = createToggleSetting("controllable.options.quickCraft", Config.CLIENT.options.quickCraft);
    public static final ControllerSetting<Boolean> UI_SOUNDS = createToggleSetting("controllable.options.uiSounds", Config.CLIENT.options.uiSounds);
    public static final ControllerSetting<Double> HOVER_MODIFIER = createSliderSetting("controllable.options.hoverModifier", Config.CLIENT.options.hoverModifier, 0.05);
    public static final ControllerSetting<ActionVisibility> SHOW_ACTIONS = createValuesSetting("controllable.options.showActions", ActionVisibility.class, Config.CLIENT.options.showActions);
    public static final ControllerSetting<Thumbstick> RADIAL_THUMBSTICK = createValuesSetting("controllable.options.radialThumbstick", Thumbstick.class, Config.CLIENT.options.radialThumbstick);
    public static final ControllerSetting<SneakMode> SNEAK_MODE = createValuesSetting("controllable.options.sneakMode", SneakMode.class, Config.CLIENT.options.sneakMode);
    public static final ControllerSetting<Thumbstick> CURSOR_THUMBSTICK = createValuesSetting("controllable.options.cursorThumbstick", Thumbstick.class, Config.CLIENT.options.cursorThumbstick);

    public static ControllerSetting<Boolean> createToggleSetting(String key, ForgeConfigSpec.ConfigValue<Boolean> configValue)
    {
        return new ControllerToggleSetting(key, configValue);
    }

    public static <T extends Enum<?> & SettingEnum> ControllerSetting<T> createValuesSetting(String key, Class<T> clazz, ForgeConfigSpec.ConfigValue<T> configValue)
    {
        return new ControllerEnumSetting<>(key, clazz, configValue);
    }

    public static ControllerSetting<Double> createSliderSetting(String key, ForgeConfigSpec.ConfigValue<Double> configValue, double stepSize)
    {
        ForgeConfigSpec.ValueSpec spec = getValueSpec(configValue);
        if(spec == null) throw new IllegalArgumentException("Invalid config value. Missing value spec");
        Pair<Object, Object> minMax = getMinMax(spec.getRange());
        if(minMax == null) throw new IllegalArgumentException("Invalid config value for slider. Can only be of type Double or Integer");
        return new ControllerSliderSetting(key, configValue, (double) minMax.getLeft(), (double) minMax.getRight(), stepSize);
    }

    @Nullable
    private static ForgeConfigSpec.ValueSpec getValueSpec(ForgeConfigSpec.ConfigValue<?> value)
    {
        return ((ConfigValueAccessor)value).getSpec().get(value.getPath());
//        try
//        {
//            Field field = ForgeConfigSpec.ConfigValue.class.getDeclaredField("spec");
//            if(field.get(value) instanceof ForgeConfigSpec configSpec)
//            {
//                return configSpec.getSpec().get(value.getPath());
//            }
//        }
//        catch(IllegalAccessException | NoSuchFieldException e)
//        {
//            e.printStackTrace();
//        }
//        return null;
    }

    @Nullable
    private static Pair<Object, Object> getMinMax(@Nullable Object range)
    {
        if(range != null)
        {
            Object min = ((RangeAccessor)range).getMin();
            Object max = ((RangeAccessor<?>) range).getMax();
            return Pair.of(min, max);

        }
        return null;
    }
}
