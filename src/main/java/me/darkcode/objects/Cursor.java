package me.darkcode.objects;

import me.darkcode.game.Game;
import org.lwjgl.glfw.GLFW;

public class Cursor {

    private static float lastX, lastY;

    public static void disableCursor(Game game){
        GLFW.glfwSetInputMode(game.getWindowReference().getWindowId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_DISABLED);
    }

    public static void normalCursor(Game game){
        GLFW.glfwSetInputMode(game.getWindowReference().getWindowId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_NORMAL);
    }

    public static void hiddenCursor(Game game){
        GLFW.glfwSetInputMode(game.getWindowReference().getWindowId(), GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
    }

    public static float diffX(float x) {
        float diff = lastX - x;
        lastX = x;
        return diff;
    }

    public static float diffY(float y) {
        float diff = lastY - y;
        lastY = y;
        return diff;
    }
}