package me.darkcode.objects.world;

import me.darkcode.game.GameRenderer;
import me.darkcode.objects.Location;
import me.darkcode.objects.ResourceKey;
import me.darkcode.render.RenderingFace;
import me.darkcode.render.TextureReference;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.BitSet;

public class Chunk {

    private final @NotNull World world;
    private final int chunkX, chunkZ;
    private final ChunkSection[] sections;

    public Chunk(@NotNull World world, int chunkX, int chunkZ) {
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.sections = new ChunkSection[World.MAX_WORLD_HEIGHT / 16];
    }

    public Chunk(@NotNull World world, int chunkX, int chunkZ, ChunkSection[] sections) {
        if(sections.length!=World.MAX_WORLD_HEIGHT/16){
            throw new IllegalArgumentException("ChunkSections invalid length! Must be " + (World.MAX_WORLD_HEIGHT/16));
        }
        this.world = world;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.sections = sections;
    }

    public @NotNull World getWorld() {
        return world;
    }

    public int getChunkX() {
        return chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public ChunkSection[] getSections() {
        return sections;
    }

    public AbstractBlockData getBlockType(int chunkBlockX, int chunkBlockY, int chunkBlockZ) {
        ChunkSection chunkSection = sections[chunkBlockY/16];
        if(chunkSection == null){
            chunkSection = sections[chunkBlockY/16] = loadSection(chunkBlockY/16);
        }
        return chunkSection.getBlockData(chunkBlockX, chunkBlockY&15, chunkBlockZ);
    }

    private ChunkSection loadSection(int chunkSectionY) {
        return new ChunkSection(this, chunkSectionY);
    }

    public void setBlockType(int chunkBlockX, int chunkBlockY, int chunkBlockZ, AbstractBlockData blockData) {
        ChunkSection chunkSection = sections[chunkBlockY/16];
        if(chunkSection == null){
            chunkSection = sections[chunkBlockY/16] = loadSection(chunkBlockY/16);
        }
        chunkSection.setBlockData(chunkBlockX, chunkBlockY&15, chunkBlockZ, blockData);
    }

    public void updateFacesToRender(GameRenderer gameRenderer) {
        int x,y,z;
        ArrayList<RenderingFace> faces = new ArrayList<>();
        for (ChunkSection section : sections) {
            if(section == null){
                continue;
            }
            BitSet data = section.getSOLID_BLOCKS();
            for(int i = 0; i < 4096; i++){
                if(data.get(i)) {
                    x = chunkX*16 + i%16;
                    y = section.getY() * 16 + i/256;
                    z = chunkZ * 16 + (i/16)%16;
                    Block block = world.getBlock(x, y, z);
                    for (BlockFace value : BlockFace.values()) {
                        if(!solid(x+value.getX(), y+value.getY(), z+value.getZ())){
                            TextureReference texture = TextureReference.getTexture(new ResourceKey(block.getBlockData().getFaceTextureKey(value)));
                            if(texture != null){
                                faces.add(new RenderingFace(x, y, z, texture.getId(), value));
                            }
                        }
                    }
                }
            }
        }
        gameRenderer.updateFaces(this, faces);
    }

    private boolean solid(int x, int y, int z) {
        return world.getBlock(x, y, z).isSolid();
    }

    public void generate() {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < World.MAX_WORLD_HEIGHT; y++) {
                    if(y == 0){
                        AbstractBlockData data = BlockType.STONE.getDefaultData();
                        setBlockType(x, y, z, data);
                    }
                }
            }
        }
    }

    public Location getCenterLocation() {
        return new Location(chunkX*16 + 8, 0, chunkZ*16 + 8);
    }

    public long getKey() {
        return (((long) chunkX) << 32) | (chunkZ & 0xffffffffL);
    }
}