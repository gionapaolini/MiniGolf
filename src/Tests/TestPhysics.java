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
        RawModel model1 = OBJLoader.loadObjModel("slope", loader);

        ModelTexture white = new ModelTexture(loader.loadTexture("white"));
        ModelTexture red = new ModelTexture(loader.loadTexture("red"));

        TexturedModel texturedModel1 = new TexturedModel(model,white);
        TexturedModel texturedModel3 = new TexturedModel(model1,red);
        Entity n = new Entity(texturedModel1, new Vector3f(1, 0, 0), 0, 0, 0, 1);

        Ball ball = new Ball(n);

        List<Player> players = new ArrayList<Player>();
        Human player = new Human(ball);
        players.add(player);

        Camera camera = new Camera(player);
        TexturedModel arrowModel = new TexturedModel(OBJLoader.loadObjModel("arrow", loader),white);
        Entity arrow = new Entity(arrowModel, new Vector3f(1,0.001f,1), 0,0,0,1);
        TexturedModel obstacleModel = new TexturedModel(OBJLoader.loadObjModel("slope", loader),white);
        Entity obsta = new Entity(obstacleModel, new Vector3f(3,0,0), 0,0,0,1);

        Obstacle obstacle = new Obstacle(obsta);
        MasterRenderer renderer = new MasterRenderer();
        PlayerControl playerControl = new PlayerControl(players,camera, arrow,30);

        MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(), terrain);
        System.out.println(n.getHighestPoint());
        float time = 0.0083f;
        Vector3f normal = new Vector3f(0,1,0);

        long lastCall = 0;
        while (!Display.isCloseRequested()){
            camera.move();
            picker.update();
            playerControl.moveArrow(arrow,picker.getCurrentTerrainPoint());
            Physics.applyGravity(ball,time);
            Physics.applyFriction(ball,time);
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                playerControl.nextPlayer();
            }
            if(System.currentTimeMillis()-time>100 && Mouse.isButtonDown(0)){
                playerControl.shot(picker.getCurrentTerrainPoint());
            }
            if(ball.getPosition().y<=0){
                Physics.applyCollision(ball, normal, time);

            }

            lastCall = Physics.collision(ball,obstacle,time,lastCall);
            Physics.setNewPosition(ball,time,terrain);

            if(Keyboard.isKeyDown(Keyboard.KEY_ADD)){
                time+=0.0001;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)){
                time-=0.0001;
            }
            renderer.render(light,camera);
            renderer.processTerrain(terrain);
            renderer.processEntity(n);
            renderer.processEntity(obsta);
            renderer.processEntity(arrow);


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }



}
