package me.darkcode.game.network;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.function.Supplier;

public class PacketMapper {
    private static final HashMap<Class<? extends Packet>, Integer> clazzToId = new HashMap<>();
    private static final HashMap<Integer, Supplier<? extends Packet>> idToConstructor = new HashMap<>();

    static{

    }

    public static int getId(Class<? extends Packet> clazz){
        return clazzToId.getOrDefault(clazz, -1);
    }

    public static @Nullable Packet createPacket(int id){
        return idToConstructor.getOrDefault(id, () -> null).get();
    }

    private static void registerPacket(Class<? extends Packet> clazz, Supplier<? extends Packet> constructor, int id){
        clazzToId.put(clazz, id);
        idToConstructor.put(id, constructor);
    }

}