package Tests;

import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Light;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.RenderEngine.*;
import GraphicsEngine.Model.RawModel;
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

        StaticShader shader = new StaticShader();
        MasterRenderer renderer = new MasterRenderer();
       // Terrain terrain = new Terrain(0,0,5,5,loader,new ModelTexture(loader.loadTexture("grassy2")));
        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));

        Camera camera = new Camera();

        RawModel model = OBJLoader.loadObjModel("tree", loader);
        ModelTexture texture1 = new ModelTexture(loader.loadTexture("tree"));
        TexturedModel texturedModel = new TexturedModel(model,texture1);
        Entity entity = new Entity(texturedModel,new Vector3f(0,0,-5),0,0,0,1);

        while (!Display.isCloseRequested()){

            camera.moveOnSight();
            renderer.prepare();
            shader.start();
            shader.loadLight(light);
            shader.loadViewMatrix(camera);
            renderer.processEntity(entity);
  //          renderer.processTerrain(terrain);
            shader.stop();
            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        shader.cleanUp();
        DisplayManager.closeDisplay();


    }

}
