package Tests;

import GraphicsEngine.DisplayManager;
import GraphicsEngine.Model.Loader;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.Renderer;
import GraphicsEngine.Shaders.StaticShader;
import org.lwjgl.opengl.Display;

/**
 * Created by giogio on 04/06/16.
 */
public class Test {

    public static void main(String[] args){

        DisplayManager.createDisplay("CrazyGolf Game");

        Loader loader = new Loader();
        Renderer renderer = new Renderer();
        StaticShader shader = new StaticShader();

        float[] vertices = { -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f
        };

        int[] indices = {0,1,3,3,1,2};

        RawModel model = loader.loadToVAO(vertices,indices);
        while (!Display.isCloseRequested()){
            renderer.prepare();
            shader.start();
            renderer.render(model);
            shader.start();
            DisplayManager.updateDisplay();
        }
        loader.cleanUP();
        shader.cleanUp();
        DisplayManager.closeDisplay();


    }

}
