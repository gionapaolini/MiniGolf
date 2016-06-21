package Tests;

import CourseDesigner.ControlGui;
import GameEngine.*;
import GolfObjects.Ball;
import GolfObjects.Obstacle;
import GolfObjects.PutHole;
import GolfObjects.Surface;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Light;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.Guis.GuiCourseCreator;
import GraphicsEngine.Guis.GuiGame;
import GraphicsEngine.Guis.GuiMenu;
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

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 19/06/16.
 */
public class TestMenu {

    public static void main(String[] args) throws FileNotFoundException {

        DisplayManager.createDisplay("CrazyGolf Game");
        Loader loader = new Loader();
        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));
        Camera camera = new Camera();
        MasterRenderer masterRenderer = new MasterRenderer();
        Settings settings = new Settings();
        Terrain terrain = new Terrain(-10,-10,20,20,loader,new ModelTexture(loader.loadTexture("grassy2")));
        GuiCourseCreator guiCourseCreator = new GuiCourseCreator(loader);
        TextMaster.init(loader);
        MousePicker mousePicker = new MousePicker(camera,masterRenderer.getProjectionMatrix(),terrain);
        GuiMenu menu = new GuiMenu(loader);
        MasterRenderer renderer = new MasterRenderer();
        ModelTexture black = new ModelTexture(loader.loadTexture("black"));

        Entity putHoleEnt = new Entity(new TexturedModel(OBJLoader.loadObjModel("underHole",loader),black),new Vector3f(-1,0,0),0,0,0,1);
        Entity fakeHole = new Entity(new TexturedModel(OBJLoader.loadObjModel("putHole",loader),black),new Vector3f(-1,0,0),0,0,0,1);
        PutHole putHole = new PutHole(putHoleEnt,fakeHole);
        ModelTexture white = new ModelTexture(loader.loadTexture("white"));

        List<Ball> balls = new ArrayList<Ball>();
        List<Obstacle> obstacles = new ArrayList<Obstacle>();
        List<Player> players = new ArrayList<Player>();
        TexturedModel arrowModel = new TexturedModel(OBJLoader.loadObjModel("arrow", loader),white);
        Entity arrow = new Entity(arrowModel, new Vector3f(1,0.001f,1), 0,0,0,1);
        TexturedModel arrow3DModel = new TexturedModel(OBJLoader.loadObjModel("arrow3D", loader),white);
        Entity arrow3D = new Entity(arrow3DModel, new Vector3f(1,0.001f,1), 0,0,0,1);
        List<Surface> surfaces = new ArrayList<Surface>();
        ControlGui controlGui = new ControlGui(loader,guiCourseCreator,balls,obstacles,putHole,terrain,mousePicker,settings, surfaces);
        PlayerControl playerControl = new PlayerControl(players,camera, arrow, arrow3D,30, mousePicker);
        MenuControl menuControl = new MenuControl(menu,mousePicker,settings,controlGui,players,balls,playerControl);

        GuiGame guiGame = new GuiGame(loader);
        GuiControlGame guiControlGame = new GuiControlGame(guiGame,playerControl,mousePicker,settings);

        float timePhysics = 0.0084f;
        while (!Display.isCloseRequested()){

            switch(settings.getPhase()){
                case 1:
                    camera.moveOnSight();
                    renderer.render(light,camera);
                    renderer.processTerrain(terrain);
                    guiCourseCreator.render();
                    controlGui.controls();
                    for(Ball ball: balls){

                        renderer.processEntity(ball.getModel());
                    }
                    for(Obstacle obstacle: obstacles){
                        renderer.processEntity(obstacle.getModel());
                    }
                    for(Surface surface: surfaces){
                        renderer.processEntity(surface.getModel());
                    }
                    renderer.processEntity(putHole.getFakeHole());

                    break;
                case 2:
                    renderer.render(light,camera);
                    menuControl.checkButtonClick();
                    menu.render();

                    break;
                case 3:
                    mousePicker.update();
                    guiControlGame.checkButtonsClick();
                    playerControl.game(obstacles,terrain,putHole,timePhysics,surfaces);
                    camera.move();
                    renderer.render(light,camera);
                    renderer.processTerrain(terrain);
                    for(Ball ball: balls){

                        renderer.processEntity(ball.getModel());
                    }
                    for(Obstacle obstacle: obstacles){
                        renderer.processEntity(obstacle.getModel());
                    }
                    for(Surface surface: surfaces){
                        renderer.processEntity(surface.getModel());
                    }
                    renderer.processEntity(putHole.getFakeHole());
                    if(!playerControl.disabledShot){
                        renderer.processEntity(arrow);
                        renderer.processEntity(arrow3D);
                    }

                    guiGame.render();

                    menuControl.time = System.currentTimeMillis();
                    break;



            }



            DisplayManager.updateDisplay();


        }
        menu.cleanUp();
        loader.cleanUp();
        masterRenderer.cleanUp();

        DisplayManager.closeDisplay();


    }
}
