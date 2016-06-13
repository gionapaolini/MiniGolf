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
        RawModel model1 = OBJLoader.loadObjModel("cubone", loader);

        ModelTexture texture1 = new ModelTexture(loader.loadTexture("white"));
        ModelTexture texture2 = new ModelTexture(loader.loadTexture("black"));

        TexturedModel texturedModel1 = new TexturedModel(model,texture1);
        TexturedModel texturedModel3 = new TexturedModel(model1,texture2);


        Entity entity = new Entity(texturedModel1,new Vector3f(2.5f,0,3f),0,0,0,0.25f);
        Entity entity2 = new Entity(texturedModel3,new Vector3f(0,0,0),0,45,0,1);



        Ball ball = new Ball(entity);
        Obstacle triangle1 = new Obstacle(entity2);
        ModelTexture red = new ModelTexture(loader.loadTexture("red"));

        MasterRenderer renderer = new MasterRenderer();
        float time = 0.0083f;
        Vector3f normal = new Vector3f(0,1,0);
        long last =0;
        TrianglePlane triangle;

        TrianglePlane[] trianglePlane = triangle1.getModel().getTriangles();
        for(int i = 0;i<trianglePlane.length;i++){
            if(trianglePlane[i].isFrontFacingTo(new Vector3f(0,0,-1)))
                System.out.println("Facing "+i);
        }
        ball.setVelocity(new Vector3f(0,0,-1));
        while (!Display.isCloseRequested()){
            /*if(Physics.checkBroadCollision(ball,triangle1))
                System.out.println(Physics.checkTriangle(ball,triangle));
                */
            Physics.setNewPosition(ball,time);

            //   points = entity2.getWorldPoints();
           // triangle = new TrianglePlane(points[0],points[1],points[2]);

          //  entity2.setRz(entity2.getRz()+0.5f);


            if(Keyboard.isKeyDown(Keyboard.KEY_V)){
                ball.setVelocity(new Vector3f(0,0,-1));
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_B)){
                ball.setVelocity(new Vector3f(0,0,1));
            }

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


            DisplayManager.updateDisplay();
        }
        loader.cleanUp();
        renderer.cleanUp();
        DisplayManager.closeDisplay();


    }



}
