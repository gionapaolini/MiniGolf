package CourseDesigner;

import GameEngine.Settings;
import GolfObjects.*;
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
import Toolbox.SaveAndLoad;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * Created by giogio on 16/06/16.
 */
public class ControlGui {
    Loader loader;
    GuiCourseCreator guiCourseCreator;
    List<Ball> balls;
    List<Obstacle> obstacles;
    List<Surface> surfaces;
    PutHole putHole;
    Terrain terrain;
    MousePicker picker;
    RawModel model;

    GuiButton ballButton, cubeButton, slopeButton, barButton, selectButton, saveButton, playButton, closeButton;

    boolean selectMode, isColliding, widthMode;

    GolfObject currentObj;
    GolfObject overMouse;

    RawModel ballModel;
    RawModel cubeModel;
    RawModel slopeModel;
    RawModel barModel;

    ModelTexture white;
    ModelTexture sand,bar,wood;
    ModelTexture red, mud, box;
    int count;
    Vector3f[] frictionTriangle;

    long timeClickGui = 0;
    long timeTerrainSize = 0;
    long timeForTriangle;

    Settings settings;



    Vector3f previousPositionHole, previousPositionBall;

    private float mostNegativeX, mostNegativeZ, mostPositiveX, mostPositiveZ;


    public ControlGui(Loader loader, GuiCourseCreator guiCourseCreator, List<Ball> balls, List<Obstacle> obstacles, PutHole putHoles, Terrain terrain, MousePicker picker, Settings settings, List<Surface> surfaces) {
        this.loader = loader;
        this.guiCourseCreator = guiCourseCreator;
        this.balls = balls;
        this.obstacles = obstacles;
        this.terrain = terrain;
        this.surfaces = surfaces;
        this.picker = picker;

        ballButton = guiCourseCreator.getBallButton();
        saveButton = guiCourseCreator.getSaveButtonButton();
        cubeButton = guiCourseCreator.getCubeButton();
        slopeButton = guiCourseCreator.getSlopeButton();
        barButton = guiCourseCreator.getBarButton();
        selectButton = guiCourseCreator.getSelectButton();
        frictionTriangle = new Vector3f[3];

        white = new ModelTexture(loader.loadTexture("white"));
        red = new ModelTexture(loader.loadTexture("red"));
        mud = new ModelTexture(loader.loadTexture("mud"));
        sand = new ModelTexture(loader.loadTexture("sand"));
        box = new ModelTexture(loader.loadTexture("box"));
        wood = new ModelTexture(loader.loadTexture("wood"));
        bar = new ModelTexture(loader.loadTexture("bar"));


        this.putHole = putHoles;
        selectMode = false;
        saveButton.select();
        this.settings = settings;
        if(balls.size()==0) {
            attachNewBall();
        }else {
            setAllLimitTerrain();
        }
    }

    public void moveCurrentObj(){
        if(currentObj!=null) {

            picker.update();
            Vector3f point = picker.getCurrentTerrainPoint();
            if (point != null) {
                currentObj.setPosition(point);
                isColliding = false;
                if(!(currentObj instanceof Surface)) {
                    Entity currentEntity;
                    if(currentObj instanceof PutHole)
                        currentEntity= ((PutHole) currentObj).getFakeHole();
                    else
                        currentEntity = currentObj.getModel();
                    for (Ball ball : balls) {
                        if (currentObj != ball && Physics.checkBroadCollision(currentEntity, ball.getModel())) {
                            isColliding = true;
                            break;
                        }
                    }
                    if (!isColliding)
                        for (Obstacle obstacle : obstacles) {
                            if (currentObj != obstacle && Physics.checkBroadCollision(currentEntity, obstacle.getModel())) {
                                isColliding = true;
                                break;
                            }
                        }
                    if (!isColliding)
                        if (currentObj != putHole && Physics.checkBroadCollision(currentEntity, putHole.getFakeHole())) {
                            isColliding = true;
                        }
                    if (isColliding) {
                        currentObj.setCollideColor(red);
                    } else {
                        currentObj.unsetCollideColor();
                    }
                }else {
                    currentObj.unsetCollideColor();
                }
            }
        }

    }

    public void controls() throws FileNotFoundException {
        selectMode();
        checkButtonClicks();
        moveCurrentObj();
        transformCurrentObject();
        changeSizeTerrain();
        createTriangleFriction();

    }

    public void applyNumberBalls(){
        int n = settings.getnBot()+settings.getnHuman();
        if(n>balls.size()){
            int s = balls.size();
            for(int i=0;i<n-s;i++){
                attachNewBall();
            }
        }
        if(n<balls.size()){
            int s = balls.size();
            for(int i=0;i<s-n;i++){
                balls.remove(balls.size()-1);
            }
        }

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
                for (Surface surface : surfaces) {
                    if (surface.mouseOver(point)) {
                        overMouse = surface;
                        surface.setCollideColor(red);
                        break;
                    } else {
                        surface.unsetCollideColor();
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
        Vector3f[] points;
        if(obj instanceof PutHole)
            points = ((PutHole) obj).getFakeHole().getWorldProjectionPoints();
        else
            points = obj.getWorldProjectionPoint();
        float area1 = areaTriangle(points[0],point,points[2]);
        float area2 = areaTriangle(points[2],point,points[3]);
        float area3 = areaTriangle(points[3],point,points[1]);
        float area4 = areaTriangle(point,points[1],points[0]);
        float sum = area1+area2+area3+area4;
        Vector4f[] pointsProj = obj.getProjectionPoints();
        Matrix4f worldPosition = Maths.createTransformationMatrix(new Vector3f(0,0,0),0,0,0,obj.getModel().getScaleX(),obj.getModel().getScaleY(),obj.getModel().getScaleZ());

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
            if(currentObj instanceof Obstacle || currentObj instanceof Surface) {
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD4)) {
                    currentObj.getModel().setRy(currentObj.getModel().getRy() + 1);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD6)) {
                    currentObj.getModel().setRy(currentObj.getModel().getRy() - 1);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
                    currentObj.getModel().setScale(currentObj.getModel().getScale() + 0.025f);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD2)) {
                    currentObj.getModel().setScale(currentObj.getModel().getScale() - 0.025f);
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DELETE)) {
                isColliding = true;
                executeClick();
                deactivateButtons();
                checkButtons();

            }

        }


    }

    public void deactivateButtons(){
        ballButton.deselect();
        cubeButton.deselect();
        slopeButton.deselect();
        barButton.deselect();
        saveButton.deselect();
    }



    public void checkButtonClicks() throws FileNotFoundException {

        long current_time = System.currentTimeMillis();

        if((current_time - timeClickGui)>250) {

            if (Mouse.isButtonDown(0)) {

                Vector2f mouseC = picker.getNormalCoord();


                if ((mouseC.x >= -0.898) && (mouseC.x <= -0.6) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    settings.setPhase(2);
                    selectButton.deselect();
                    saveButton.deselect();

                    ballButton.deselect();
                    cubeButton.deselect();
                    barButton.deselect();
                    slopeButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= -0.598) && (mouseC.x <= -0.3) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    cubeButton.swap();
                    ballButton.deselect();
                    slopeButton.deselect();
                    barButton.deselect();
                    saveButton.deselect();

                    selectButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= -0.298) && (mouseC.x <= 0) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    barButton.swap();
                    ballButton.deselect();
                    slopeButton.deselect();
                    saveButton.deselect();

                    cubeButton.deselect();
                    selectButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= 0) && (mouseC.x <= 0.299) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    slopeButton.swap();
                    ballButton.deselect();
                    saveButton.deselect();

                    cubeButton.deselect();
                    barButton.deselect();
                    selectButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= 0.301) && (mouseC.x <= 0.6) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    selectButton.swap();
                    ballButton.deselect();
                    cubeButton.deselect();
                    barButton.deselect();
                    saveButton.deselect();

                    slopeButton.deselect();
                    checkButtons();
                }else if ((mouseC.x >= 0.601) && (mouseC.x <= 0.9) && (mouseC.y <= 0.894) && (mouseC.y >= 0.783)) {
                    SaveAndLoad.save(terrain, obstacles, balls, surfaces, putHole,"save");
                }else{

                    ballButton.deselect();
                    cubeButton.deselect();
                    slopeButton.deselect();
                    barButton.deselect();
                    saveButton.deselect();
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
                if(currentObj instanceof Ball && balls.size()==1){
                    previousPositionBall = currentObj.getPosition();
                }else if(currentObj instanceof PutHole){
                    previousPositionHole = currentObj.getPosition();
                }

                selectMode = false;

        }else if(currentObj!=null){
            if(isColliding){
                if(currentObj instanceof PutHole){
                    putHole.setPosition(previousPositionHole);
                    putHole.unsetCollideColor();
                }else if(currentObj instanceof Ball && balls.size()>1){
                    balls.remove(currentObj);
                }else if(currentObj instanceof Ball && balls.size()==1){
                    currentObj.setPosition(previousPositionBall);
                }else if(currentObj instanceof Surface){
                    surfaces.remove(currentObj);
                }else {
                    obstacles.remove(currentObj);
                }
            }

            currentObj = null;
            setAllLimitTerrain();
        }
    }

    private void checkButtons(){
        selectMode = false;
        if(!selectButton.isSelected() && currentObj!=null){
            isColliding=true;
            executeClick();

        }

        if(ballButton.isSelected()){

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
        if (ballModel == null) {
            ballModel = OBJLoader.loadObjModel("ball", loader);
        }
        TexturedModel model = new TexturedModel(ballModel, white);
        boolean colliding;
        Entity n;
        do {
            colliding = false;
            float randX = (float) Math.random() *2* (terrain.getX() + terrain.width-0.2f) - (terrain.getX() + terrain.width -0.2f);
            float randZ = (float) Math.random() *2* (terrain.getZ() + terrain.height-0.2f) - (terrain.getZ() + terrain.height -0.2f);
            n = new Entity(model, new Vector3f(randX, 0, randZ), 0, 0, 0, 1);
            for(Ball ball: balls){
                if(Physics.checkBroadCollision(n,ball.getModel())) {
                    colliding = true;
                    break;
                }
            }
            if(!colliding) {
                for (Obstacle obstacle : obstacles) {
                    if (Physics.checkBroadCollision(n, obstacle.getModel())) {
                        colliding = true;
                        break;
                    }
                }
            }
            if(!colliding && Physics.checkBroadCollision(n,putHole.getFakeHole())) {
                colliding = true;
            }
        }while(colliding);

        balls.add(new Ball(n));
        setAllLimitTerrain();

    }

    private void attachNewCube(){
        if(cubeModel==null) {
            cubeModel = OBJLoader.loadObjModel("cube", loader);
        }
        TexturedModel model = new TexturedModel(cubeModel,box);
        Entity n = new Entity(model,new Vector3f(0,0,0),0,0,0,1);
        currentObj = new Obstacle(n,"cube");
        obstacles.add((Obstacle)currentObj);
    }

    private void attachNewSlope(){
        if(slopeModel==null) {
            slopeModel = OBJLoader.loadObjModel("slope", loader);
        }
        TexturedModel model = new TexturedModel(slopeModel,wood);
        Entity n = new Entity(model,new Vector3f(0,0,0),0,0,0,1);
        currentObj = new Obstacle(n,"slope");
        obstacles.add((Obstacle)currentObj);
    }

    private void attachNewBar(){
        if(barModel==null) {
            barModel = OBJLoader.loadObjModel("bar", loader);
        }
        TexturedModel model = new TexturedModel(barModel,bar);
        Entity n = new Entity(model,new Vector3f(0,0,0),0,0,0,1);
        currentObj = new Obstacle(n,"bar");
        obstacles.add((Obstacle)currentObj);
    }

    private void setAllLimitTerrain(){
        mostPositiveX = -100;
        mostNegativeX = 100;
        mostNegativeZ = 100;
        mostPositiveZ = -100;
        for(Obstacle obstacle: obstacles){
            setLimitTerrain(obstacle);
        }
        for(Ball obstacle: balls){
            setLimitTerrain(obstacle);
        }
        setLimitTerrain(putHole);
    }

    private void setLimitTerrain(GolfObject obj){
        Vector3f[] points = obj.getWorldProjectionPoint();
        for(int i=0;i<4;i++){
            if(points[i].x<mostNegativeX)
                mostNegativeX = points[i].x;
            if(points[i].z<mostNegativeZ)
                mostNegativeZ = points[i].z;
            if(points[i].x>mostPositiveX)
                mostPositiveX = points[i].x;
            if(points[i].z>mostPositiveZ)
                mostPositiveZ = points[i].z;
        }
    }

    private void createTriangleFriction(){


        if(Keyboard.isKeyDown(Keyboard.KEY_X) || Keyboard.isKeyDown(Keyboard.KEY_C)){
            picker.update();
            if(System.currentTimeMillis()-timeForTriangle>500 && Mouse.isButtonDown(0) && picker.getCurrentTerrainPoint()!=null){

                frictionTriangle[count] = picker.getCurrentTerrainPoint();
                count++;
                timeForTriangle = System.currentTimeMillis();
                if(count==3){
                    Vector3f p2p1sub = Maths.vector3SUB(frictionTriangle[1],frictionTriangle[0]);
                    Vector3f p3p1sub = Maths.vector3SUB(frictionTriangle[2],frictionTriangle[0]);
                    Vector3f normal = Maths.crossProduct(p2p1sub,p3p1sub);
                    normal.normalise();
                    if(normal.y<0){
                        Vector3f temp = frictionTriangle[0];
                        frictionTriangle[0] = frictionTriangle[2];
                        frictionTriangle[2] = temp;
                    }
                    float centreX =(frictionTriangle[0].x + frictionTriangle[1].x +frictionTriangle[2].x)/3;
                    float centreZ =(frictionTriangle[0].z + frictionTriangle[1].z +frictionTriangle[2].z)/3;


                    float[] vertexArray = new float[9];
                    for(int j=0;j<3;j++){
                        vertexArray[j*3]=frictionTriangle[j].x-centreX;
                        vertexArray[j*3+1]=frictionTriangle[j].y+0.008f;
                        vertexArray[j*3+2]=frictionTriangle[j].z-centreZ;
                    }
                    int[] indices = new int[3];
                    indices[0] = 0;
                    indices[1] = 1;
                    indices[2] = 2;
                    float[] textureArray = new float[3*2];
                    textureArray[0]=1;
                    textureArray[1]=0;
                    textureArray[2]=0;
                    textureArray[3]=0;
                    textureArray[4]=0;
                    textureArray[5]=1;
                    float[] normalArray = new float[3*3];
                    for(int j=0;j<3;j++){
                        normalArray[j*3]=0;
                        normalArray[j*3+1]=1;
                        normalArray[j*3+2]=0;
                    }

                    for(int j=0;j<9;j++){
                        System.out.println(vertexArray[j]);
                    }




                    model = loader.loadToVAO(vertexArray,textureArray,normalArray,indices);

                    model.setVerticesArray(vertexArray);
                    model.setNormalsArray(normalArray);
                    model.setIndicesArray(indices);
                    if(Keyboard.isKeyDown(Keyboard.KEY_X)) {
                        TexturedModel model1 = new TexturedModel(model, mud);
                        Entity newEnt = new Entity(model1, new Vector3f(centreX, 0, centreZ), 0, 0, 0, 1);
                        surfaces.add(new Surface(newEnt,8f,frictionTriangle, "mud"));
                    }else if(Keyboard.isKeyDown(Keyboard.KEY_C)){
                        TexturedModel model1 = new TexturedModel(model, sand);
                        Entity newEnt1 = new Entity(model1, new Vector3f(centreX, 0, centreZ), 0, 0, 0, 1);
                        surfaces.add(new Surface(newEnt1,16f,frictionTriangle, "sand"));
                    }
                    System.out.println("Current Triangle");
                    for(int i =0;i<3;i++){
                        System.out.println(frictionTriangle[i]);
                    }
                    System.out.println("Surface list");

                    for(Surface surface: surfaces) {
                        System.out.println("Triangle");
                        for (int i=0;i<3;i++)
                        System.out.println(surface.points[i]);
                    }
                    count = 0;
                }
            }
        }else {
            count=0;
        }
    }



    private void changeSizeTerrain(){
        if(currentObj==null) {
            float width = terrain.width;
            float height = terrain.height;
            long current_time = System.currentTimeMillis();

            if ((current_time - timeTerrainSize) > 250) {
                if (Keyboard.isKeyDown(Keyboard.KEY_DIVIDE)) {
                    widthMode = !widthMode;
                    timeTerrainSize = System.currentTimeMillis();
                }


            }
            if (Keyboard.isKeyDown(Keyboard.KEY_ADD)) {
                if (widthMode && width < 50) {
                    width += 1;
                } else if (!widthMode && height < 50) {
                    height += 1;
                }
                terrain.changeSize(width, height, loader);


            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SUBTRACT)) {
                System.out.println("If "+widthMode+" and "+ (width / 2)+">"+mostPositiveX+" and "+(-width / 2)+"<"+mostNegativeX+" and "+width +">1");
                if (widthMode && (width / 2) > mostPositiveX +0.5f && (-width / 2) < mostNegativeX -0.5f && width > 1) {
                    width -= 1;
                } else if (!widthMode && (height / 2) > mostPositiveZ + 0.5f && (-height / 2) < mostNegativeZ -0.5f && height > 1) {
                    height -= 1;
                }
                terrain.changeSize(width, height, loader);

            }
        }
    }



}
