package me.darkcode;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;

public class WindowReference {

    private long windowId;

    public WindowReference(long windowId) {
        this.windowId = windowId;
    }

    public long getWindowId() {
        return windowId;
    }

    public void setWindowId(long windowId) {
        this.windowId = windowId;
    }

    public void destroy(){
        GLFW.glfwDestroyWindow(windowId);
    }

    public void hide(){
        GLFW.glfwHideWindow(windowId);
    }

    public void show(){
        GLFW.glfwShowWindow(windowId);
    }

    public int getWidth() {
        int[] width = new int[1];
        GLFW.glfwGetWindowSize(windowId, width, null);
        return width[0];
    }

    public int getHeight() {
        int[] height = new int[1];
        GLFW.glfwGetWindowSize(windowId, null, height);
        return height[0];
    }

    public void resolution(int width, int height) {
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        assert vidMode != null;
        GLFW.glfwSetWindowMonitor(windowId, GLFW.glfwGetPrimaryMonitor(), 0, 0, width, height, vidMode.refreshRate());
    }

    public void windowMode(int width, int height) {
        GLFWVidMode vidMode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
        assert vidMode != null;
        GLFW.glfwSetWindowMonitor(windowId, 0, (vidMode.width()-width)/2, (vidMode.height()-height)/2, width, height, vidMode.refreshRate());
    }
}