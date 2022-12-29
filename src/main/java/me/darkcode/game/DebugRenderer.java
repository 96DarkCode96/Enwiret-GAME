package me.darkcode.game;

import me.darkcode.WindowReference;
import me.darkcode.objects.FPS;
import me.darkcode.render.Renderer;
import me.darkcode.render.text.GUIText;
import me.darkcode.render.text.TextManager;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;

public class DebugRenderer implements Renderer {
    private final Game game;
    private boolean needRender = false;
    private boolean wireframe = false;

    private HashMap<String, GUIText> renderDebugData = new HashMap<>();

    public DebugRenderer(Game game) {
        this.game = game;
        add("FPS", "---");
        add("POS", "-|-|- -|-");
        add("CPOS", "-|-|-");
        add("CHUNKS", "---");
        add("FACES", "---");
    }

    public void update(String key, String text){
        renderDebugData.get(key).setText(key + ": " + text);
    }

    public void add(String key, String text){
        GUIText guiText = new GUIText(key + ": " + text, 0.8f, TextManager.getFont("Monospaced"),
                new Vector2f(0.01f, 0.01f + (0.03f*renderDebugData.size())), 1f, false);
        guiText.setColour(0, 0, 1);
        guiText.getPosition().y /= ((float) game.getWindowReference().getWidth() / (float) game.getWindowReference().getHeight());
        guiText.setWidth(0.46f);
        guiText.setEdge(0.19f);
        renderDebugData.put(key, guiText);
    }

    @Override
    public boolean needRender(WindowReference windowReference) {
        boolean b = (needRender && game.getController().rendering());
        for (GUIText value : renderDebugData.values()) {
            value.setRender(b);
        }
        return b;
    }

    @Override
    public void render(WindowReference windowReference, int tick) {
        update("FPS", "" + FPS.getFps());
        GameRenderer renderer = (GameRenderer) game.getRenderers().stream().filter(a -> a instanceof GameRenderer).findFirst().get();
        update("POS", "" + renderer.getGame().getController().getPlayerPOV()
                .getLocation().format("%.2f|%.2f|%.2f") + " " + renderer.getCamera().getRotation().format("%.2f|%.2f"));
        update("CPOS", "" + renderer.getGame().getController().getPlayerPOV().getLocation().formatChunk("%d|%d|%d"));
        update("CHUNKS", String.valueOf(renderer.faces.keySet().size()));
        update("FACES", String.valueOf(renderer.faces.values().stream().mapToInt(ArrayList::size).sum()));
    }

    @Override
    public void destroy() {
        for (GUIText value : renderDebugData.values()) {
            value.remove();
        }
        renderDebugData.clear();
    }

    @Override
    public void mousePos(float x, float y) {

    }

    @Override
    public void mouseKey(int button, int action, int mods) {

    }

    @Override
    public void keyboardKey(int key, int scancode, int action, int mods) {
        if (key == GLFW.GLFW_KEY_KP_DIVIDE && action == GLFW.GLFW_PRESS) {
            needRender = !needRender;
        } else if (key == GLFW.GLFW_KEY_KP_MULTIPLY && action == GLFW.GLFW_PRESS) {
            wireframe = !wireframe;
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, wireframe ? GL11.GL_LINE : GL11.GL_FILL);
        }
    }

    @Override
    public void charKey(int codepoint) {

    }

    public Game getGame() {
        return game;
    }
}
