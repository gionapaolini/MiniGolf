package Tests;

import GameEngine.Human;
import GameEngine.Player;
import GameEngine.PlayerControl;
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

import Toolbox.MousePicker;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 04/06/16.
 */
public class TestPhysics{

    public static void main(String[] args){

        DisplayManager.createDisplay("CrazyGolf Game");

        Loader loader = new Loader();
        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));
        Terrain terrain = new Terrain(-10,-10,20,20,loader,new ModelTexture(loader.loadTexture("grassy2")));

        RawModel model = OBJLoader.loadObjModel("ball", loader);


        ModelTexture white = new ModelTexture(loader.loadTexture("white"));
        ModelTexture red = new ModelTexture(loader.loadTexture("red"));

        TexturedModel texturedModel1 = new TexturedModel(model,white);
        Entity n = new Entity(texturedModel1, new Vector3f(1, 0, 0), 0, 0, 0, 1);

        Ball ball = new Ball(n);

        Camera camera = new Camera();

        TexturedModel obstacleModel = new TexturedModel(OBJLoader.loadObjModel("underHole", loader),white);
        Entity obsta = new Entity(obstacleModel, new Vector3f(0,0.01f,3), 0,0,0,1);

        Obstacle obstacle = new Obstacle(obsta);
        MasterRenderer renderer = new MasterRenderer();

        MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(), terrain);
        float time = 0.0083f;
        Vector3f normal = new Vector3f(0,1,0);
        ball.setPosition(new Vector3f(0,0,0));
        ball.setVelocity(new Vector3f(0,0,6));
        while (!Display.isCloseRequested()){
            camera.move();
            picker.update();
            Physics.applyGravity(ball,time);
            //Physics.applyFriction(ball,time);


            if(!Physics.checkBroadCollision(ball,obstacle) && ball.getPosition().y<=0){
                Physics.applyCollision(ball, normal, time);

            }
            Physics.collision(ball,obstacle,time);
            Physics.setNewPosition(ball,time,terrain);

            if(Keyboard.isKeyDown(Keyboard.KEY_ADD)){
                time+=0.0001;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)){
                time-=0.0001;
            }

            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                ball.setPosition(new Vector3f(0,0,0));
                ball.setVelocity(new Vector3f(0,0,6));
            }
            renderer.render(light,camera);
            renderer.processTerrain(terrain);
            renderer.processEntity(n);
            renderer.processEntity(obsta);
            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }



}
