package com.tyrellplayz.zlib.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class GuiScreen extends Screen {

    /** The X size of the inventory window in pixels. */
    protected final int xSize;
    /** The Y size of the inventory window in pixels. */
    protected final int ySize;
    /** Starting X position for the Gui. Inconsistent use for Gui backgrounds. */
    protected int left;
    /** Starting Y position for the Gui. Inconsistent use for Gui backgrounds. */
    protected int top;

    public GuiScreen(Component title, int xSize, int ySize) {
        super(title);
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    protected void init() {
        this.left = (this.width - xSize) / 2;
        this.top = (this.height - ySize) / 2;
    }
    
    @Override
    public abstract void render(PoseStack stack, int mouseX, int mouseY, float partialTicks);

    public int getLeft() {
        return left;
    }

    public int getTop() {
        return top;
    }
}