package me.darkcode.render.text;

import me.darkcode.game.Game;
import me.darkcode.objects.ResourceKey;
import me.darkcode.render.TextureReference;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.io.File;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class TextManager {

    private static final HashMap<String, FontType> fonts = new HashMap<>();
    private static final HashMap<FontType, ArrayList<GUIText>> texts = new HashMap<>();

    public static void init(Game game){
        fonts.put("Monospaced", new FontType(TextureReference.getTexture(new ResourceKey("/fonts/monospaced.png")).getId(), game.getWindowReference(),
                new File("./src/main/resources/fonts/monospaced.fnt")));
    }

    public static void render(){
        FontRenderer.render(texts);
    }

    public static FontType getFont(String key){
        return fonts.get(key);
    }

    public static void generate(GUIText text){
        FontType font = text.getFont();
        TextMeshData data = font.loadText(text);
        int vao = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vao);
        int vbo1 = storeDataInAttributeList(0, data.getVertexPositions(), 2);
        int vbo2 = storeDataInAttributeList(1, data.getTextureCoords(), 2);
        GL30.glBindVertexArray(0);
        text.setMeshInfo(vao, data.getVertexCount(), vbo1, vbo2);
        ArrayList<GUIText> batch = texts.computeIfAbsent(font, (e) ->new ArrayList<>());
        batch.add(text);
        texts.put(font, batch);
    }

    public static void remove(GUIText text) {
        ArrayList<GUIText> batch = texts.get(text.getFont());
        batch.remove(text);
        if(batch.isEmpty()){
            texts.remove(text.getFont());
        }else{
            texts.put(text.getFont(), batch);
        }

        GL30.glDeleteVertexArrays(text.getMesh());
        GL30.glDeleteBuffers(text.getVbo1());
        GL30.glDeleteBuffers(text.getVbo2());
    }

    private static int storeDataInAttributeList(int attributeNumber, float[] data, int size){
        int vbo = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        return vbo;
    }

    private static FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}