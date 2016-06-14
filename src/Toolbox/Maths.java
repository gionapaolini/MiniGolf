package Toolbox;

import GraphicsEngine.Entities.Camera;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by giogio on 05/06/16.
 */
public class Maths {
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation,matrix,matrix);
        Matrix4f.rotate((float)(Math.toRadians(rx)), new Vector3f(1,0,0), matrix,matrix);
        Matrix4f.rotate((float)(Math.toRadians(ry)), new Vector3f(0,1,0), matrix,matrix);
        Matrix4f.rotate((float)(Math.toRadians(rz)), new Vector3f(0,0,1), matrix,matrix);
        Matrix4f.scale(new Vector3f(scale,scale,scale),matrix,matrix);

        return matrix;

    }

    public static Matrix4f createViewMatrix(Camera camera) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Vector3f cameraPos = camera.getPosition();
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }

    public static Vector3f vector3SUB(Vector3f first, Vector3f second){
        return new Vector3f(first.x-second.x,first.y-second.y,first.z-second.z);
    }
    public static Vector3f vector3SUM(Vector3f first, Vector3f second){
        return new Vector3f(first.x+second.x,first.y+second.y,first.z+second.z);
    }

    public static Vector3f scalarProduct(Vector3f vector, float n){
        return new Vector3f(vector.x*n,vector.y*n,vector.z*n);
    }

    public static float dot(Vector3f first, Vector3f second){
        return first.x*second.x + first.y*second.y + first.z*second.z;
    }

    public static Vector4f Vector4Matrix4Product(Matrix4f mat,Vector4f vec){

        float first = mat.m00*vec.x + mat.m01*vec.y + mat.m02*vec.z +mat.m03*vec.w;
        float second = mat.m10*vec.x + mat.m11*vec.y + mat.m12*vec.z +mat.m13*vec.w;
        float third = mat.m20*vec.x + mat.m21*vec.y + mat.m22*vec.z +mat.m23*vec.w;
        float fourth = mat.m30*vec.x + mat.m31*vec.y + mat.m32*vec.z +mat.m33*vec.w;
        return new Vector4f(first,second,third,fourth);

    }

    public static Vector3f crossProduct(Vector3f first, Vector3f second){
        float cx = first.y*second.z - first.z*second.y;

        float cy = first.z*second.x - first.x*second.z;

        float cz = first.x*second.y - first.y*second.x;

        return new Vector3f(cx,cy,cz);

    }

    public static float distancePoints(Vector3f p1,Vector3f p2){
        return (float) Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2) + Math.pow(p1.z - p2.z, 2));
    }
}
