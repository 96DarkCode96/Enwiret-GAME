package me.darkcode.game;

import me.darkcode.WindowReference;
import me.darkcode.objects.Camera;
import me.darkcode.objects.Cursor;
import me.darkcode.objects.Location;
import me.darkcode.objects.Rotation;
import me.darkcode.objects.world.Chunk;
import me.darkcode.render.Renderer;
import me.darkcode.render.RenderingFace;
import me.darkcode.render.RenderingModel;
import me.darkcode.shader.MainShader;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import static me.darkcode.render.RendererUtil.createTransformMatrix;

public class GameRenderer implements Renderer {

    private static final RenderingModel FACE_FRONT = new RenderingModel(new float[]{
            0, 1, 0,
            0, 0, 0,
            1, 1, 0,
            1, 0, 0
    }, new float[]{
            0, 0,
            0, 1,
            1, 0,
            1, 1
    }, new float[]{
            0, 0, 0,
            0, 0, 0,
            0, 0, 0,
            0, 0, 0
    }, new int[]{
            0, 1, 2,
            1, 3, 2
    });
    public final ConcurrentHashMap<Long, ArrayList<RenderingFace>> faces = new ConcurrentHashMap<>();
    private final Game game;
    private final MainShader shader = new MainShader();
    private final Camera camera = new Camera(new Location(0, 2.85f, 0), new Rotation());
    private long lastFrameTime = System.currentTimeMillis();
    private float delta;

    public GameRenderer(Game game) {
        this.game = game;
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
        shader.start();
        shader.loadProjection(game.getWindowReference());
        shader.stop();
    }

    public void updateFaces(Chunk chunk, @Nullable ArrayList<RenderingFace> faces) {
        long key = (((long) chunk.getChunkX()) << 32) | (chunk.getChunkZ() & 0xffffffffL);
        if (faces == null || faces.isEmpty()) {
            this.faces.remove(key);
        } else {
            this.faces.put(key, faces);
        }
    }

    public void updateFaces(int chunkX, int chunkZ, @Nullable ArrayList<RenderingFace> faces) {
        long key = (((long) chunkX) << 32) | (chunkZ & 0xffffffffL);
        if (faces == null || faces.isEmpty()) {
            this.faces.remove(key);
        } else {
            this.faces.put(key, faces);
        }
    }

    @Override
    public boolean needRender(WindowReference windowReference) {
        return game.getController().rendering();
    }

    @Override
    public void render(WindowReference windowReference, int tick) {

        long currentFrameTime = System.currentTimeMillis();
        delta = (currentFrameTime - lastFrameTime) / 1000f;
        lastFrameTime = currentFrameTime;

        shader.start();
        game.getController().getPlayerPOV().move(this);
        shader.loadView(camera);
        prepareRenderFaces();
        this.faces.forEach((key, value) -> {
            int x = (int) (key >> 32);
            int y = (int) (key & 0xffffffffL);
            if (checkNeedRender(x, y))
                value.forEach(face -> renderFace(face, shader));
        });
        endRenderingFaces();
        shader.stop();
        handleKeyEvents(windowReference);

    }

    private void prepareRenderFaces() {
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL30.glBindVertexArray(FACE_FRONT.getVao());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    private boolean checkNeedRender(int x, int y) {
        return true;
    }

    private void renderFace(RenderingFace face, MainShader shader) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, face.getTextureId());
        shader.loadTransformMatrix(createTransformMatrix(face.getLocation().add(face.getBlockFace().getRenderFace()), face.getBlockFace().getRenderRotation(), 1));
        GL11.glDrawElements(GL11.GL_TRIANGLES, FACE_FRONT.getLength(), GL11.GL_UNSIGNED_INT, 0);
    }

    private void endRenderingFaces() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }

    private void handleKeyEvents(WindowReference windowReference) {
        boolean fwd = GLFW.glfwGetKey(windowReference.getWindowId(), GameSettings.KEY_BINDING_MOVE_FORWARD) == GLFW.GLFW_PRESS;
        boolean bwd = GLFW.glfwGetKey(windowReference.getWindowId(), GameSettings.KEY_BINDING_MOVE_BACKWARD) == GLFW.GLFW_PRESS;
        boolean lef = GLFW.glfwGetKey(windowReference.getWindowId(), GameSettings.KEY_BINDING_MOVE_LEFT) == GLFW.GLFW_PRESS;
        boolean rig = GLFW.glfwGetKey(windowReference.getWindowId(), GameSettings.KEY_BINDING_MOVE_RIGHT) == GLFW.GLFW_PRESS;

        int fwdInt = (fwd ? 1 : 0) - (bwd ? 1 : 0);
        int dirInt = (rig ? 1 : 0) - (lef ? 1 : 0);

        if (fwdInt == 1) {
            if (dirInt == 1) {
                game.getController().move(45);
            } else if (dirInt == -1) {
                game.getController().move(315);
            } else {
                game.getController().move(0);
            }
        } else if (fwdInt == -1) {
            if (dirInt == 1) {
                game.getController().move(135);
            } else if (dirInt == -1) {
                game.getController().move(225);
            } else {
                game.getController().move(180);
            }
        } else {
            if (dirInt == 1) {
                game.getController().move(90);
            } else if (dirInt == -1) {
                game.getController().move(270);
            }else{
                game.getController().cancelMove();
            }
        }
    }

    @Override
    public void destroy() {
        shader.deleteShader();
    }

    @Override
    public void mousePos(float x, float y) {
        float xDiff = Cursor.diffX(x);
        float yDiff = Cursor.diffY(y);
        game.getController().rotate(-xDiff / 100f * GameSettings.getCursorXSensitive(), yDiff / 100f * GameSettings.getCursorYSensitive());
    }

    @Override
    public void mouseKey(int button, int action, int mods) {

    }

    @Override
    public void keyboardKey(int key, int scancode, int action, int mods) {
        game.getController().key(key, scancode, action, mods);
    }

    @Override
    public void charKey(int codepoint) {
    }

    public float getDelta() {
        return delta;
    }

    public Game getGame() {
        return game;
    }

    public MainShader getShader() {
        return shader;
    }

    public Camera getCamera() {
        return camera;
    }

}