package GraphicsEngine.Shaders;

import org.lwjgl.util.vector.Matrix4f;

/**
 * Created by giogio on 05/06/16.
 */
public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/GraphicsEngine/Shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/GraphicsEngine/Shaders/fragmentShader.txt";

    private int location_transformationMatrix;

    public StaticShader(){
        super(VERTEX_FILE,FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes(){
        super.bindAttributes(0,"positions");
        super.bindAttributes(1,"textureCoords");
    }

    @Override
    protected void getAllUniformLocations(){
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix,matrix);
    }
}
