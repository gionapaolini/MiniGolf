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
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.RenderEngine.DisplayManager;
import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.RenderEngine.MasterRenderer;
import GraphicsEngine.RenderEngine.OBJLoader;
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
 * Created by giogio on 18/06/16.
 */
public class TestGame {
    public static void main(String[] args){

        DisplayManager.createDisplay("CrazyGolf Game");

        Loader loader = new Loader();
        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));
        Terrain terrain = new Terrain(-10,-10,20,20,loader,new ModelTexture(loader.loadTexture("grassy2")));

        MasterRenderer renderer = new MasterRenderer();

        RawModel ballModel = OBJLoader.loadObjModel("ball", loader);
        ModelTexture white = new ModelTexture(loader.loadTexture("white"));
        TexturedModel model = new TexturedModel(ballModel, white);
        Entity n = new Entity(model, new Vector3f(1, 0, 0), 0, 0, 0, 1);
        Entity n1 = new Entity(model, new Vector3f(1, 0, 1), 0, 0, 0, 1);

        Ball ball = new Ball(n);
        Ball ball1 = new Ball(n1);

        List<Player> players = new ArrayList<Player>();
        Human player = new Human(ball);
        Human player2 = new Human(ball1);

        players.add(player);
        players.add(player2);


        Camera camera = new Camera(player);
        TexturedModel arrowModel = new TexturedModel(OBJLoader.loadObjModel("arrow", loader),white);
        Entity arrow = new Entity(arrowModel, new Vector3f(1,0.001f,1), 0,0,0,1);

        TexturedModel obstacleModel = new TexturedModel(OBJLoader.loadObjModel("slope", loader),white);
        Entity obsta = new Entity(obstacleModel, new Vector3f(-4,0,0),0,0,0,1);
        Obstacle obstacle = new Obstacle(obsta);


        PlayerControl playerControl = new PlayerControl(players,camera, arrow);

        MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(), terrain);




        float timePhysics = 0.0084f;
        long lastCall = 0;
        while (!Display.isCloseRequested()){
            camera.move();
            picker.update();
            playerControl.moveArrow(arrow,picker.getCurrentTerrainPoint());
            playerControl.shot(picker.getCurrentTerrainPoint());
            Ball currentBall = playerControl.getCurrentPlayer().getBall();
            lastCall = 0;
            Physics.collision(currentBall,obstacle,timePhysics,lastCall);
            Physics.applyGravity(currentBall,timePhysics);
            Physics.applyFriction(currentBall,timePhysics);
            Physics.terrainCollision(currentBall,terrain,timePhysics);
            Physics.setNewPosition(currentBall,timePhysics);
            playerControl.nextPlayer();
            renderer.render(light,camera);
            renderer.processTerrain(terrain);
            renderer.processEntity(ball.getModel());
            renderer.processEntity(ball1.getModel());
            if(!playerControl.disabledShot)
                renderer.processEntity(arrow);
            renderer.processEntity(obsta);

            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }

}
