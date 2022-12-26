package me.darkcode.render;

import me.darkcode.objects.Location;
import me.darkcode.objects.Rotation;

public class RenderingEntity {

    private TexturedModel texturedModel;
    private Location location;
    private Rotation rotation;
    private float scale;

    public RenderingEntity(TexturedModel texturedModel, Location location, Rotation rotation, float scale) {
        this.texturedModel = texturedModel;
        this.location = location;
        this.rotation = rotation;
        this.scale = scale;
    }

    public TexturedModel getTexturedModel() {
        return texturedModel;
    }

    public void setTexturedModel(TexturedModel texturedModel) {
        this.texturedModel = texturedModel;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}