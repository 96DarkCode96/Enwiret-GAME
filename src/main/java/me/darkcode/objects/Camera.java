package me.darkcode.objects;

public class Camera {

    private Location location;
    private Rotation rotation;

    public Camera(Location location, Rotation rotation) {
        this.location = location;
        this.rotation = rotation;
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
}