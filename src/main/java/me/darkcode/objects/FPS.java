package me.darkcode.objects;

public class FPS {

    private static int tempFps = 0;
    private static int lastFps = 0;
    private static long lastClean = System.currentTimeMillis();

    public static void tick(){
        long now = System.currentTimeMillis();
        if(now - lastClean >= 1000L){
            lastFps = tempFps;
            tempFps = 0;
            lastClean = System.currentTimeMillis();
        }
        tempFps++;
    }

    public static int getFps(){
        return lastFps;
    }

}