package Tests;

import GraphicsEngine.RenderEngine.DisplayManager;
import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.RenderEngine.Renderer;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Shaders.StaticShader;
import GraphicsEngine.Textures.ModelTexture;
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

        float[] textCoord = {
                0,0,
                0,1,
                1,1,
                1,0
        };

        int[] indices = {0,1,3,3,1,2};

        RawModel model = loader.loadToVAO(vertices, textCoord,indices);
        ModelTexture texture1 = new ModelTexture(loader.loadTexture("tree"));
        TexturedModel texturedModel = new TexturedModel(model,texture1);

        while (!Display.isCloseRequested()){
            renderer.prepare();
            shader.start();
            renderer.render(texturedModel);
            shader.stop();
            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        shader.cleanUp();
        DisplayManager.closeDisplay();


    }

}
