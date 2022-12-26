package me.darkcode.shader;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.FloatBuffer;

public abstract class ShaderProgram {

    private static final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    private final int programId;
    private final int vertexShaderId;
    private final int fragmentShaderId;

    public ShaderProgram(String vertexFilePath, String fragmentFilePath){
        vertexShaderId = loadShader(vertexFilePath, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentFilePath, GL20.GL_FRAGMENT_SHADER);
        programId = GL20.glCreateProgram();
        GL20.glAttachShader(programId, vertexShaderId);
        GL20.glAttachShader(programId, fragmentShaderId);
        bindAttributes();
        GL20.glLinkProgram(programId);
        GL20.glValidateProgram(programId);
        getAllUniformLocations();
    }

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programId, uniformName);
    }

    public void deleteShader(){
        stop();
        GL20.glDetachShader(programId, vertexShaderId);
        GL20.glDetachShader(programId, fragmentShaderId);
        GL20.glDeleteShader(vertexShaderId);
        GL20.glDeleteShader(fragmentShaderId);
        GL20.glDeleteProgram(programId);
    }

    public void start(){
        GL20.glUseProgram(programId);
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    protected abstract void bindAttributes();

    protected void bindAttribute(int attribute, String variableName){
        GL20.glBindAttribLocation(programId, attribute, variableName);
    }

    protected void loadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }

    protected void loadVector(int location, Vector3f value){
        GL20.glUniform3f(location, value.x, value.y, value.z);
    }

    protected void loadBoolean(int location, boolean value){
        loadFloat(location, value?1:0);
    }

    protected void loadMatrix(int location, Matrix4f matrix){
        matrix.get(matrixBuffer);
        GL20.glUniformMatrix4fv(location, false, matrixBuffer);
    }

    //<editor-fold desc="private static int loadShader(String filePath, int type);">
    private static int loadShader(String filePath, int type) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
        int shaderId = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderId, sb);
        GL20.glCompileShader(shaderId);
        if (GL20.glGetShaderi(shaderId, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new IllegalArgumentException("Shader not valid! \n" + GL20.glGetShaderInfoLog(shaderId));
        }
        return shaderId;
    }
    //</editor-fold>

}