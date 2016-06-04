package Tests;

import GraphicsEngine.DisplayManager;
import GraphicsEngine.Model.Loader;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.Renderer;
import org.lwjgl.opengl.Display;

/**
 * Created by giogio on 04/06/16.
 */
public class Test {

    public static void main(String[] args){

        DisplayManager.createDisplay("CrazyGolf Game");

        Loader loader = new Loader();
        Renderer renderer = new Renderer();

        float[] vertices = { -0.5f, 0.5f, 0f, -0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f, 0.5f, -0.5f, 0f, 0.5f, 0.5f, 0f, -0.5f, 0.5f, 0f
        };

        RawModel model = loader.loadToVAO(vertices);
        while (!Display.isCloseRequested()){
            renderer.prepare();
            renderer.render(model);
            DisplayManager.updateDisplay();
        }
        loader.cleanUP();
        DisplayManager.closeDisplay();


    }

}
