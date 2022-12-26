package me.darkcode.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class RenderingModel {

    private int vao;
    private int[] vbo;
    private int length;

    public RenderingModel(float[] vertexPoints, float[] texturePoints, float[] normalVectors, int[] indices){
        if(indices.length < 3)
            throw new IllegalArgumentException(String.format("Model needs minimum of 3 indices (= %d)", indices.length));
        if(indices.length%3!=0)
            throw new IllegalArgumentException(String.format("Model is using a invalid number of indices! (%d extra)", indices.length%3));
        if(vertexPoints.length%3!=0)
            throw new IllegalArgumentException(String.format("Model is using a invalid number of vertices! (%d extra)", vertexPoints.length%3));
        if(texturePoints.length%2!=0)
            throw new IllegalArgumentException("Model is using a invalid number of UV Points! (1 extra)");
        if(normalVectors.length%3!=0)
            throw new IllegalArgumentException(String.format("Model is using a invalid number of normals! (%d extra)", normalVectors.length%3));

        length = indices.length;
        vbo = new int[4];
        vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);

        // -------------------------------------------------
        //
        //   DATA -> INDICES, VERTICES, TEXTURES, NORMALS
        //
        // -------------------------------------------------

        bindIndicesBuffer(indices);
        storeDataInAttributeList(0, vertexPoints, 3);
        storeDataInAttributeList(1, texturePoints, 2);
        storeDataInAttributeList(2, normalVectors, 3);

        // ----------------
        //    UNBIND VAO
        // ----------------
        GL30.glBindVertexArray(0);
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data, int size){
        vbo[attributeNumber+1] = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo[attributeNumber+1]);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices){
        vbo[0] = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo[0]);
        IntBuffer buffer = storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
    }

    private IntBuffer storeDataInIntBuffer(int[] data){
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public int getVao() {
        return vao;
    }

    public int[] getVbo() {
        return vbo;
    }

    public int getLength() {
        return length;
    }

    public void deleteModel(){
        GL30.glDeleteVertexArrays(vao);
        GL30.glDeleteBuffers(vbo);
        vao = 0;
        vbo = null;
        length = 0;
    }
}