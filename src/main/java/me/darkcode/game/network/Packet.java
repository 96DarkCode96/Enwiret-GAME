package me.darkcode.game.network;

import io.netty.buffer.ByteBuf;

public interface Packet {
    void read(ByteBuf data, Direction direction);
    void write(ByteBuf data, Direction direction);
    long minBytes(Direction direction);
    long maxBytes(Direction direction);
    boolean handle(PacketHandler handler);

    public enum Direction{
        SERVER, CLIENT;
    }
}