package Tests;

import CourseDesigner.ControlGui;
import GolfObjects.Ball;
import GolfObjects.Obstacle;
import GolfObjects.PutHole;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Light;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.Guis.GuiCourseCreator;

import GraphicsEngine.RenderEngine.*;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Textures.ModelTexture;

import Toolbox.MousePicker;

import org.lwjgl.opengl.Display;

import org.lwjgl.util.vector.Vector3f;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 04/06/16.
 */
public class TestCourseDesigner {

    public static void main(String[] args) throws FileNotFoundException {

        DisplayManager.createDisplay("CrazyGolf Game");

        Loader loader = new Loader();
        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));
        Camera camera = new Camera();
        Terrain terrain = new Terrain(-10,-10,20,20,loader,new ModelTexture(loader.loadTexture("grassy2")));

        MasterRenderer renderer = new MasterRenderer();

        MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(), terrain);

        GuiCourseCreator guiCourseCreator = new GuiCourseCreator(loader);

        ModelTexture black = new ModelTexture(loader.loadTexture("black"));

        Entity putHoleEnt = new Entity(new TexturedModel(OBJLoader.loadObjModel("underHole",loader),black),new Vector3f(-1,0,0),0,0,0,1);
        Entity fakeHole = new Entity(new TexturedModel(OBJLoader.loadObjModel("putHole",loader),black),new Vector3f(-1,0,0),0,0,0,1);
        PutHole putHole = new PutHole(putHoleEnt,fakeHole);


        List<Ball> balls = new ArrayList<Ball>();
        List<Obstacle> obstacles = new ArrayList<Obstacle>();


        ControlGui controlGui = new ControlGui(loader,guiCourseCreator,balls,obstacles,putHole,terrain,picker,null, null);


        while (!Display.isCloseRequested()){
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
            renderer.processEntity(putHole.getFakeHole());


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        guiCourseCreator.cleanUp();
        DisplayManager.closeDisplay();


    }



}
