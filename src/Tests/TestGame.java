package Tests;

import GameEngine.Human;
import GameEngine.Player;
import GameEngine.PlayerControl;
import GolfObjects.Ball;
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
import Toolbox.MousePicker;
import org.lwjgl.input.Keyboard;
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
        Entity n1 = new Entity(model, new Vector3f(0, 0, 1), 0, 0, 0, 1);
        Entity n2 = new Entity(model, new Vector3f(1, 0, 1), 0, 0, 0, 1);
        Ball ball = new Ball(n);
        Ball ball1 = new Ball(n1);
        Ball ball2 = new Ball(n2);
        List<Player> players = new ArrayList<Player>();
        Human player = new Human(ball);
        Human player1 = new Human(ball1);
        Human player2 = new Human(ball2);
        players.add(player);
        players.add(player1);
        players.add(player2);

        Camera camera = new Camera(player);
        

        PlayerControl playerControl = new PlayerControl(players,camera);

        MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(), terrain);




        while (!Display.isCloseRequested()){
            camera.move();
            renderer.render(light,camera);
            renderer.processTerrain(terrain);
            renderer.processEntity(ball.getModel());
            renderer.processEntity(ball1.getModel());
            renderer.processEntity(ball2.getModel());

            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
                playerControl.nextPlayer();
            }


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }

}
