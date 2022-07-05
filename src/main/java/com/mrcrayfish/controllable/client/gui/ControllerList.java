package com.mrcrayfish.controllable.client.gui;

import com.mrcrayfish.controllable.Controllable;
import com.mrcrayfish.controllable.client.Controller;
import com.mrcrayfish.controllable.client.ControllerManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;

import java.util.List;
import java.util.Map;

/**
 * Author: MrCrayfish
 */
public class ControllerList extends AbstractSelectionList<ControllerEntry>
{
    private ControllerManager manager;

    public ControllerList(ControllerManager manager, Minecraft mcIn, int widthIn, int heightIn, int topIn, int bottomIn, int slotHeightIn)
    {
        super(mcIn, widthIn, heightIn, topIn, bottomIn, slotHeightIn);
        this.manager = manager;
        this.reload();
    }

    public void reload()
    {
        this.clearEntries();
        Map<Integer, String> controllers = this.manager.getControllers();
        //System.out.println(controllers);
        controllers.forEach((jid, name) -> {
            this.addEntry(new ControllerEntry(this, jid));
        });
        this.updateSelected();
    }

    public void updateSelected()
    {
        Controller controller = Controllable.getController();
        if(controller == null)
        {
            this.setSelected(null);
            return;
        }

        List<ControllerEntry> entries = this.children();
        for(ControllerEntry entry : entries)
        {
            if(entry.getJid() == controller.getJid())
            {
                this.setSelected(entry);
                break;
            }
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput output)
    {

    }
}
