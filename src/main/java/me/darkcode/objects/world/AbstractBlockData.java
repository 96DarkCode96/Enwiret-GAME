package me.darkcode.objects.world;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Objects;

public class AbstractBlockData implements Cloneable {

    private JsonObject object;

    public AbstractBlockData(JsonObject object) {
        this.object = object;
    }

    public JsonObject getObject() {
        return object;
    }

    public void setObject(JsonObject object) {
        this.object = object;
    }

    public BlockType getType() {
        JsonElement element = object.get("type");
        return element == null ? BlockType.AIR : BlockType.getById(element.getAsInt());
    }

    public void setType(BlockType type){
        object.addProperty("type", type.getId());
    }

    @Override
    public AbstractBlockData clone() {
        try {
            AbstractBlockData clone = (AbstractBlockData) super.clone();
            clone.setObject(this.getObject().deepCopy());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public void setTextureKey(String textureKey){
        object.addProperty("texture", textureKey);
    }

    public void setFaceTextureKey(BlockFace face, String textureKey){
        object.addProperty("texture_face_" + face.name().toLowerCase(), textureKey);
    }

    public String getFaceTextureKey(BlockFace face) {
        JsonElement element = object.get("texture_face_" + face.name().toLowerCase());
        JsonElement global = object.get("texture");
        return element == null ? (global==null ? null : global.getAsString()) : element.getAsString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractBlockData that)) return false;
        return Objects.equals(getObject(), that.getObject());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getObject());
    }
}