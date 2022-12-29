package me.darkcode.objects;

import java.util.Locale;

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

    public Location add(Location location) {
        this.x += location.getX();
        this.y += location.getY();
        this.z += location.getZ();
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

    public float distanceXZ(Location to){
        float diffX = Math.abs(getX() - to.getX());
        float diffZ = Math.abs(getZ() - to.getZ());
        return (float) Math.sqrt(Math.pow(diffX, 2) + Math.pow(diffZ, 2));
    }

    public String format(String format) {
        return String.format(Locale.US, format, getX(), getY(), getZ());
    }

    public int getBlockX() {
        return (int) Math.floor(getX());
    }

    public int getBlockY() {
        return (int) Math.floor(getY());
    }

    public int getBlockZ() {
        return (int) Math.floor(getZ());
    }

    public String formatChunk(String format) {
        return String.format(Locale.US, format, getBlockX()>>4, getBlockY()/16, getBlockZ()>>4);
    }
}