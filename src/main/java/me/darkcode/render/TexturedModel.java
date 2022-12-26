package me.darkcode.render;

public class TexturedModel {

    private TextureReference texture;
    private RenderingModel model;

    public TexturedModel(TextureReference texture, RenderingModel model) {
        this.texture = texture;
        this.model = model;
    }

    public TextureReference getTexture() {
        return texture;
    }

    public void setTexture(TextureReference texture) {
        this.texture = texture;
    }

    public RenderingModel getModel() {
        return model;
    }

    public void setModel(RenderingModel model) {
        this.model = model;
    }
}