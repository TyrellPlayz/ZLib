package com.tyrellplayz.zlib.client.gui.screen;

public interface ScreenEventListener {

    /**
     * Get called when a mouse button is clicked.
     * @param mouseX The X pos of the mouse.
     * @param mouseY The Y pos of the mouse.
     * @param mouseButton The mouse button being clicked. See {@link org.lwjgl.glfw.GLFW} for codes.
     */
    void onMouseClicked(double mouseX, double mouseY, int mouseButton);

    /**
     * Get called when a mouse button is released.
     * @param mouseX The X pos of the mouse.
     * @param mouseY The Y pos of the mouse.
     * @param mouseButton The mouse button being clicked. See {@link org.lwjgl.glfw.GLFW} for codes.
     */
    void onMouseReleased(double mouseX, double mouseY, int mouseButton);

    /**
     * Gets called when the mouse moved.
     * @param mouseX The X pos of the mouse.
     * @param mouseY The Y pos of the mouse.
     */
    void onMouseMoved(double mouseX, double mouseY);

    /**
     * Gets called when the mouse wheel is scrolled.
     * @param mouseX The X pos of the mouse.
     * @param mouseY The Y pos of the mouse.
     * @param scroll
     */
    void onMouseScrolled(double mouseX, double mouseY, double scroll);

    /**
     * Gets called when the mouse is dragging.
     * @param mouseX The X pos of the mouse.
     * @param mouseY The Y pos of the mouse.
     * @param mouseButton The mouse button being clicked. See {@link org.lwjgl.glfw.GLFW} for codes.
     * @param distanceX
     * @param distanceY
     */
    void onMouseDragged(double mouseX, double mouseY, int mouseButton, double distanceX, double distanceY);

    /**
     * Gets called when a key is pressed or being held down.
     * @param keyCode
     * @param scanCode
     * @param modifiers
     */
    void onKeyPressed(int keyCode, int scanCode, int modifiers);

    /**
     * Gets called when a key is released.
     * @param keyCode
     * @param scanCode
     * @param modifiers
     */
    void onKeyReleased(int keyCode, int scanCode, int modifiers);

    /**
     * Gets called when a character is typed or being held down.
     * @param character
     * @param modifiers
     */
    void onCharTyped(char character, int modifiers);

    /**
     * Gets called when the focus is changed.
     * @param lostFocus
     */
    void onFocusChanged(boolean lostFocus);

    /**
     * @return If the mouse is inside.
     */
    boolean isMouseInside(double mouseX, double mouseY);

}
