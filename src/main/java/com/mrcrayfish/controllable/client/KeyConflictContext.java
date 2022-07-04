package com.mrcrayfish.controllable.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;

public enum KeyConflictContext implements IKeyConflictContext
{
    /**
     * Universal key bindings are used in every context and will conflict with any other context.
     * Key Bindings are universal by default.
     */
    UNIVERSAL {
        @Override
        public boolean isActive()
        {
            return true;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other)
        {
            return true;
        }
    },

    /**
     * Gui key bindings are only used when a {@link Screen} is open.
     */
    GUI {
        @Override
        public boolean isActive()
        {
            return Minecraft.getInstance().screen != null;
        }

        @Override
        public boolean conflicts(IKeyConflictContext other)
        {
            return this == other;
        }
    },

    /**
     * In-game key bindings are only used when a {@link Screen} is not open.
     */
    IN_GAME {
        @Override
        public boolean isActive()
        {
            return !GUI.isActive();
        }

        @Override
        public boolean conflicts(IKeyConflictContext other)
        {
            return this == other;
        }
    }
}