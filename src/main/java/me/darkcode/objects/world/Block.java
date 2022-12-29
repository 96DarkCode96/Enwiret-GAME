package me.darkcode.objects.world;

public class Block {

    private final int blockX, blockY, blockZ;
    private final World world;

    public Block(int blockX, int blockY, int blockZ, World world) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.world = world;
    }

    public AbstractBlockData getBlockData(){
        return getChunk().getBlockType(blockX&15, blockY, blockZ&15);
    }

    public void setBlockData(AbstractBlockData blockData){
        getChunk().setBlockType(blockX&15, blockY, blockZ&15, blockData);
    }

    public Block getBlockAround(BlockFace face){
        return world.getBlock(blockX + face.getX(), blockY + face.getY(), + blockZ + face.getZ());
    }

    public boolean isChunkLoaded(){
        return world.isChunkLoaded(blockX>>4, blockZ>>4);
    }

    public Chunk getChunk(){
        return world.getChunk(blockX>>4, blockZ>>4);
    }

    public World getWorld() {
        return world;
    }

    public int getBlockX() {
        return blockX;
    }

    public int getBlockY() {
        return blockY;
    }

    public int getBlockZ() {
        return blockZ;
    }

    public boolean isSolid() {
        if(blockY<0 || blockY>World.MAX_WORLD_HEIGHT || !isChunkLoaded()){
            return false;
        }
        Chunk chunk = getChunk();
        ChunkSection section = chunk.getSections()[getBlockY()/16];
        if(section == null){
            return false;
        }
        return section.getSOLID_BLOCKS().get((getBlockX()&15) + (getBlockY()&15)*256 + (getBlockZ()&15)*16);
    }
}