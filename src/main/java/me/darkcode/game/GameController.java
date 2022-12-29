package me.darkcode.game;

import me.darkcode.objects.Cursor;
import me.darkcode.objects.Location;
import me.darkcode.objects.PlayerPOV;
import me.darkcode.objects.Rotation;
import me.darkcode.objects.world.Block;
import me.darkcode.objects.world.World;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

public class GameController {

    private final Game game;
    private boolean inGame = false;
    private boolean movementControlInput = true;
    private @Nullable World world;
    private final PlayerPOV playerPOV;

    public GameController(Game game) {
        this.game = game;
        this.playerPOV = new PlayerPOV(this::getNextYBottomCollision);
        updateInGame(true);
        world = new World("MainWorld");
        world.getChunk(0, 0).updateFacesToRender(getGameRenderer());
        world.getChunk(-1, 0).updateFacesToRender(getGameRenderer());
        world.getChunk(-1, -1).updateFacesToRender(getGameRenderer());
        world.getChunk(0, -1).updateFacesToRender(getGameRenderer());
        getGameRenderer().getCamera().setLocation(playerPOV.getLocationCamera());
    }

    private Float getNextYBottomCollision(Location location) {
        if(this.world == null){
            return Float.MIN_VALUE;
        }
        Location clone = location.clone();
        while(clone.getBlockY()>=0){
            clone.add(0, -1, 0);
            Block block = world.getBlock(clone.getBlockX(), clone.getBlockY(), clone.getBlockZ());
            if(block.isSolid()){
                return clone.getBlockY()+1f;
            }
        }
        return Float.MIN_VALUE;
    }

    private GameRenderer getGameRenderer(){
        return (GameRenderer) game.getRenderers().stream().filter(a -> a instanceof GameRenderer).findFirst().get();
    }

    public void rotate(float yaw, float pitch){
        if(inGame && movementControlInput){
            getGameRenderer().getCamera().getRotation().add(yaw, -pitch);
        }
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public boolean isMovementControlInput() {
        return movementControlInput;
    }

    public void setMovementControlInput(boolean movementControlInput) {
        this.movementControlInput = movementControlInput;
    }

    public void move(float angle){
        if(inGame && movementControlInput && world != null){
            Rotation rotation = getGameRenderer().getCamera().getRotation();
            float xAdd = (float) Math.sin(Math.toRadians(rotation.getYaw()+angle))/10;
            float zAdd = -(float) Math.cos(Math.toRadians(rotation.getYaw()+angle))/10;
            playerPOV.moveInputs(xAdd, zAdd);
        }else{
            playerPOV.moveInputs(0, 0);
        }
    }

    public void cancelMove(){
        if(inGame && movementControlInput && world != null){
            playerPOV.moveInputs(0, 0);
        }
    }

    public PlayerPOV getPlayerPOV() {
        return playerPOV;
    }

    public void key(int key, int scancode, int action, int mods){
        if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_PRESS){
            updateInGame(!inGame);
        }else if(key == GLFW.GLFW_KEY_LEFT_CONTROL){
            if(action == GLFW.GLFW_PRESS){
                playerPOV.setSprinting(true);
            }else if(action == GLFW.GLFW_RELEASE){
                playerPOV.setSprinting(false);
            }
        }else if(key == GLFW.GLFW_KEY_LEFT_SHIFT){
            if(action == GLFW.GLFW_PRESS){
                playerPOV.setSneaking(true);
            }else if(action == GLFW.GLFW_RELEASE){
                playerPOV.setSneaking(false);
            }
        }else if(key == GLFW.GLFW_KEY_SPACE){
            if(action == GLFW.GLFW_PRESS){
                playerPOV.setJumping(true);
            }else if(action == GLFW.GLFW_RELEASE){
                playerPOV.setJumping(false);
            }
        }
    }

    public Game getGame() {
        return game;
    }

    public void updateInGame(boolean value){
        inGame = value;
        if(value){
            Cursor.disableCursor(game);
        }else{
            Cursor.normalCursor(game);
        }
    }

    public boolean rendering() {
        return inGame;
    }
}