package GraphicsEngine.Entities;

import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Textures.ModelTexture;
import PhysicsEngine.TrianglePlane;
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
    public Vector4f[] squarePoints;
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

        for(int i=0;i<vertexArray.length/3;i++){
            if(vertexArray[i*3]<minX)
                minX = vertexArray[i*3];
            if(vertexArray[i*3]>maxX)
                maxX = vertexArray[i*3];
            if(vertexArray[i*3+2]<minZ)
                minZ = vertexArray[i*3+2];
            if(vertexArray[i*3+2]>maxZ)
                maxZ = vertexArray[i*3+2];
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

    public Vector3f[] getWorldPoints(){
        Matrix4f worldPosition = Maths.createTransformationMatrix(position,
                rx, ry, rz, scale);
        worldPosition.transpose();
        float[] vertexArray = model.getVertexArray();

        Vector4f[] points4 = new Vector4f[vertexArray.length/3];
        Vector3f[] points = new Vector3f[vertexArray.length/3];

        for(int i = 0; i<points.length;i++){
            points4[i] = Maths.Vector4Matrix4Product(worldPosition,new Vector4f(vertexArray[i*3],vertexArray[i*3+1],vertexArray[i*3+2],1));
            points[i] = new Vector3f(points4[i].x,points4[i].y,points4[i].z);
        }
        return points;
    }

    public float getHighestPoint(){
        Vector3f[] worldPoints = getWorldPoints();
        float highest = -1000;
        for (int i = 0; i<worldPoints.length;i++){
            if(worldPoints[i].y>highest)
                highest = worldPoints[i].y;
        }
        return highest;
    }
    public float getLowestPoint(){

        Vector3f[] worldPoints = getWorldPoints();
        float lowest = 1000;
        for (int i = 0; i<worldPoints.length;i++){
            if(worldPoints[i].y<lowest)
                lowest = worldPoints[i].y;
        }
        return lowest;

    }


    public TrianglePlane[] getTriangles(){

        Vector3f[] worldPoints = getWorldPoints();

        int[] indices = model.getIndicesArray();
        TrianglePlane[] trianglePlanes = new TrianglePlane[indices.length/3];

        for(int i=0;i<indices.length/3;i++) {
            Vector3f p1 = new Vector3f(worldPoints[indices[i*3]].x, worldPoints[indices[i*3]].y, worldPoints[indices[i*3]].z);
            Vector3f p2 = new Vector3f(worldPoints[indices[i*3+1]].x, worldPoints[indices[i*3+1]].y, worldPoints[indices[i*3+1]].z);
            Vector3f p3 = new Vector3f(worldPoints[indices[i*3+2]].x, worldPoints[indices[i*3+2]].y, worldPoints[indices[i*3+2]].z);
            trianglePlanes[i] = new TrianglePlane(p1,p2,p3);
        }
        return trianglePlanes;

    }
}
