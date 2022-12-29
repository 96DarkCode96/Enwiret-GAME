package me.darkcode.render.text;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class GUIText {

    private final Vector3f colour = new Vector3f(0f, 0f, 0f);
    private final Vector2f position;
    private String textString;
    private final float fontSize;
    private int textMeshVao, vbo1, vbo2;
    private int vertexCount;
    private final float lineMaxSize;
    private int numberOfLines;

    private final FontType font;

    private boolean centerText = false;
    private float width = 0.5f;
    private float edge = 0.1f;

    public boolean render;

    public GUIText(String text, float fontSize, FontType font, Vector2f position, float maxLineLength, boolean centered) {
        this.textString = text;
        this.fontSize = fontSize;
        this.font = font;
        this.position = position;
        this.lineMaxSize = maxLineLength;
        this.centerText = centered;
        TextManager.generate(this);
    }

    public boolean isRender() {
        return render;
    }

    public void setRender(boolean render) {
        this.render = render;
    }

    public void setText(String text) {
        remove();
        this.textString = text;
        TextManager.generate(this);
    }

    public void remove() {
        TextManager.remove(this);
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getEdge() {
        return edge;
    }

    public void setEdge(float edge) {
        this.edge = edge;
    }

    public FontType getFont() {
        return font;
    }

    public void setColour(float r, float g, float b) {
        colour.set(r, g, b);
    }

    public Vector3f getColour() {
        return colour;
    }

    public int getNumberOfLines() {
        return numberOfLines;
    }

    protected void setNumberOfLines(int number) {
        this.numberOfLines = number;
    }

    public Vector2f getPosition() {
        return position;
    }

    public int getMesh() {
        return textMeshVao;
    }

    public void setMeshInfo(int vao, int verticesCount, int vbo1, int vbo2) {
        this.textMeshVao = vao;
        this.vertexCount = verticesCount;
        this.vbo1 = vbo1;
        this.vbo2 = vbo2;
    }

    public int getVertexCount() {
        return this.vertexCount;
    }

    protected float getFontSize() {
        return fontSize;
    }

    protected boolean isCentered() {
        return centerText;
    }

    protected float getMaxLineSize() {
        return lineMaxSize;
    }

    protected String getTextString() {
        return textString;
    }

    public int getVbo1() {
        return vbo1;
    }

    public int getVbo2() {
        return vbo2;
    }
}
