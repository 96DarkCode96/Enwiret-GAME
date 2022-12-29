package me.darkcode.game;

public class GameSettings {

    public static char KEY_BINDING_MOVE_FORWARD = 'W';
    public static char KEY_BINDING_MOVE_BACKWARD = 'S';
    public static char KEY_BINDING_MOVE_LEFT = 'A';
    public static char KEY_BINDING_MOVE_RIGHT = 'D';
    private static float FOV = 90;
    private static float NEAR_PLANE = 0.001f;
    private static float FAR_PLANE = 16*64f;
    private static int RENDER_DISTANCE = 2;

    private static float CURSOR_X_SENSITIVE = 1.8f;
    private static float CURSOR_Y_SENSITIVE = 1.8f;

    public static float getCursorXSensitive() {
        return CURSOR_X_SENSITIVE;
    }

    public static void setCursorXSensitive(float cursorXSensitive) {
        CURSOR_X_SENSITIVE = cursorXSensitive;
    }

    public static float getCursorYSensitive() {
        return CURSOR_Y_SENSITIVE;
    }

    public static void setCursorYSensitive(float cursorYSensitive) {
        CURSOR_Y_SENSITIVE = cursorYSensitive;
    }

    public static float getFOV() {
        return FOV;
    }

    public static void setFOV(float FOV) {
        GameSettings.FOV = FOV;
    }

    public static float getNearPlane() {
        return NEAR_PLANE;
    }

    public static void setNearPlane(float nearPlane) {
        NEAR_PLANE = nearPlane;
    }

    public static float getFarPlane() {
        return FAR_PLANE;
    }

    public static void setRenderDistance(int renderDistance) {
        RENDER_DISTANCE = renderDistance;
    }

    public static int getRenderDistance() {
        return RENDER_DISTANCE;
    }
}