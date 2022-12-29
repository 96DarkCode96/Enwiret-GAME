package me.darkcode.render.text;

import me.darkcode.shader.FontShader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.Map;

public class FontRenderer {

    private static final FontShader shader = new FontShader();

    public static void cleanUp() {
        shader.deleteShader();
    }

    public static void render(Map<FontType, ArrayList<GUIText>> texts) {
        prepare();
        texts.forEach((fontType, guiTexts) -> {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, fontType.getTextureAtlas());
            for (GUIText guiText : guiTexts) {
                if (guiText.isRender()) {
                    renderText(guiText);
                }
            }
        });
        endRendering();
    }

    private static void prepare() {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        shader.start();
    }

    private static void renderText(GUIText text) {
        GL30.glBindVertexArray(text.getMesh());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        shader.loadColor(text.getColour());
        shader.loadTranslation(text.getPosition());
        shader.loadWidth(text.getWidth());
        shader.loadEdge(text.getEdge());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, text.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
    }

    private static void endRendering() {
        shader.stop();
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }

}
