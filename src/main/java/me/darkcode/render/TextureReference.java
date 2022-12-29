package me.darkcode.render;


import de.matthiasmann.twl.utils.PNGDecoder;
import me.darkcode.objects.ResourceKey;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class TextureReference {

    private static final HashMap<ResourceKey, TextureReference> textures = new HashMap<>();

    static{
        loadTexture(new ResourceKey("/textures/images/game_icon_64x.png"));
        loadTexture(new ResourceKey("/textures/images/stone.png"));
        loadTexture(new ResourceKey("/fonts/monospaced.png"), 0f);
    }

    public static void cleanUp(){
        textures.forEach((resourceKey, textureReference) -> textureReference.delete());
        textures.clear();
    }

    public static TextureReference loadTexture(ResourceKey resourceKey) {
        TextureReference textureReference = new TextureReference(resourceKey);
        textures.put(resourceKey, textureReference);
        return textureReference;
    }

    public static TextureReference loadTexture(ResourceKey resourceKey, float mipmapBias) {
        TextureReference textureReference = new TextureReference(resourceKey, mipmapBias);
        textures.put(resourceKey, textureReference);
        return textureReference;
    }

    public static @Nullable TextureReference getTexture(ResourceKey resourceKey){
        return textures.get(resourceKey);
    }

    private final int id;

    public TextureReference(ResourceKey resourceKey, float mipmapBias) {
        try {
            PNGDecoder decoder = new PNGDecoder(TextureReference.class.getResourceAsStream(resourceKey.key()));
            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            id = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            GL11.glTexParameteri(GL11.GL_TEXTURE, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
            GL11.glTexParameterf(GL11.GL_TEXTURE, GL14.GL_TEXTURE_LOD_BIAS, mipmapBias);
            buffer.clear();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private TextureReference(ResourceKey resourceKey) {
        this(resourceKey, -0.4f);
    }

    public int getId() {
        return id;
    }

    public void delete(){
        GL30.glDeleteTextures(getId());
    }
}