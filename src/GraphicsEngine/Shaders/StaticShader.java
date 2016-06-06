package GraphicsEngine.Shaders;

import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Light;
import Toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by giogio on 05/06/16.
 */
public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/GraphicsEngine/Shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/GraphicsEngine/Shaders/fragmentShader.txt";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;
    private int location_lightPosition;
    private int location_lightColour;

    public StaticShader(){
        super(VERTEX_FILE,FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes(){
        super.bindAttributes(0,"positions");
        super.bindAttributes(1,"textureCoords");
        super.bindAttributes(2,"normal");
    }

    @Override
    protected void getAllUniformLocations(){
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColour = super.getUniformLocation("lightColour");


    }

    public void loadLight(Light light){
        super.loadVector(location_lightPosition,light.getPosition());
        super.loadVector(location_lightColour,light.getColour());

    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix,matrix);
    }
    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(location_projectionMatrix,matrix);
    }
    public void loadViewMatrix(Camera camera){
        Matrix4f viewMatrix = Maths.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix,viewMatrix);
    }
}
