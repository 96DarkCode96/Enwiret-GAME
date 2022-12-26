package me.darkcode.game;

import me.darkcode.WindowReference;
import me.darkcode.objects.Camera;
import me.darkcode.objects.Location;
import me.darkcode.objects.ResourceKey;
import me.darkcode.objects.Rotation;
import me.darkcode.render.*;
import me.darkcode.shader.MainShader;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class Game {

    private final List<Renderer> renderers = new ArrayList<>();
    private int renderingTick;

    private final WindowReference windowReference;
    private final RenderingEntity entity = new RenderingEntity(new TexturedModel(TextureReference.getTexture(new ResourceKey("/textures/images/game_icon_64x.png")),
            new RenderingModel(
                    new float[]{
                            -0.5f, 0.5f, 0,
                            -0.5f, -0.5f, 0,
                            0.5f, -0.5f, 0,
                            0.5f, 0.5f, 0
                    },
                    new float[]{
                            0, 0,
                            0, 1,
                            1, 1,
                            1, 0
                    },
                    new float[]{
                            0, 0, 0,
                            0, 0, 0,
                            0, 0, 0,
                            0, 0, 0
                    },
                    new int[]{
                            0, 1, 3,
                            3, 1, 2
                    }
            )), new Location(0, 0, -10f), new Rotation(0, 0), 1);

    public Game(WindowReference windowReference) {
        this.windowReference = windowReference;
        renderers.add(new Renderer() {
            MainShader shader = new MainShader();
            Camera camera = new Camera(90, 1000, 0.1f, new Location(), new Rotation());
            {
                shader.start();
                shader.loadProjection(windowReference, camera);
                shader.stop();
            }
            @Override
            public boolean needRender(WindowReference windowReference) {
                return true;
            }

            @Override
            public void render(WindowReference windowReference, int tick) {
                shader.start();
                camera.getRotation().add(1, 0);
                shader.loadView(camera);
                entity.getLocation().add(0, 0, -0.001f);
                RendererUtil.render(shader, entity);
                shader.stop();
            }

            @Override
            public void destroy() {
                shader.deleteShader();
            }

        });
    }

    public WindowReference getWindowReference() {
        return windowReference;
    }

    public void render(){
        GL11.glClearColor(0f, 1f, 1f, 1f);
        renderers.stream().filter(a -> a.needRender(windowReference)).forEach(a -> a.render(windowReference, renderingTick));
        renderingTick++;
        renderingTick%=(Integer.MAX_VALUE-1);
    }

    public void exit(Exit exitReason) {
        if(exitReason.equals(Exit.RIGHT_WAY)){
            windowReference.destroy();
        }
        for (Renderer renderer : renderers) {
            renderer.destroy();
        }
        renderers.clear();
        TextureReference.cleanUp();
    }

    public enum Exit{
        WINDOW_CLOSED, RIGHT_WAY, UNKNOWN;
    }

}