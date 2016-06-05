package Tests;

import GraphicsEngine.Entities.Entity;
import GraphicsEngine.RenderEngine.DisplayManager;
import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.RenderEngine.Renderer;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Shaders.StaticShader;
import GraphicsEngine.Textures.ModelTexture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

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
        Entity entity = new Entity(texturedModel,new Vector3f(-1,0,0),0,0,0,1);

        while (!Display.isCloseRequested()){
            entity.increasePosition(0.002f,0,0);
            entity.increaseRotation(0,1,0);
            renderer.prepare();
            shader.start();
            renderer.render(entity,shader);
            shader.stop();
            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        shader.cleanUp();
        DisplayManager.closeDisplay();


    }

}
