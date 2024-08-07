package xyz.snaker.jsnake.keyboard;

import org.lwjgl.glfw.GLFW;

/**
 * Created by SnakerBone on 15/08/2023
 **/
public class Keyboard
{
    /**
     * Checks if a key is being pressed
     *
     * @param handle The window of the current GLFW window
     * @param key    A GLFW printable key
     * @return True if the key is currently being pressed
     **/
    public static boolean isKeyDown(long handle, int key)
    {
        return GLFW.glfwGetKey(handle, key) == 1;
    }

    /**
     * Checks if a key is not being pressed
     *
     * @param handle The window of the current GLFW window
     * @param key    A GLFW printable key
     * @return True if the key is currently not being pressed
     **/
    public static boolean isKeyUp(long handle, int key)
    {
        return GLFW.glfwGetKey(handle, key) == 0;
    }
}
