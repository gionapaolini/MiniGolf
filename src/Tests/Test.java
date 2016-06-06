package Tests;

import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Light;
import GraphicsEngine.RenderEngine.*;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
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

        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));

        Camera camera = new Camera();

        RawModel model = OBJLoader.loadObjModel("tree", loader);
        ModelTexture texture1 = new ModelTexture(loader.loadTexture("tree"));
        TexturedModel texturedModel = new TexturedModel(model,texture1);
        Entity entity = new Entity(texturedModel,new Vector3f(0,0,-5),0,0,0,1);

        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()){

            camera.moveOnSight();

            renderer.render(light,camera);

            renderer.processEntity(entity);


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }

}
