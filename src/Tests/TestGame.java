package Tests;

import GameEngine.GuiControlGame;
import GameEngine.Human;
import GameEngine.Player;
import GameEngine.PlayerControl;
import GolfObjects.Ball;
import GolfObjects.Obstacle;
import GolfObjects.PutHole;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Light;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.Guis.GuiGame;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.RenderEngine.DisplayManager;
import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.RenderEngine.MasterRenderer;
import GraphicsEngine.RenderEngine.OBJLoader;
import GraphicsEngine.Textures.ModelTexture;
import GraphicsEngine.fontRendering.TextMaster;
import Toolbox.MousePicker;
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
        TextMaster.init(loader);
        GuiGame guiGame = new GuiGame(loader);
        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));
        Terrain terrain = new Terrain(-10,-10,20,20,loader,new ModelTexture(loader.loadTexture("grassy2")));

        MasterRenderer renderer = new MasterRenderer();

        RawModel ballModel = OBJLoader.loadObjModel("ball", loader);
        ModelTexture white = new ModelTexture(loader.loadTexture("white"));
        ModelTexture black = new ModelTexture(loader.loadTexture("black"));

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

        Obstacle obstacle = new Obstacle(obsta,null);


        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        obstacles.add(obstacle);

        MousePicker picker = new MousePicker();
        PlayerControl playerControl = new PlayerControl(players,camera, arrow,null,30,picker);

        GuiControlGame guiControlGame = new GuiControlGame(guiGame,playerControl,picker,null);

        Entity putHoleEnt = new Entity(new TexturedModel(OBJLoader.loadObjModel("underHole",loader),black),new Vector3f(2,-0.01f,2),0,0,0,1);
        Entity fakeHole = new Entity(new TexturedModel(OBJLoader.loadObjModel("putHole",loader),black),new Vector3f(2,0.001f,2),0,0,0,1);
        PutHole putHole = new PutHole(putHoleEnt,fakeHole);



        float timePhysics = 0.0084f;
        while (!Display.isCloseRequested()){
            camera.move();
            picker.update();
            guiControlGame.checkButtonsClick();
            playerControl.game(obstacles,terrain,putHole,timePhysics,null);
            renderer.render(light,camera);
            renderer.processTerrain(terrain);
            renderer.processEntity(ball.getModel());
            renderer.processEntity(ball1.getModel());
            if(!playerControl.disabledShot)
                renderer.processEntity(arrow);

            renderer.processEntity(obsta);
            renderer.processEntity(fakeHole);
            renderer.processEntity(putHoleEnt);

            guiGame.render();
            DisplayManager.updateDisplay();
        }
        TextMaster.cleanUp();
        guiGame.cleanUp();
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }

}
