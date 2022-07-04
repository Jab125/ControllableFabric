package com.mrcrayfish.controllable.client;

import com.mrcrayfish.controllable.Controllable;
import net.minecraft.client.resources.language.I18n;;

import java.util.List;

/**
 * Author: MrCrayfish
 */
public class ButtonBinding implements Comparable<ButtonBinding>
{
    private final int defaultButton;
    private int button;
    private String descriptionKey;
    private String category;
    private IKeyConflictContext context;
    private boolean pressed;
    private int pressedTime;
    private boolean reserved;
    private boolean active;

    public ButtonBinding(int button, String descriptionKey, String category, IKeyConflictContext context)
    {
        this(button, descriptionKey, category, context, false);
    }

    ButtonBinding(int button, String descriptionKey, String category, IKeyConflictContext context, boolean reserved)
    {
        this.defaultButton = button;
        this.button = button;
        this.descriptionKey = descriptionKey;
        this.category = category;
        this.context = context;
        this.reserved = reserved;
    }

    public int getButton()
    {
        return this.button;
    }

    public void setButton(int button)
    {
        this.button = button;
    }

    public String getLabelKey()
    {
        return this.descriptionKey;
    }

    public String getDescription()
    {
        return this.descriptionKey;
    }

    public String getCategory()
    {
        return this.category;
    }

    public boolean isDefault()
    {
        return this.button == this.defaultButton;
    }

    protected void setPressed(boolean pressed)
    {
        this.pressed = pressed;
    }

    public boolean isButtonPressed()
    {
        return this.pressed && this.pressedTime == 0 && this.isActiveAndValidContext();
    }

    public boolean isNotReserved()
    {
        return !this.reserved;
    }

    public boolean isButtonDown()
    {
        return this.pressed && this.isActiveAndValidContext();
    }

    public void reset()
    {
        this.button = this.defaultButton;
    }

    protected void onPressTick() {};

    public static void tick()
    {
        for(ButtonBinding binding : BindingRegistry.getInstance().getRegisteredBindings())
        {
            if(binding.isButtonDown() || (binding.active && ButtonBindings.RADIAL_MENU.isButtonDown()))
            {
                binding.pressedTime--;
            }
            if(binding.active && !ButtonBindings.RADIAL_MENU.isButtonDown())
            {
                Controllable.getInput().handleButtonInput(Controllable.getController(), -1, false, true);
                binding.active = false;
                binding.setPressed(false);
            }
        }
    }

    public static void setButtonState(int button, boolean state)
    {
        List<ButtonBinding> bindings = BindingRegistry.getInstance().getBindingListForButton(button);
        for(ButtonBinding binding : bindings)
        {
            binding.setPressed(state);
            if(state)
            {
                binding.pressedTime = 0;
            }
        }
    }

    /**
     * Resets all buttons states. Called when a GUI is opened.
     */
    public static void resetButtonStates()
    {
        for(ButtonBinding binding : BindingRegistry.getInstance().getRegisteredBindings())
        {
            binding.pressed = false;
        }
    }

    @Override
    public int compareTo(ButtonBinding o)
    {
        return I18n.get(this.descriptionKey).compareTo(I18n.get(o.descriptionKey));
    }

    public boolean isConflictingContext()
    {
        List<ButtonBinding> bindings = BindingRegistry.getInstance().getBindingListForButton(this.button);

        if(bindings == null)
            return false;

        for(ButtonBinding binding : bindings)
        {
            if(this.conflicts(binding))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the context is active and that this binding does not conflict with any other binding.
     */
    private boolean isActiveAndValidContext()
    {
        return this.context.isActive() && !this.isConflictingContext();
    }

    /**
     * Tests if the given binding conflicts with this binding
     *
     * @param binding the binding to test against
     * @return true if the bindings conflict
     */
    private boolean conflicts(ButtonBinding binding)
    {
        return this != binding && this.button == binding.getButton() && this.context.conflicts(binding.context);
    }

    @Override
    public int hashCode()
    {
        return this.descriptionKey.hashCode();
    }

    @Override
    public boolean equals(Object obj)
    {
        return this == obj;
    }

    /**
     * Sets the binding as active and will use the radial menu button to determine it's state
     */
    void setActiveAndPressed()
    {
        this.active = true;
        this.setPressed(true);
        this.pressedTime = 0;
    }
}