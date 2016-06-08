package Tests;

import GolfObjects.Ball;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Light;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.RenderEngine.*;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Textures.ModelTexture;
import PhysicsEngine.Physics;
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
        Terrain terrain = new Terrain(0,0,5,5,loader,new ModelTexture(loader.loadTexture("grassy2")));

        RawModel model = OBJLoader.loadObjModel("pallo", loader);
        ModelTexture texture1 = new ModelTexture(loader.loadTexture("white"));
        TexturedModel texturedModel = new TexturedModel(model,texture1);
        Entity entity = new Entity(texturedModel,new Vector3f(2.5f,2,2.5f),0,0,0,0.25f);

        Ball ball = new Ball(entity);
        MasterRenderer renderer = new MasterRenderer();
        float time = 0.0183f;
        Vector3f normal = new Vector3f(0,1,0);
        while (!Display.isCloseRequested()){

            Physics.applyGravity(ball, time);
            if(ball.getPosition().y<=0)
                Physics.applyCollision(ball, normal, time);
            Physics.setNewPosition(ball, time);
            camera.moveOnSight();
            renderer.render(light,camera);
            renderer.processTerrain(terrain);
            renderer.processEntity(entity);


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }

}
