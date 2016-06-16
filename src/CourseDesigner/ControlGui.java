package CourseDesigner;

import GolfObjects.Ball;
import GolfObjects.GolfObject;
import GolfObjects.Obstacle;
import GolfObjects.PutHole;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.Guis.GuiButton;
import GraphicsEngine.Guis.GuiCourseCreator;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.RenderEngine.OBJLoader;
import GraphicsEngine.Textures.ModelTexture;
import PhysicsEngine.Physics;
import Toolbox.Maths;
import Toolbox.MousePicker;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.List;

/**
 * Created by giogio on 16/06/16.
 */
public class ControlGui {
    Loader loader;
    GuiCourseCreator guiCourseCreator;
    List<Ball> balls;
    List<Obstacle> obstacles;
    PutHole putHole;
    Terrain terrain;
    Camera camera;
    MousePicker picker;

    GuiButton ballButton, putHoleButton, cubeButton, slopeButton, barButton, selectButton, saveButton, playButton, closeButton;

    boolean selectMode, isColliding;

    GolfObject currentObj;
    GolfObject overMouse;

    RawModel ballModel;
    RawModel cubeModel;
    RawModel slopeModel;
    RawModel barModel;

    ModelTexture white;
    ModelTexture black;
    ModelTexture red;

    long timeClickGui = 0;

    Vector3f previousPosition;


    public ControlGui(Loader loader, GuiCourseCreator guiCourseCreator, List<Ball> balls, List<Obstacle> obstacles, PutHole putHoles, Terrain terrain, Camera camera, MousePicker picker) {
        this.loader = loader;
        this.guiCourseCreator = guiCourseCreator;
        this.balls = balls;
        this.obstacles = obstacles;
        this.terrain = terrain;
        this.camera = camera;
        this.picker = picker;

        ballButton = guiCourseCreator.getBallButton();
        putHoleButton = guiCourseCreator.getPutholeButton();
        cubeButton = guiCourseCreator.getCubeButton();
        slopeButton = guiCourseCreator.getSlopeButton();
        barButton = guiCourseCreator.getBarButton();
        selectButton = guiCourseCreator.getSelectButton();


        white = new ModelTexture(loader.loadTexture("white"));
        red = new ModelTexture(loader.loadTexture("red"));
      //  black = new ModelTexture(loader.loadTexture("black"));
        this.putHole = putHoles;
        selectMode = false;
    }

    public void moveCurrentObj(){
        if(currentObj!=null) {
            picker.update();
            Vector3f point = picker.getCurrentTerrainPoint();
            if (point != null) {
                currentObj.setPosition(point);
                isColliding = false;
                for (Ball ball: balls) {
                    if (currentObj != ball && Physics.checkBroadCollision(currentObj, ball)) {
                        isColliding = true;
                        break;
                    }
                }
                if (!isColliding)
                    for (Obstacle obstacle : obstacles) {
                        if (currentObj != obstacle && Physics.checkBroadCollision(currentObj, obstacle)) {
                            isColliding = true;
                            break;
                        }
                    }
                if (!isColliding)
                    if (currentObj != putHole && Physics.checkBroadCollision(currentObj, putHole)) {
                        isColliding = true;
                    }
                if (isColliding) {
                    currentObj.setCollideColor(red);
                } else {
                    currentObj.unsetCollideColor();
                }
            }
        }

    }

    public void controls(){
        selectMode();
        checkButtonClicks();
        moveCurrentObj();
        transformCurrentObject();

    }

    private void selectMode(){
        if(selectMode && currentObj==null){
            picker.update();
            overMouse = null;
            Vector3f point = picker.getCurrentTerrainPoint();
            if(point !=null) {
                for (Obstacle obstacle : obstacles) {
                    if (pointInsideRectangle(point, obstacle)) {
                        overMouse = obstacle;
                        obstacle.setCollideColor(red);

                        break;
                    } else {
                        obstacle.unsetCollideColor();
                    }
                }
                for (Ball ball : balls) {
                    if (pointInsideRectangle(point, ball)) {
                        overMouse = ball;
                        ball.setCollideColor(red);
                        break;
                    } else {
                        ball.unsetCollideColor();
                    }
                }
                if (pointInsideRectangle(point, putHole)) {
                    overMouse = putHole;
                    putHole.setCollideColor(red);
                } else {
                    putHole.unsetCollideColor();
                }
            }
        }else {
            selectMode = false;
        }

    }

    private boolean pointInsideRectangle(Vector3f point,GolfObject obj){
        Vector3f[] points = obj.getWorldProjectionPoint();
        float area1 = areaTriangle(points[0],point,points[2]);
        float area2 = areaTriangle(points[2],point,points[3]);
        float area3 = areaTriangle(points[3],point,points[1]);
        float area4 = areaTriangle(point,points[1],points[0]);
        float sum = area1+area2+area3+area4;
        Vector4f[] pointsProj = obj.getProjectionPoints();
        Matrix4f worldPosition = Maths.createTransformationMatrix(new Vector3f(0,0,0),0,0,0,obj.getModel().getScale());

        float l1 = Maths.Vector4Matrix4Product(worldPosition,pointsProj[1]).x - Maths.Vector4Matrix4Product(worldPosition,pointsProj[0]).x;
        float l2 = Maths.Vector4Matrix4Product(worldPosition,pointsProj[2]).z - Maths.Vector4Matrix4Product(worldPosition,pointsProj[0]).z;
        if(sum>((l1*l2)+0.05f))
            return false;
        return true;

    }

    private float areaTriangle(Vector3f A,Vector3f B,Vector3f C){

        return (Math.abs(A.x * (B.z - C.z) + B.x * (C.z - A.z) + C.x*(A.z-B.z)))/2;

    }


    private void transformCurrentObject(){
        if(currentObj!=null){
            if(currentObj instanceof Obstacle) {
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
                    currentObj.getModel().setRy(currentObj.getModel().getRy() + 1);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
                    currentObj.getModel().setRy(currentObj.getModel().getRy() - 1);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
                    currentObj.getModel().setScale(currentObj.getModel().getScale() + 0.05f);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
                    currentObj.getModel().setScale(currentObj.getModel().getScale() - 0.05f);
                }
            }
            if(!(currentObj instanceof PutHole)){
                if(Keyboard.isKeyDown(Keyboard.KEY_DELETE)){
                    isColliding = true;
                    executeClick();
                }
            }
        }


    }

    public void checkButtonClicks(){

        long current_time = System.currentTimeMillis();

        if((current_time - timeClickGui)>250) {

            if (Mouse.isButtonDown(0)) {

                Vector2f mouseC = picker.getNormalCoord();


                if ((mouseC.x >= -0.898) && (mouseC.x <= -0.6) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    ballButton.swap();
                    putHoleButton.deselect();
                    cubeButton.deselect();
                    slopeButton.deselect();
                    barButton.deselect();
                    selectButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= -0.598) && (mouseC.x <= -0.3) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    putHoleButton.swap();
                    ballButton.deselect();
                    cubeButton.deselect();
                    slopeButton.deselect();
                    barButton.deselect();
                    selectButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= -0.298) && (mouseC.x <= 0) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    cubeButton.swap();
                    ballButton.deselect();
                    putHoleButton.deselect();
                    slopeButton.deselect();
                    barButton.deselect();
                    selectButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= 0) && (mouseC.x <= 0.299) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    barButton.swap();
                    ballButton.deselect();
                    putHoleButton.deselect();
                    cubeButton.deselect();
                    slopeButton.deselect();
                    selectButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= 0.301) && (mouseC.x <= 0.6) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    slopeButton.swap();
                    ballButton.deselect();
                    putHoleButton.deselect();
                    cubeButton.deselect();
                    barButton.deselect();
                    selectButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= 0.601) && (mouseC.x <= 0.9) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    selectButton.swap();
                    ballButton.deselect();
                    putHoleButton.deselect();
                    cubeButton.deselect();
                    barButton.deselect();
                    slopeButton.deselect();
                    checkButtons();

                }else{

                    ballButton.deselect();
                    putHoleButton.deselect();
                    cubeButton.deselect();
                    slopeButton.deselect();
                    barButton.deselect();
                    //selectButton.deselect();
                    executeClick();
                    checkButtons();

                }

                timeClickGui = System.currentTimeMillis();


            }
        }
    }

    private void executeClick(){
        if(selectMode && overMouse!=null){
                currentObj = overMouse;
                selectMode = false;

        }else if(currentObj!=null){
            if(isColliding){
                if(currentObj instanceof PutHole){
                    putHole.setPosition(previousPosition);
                    putHole.unsetCollideColor();
                }else if(currentObj instanceof Ball){
                    balls.remove(currentObj);
                }else {
                    obstacles.remove(currentObj);
                }
            }
            currentObj = null;
        }
    }

    private void checkButtons(){
        selectMode = false;
        if(!selectButton.isSelected() && currentObj!=null){
            if(currentObj instanceof PutHole){
                putHole.setPosition(previousPosition);
            }else if(currentObj instanceof Ball){
                balls.remove(currentObj);
            }else {
                obstacles.remove(currentObj);
            }
            currentObj = null;
        }

        if(ballButton.isSelected()){
            attachNewBall();
        }else if(putHoleButton.isSelected()){
            attachPutHole();
        }else if(cubeButton.isSelected()){
            attachNewCube();
        }else if(barButton.isSelected()){
            attachNewBar();
        }else if(slopeButton.isSelected()){
            attachNewSlope();
        }else if(selectButton.isSelected()){
            selectMode = true;
        }


    }

    private void attachNewBall(){
        if(ballModel==null) {
            ballModel = OBJLoader.loadObjModel("ball", loader);
        }
        TexturedModel model = new TexturedModel(ballModel,white);
        Entity n = new Entity(model,new Vector3f(0,0,0),0,0,0,1);
        currentObj = new Ball(n);
        balls.add((Ball)currentObj);
    }

    private void attachNewCube(){
        if(cubeModel==null) {
            cubeModel = OBJLoader.loadObjModel("cube2", loader);
        }
        TexturedModel model = new TexturedModel(cubeModel,white);
        Entity n = new Entity(model,new Vector3f(0,0,0),0,0,0,1);
        currentObj = new Obstacle(n);
        obstacles.add((Obstacle)currentObj);
    }

    private void attachNewSlope(){
        if(slopeModel==null) {
            slopeModel = OBJLoader.loadObjModel("slope", loader);
        }
        TexturedModel model = new TexturedModel(slopeModel,white);
        Entity n = new Entity(model,new Vector3f(0,0,0),0,0,0,1);
        currentObj = new Obstacle(n);
        obstacles.add((Obstacle)currentObj);
    }

    private void attachNewBar(){
        if(barModel==null) {
            barModel = OBJLoader.loadObjModel("bar", loader);
        }
        TexturedModel model = new TexturedModel(barModel,white);
        Entity n = new Entity(model,new Vector3f(0,0,0),0,0,0,1);
        currentObj = new Obstacle(n);
        obstacles.add((Obstacle)currentObj);
    }

    private void attachPutHole(){
        currentObj = putHole;
        previousPosition = putHole.getPosition();
    }


}
