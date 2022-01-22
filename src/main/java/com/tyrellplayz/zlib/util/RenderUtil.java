package com.tyrellplayz.zlib.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RenderUtil {

    public static final int DEFAULT_SOURCE_SIZE = 256;

    private RenderUtil() {}

    public static void drawText(PoseStack stack, String text, double x, double y, Color color) {
        Minecraft.getInstance().font.draw(stack,text,(float) x,(float)y,color.getRGB());
    }

    public static void drawShadowText(PoseStack stack, String text, double x, double y, Color color) {
        Minecraft.getInstance().font.drawShadow(stack,text,(float) x,(float)y,color.getRGB());
    }

    public static int getTextWidth(String text) {
        return Minecraft.getInstance().font.width(text);
    }

    public static void drawTextClipped(PoseStack stack, String text, int x, int y, int width, int color, boolean shadow) {
        Minecraft.getInstance().font.draw(stack,clipTextToWidth(text, width) + ChatFormatting.RESET, x, y, color);
    }

    public static void drawShadowTextClipped(PoseStack stack, String text, int x, int y, int width, int color, boolean shadow) {
        Minecraft.getInstance().font.drawShadow(stack,clipTextToWidth(text, width) + ChatFormatting.RESET, x, y, color);
    }

    public static String clipTextToWidth(String text, int width) {
        Font font = Minecraft.getInstance().font;
        String clipped = text;
        if(font.width(clipped) > width) {
            clipped = font.plainSubstrByWidth(clipped, width - 8) + "...";
        }
        return clipped;
    }

    public static String[] clipTextToWidthArray(String text, int width) {
        Font font = Minecraft.getInstance().font;
        List<String> clippedList = new ArrayList<>();
        String textTemp = text;
        boolean flag = true;

        while (flag) {
            if(font.width(textTemp) > width) {
                //clippedList.add(fontRenderer.trimStringToWidth(textTemp,width,false));
                //textTemp = fontRenderer.trimStringToWidth(textTemp,fontRenderer.getStringWidth(textTemp)-width,true);
                clippedList.add(font.plainSubstrByWidth(textTemp,width));
                textTemp = font.plainSubstrByWidth(textTemp,font.width(textTemp)-width);
            }else {
                clippedList.add(textTemp);
                flag = false;
            }
        }
        return clippedList.toArray(new String[]{});
    }

    /**
     * Render an item.
     * @param x X position on screen.
     * @param y Y position on screen.
     * @param stack ItemStack to be rendered.
     * @param renderAmount Render amount of items in stack.
     */
    public static void drawItem(int x, int y, ItemStack stack, boolean renderAmount) {
        Minecraft.getInstance().getItemRenderer().renderGuiItem(stack,x,y);
        if(renderAmount) Minecraft.getInstance().getItemRenderer().renderGuiItemDecorations(Minecraft.getInstance().font,stack,x,y);
    }

    /**
     * Render a coloured in rectangle.
     * @param stack
     * @param x X position on screen.
     * @param y Y position on screen.
     * @param width Width of rectangle.
     * @param height Height of rectangle.
     * @param color The colour of the rectangle.
     */
    public static void drawRectWithColour(PoseStack stack, double x, double y, int width, int height, Color color) {
        Matrix4f matrix = stack.last().pose();

        float red = color.getRed() / 255.0F;
        float green = color.getGreen() / 255.0F;
        float blue = color.getBlue() / 255.0F;
        float alpha = color.getAlpha() / 255.0F;

        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        builder.vertex(matrix,(float)x,(float)y + height, 0.0F).color(red,green,blue,alpha).endVertex();
        builder.vertex(matrix,(float)x + width,(float)y + height, 0.0F).color(red,green,blue,alpha).endVertex();
        builder.vertex(matrix,(float)x + width,(float)y, 0.0F).color(red,green,blue,alpha).endVertex();
        builder.vertex(matrix,(float)x,(float)y, 0.0F).color(red,green,blue,alpha).endVertex();
        builder.end();
        BufferUploader.end(builder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }
    /*
     /**
      * Render the entire texture within the given width and height.
      * @param stack
      * @param x X position on screen.
      * @param y Y position on screen.
      * @param width Width of texture on screen.
      * @param height Height of texture on screen.
      */
    /*public static void renderRectWithFullTexture(PoseStack stack, double x, double y, int width, int height) {
        renderRectWithTexture(stack,x,y,0,0,width,height,width,height,width,height);

        Matrix4f matrix = stack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        builder.vertex(matrix, (float)x, (float)y+height,0)
                .uv(0,0+height).endVertex();
        builder.vertex(matrix, (float)x+width, (float)y+height,0)
                .uv(0+width,0+height).endVertex();
        builder.vertex(matrix, (float)x+width, (float)y,0)
                .uv(0+width,0).endVertex();
        builder.vertex(matrix, (float)x, (float)y,0)
                .uv(0,0).endVertex();

        //builder.vertex(matrix, (float)x, (float)y+height,0).uv(1,1).endVertex();
        //builder.vertex(matrix, (float)x+width, (float)y+height,0).uv(1,1).endVertex();
        //builder.vertex(matrix, (float)x+width, (float)y,0).uv(1,1).endVertex();
        //builder.vertex(matrix, (float)x, (float)y,0).uv(1,1).endVertex();
        builder.end();
        BufferUploader.end(builder);


    }*/

    /*
    public static void renderRectWithTexture(PoseStack stack, double x, double y, float u, float v, float width, float height, float textureWidth, float textureHeight) {
        Matrix4f matrix = stack.last().pose();
        float scale = 0.00390625F;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.vertex(matrix,(float) x,(float)y+height,(float)0).uv(u*scale,(v+textureHeight)*scale).endVertex();
        builder.vertex(matrix,(float) x+width,(float)y+height,(float)0).uv((u+textureWidth)*scale,(v+textureHeight)*scale).endVertex();
        builder.vertex(matrix,(float) x+width,(float)y,(float)0).uv((u+textureWidth)*scale,v*scale).endVertex();
        builder.vertex(matrix,(float) x,(float)y,(float)0).uv(u*scale,v*scale).endVertex();
        builder.end();
        BufferUploader.end(builder);
    }
     */

    /*
    Textures that use Minecraft's default image size
     */

    /**
     * Render a texture that has a source size of 256*256 and uses the full texture.
     * @param stack
     * @param x Start X position of the render on the screen.
     * @param y Start Y position of the render on the screen.
     * @param width Width of render on screen.
     * @param height Height of render on screen.
     */
    public static void drawRectWithDefaultTexture(PoseStack stack, double x, double y, int width, int height) {
        drawRectWithTexture(stack,x,y,0,0,width,height,width,height,DEFAULT_SOURCE_SIZE,DEFAULT_SOURCE_SIZE);
    }

    /**
     * Render a texture that has a source size of 256*256 and uses the full texture.
     * @param stack
     * @param x Start X position of the render on the screen.
     * @param y Start Y position of the render on the screen.
     * @param v Start X position of the render withing the texture. Set to 0 for first pixel in the texture.
     * @param v Start Y position of the render withing the texture. Set to 0 for first pixel in the texture.
     * @param width Width of render on screen.
     * @param height Height of render on screen.
     */
    public static void drawRectWithDefaultTexture(PoseStack stack, double x, double y, int u, int v, int width, int height) {
        drawRectWithTexture(stack,x,y,u,v,width,height,width,height,DEFAULT_SOURCE_SIZE,DEFAULT_SOURCE_SIZE);
    }

    /**
     * Render a texture that has a source size of 256*256.
     * @param stack
     * @param x Start X position of the render on the screen.
     * @param y Start Y position of the render on the screen.
     * @param v Start X position of the render withing the texture. Set to 0 for first pixel in the texture.
     * @param v Start Y position of the render withing the texture. Set to 0 for first pixel in the texture.
     * @param width Width of render on screen.
     * @param height Height of render on screen.
     * @param textureWidth Width of the texture within the render. Set to width to use entire texture.
     * @param textureHeight Height of the texture within the render. Set to height to use entire texture.
     */
    public static void drawRectWithDefaultTexture(PoseStack stack, double x, double y, int u, int v, int width, int height, int textureWidth, int textureHeight) {
        drawRectWithTexture(stack,x,y,u,v,width,height,textureWidth,textureHeight,DEFAULT_SOURCE_SIZE,DEFAULT_SOURCE_SIZE);
    }

    /*
    Textures that use a custom image size.
     */

    /**
     * Render a texture in full.
     * @param stack
     * @param x
     * @param y
     * @param width
     * @param height
     */
    public static void drawRectWithFullTexture(PoseStack stack, double x, double y, int width, int height) {
        drawRectWithTexture(stack,x,y,0,0,width,height);
    }

    public static void drawRectWithTexture(PoseStack stack, double x, double y, int u, int v, int width, int height) {
        drawRectWithTexture(stack,x,y,u,v,width,height,width,height,width,height);
    }

    public static void drawRectWithTexture(PoseStack stack, double x, double y, int u, int v, int width, int height, int sourceWidth, int sourceHeight) {
        drawRectWithTexture(stack,x,y,u,v,width,height,width,height,sourceWidth,sourceHeight);
    }

    /**
     * Render a texture with the given params.
     * @param stack
     * @param x Start X position of the render on the screen.
     * @param y Start Y position of the render on the screen.
     * @param v Start X position of the render withing the texture. Set to 0 for first pixel in the texture.
     * @param v Start Y position of the render withing the texture. Set to 0 for first pixel in the texture.
     * @param width Width of render on screen.
     * @param height Height of render on screen.
     * @param textureWidth Width of the texture within the render. Set to width to use entire texture.
     * @param textureHeight Height of the texture within the render. Set to height to use entire texture.
     * @param sourceWidth The width of the source image.
     * @param sourceHeight The height of the source image.
     */
    public static void drawRectWithTexture(PoseStack stack, double x, double y, int u, int v, int width, int height, int textureWidth, int textureHeight, int sourceWidth, int sourceHeight) {
        Matrix4f matrix = stack.last().pose();
        float scaleWidth = 1.0F / sourceWidth;
        float scaleHeight = 1.0F / sourceHeight;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        builder.vertex(matrix,(float)x,(float)y+height,0)
                .uv((u*scaleWidth),((v+textureHeight)*scaleHeight)).endVertex();
        builder.vertex(matrix,(float)x+width,(float)y+height,0)
                .uv(((u+textureWidth)*scaleWidth),((v+textureHeight)*scaleHeight)).endVertex();
        builder.vertex(matrix,(float)x+width,(float)y,0)
                .uv(((u+textureWidth)*scaleWidth),v*scaleHeight).endVertex();
        builder.vertex(matrix,(float)x,(float)y,0)
                .uv(u*scaleWidth,v*scaleHeight).endVertex();
        builder.end();
        BufferUploader.end(builder);
    }

    public static boolean isMouseWithin(double mouseX, double mouseY, double x, double y, int width, int height) {
        return isMouseInside(mouseX,mouseY,x,y,x+width, y+height);
    }

    public static boolean isMouseInside(double mouseX, double mouseY, double x1, double y1, double x2, double y2) {
        return mouseX >= x1 && mouseX <= x2 && mouseY >= y1 && mouseY <= y2;
    }

    public static void drawScreenWithTexture(PoseStack stack, double posX, double posY, int border, int width, int height){
        int screenWidth = width - border * 2;
        int screenHeight = height - border * 2;
        /* Corners */
        RenderUtil.drawRectWithDefaultTexture(stack,posX, posY, 0, 0, border, border); // TOP-LEFT
        RenderUtil.drawRectWithDefaultTexture(stack,posX + width - border, posY, 11, 0, border, border); // TOP-RIGHT
        RenderUtil.drawRectWithDefaultTexture(stack,posX + width - border, posY + height - border, 11, 11, border, border); // BOTTOM-RIGHT
        RenderUtil.drawRectWithDefaultTexture(stack,posX, posY + height - border, 0, 11, border, border); // BOTTOM-LEFT
        /* Edges */
        RenderUtil.drawRectWithDefaultTexture(stack,posX + border, posY, 10, 0, screenWidth, border, 1, border); // TOP
        RenderUtil.drawRectWithDefaultTexture(stack,posX + width - border, posY + border, 11, 10, border, screenHeight, border, 1); // RIGHT
        RenderUtil.drawRectWithDefaultTexture(stack,posX + border, posY + height - border, 10, 11, screenWidth, border, 1, border); // BOTTOM
        RenderUtil.drawRectWithDefaultTexture(stack,posX, posY + border, 0, 11, border, screenHeight, border, 1); // LEFT
        /* Center */
        RenderUtil.drawRectWithDefaultTexture(stack,posX + border, posY + border, 10, 10, screenWidth, screenHeight, 1, 1);
    }

}
