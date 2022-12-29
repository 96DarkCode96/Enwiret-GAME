package me.darkcode.objects;

import me.darkcode.game.GameRenderer;

import java.util.function.Function;

public class PlayerPOV {

    private static final float MOVE_SPEED = 40;
    private static final float RUN_SPEED_MULTIPLIER = 1.65f;
    private static final float SNEAK_SPEED_MULTIPLIER = 0.3f;
    private static final float GRAVITY = 10;
    private static final float PLAYER_EYE_Y_LEVEL = 1.85f;
    private final Function<Location, Float> yBottomCollisionFunction;
    private float sizeXY = 0.4f;
    private float MASS = 4;
    private boolean sprinting, sneaking, jumping;
    private float xSpeed;
    private float zSpeed;
    private float ySpeed;
    private Location location = new Location();

    public PlayerPOV(Function<Location, Float> yBottomCollisionFunction) {
        this.yBottomCollisionFunction = yBottomCollisionFunction;
    }

    public void teleport(float x, float y, float z){
        this.location.setX(x);
        this.location.setY(y+PLAYER_EYE_Y_LEVEL);
        this.location.setZ(z);
    }

    public void teleport(Location location){
        this.location.setX(location.getX());
        this.location.setY(location.getY()+PLAYER_EYE_Y_LEVEL);
        this.location.setZ(location.getZ());
    }

    public boolean isSprinting() {
        return sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public float getMASS() {
        return MASS;
    }

    public void setMASS(float MASS) {
        this.MASS = MASS;
    }

    public Location getLocation() {
        return location.clone().subtract(0, PLAYER_EYE_Y_LEVEL, 0);
    }

    public void setLocation(Location location) {
        this.location = location.clone().add(0, PLAYER_EYE_Y_LEVEL, 0);
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public void setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
    }

    public boolean isJumping() {
        return jumping;
    }

    public void setJumping(boolean jumping) {
        this.jumping = jumping;
    }

    public void move(GameRenderer gameRenderer) {
        ySpeed -= GRAVITY * MASS * gameRenderer.getDelta();
        float distanceX = xSpeed * gameRenderer.getDelta();
        float distanceZ = zSpeed * gameRenderer.getDelta();
        float distanceY = ySpeed * gameRenderer.getDelta();
        distanceY = updateCollisionY(distanceX, distanceY, distanceZ);
        location.add(distanceX, distanceY, distanceZ);
    }

    private float updateCollisionY(float addX, float addY, float addZ) {
        if(addY<0){
            Location newPosition = location.clone().add(addX, 0, addZ);
            float yBottomCollision = yBottomCollisionFunction.apply(newPosition) + PLAYER_EYE_Y_LEVEL;
            newPosition.add(0, addY, 0);
            if(newPosition.getY()<=yBottomCollision){
                ySpeed = 0;
                addY+=(yBottomCollision-newPosition.getY());
                tryJump();
            }
        }
        return addY;
    }

    private void tryJump() {
        if(isJumping()){
            ySpeed+=(GRAVITY/MASS)*3.75f;
        }
    }

    public float getSizeXY() {
        return sizeXY;
    }

    public void setSizeXY(float sizeXY) {
        this.sizeXY = sizeXY;
    }

    public void moveInputs(float xAdd, float zAdd) {
        float multiplier = MOVE_SPEED * (sprinting && sneaking ? 1 : (sprinting ? RUN_SPEED_MULTIPLIER : (sneaking ? SNEAK_SPEED_MULTIPLIER : 1)));
        this.xSpeed = xAdd * multiplier;
        this.zSpeed = zAdd * multiplier;
    }

    public Location getLocationCamera() {
        return location;
    }
}