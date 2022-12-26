package me.darkcode.objects;

public class Location implements Cloneable{

    private float x, y, z;

    public Location(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location() {}

    public Location add(float x, float y, float z){
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Location add(int x, int y, int z){
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Location subtract(float x, float y, float z){
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Location subtract(int x, int y, int z){
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public Location clone() {
        try {
            Location clone = (Location) super.clone();
            clone.setX(this.x);
            clone.setY(this.y);
            clone.setZ(this.z);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}