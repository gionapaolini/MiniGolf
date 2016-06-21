package Toolbox;

import GameEngine.Settings;
import GolfObjects.Ball;
import GolfObjects.Obstacle;
import GolfObjects.PutHole;
import GolfObjects.Surface;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.RenderEngine.OBJLoader;
import GraphicsEngine.Textures.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

import java.io.*;
import java.util.List;

/**
 * Created by HenriV on 21.06.2016.
 */
public class SaveAndLoad {

    //private File file = new File("/res/save.txt");
    private static RawModel ballModel, cubeModel, barModel, slopeModel;
    private static ModelTexture mud, sand;

    public static void save(Terrain terrain, List<Obstacle> obstacles, List<Ball> balls, List<Surface> surfaces, PutHole putHole, String name) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter("res/"+name+".txt");
        writer.println("putHole" + " " + putHole.getPosition().getX() + " " + putHole.getPosition().getY() + " " + putHole.getPosition().getZ());
        writer.println("Terrain" + " " + terrain.getX() + " " + terrain.getZ() + " " + terrain.getWidth() + " " + terrain.getHeight());
        for(Ball ball:balls){
            writer.println("Ball" + " " + ball.getModel().getRx() + " " + ball.getModel().getRy() + " " + ball.getModel().getRz() + " " + ball.getModel().getScale() + " "  + ball.getPosition().getX() + " " + ball.getPosition().getY() + " " + ball.getPosition().getZ());
        }
        for(Obstacle obs: obstacles) {

            String n = obs.getName() + " " + obs.getModel().getRx() + " " + obs.getModel().getRy() + " " + obs.getModel().getRz() + " " + obs.getModel().getScale() + " " + obs.getPosition().getX() + " " + obs.getPosition().getY() + " " + obs.getPosition().getZ();
            System.out.println(n);
            writer.println(obs.getName() + " " + obs.getModel().getRx() + " " + obs.getModel().getRy() + " " + obs.getModel().getRz() + " " + obs.getModel().getScale() + " " + obs.getPosition().getX() + " " + obs.getPosition().getY() + " " + obs.getPosition().getZ());
        }
        for(Surface surf: surfaces)
        {
            writer.print(surf.name() + " " + surf.getModel().getRx() + " " + surf.getModel().getRy() + " " + surf.getModel().getRz() + " " + surf.getModel().getScale() + " " + surf.getCoefficientFriction() + " " + surf.getPosition().getX() + " " + surf.getPosition().getY() + " " + surf.getPosition().getZ());
            for(int i = 0; i<9; i++) {
                writer.print(" " + surf.getModel().getModel().getVertexArray()[i]);
            }
            writer.println();
        }

        writer.close();
    }


    public static void load(Terrain terrain, List<Obstacle> obstacles, List<Ball> balls, List<Surface> surfaces, PutHole putHole, Loader loader, String name, Settings settings)
    {
        int countBall = 0;
        FileReader fr = null;
        try {
            fr = new FileReader(new File("res/"+name+".txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line;
        BufferedReader reader = new BufferedReader(fr);
        try {
            while (true) {

                line = reader.readLine();
                if(line==null)
                    return;
                String[] currentLine = line.split(" ");
                if (line.startsWith("Ball")&&countBall<settings.getnHuman()+settings.getnBot()) {
                    if (ballModel == null) {
                        ballModel = OBJLoader.loadObjModel("ball", loader);
                    }
                    ModelTexture white = new ModelTexture(loader.loadTexture("white"));
                    TexturedModel model = new TexturedModel(ballModel, white);
                    //System.out.println(currentLine[6]+" "+ currentLine[7]+" "+currentLine[8]+" "+currentLine[1]+" "+ currentLine[2]+" "+ currentLine[3]+" "+ currentLine[4]);
                    balls.add(new Ball(new Entity(model,new Vector3f(Float.parseFloat(currentLine[5]),
                            Float.parseFloat(currentLine[6]), Float.parseFloat(currentLine[7])),
                            Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]),
                            Float.parseFloat(currentLine[3]), Float.parseFloat(currentLine[4]) )));
                    countBall++;

                } else if (line.startsWith("putHole")) {
                    putHole.setPosition(new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3])));

                } else if (line.startsWith("Terrain")) {
                    terrain.changeSize(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]), Float.parseFloat(currentLine[4]), loader);


                } else if (line.startsWith("cube")) {
                    if(cubeModel==null) {
                        cubeModel = OBJLoader.loadObjModel("cube", loader);
                    }
                    ModelTexture box = new ModelTexture(loader.loadTexture("box"));
                    TexturedModel model = new TexturedModel(cubeModel,box);
                    obstacles.add(new Obstacle(new Entity(model,new Vector3f(Float.parseFloat(currentLine[5]), Float.parseFloat(currentLine[6]), Float.parseFloat(currentLine[7])), Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]), Float.parseFloat(currentLine[4])), "cube"));

                } else if (line.startsWith("slope")) {
                    if(slopeModel==null) {
                        slopeModel = OBJLoader.loadObjModel("slope", loader);
                    }
                    ModelTexture wood = new ModelTexture(loader.loadTexture("wood"));
                    TexturedModel model = new TexturedModel(slopeModel,wood);
                    obstacles.add(new Obstacle(new Entity(model,new Vector3f(Float.parseFloat(currentLine[5]), Float.parseFloat(currentLine[6]), Float.parseFloat(currentLine[7])), Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]), Float.parseFloat(currentLine[4])), "slope"));


                } else if (line.startsWith("bar")) {
                    if(barModel==null) {
                        barModel = OBJLoader.loadObjModel("bar", loader);
                    }
                    ModelTexture bar = new ModelTexture(loader.loadTexture("bar"));
                    TexturedModel model = new TexturedModel(barModel,bar);
                    obstacles.add(new Obstacle(new Entity(model,new Vector3f(Float.parseFloat(currentLine[5]), Float.parseFloat(currentLine[6]), Float.parseFloat(currentLine[7])), Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]), Float.parseFloat(currentLine[4])), "bar"));



                } else if (line.startsWith("sand") || line.startsWith("mud")){
                    float[] vertexArray = new float[9];
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
                    for(int i = 0; i<9; i++)
                    {
                        vertexArray[i] = Float.parseFloat(currentLine[9+i]);
                    }

                    RawModel mudel = loader.loadToVAO(vertexArray, textureArray, normalArray, indices);
                    Vector3f[] points= new Vector3f[3];
                    for(int i=0;i<3;i++){
                        points[i] = new Vector3f(vertexArray[i*3],vertexArray[i*3+1],vertexArray[i*3+2]);
                    }
                    String actualName = null;
                    TexturedModel model = null;
                    if(line.startsWith("mud")){
                        actualName = "mud";
                        if(mud==null){
                            mud = new ModelTexture(loader.loadTexture(actualName));
                        }
                        model = new TexturedModel(mudel,mud);
                    }
                    else if(line.startsWith("sand")){
                        actualName = "sand";
                        if(sand==null){
                            sand = new ModelTexture(loader.loadTexture(actualName));
                        }
                        model = new TexturedModel(mudel,sand);
                    }

                    mudel.setVerticesArray(vertexArray);
                    mudel.setNormalsArray(normalArray);
                    mudel.setIndicesArray(indices);

                    surfaces.add(new Surface(new Entity(model, new Vector3f(Float.parseFloat(currentLine[6]), Float.parseFloat(currentLine[7]), Float.parseFloat(currentLine[8]))
                            ,Float.parseFloat(currentLine[1]),Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]), Float.parseFloat(currentLine[4])),Float.parseFloat(currentLine[5]), points, actualName));


                }else{
                    if(line==null)
                      break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
