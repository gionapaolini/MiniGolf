package Tests;

import GraphicsEngine.DisplayManager;
import org.lwjgl.opengl.Display;

/**
 * Created by giogio on 04/06/16.
 */
public class Test {

    public static void main(String[] args){

        DisplayManager.createDisplay("CrazyGolf Game");

        while (!Display.isCloseRequested()){
            DisplayManager.updateDisplay();
        }

        DisplayManager.closeDisplay();


    }

}
