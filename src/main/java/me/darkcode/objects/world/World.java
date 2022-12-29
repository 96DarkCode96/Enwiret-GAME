package me.darkcode.objects.world;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class World {

    private final ExecutorService service = Executors.newFixedThreadPool(10);

    public static final int MAX_WORLD_HEIGHT = 16 * 16;

    private final String name;
    private final Cache<Long, Chunk> loadedChunks = CacheBuilder.newBuilder()
            .maximumSize(25 * 25)
            .build();

    public World(String name) {
        this.name = name;
    }

    public Chunk loadChunk(int chunkX, int chunkZ) {
        long key = (((long) chunkX) << 32) | (chunkZ & 0xffffffffL);
        Chunk chunk = new Chunk(this, chunkX, chunkZ);
        chunk.generate();
        loadedChunks.put(key, chunk);
        return chunk;
    }

    public Block getBlock(int x, int y, int z) {
        return new Block(x, y, z, this);
    }

    public Chunk getChunk(int chunkX, int chunkZ) {
        long key = (((long) chunkX) << 32) | (chunkZ & 0xffffffffL);
        Chunk chunk = loadedChunks.getIfPresent(key);
        if (chunk == null) {
            chunk = loadChunk(chunkX, chunkZ);
        }
        return chunk;
    }

    public void unloadChunk(int chunkX, int chunkZ) {
        long key = (((long) chunkX) << 32) | (chunkZ & 0xffffffffL);
        loadedChunks.invalidate(key);
    }

    public String getName() {
        return name;
    }

    public boolean isChunkLoaded(int chunkX, int chunkZ) {
        return loadedChunks.getIfPresent((((long) chunkX) << 32) | (chunkZ & 0xffffffffL)) != null;
    }

    public Collection<Chunk> getLoadedChunks() {
        return loadedChunks.asMap().values();
    }

    public CompletableFuture<Chunk> loadAsync(int chunkX, int chunkZ) {
        if(isChunkLoaded(chunkX, chunkZ)){
            return CompletableFuture.completedFuture(getChunk(chunkX, chunkX));
        }
        return CompletableFuture.supplyAsync(() -> getChunk(chunkX, chunkZ), service);
    }
}