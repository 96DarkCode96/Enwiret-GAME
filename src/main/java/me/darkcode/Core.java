package me.darkcode;

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
        long windowId = GLFW.glfwCreateWindow(1920, 1080, "Enwiret 0.1", GLFW.glfwGetPrimaryMonitor(), 0);
        if(windowId == 0L){
            System.exit(-202);
            return;
        }
        GLFW.glfwMakeContextCurrent(windowId);
        GLFW.glfwSwapInterval(1);
        GLFW.glfwShowWindow(windowId);

        // -----------------------------------------------------
        //
        //                       RENDER
        //
        // -----------------------------------------------------

        GL.createCapabilities();
        GL11.glClearColor(0, 0, 0, 0);

        while(!GLFW.glfwWindowShouldClose(windowId)){
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_STENCIL_BUFFER_BIT);



            GLFW.glfwSwapBuffers(windowId);
            GLFW.glfwPollEvents();
        }

        // -----------------------------------------------------
        //
        //                       DESTROY
        //
        // -----------------------------------------------------
        Callbacks.glfwFreeCallbacks(windowId);
        GLFW.glfwDestroyWindow(windowId);

        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }

}