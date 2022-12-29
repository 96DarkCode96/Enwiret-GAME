package me.darkcode.objects.world;

import com.google.gson.JsonObject;

import java.util.HashMap;

public enum BlockType {
    AIR(0, false),
    STONE(1);

    private static final HashMap<Integer, BlockType> mapping = new HashMap<>();

    static{
        STONE.data.setTextureKey("/textures/images/stone.png");
        for (BlockType value : values()) {
            mapping.put(value.getId(), value);
        }
    }

    public static BlockType getById(int type) {
        return mapping.getOrDefault(type, AIR);
    }

    private final int id;
    private final boolean solid;
    private final AbstractBlockData data;

    BlockType(int id) {
        this(id, true);
    }

    BlockType(int id, boolean solid) {
        this(id, solid, new AbstractBlockData(new JsonObject()));
        data.setType(this);
    }

    BlockType(int id, boolean solid, AbstractBlockData data) {
        this.id = id;
        this.solid = solid;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public boolean isSolid() {
        return solid;
    }

    public AbstractBlockData getDefaultData() {
        return data.clone();
    }
}