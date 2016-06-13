package PhysicsEngine;

import GolfObjects.GolfObject;
import Toolbox.Maths;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

/**
 * Created by giogio on 04/06/16.
 */
public class Physics {

    public static void applyGravity(GolfObject obj, float time){
        Vector3f vel = obj.getVelocity();
        obj.setVelocity(new Vector3f(vel.x,(float)(vel.y-(9.8*time)),vel.z));
    }

    public static void setNewPosition(GolfObject obj, float time){
        Vector3f position = obj.getPosition();
        Vector3f vel = obj.getVelocity();
        obj.setPosition(new Vector3f((float)(position.x+(vel.x*time)),(float)Math.max(position.y+(vel.y*time),0),(float)(position.z+(vel.z*time))));
    }

    public static Vector3f pointCollision(GolfObject obj1, GolfObject obj2){
        //Discard points checking the direction
        return new Vector3f(0,0,0);
    }

    public static void applyCollision(GolfObject obj, Vector3f normals, float time){
        Vector3f vel = obj.getVelocity();
        float dot = Maths.dot(vel,normals);
        Vector3f normalProc = Maths.scalarProduct(Maths.scalarProduct(normals,(1+obj.getCor())), dot);
        Vector3f newVel = Maths.vector3SUB(vel, normalProc);
        obj.setVelocity(newVel);
    }

    public static void applyCollision(GolfObject obj, GolfObject obj2, Vector3f normals, float time){
        Vector3f vel1 = obj.getVelocity();
        Vector3f vel2 = obj2.getVelocity();

        Vector3f Vr = Maths.vector3SUB(vel1, vel2);
        float dot = Maths.dot(Vr,normals);
        Vector3f I = Maths.scalarProduct(normals,(1+obj.getCor()*dot/(1/obj.getMass() + 1/obj2.getMass())));
        obj.setVelocity(Maths.vector3SUB(vel1,Maths.scalarProduct(I,1/obj.getMass())));
        obj2.setVelocity(Maths.vector3SUM(vel2,Maths.scalarProduct(I,1/obj.getMass())));
    }

    public static boolean checkBroadCollision(GolfObject en1, GolfObject en2){
        Vector3f[] obsBound1 = en1.getWorldProjectionPoint();
        Vector3f[] obsBound2 = en2.getWorldProjectionPoint();

        Vector2f[] axis = new Vector2f[4];
        axis[0] = new Vector2f(obsBound1[1].x - obsBound1[0].x,obsBound1[1].z - obsBound1[0].z);
        axis[1] = new Vector2f(obsBound1[1].x - obsBound1[3].x,obsBound1[1].z - obsBound1[3].z);
        axis[2] = new Vector2f(obsBound2[0].x - obsBound2[2].x,obsBound2[0].z - obsBound2[2].z);
        axis[3] = new Vector2f(obsBound2[0].x - obsBound2[1].x,obsBound2[0].z - obsBound2[1].z);
        for(int j = 0;j<axis.length;j++) {
            Vector2f[] projectionSQ1 = new Vector2f[4];
            Vector2f[] projectionSQ2 = new Vector2f[4];
            for (int i = 0; i < obsBound2.length; i++) {
                projectionSQ1[i] = new Vector2f();
                projectionSQ1[i].x = (float) (((obsBound1[i].x * axis[j].x) + (obsBound1[i].z * axis[j].y)) / (Math.pow(axis[j].x, 2) + Math.pow(axis[j].y, 2)) * axis[j].x);
                projectionSQ1[i].y = (float) (((obsBound1[i].x * axis[j].x) + (obsBound1[i].z * axis[j].y)) / (Math.pow(axis[j].x, 2) + Math.pow(axis[j].y, 2)) * axis[j].y);

                projectionSQ2[i] = new Vector2f();
                projectionSQ2[i].x = (float) (((obsBound2[i].x * axis[j].x) + (obsBound2[i].z * axis[j].y)) / (Math.pow(axis[j].x, 2) + Math.pow(axis[j].y, 2)) * axis[j].x);
                projectionSQ2[i].y = (float) (((obsBound2[i].x * axis[j].x) + (obsBound2[i].z * axis[j].y)) / (Math.pow(axis[j].x, 2) + Math.pow(axis[j].y, 2)) * axis[j].y);

            }



            float minA = 1000000;
            float minB = 1000000;
            float maxA = -100000;
            float maxB = -1000000;
            for (int i = 0; i < 4; i++) {
                float dotProd = projectionSQ1[i].x * axis[j].x + projectionSQ1[i].y * axis[j].y;

                if (dotProd > maxA)
                    maxA = dotProd;
                if (dotProd < minA)
                    minA = dotProd;
                dotProd = projectionSQ2[i].x * axis[j].x + projectionSQ2[i].y * axis[j].y;
                if (dotProd > maxB)
                    maxB = dotProd;
                if (dotProd < minB)
                    minB = dotProd;
            }



            if (!(minB <= maxA && maxB >= minA)) {

                return false;
            }
        }
        return true;

    }


    public static boolean checkTriangle(GolfObject obj,TrianglePlane triangle) {
        Vector3f normVel = new Vector3f();
        obj.getVelocity().normalise(normVel);

        if (triangle.isFrontFacingTo(normVel)){


            // Get interval of plane intersection:
            double t0, t1;
            boolean embeddedInPlane = false;
            double signedDistToTrianglePlane =triangle.signedDistanceTo(obj.getPosition());
            float normalDotVelocity =Maths.dot(triangle.normal,obj.getVelocity());
            if (normalDotVelocity == 0.0f) {
                if (Math.abs(signedDistToTrianglePlane) >= 1.0f) {
                    return false;
                }
                else {
                    embeddedInPlane = true;
                    t0 = 0.0;
                    t1 = 1.0;
                }

            }else {
                t0=(-1.0-signedDistToTrianglePlane)/normalDotVelocity;
                t1=( 1.0-signedDistToTrianglePlane)/normalDotVelocity;
                if (t0 > t1) {
                    double temp = t1;
                    t1 = t0;
                    t0 = temp;
                }
                if (t0 > 1.0f || t1 < 0.0f) {
                    return false;
                }
                if (t0 < 0.0) t0 = 0.0;
                if (t1 > 1.0) t1 = 1.0;
            }

            Vector3f collisionPoint;
            boolean foundCollison = false;
            float t = 1.0f;

            if (!embeddedInPlane) {

                Vector3f t0Vel = new Vector3f(obj.getVelocity().x,obj.getVelocity().y,obj.getVelocity().z);
                t0Vel.scale((float) t0);

                Vector3f planeIntersectionPoint =Maths.vector3SUM(Maths.vector3SUB(obj.getPosition(),triangle.normal),  t0Vel);
                if (checkPointInTriangle(planeIntersectionPoint,triangle.p1,triangle.p2,triangle.p3))
                {
                    System.out.println("FOUND COLLISION");

                    foundCollison = true;
                    t = (float) t0;
                    collisionPoint = planeIntersectionPoint;
                    return true;
                }

            }
        }
        return false;

    }


    public static boolean checkPointInTriangle(Vector3f point, Vector3f pa, Vector3f pb, Vector3f pc){

        Vector3f e10=Maths.vector3SUB(pb,pa);
        Vector3f e20=Maths.vector3SUB(pc,pa);
        float a = Vector3f.dot(e10,e10);
        float b = Vector3f.dot(e10,e20);
        float c = Vector3f.dot(e20,e20);
        float ac_bb=(a*c)-(b*b);
        Vector3f vp = new Vector3f(point.x-pa.x, point.y-pa.y, point.z-pa.z);
        float d = Vector3f.dot(vp,e10);
        float e = Vector3f.dot(vp,e20);
        float x = (d*c)-(e*b);
        float y = (e*a)-(d*b);
        float z = x+y-ac_bb;
        return z < 0 && x >= 0 && y >= 0;

    }
}
