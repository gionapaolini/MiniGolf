package GraphicsEngine.Entities;

import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Textures.ModelTexture;
import Toolbox.Maths;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by giogio on 05/06/16.
 */
public class Entity {
    private TexturedModel model;
    private Vector3f position;
    private float rx,ry,rz;
    private float scale;
    private Vector4f[] squarePoints;
    private Vector3f[] worldProjectionPoints;

    public Entity(TexturedModel model, Vector3f position, float rx, float ry, float rz, float scale) {
        this.model = model;
        this.position = position;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
        this.scale = scale;
        setBorderProjection();
        setProjectionPoint();
    }
    public void setBorderProjection(){
        float[] vertexArray = model.getVertexArray();
        float minX = 100;
        float maxX = -100;
        float minZ = 100;
        float maxZ = -100;

        for(int i = 0; i<vertexArray.length/3; i+=3){
            if(vertexArray[i]<minX)
                minX = vertexArray[i];
            if(vertexArray[i]>maxX)
                maxX = vertexArray[i];
            if(vertexArray[i+2]<minZ)
                minZ = vertexArray[i+2];
            if(vertexArray[i+2]>maxZ)
                maxZ = vertexArray[i+2];
        }

        squarePoints = new Vector4f[4];
        squarePoints[0] = new Vector4f(minX,0,minZ,1);
        squarePoints[1] = new Vector4f(maxX,0,minZ,1);
        squarePoints[2] = new Vector4f(minX,0,maxZ,1);
        squarePoints[3] = new Vector4f(maxX,0,maxZ,1);

    }
    public void setProjectionPoint() {
        Matrix4f worldPosition = Maths.createTransformationMatrix(position,
                rx, ry, rz, scale);

        worldPosition.transpose();
        Vector4f[] worldProjection = new Vector4f[4];
        worldProjection[0] = Maths.Vector4Matrix4Product(worldPosition,squarePoints[0]);
        worldProjection[1] = Maths.Vector4Matrix4Product(worldPosition,squarePoints[1]);
        worldProjection[2] = Maths.Vector4Matrix4Product(worldPosition,squarePoints[2]);
        worldProjection[3] = Maths.Vector4Matrix4Product(worldPosition,squarePoints[3]);

        Vector3f[] worldProjectionPoints = new Vector3f[4];
        worldProjectionPoints[0] = new Vector3f(worldProjection[0].x,worldProjection[0].y,worldProjection[0].z);
        worldProjectionPoints[1] = new Vector3f(worldProjection[1].x,worldProjection[1].y,worldProjection[1].z);
        worldProjectionPoints[2] = new Vector3f(worldProjection[2].x,worldProjection[2].y,worldProjection[2].z);
        worldProjectionPoints[3] = new Vector3f(worldProjection[3].x,worldProjection[3].y,worldProjection[3].z);



        this.worldProjectionPoints = worldProjectionPoints;

    }

    public Vector3f[] getWorldProjectionPoints(){
        return worldProjectionPoints;
    }
    public void setTexture(ModelTexture texture){
        model.setTexture(texture);
    }

    public TexturedModel getModel() {
        return model;
    }

    public void setModel(TexturedModel model) {
        this.model = model;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
        setProjectionPoint();
    }

    public float getRx() {
        return rx;
    }

    public void setRx(float rx) {
        this.rx = rx;
        setProjectionPoint();
    }

    public float getRy() {
        return ry;
    }

    public void setRy(float ry) {
        this.ry = ry;
        setProjectionPoint();
    }

    public float getRz() {
        return rz;
    }

    public void setRz(float rz) {
        this.rz = rz;
        setProjectionPoint();
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
        setProjectionPoint();
    }

    public ModelTexture getTexture(){
        return model.getTexture();
    }
}
