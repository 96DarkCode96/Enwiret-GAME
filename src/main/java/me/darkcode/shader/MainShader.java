package me.darkcode.shader;

import me.darkcode.WindowReference;
import me.darkcode.objects.Camera;
import me.darkcode.render.RendererUtil;
import org.joml.Matrix4f;

public class MainShader extends ShaderProgram {

    private int transform_matrix;
    private int view_matrix;
    private int projection_matrix;

    public MainShader() {
        super("./src/main/resources/shaders/main.vert", "./src/main/resources/shaders/main.frag");
    }

    @Override
    protected void getAllUniformLocations() {
        transform_matrix = getUniformLocation("transform_matrix");
        projection_matrix = getUniformLocation("projection_matrix");
        view_matrix = getUniformLocation("view_matrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "texture_uv");
        super.bindAttribute(2, "normals");
    }

    public void loadTransformMatrix(Matrix4f matrix){
        loadMatrix(transform_matrix, matrix);
    }

    public void loadProjection(WindowReference windowReference, Camera camera){
        loadMatrix(projection_matrix, RendererUtil.createProjectionMatrix(windowReference, camera));
    }

    public void loadView(Camera camera){
        loadMatrix(view_matrix, RendererUtil.createViewMatrix(camera));
    }

}
