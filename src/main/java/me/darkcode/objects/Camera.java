package me.darkcode.objects;


import javax.swing.text.Position;

public class Camera {

    private float fov;
    private float farPlane;
    private float nearPlane;
    private Location location;
    private Rotation rotation;

    public Camera(float fov, float farPlane, float nearPlane, Location location, Rotation rotation) {
        this.fov = fov;
        this.farPlane = farPlane;
        this.nearPlane = nearPlane;
        this.location = location;
        this.rotation = rotation;
    }

    public float getFOV() {
        return fov;
    }

    public float getFarPlane() {
        return farPlane;
    }

    public float getNearPlane() {
        return nearPlane;
    }

    public void setFOV(float fov) {
        this.fov = fov;
    }

    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
    }

    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
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