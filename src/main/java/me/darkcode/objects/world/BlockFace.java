package me.darkcode.objects.world;

import me.darkcode.objects.Location;
import me.darkcode.objects.Rotation;

public enum BlockFace {
    UP(0, 1, 0, new Location(0, 1, 1), new Rotation(0, -90)),
    DOWN(0, -1, 0, new Location(0, 0, 0), new Rotation(0, 90)),
    RIGHT(1, 0, 0, new Location(1, 0, 1), new Rotation(90, 0)),
    LEFT(-1, 0, 0, new Location(0, 0, 0), new Rotation(-90, 0)),
    FRONT(0, 0, 1, new Location(0, 0, 1), new Rotation(0, 0)),
    BACK(0, 0, -1, new Location(1, 0, 0), new Rotation(180, 0));

    private final int x;
    private final int y;
    private final int z;
    private final Location renderFace;
    private final Rotation renderRotation;

    BlockFace(int x, int y, int z, Location renderFace, Rotation renderRotation) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.renderFace = renderFace;
        this.renderRotation = renderRotation;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Location getRenderFace() {
        return renderFace;
    }

    public Rotation getRenderRotation() {
        return renderRotation;
    }
}