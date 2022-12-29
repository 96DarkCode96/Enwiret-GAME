package me.darkcode.render;

import me.darkcode.objects.Location;
import me.darkcode.objects.world.BlockFace;

public class RenderingFace{
    private final Location location;
    private final int textureId;
    private final BlockFace blockFace;

    public RenderingFace(int x, int y, int z, int textureId, BlockFace blockFace){
        this.location = new Location(x, y, z);
        this.textureId = textureId;
        this.blockFace = blockFace;
    }

    public Location getLocation() {
        return location.clone();
    }

    public int getTextureId() {
        return textureId;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }
}