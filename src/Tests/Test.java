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
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("black"));

        TexturedModel texturedModel1 = new TexturedModel(model,texture1);
        TexturedModel texturedModel2 = new TexturedModel(model,texture2);

        Entity entity = new Entity(texturedModel1,new Vector3f(1f,2,2.5f),0,0,0,0.25f);
        Entity entity1 = new Entity(texturedModel2,new Vector3f(4f,2,2.5f),0,0,0,0.25f);



        Ball ball = new Ball(entity);
        Ball ball1 = new Ball(entity1);
        ball.setVelocity(new Vector3f(0.5f,0,0));
        ball1.setVelocity(new Vector3f(-0.5f,0,0));

        MasterRenderer renderer = new MasterRenderer();
        float time = 0.0183f;
        Vector3f normal = new Vector3f(0,1,0);
        long last =0;
        while (!Display.isCloseRequested()){

            Physics.applyGravity(ball, time);
            if(ball.getPosition().y<=0)
                Physics.applyCollision(ball, normal, time);
            Physics.setNewPosition(ball, time);

            Physics.applyGravity(ball1, time);
            if(ball.getPosition().y<=0)
                Physics.applyCollision(ball1, normal, time);
            Physics.setNewPosition(ball1, time);
            if(ball.getPosition().x<2.55 && ball.getPosition().x>2.45 && System.currentTimeMillis()-last>1000){
                System.out.println("d");
                Physics.applyCollision(ball, ball1, new Vector3f(1,0,0), time);
                last = System.currentTimeMillis();
            }
            camera.moveOnSight();
            renderer.render(light,camera);
            renderer.processTerrain(terrain);
            renderer.processEntity(entity);
            renderer.processEntity(entity1);


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }

}
