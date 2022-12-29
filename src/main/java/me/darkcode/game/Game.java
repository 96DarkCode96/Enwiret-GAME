package me.darkcode.game;

import me.darkcode.WindowReference;
import me.darkcode.objects.FPS;
import me.darkcode.render.Renderer;
import me.darkcode.render.TextureReference;
import me.darkcode.render.text.FontRenderer;
import me.darkcode.render.text.TextManager;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Renderer> renderers = new ArrayList<>();
    private final WindowReference windowReference;
    private int renderingTick;
    private final GameController controller;

    public GameController getController() {
        return controller;
    }

    public Game(WindowReference windowReference) {
        this.windowReference = windowReference;
        TextManager.init(this);
        this.renderers.add(new GameRenderer(this));
        this.controller = new GameController(this);
        callbacks();
        this.renderers.add(new DebugRenderer(this));
    }

    private void callbacks() {
        //<editor-fold desc="Callbacks" defaultstate="collapsed">
        GLFW.glfwSetCursorPosCallback(windowReference.getWindowId(), new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                renderers.forEach(a -> a.mousePos((float) xpos, (float) ypos));
            }
        });
        GLFW.glfwSetMouseButtonCallback(windowReference.getWindowId(), new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                renderers.forEach(a -> a.mouseKey(button, action, mods));
            }
        });
        GLFW.glfwSetCharCallback(windowReference.getWindowId(), new GLFWCharCallback() {
            @Override
            public void invoke(long window, int codepoint) {
                renderers.forEach(a -> a.charKey(codepoint));
            }
        });
        GLFW.glfwSetKeyCallback(windowReference.getWindowId(), new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                renderers.forEach(a -> a.keyboardKey(key, scancode, action, mods));
            }
        });
        //</editor-fold>
    }

    public WindowReference getWindowReference() {
        return windowReference;
    }

    public void render() {
        GL11.glClearColor(0f, 1f, 1f, 1f);
        renderers.stream().filter(a -> a.needRender(windowReference)).forEach(a -> a.render(windowReference, renderingTick));
        TextManager.render();
        renderingTick++;
        renderingTick %= (Integer.MAX_VALUE - 1);
        FPS.tick();
    }

    public void exit(Exit exitReason) {
        if (exitReason.equals(Exit.RIGHT_WAY)) {
            windowReference.destroy();
        }
        for (Renderer renderer : renderers) {
            renderer.destroy();
        }
        renderers.clear();
        TextureReference.cleanUp();
        FontRenderer.cleanUp();
    }

    public enum Exit {
        WINDOW_CLOSED, RIGHT_WAY, UNKNOWN
    }

    public int getRenderingTick() {
        return renderingTick;
    }

    public List<Renderer> getRenderers() {
        return renderers;
    }
}