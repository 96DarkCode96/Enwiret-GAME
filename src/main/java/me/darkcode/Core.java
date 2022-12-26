package me.darkcode;

import me.darkcode.game.Game;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;

public class Core {

    public static void main(String[] args) {
        // -----------------------------------------------------
        //
        //                     INIT WINDOW
        //
        // -----------------------------------------------------
        GLFWErrorCallback.createPrint(System.err).set();
        if(!GLFW.glfwInit()){
            System.exit(-201);
            return;
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_FALSE);

        WindowReference windowReference = new WindowReference(
                GLFW.glfwCreateWindow(1920, 1080, "Enwiret 0.1", GLFW.glfwGetPrimaryMonitor(), 0)
        );
        if(windowReference.getWindowId() == 0L){
            System.exit(-202);
            return;
        }
        GLFW.glfwMakeContextCurrent(windowReference.getWindowId());
        GLFW.glfwSwapInterval(1);
        windowReference.show();

        // -----------------------------------------------------
        //
        //                       RENDER
        //
        // -----------------------------------------------------

        GL.createCapabilities();
        GL11.glClearColor(0, 0, 0, 0);

        Game game = new Game(windowReference);

        while(!GLFW.glfwWindowShouldClose(windowReference.getWindowId())){
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);

            game.render();

            GLFW.glfwSwapBuffers(windowReference.getWindowId());
            GLFW.glfwPollEvents();
        }

        // -----------------------------------------------------
        //
        //                       DESTROY
        //
        // -----------------------------------------------------
        game.exit(Game.Exit.WINDOW_CLOSED);
        Callbacks.glfwFreeCallbacks(windowReference.getWindowId());
        GLFW.glfwDestroyWindow(windowReference.getWindowId());

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

}