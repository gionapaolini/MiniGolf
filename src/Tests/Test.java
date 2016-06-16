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
import GraphicsEngine.Guis.GuiRenderer;
import GraphicsEngine.Guis.GuiTexture;
import GraphicsEngine.RenderEngine.*;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Textures.ModelTexture;
import PhysicsEngine.Physics;
import Toolbox.Maths;
import Toolbox.MousePicker;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 04/06/16.
 */
public class Test {

    public static void main(String[] args){

        DisplayManager.createDisplay("CrazyGolf Game");

        Loader loader = new Loader();
        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));
        Camera camera = new Camera();
        Terrain terrain = new Terrain(0,0,15,15,loader,new ModelTexture(loader.loadTexture("grassy2")));

        MasterRenderer renderer = new MasterRenderer();

        MousePicker picker = new MousePicker(camera,renderer.getProjectionMatrix(), terrain);

        GuiCourseCreator guiCourseCreator = new GuiCourseCreator(loader);

        ModelTexture black = new ModelTexture(loader.loadTexture("black"));


        PutHole putHole = new PutHole(new Entity(new TexturedModel(OBJLoader.loadObjModel("putHole", loader),black),new Vector3f(0,0,0),0,0,0,1));


        List<Ball> balls = new ArrayList<Ball>();
        List<Obstacle> obstacles = new ArrayList<Obstacle>();


        ControlGui controlGui = new ControlGui(loader,guiCourseCreator,balls,obstacles,putHole,terrain,camera,picker);


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
            renderer.processEntity(putHole.getModel());


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        guiCourseCreator.cleanUp();
        DisplayManager.closeDisplay();


    }



}
