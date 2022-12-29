package me.darkcode.objects.world;

import java.util.BitSet;

public class ChunkSection {

    private final Chunk chunk;
    private final int y;
    private final BitSet SOLID_BLOCKS = new BitSet(4096);
    private final ChunkPalette chunkPalette = new ChunkPalette(0, BlockType.AIR.getDefaultData());

    public ChunkSection(Chunk chunk, int y) {
        this.chunk = chunk;
        this.y = y;
        recalculateAll();
    }

    private void recalculateAll() {
        int x, y, z;
        for (int i = 0; i < 4096; i++) {
            x = i % 16;
            z = (i / 16) % 16;
            y = i / 256;
            recalculateBlock(x, y, z, getBlockData(x, y, z));
        }
    }

    public void recalculateBlock(int chunkSectionBlockX, int chunkSectionBlockY, int chunkSectionBlockZ, AbstractBlockData blockData) {
        SOLID_BLOCKS.set(chunkSectionBlockX + chunkSectionBlockY * 256 + chunkSectionBlockZ * 16, blockData.getType().isSolid());
    }

    public AbstractBlockData getBlockData(int chunkSectionBlockX, int chunkSectionBlockY, int chunkSectionBlockZ) {
        return chunkPalette.getValue(chunkSectionBlockX, chunkSectionBlockY, chunkSectionBlockZ);
    }

    public ChunkPalette getChunkPalette() {
        return chunkPalette;
    }

    public BitSet getSOLID_BLOCKS() {
        return SOLID_BLOCKS;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public int getY() {
        return y;
    }

    public void setBlockData(int chunkSectionBlockX, int chunkSectionBlockY, int chunkSectionBlockZ, AbstractBlockData blockData) {
        chunkPalette.setValue(chunkSectionBlockX, chunkSectionBlockY, chunkSectionBlockZ, blockData);
        recalculateBlock(chunkSectionBlockX, chunkSectionBlockY, chunkSectionBlockZ, blockData);
    }

}