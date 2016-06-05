package GraphicsEngine.Shaders;

/**
 * Created by giogio on 05/06/16.
 */
public class StaticShader extends ShaderProgram {
    private static final String VERTEX_FILE = "src/GraphicsEngine/Shaders/vertexShader.txt";
    private static final String FRAGMENT_FILE = "src/GraphicsEngine/Shaders/fragmentShader.txt";

    public StaticShader(){
        super(VERTEX_FILE,FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes(){
        super.bindAttributes(0,"positions");
        super.bindAttributes(1,"textureCoords");
    }
}
