package me.darkcode.render;

import me.darkcode.WindowReference;
import me.darkcode.game.GameSettings;
import me.darkcode.objects.Camera;
import me.darkcode.objects.Location;
import me.darkcode.objects.Rotation;
import me.darkcode.shader.MainShader;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RendererUtil {

    private static final Matrix4f projectionMatrix = new Matrix4f();

    public static void render(MainShader shader, RenderingEntity... entities) {
        HashMap<TextureReference, ArrayList<RenderingEntity>> keyed = new HashMap<>();
        for (RenderingEntity entity : entities) {
            ArrayList<RenderingEntity> list = keyed.computeIfAbsent(entity.getTexturedModel().getTexture(), NULL -> new ArrayList<>());
            list.add(entity);
            keyed.put(entity.getTexturedModel().getTexture(), list);
        }

        for (Map.Entry<TextureReference, ArrayList<RenderingEntity>> entry : keyed.entrySet()) {

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, entry.getKey().getId());

            for (RenderingEntity entity : entry.getValue()) {
                GL30.glBindVertexArray(entity.getTexturedModel().getModel().getVao());
                GL20.glEnableVertexAttribArray(0);
                GL20.glEnableVertexAttribArray(1);
                GL20.glEnableVertexAttribArray(2);

                shader.loadTransformMatrix(createTransformMatrix(entity));

                GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getTexturedModel().getModel().getLength(), GL11.GL_UNSIGNED_INT, 0);
                GL20.glDisableVertexAttribArray(0);
                GL20.glDisableVertexAttribArray(1);
                GL20.glDisableVertexAttribArray(2);
                GL30.glBindVertexArray(0);
            }
        }
    }

    public static Matrix4f createTransformMatrix(RenderingEntity entity) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ());
        matrix.rotateY((float) Math.toRadians(entity.getRotation().getYaw()));
        matrix.rotateX((float) Math.toRadians(entity.getRotation().getPitch()));
        matrix.scale(entity.getScale());
        return matrix;
    }

    public static Matrix4f createTransformMatrix(Location location, Rotation rotation, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.identity();
        matrix.translate(location.getX(), location.getY(), location.getZ());
        matrix.rotateY((float) Math.toRadians(rotation.getYaw()));
        matrix.rotateX((float) Math.toRadians(rotation.getPitch()));
        matrix.scale(scale);
        return matrix;
    }

    public static Matrix4f createProjectionMatrix(WindowReference windowReference) {
        float aspectRatio = (float) windowReference.getWidth() / (float) windowReference.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(GameSettings.getFOV() / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = GameSettings.getFarPlane() - GameSettings.getNearPlane();

        projectionMatrix.zero();
        projectionMatrix.m00(x_scale);
        projectionMatrix.m11(y_scale);
        projectionMatrix.m22(-((GameSettings.getFarPlane()+GameSettings.getNearPlane())/frustum_length));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2*GameSettings.getNearPlane()*GameSettings.getFarPlane())/frustum_length));
        projectionMatrix.m33(0);
        return projectionMatrix;
    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.identity();
        viewMatrix.rotateX((float) Math.toRadians(camera.getRotation().getPitch()));
        viewMatrix.rotateY((float) Math.toRadians(camera.getRotation().getYaw()));
        viewMatrix.translate(-camera.getLocation().getX(), -camera.getLocation().getY(), -camera.getLocation().getZ());
        return viewMatrix;
    }

    public static Matrix4f getProjectionMatrix(){
        return projectionMatrix;
    }

}