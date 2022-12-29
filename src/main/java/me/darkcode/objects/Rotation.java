package me.darkcode.objects;

import java.util.Locale;

public class Rotation implements Cloneable{

    private float yaw;
    private float pitch;

    public Rotation(float yaw, float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Rotation() {}

    public Rotation add(float yaw, float pitch){
        this.yaw+=yaw;
        this.pitch+=pitch;
        crop();
        return this;
    }

    public Rotation add(int yaw, int pitch){
        this.yaw+=yaw;
        this.pitch+=pitch;
        crop();
        return this;
    }

    public Rotation subtract(float yaw, float pitch){
        this.yaw-=yaw;
        this.pitch-=pitch;
        crop();
        return this;
    }

    public Rotation subtract(int yaw, int pitch){
        this.yaw-=yaw;
        this.pitch-=pitch;
        crop();
        return this;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void crop(){
        pitch = Math.max(-90, Math.min(90, pitch));
        yaw%=360;
        if(yaw < 0){
            yaw+=360;
        }
    }

    @Override
    public Rotation clone() {
        try {
            Rotation clone = (Rotation) super.clone();
            clone.setPitch(this.pitch);
            clone.setYaw(this.yaw);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public String format(String format) {
        return String.format(Locale.US, format, getYaw(), getPitch());
    }

}