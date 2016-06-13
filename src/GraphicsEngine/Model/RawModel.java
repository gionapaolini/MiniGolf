package GraphicsEngine.Model;

/**
 * Created by giogio on 04/06/16.
 */
public class RawModel {

    private int vaoID;
    private int vertexCount;
    private float[] verticesArray;
    private float[] normalsArray;
    private int[] indicesArray;

    public RawModel(int vaoID, int vertexCount){
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public float[] getVerticesArray() {
        return verticesArray;
    }

    public void setVerticesArray(float[] verticesArray) {
        this.verticesArray = verticesArray;
    }

    public float[] getNormalsArray() {
        return normalsArray;
    }

    public void setNormalsArray(float[] normalsArray) {
        this.normalsArray = normalsArray;
    }

    public int[] getIndicesArray() {
        return indicesArray;
    }

    public void setIndicesArray(int[] indicesArray) {
        this.indicesArray = indicesArray;
    }
}
