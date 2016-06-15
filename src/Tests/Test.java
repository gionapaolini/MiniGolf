package Tests;

import GolfObjects.Ball;
import GolfObjects.Obstacle;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Light;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.RenderEngine.*;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Textures.ModelTexture;
import PhysicsEngine.Physics;
import PhysicsEngine.TrianglePlane;
import org.lwjgl.input.Keyboard;
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
        RawModel model1 = OBJLoader.loadObjModel("slope2", loader);

        ModelTexture texture1 = new ModelTexture(loader.loadTexture("white"));
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("red"));

        TexturedModel texturedModel1 = new TexturedModel(model,texture1);
        TexturedModel texturedModel3 = new TexturedModel(model1,texture2);


        Entity entity = new Entity(texturedModel1,new Vector3f(4.325f,0,5f),0,0,0,0.25f);
        Entity entity2 = new Entity(texturedModel3,new Vector3f(0,0,0),0,0,0,1f);
        Entity entity3 = new Entity(texturedModel3,new Vector3f(5f,0,3),0,-90,0,0.25f);


        Ball ball = new Ball(entity);
        Obstacle triangle1 = new Obstacle(entity2);
        Obstacle triangle2 = new Obstacle(entity3);
        ModelTexture red = new ModelTexture(loader.loadTexture("red"));

        MasterRenderer renderer = new MasterRenderer();
        float time = 0.0083f;
        Vector3f normal = new Vector3f(0,1,0);


        ball.setVelocity(new Vector3f(0,0,1));
        ball.setPosition(new Vector3f(1,0,-4));
        long lastCall = 0;
        while (!Display.isCloseRequested()){
            Physics.applyGravity(ball,time);
            if(ball.getPosition().y<=0){
                Physics.applyCollision(ball, normal, time);
                //Physics.applyFriction(ball,normal,time);
            }

            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                ball.setVelocity(new Vector3f(3f, -0.5f,0));
                ball.setPosition(new Vector3f(-1.7f,2.3f,0.2f));
            }

            lastCall = Physics.collision(ball,triangle1,time,lastCall);
            lastCall = Physics.collision(ball,triangle2,time,lastCall);
            Physics.setNewPosition(ball,time);
            if(Keyboard.isKeyDown(Keyboard.KEY_ADD)){
                time+=0.0001;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)){
                time-=0.0001;
            }


            camera.moveOnSight();
            renderer.render(light,camera);
            renderer.processTerrain(terrain);
            renderer.processEntity(entity);
            renderer.processEntity(entity2);
            renderer.processEntity(entity3);


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }



}
