package com.tyrellplayz.zlib.util.helper;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.Stack;

/**
 * Created by MrCrayfish.
 */
public class GLHelper {

    public static Stack<GLHelper.Scissor> scissorStack = new Stack<>();

    private GLHelper() {}

    public static void pushScissor(int x, int y, int width, int height) {
        GL11.glEnable(3089);
        if (scissorStack.size() > 0) {
            GLHelper.Scissor scissor = scissorStack.peek();
            x = Math.max(scissor.x, x);
            y = Math.max(scissor.y, y);
            width = x + width > scissor.x + scissor.width ? scissor.x + scissor.width - x : width;
            height = y + height > scissor.y + scissor.height ? scissor.y + scissor.height - y : height;
        }

        Minecraft mc = Minecraft.getInstance();
        int scale = (int)mc.getWindow().getGuiScale();
        GL11.glScissor(x * scale, mc.getWindow().getHeight() - y * scale - height * scale, Math.max(0, width * scale), Math.max(0, height * scale));
        scissorStack.push(new GLHelper.Scissor(x, y, width, height));
    }

    public static void popScissor() {
        if (!scissorStack.isEmpty()) {
            scissorStack.pop();
        }

        restoreScissor();
        GL11.glDisable(3089);
    }

    private static void restoreScissor() {
        if (!scissorStack.isEmpty()) {
            GLHelper.Scissor scissor = scissorStack.peek();
            Minecraft mc = Minecraft.getInstance();
            int scale = (int)mc.getWindow().getGuiScale();
            GL11.glScissor(scissor.x * scale, mc.getWindow().getHeight() - scissor.y * scale - scissor.height * scale, Math.max(0, scissor.width * scale), Math.max(0, scissor.height * scale));
        }

    }

    public static boolean isScissorStackEmpty() {
        return scissorStack.isEmpty();
    }

    public static void clearScissorStack() {
        scissorStack.clear();
    }

    public static class Scissor {
        public int x;
        public int y;
        public int width;
        public int height;

        Scissor(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

}
