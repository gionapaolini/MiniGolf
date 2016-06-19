package PhysicsEngine;

import GolfObjects.Ball;
import GolfObjects.GolfObject;
import GraphicsEngine.Entities.Terrain;
import Toolbox.Maths;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import java.util.Random;

/**
 * Created by giogio on 04/06/16.
 */
public class Physics {

    public static void applyGravity(GolfObject obj, float time, boolean onPutHole){
        Vector3f pos = obj.getPosition();
        if(pos.y > 0 || onPutHole) {
            Vector3f vel = obj.getVelocity();
            obj.setVelocity(new Vector3f(vel.x, (float) (vel.y - (9.8 * time)), vel.z));
            setNewPosition(obj,time);
        }

    }

    public static void setNewPosition(GolfObject obj, float time){
        Vector3f position = obj.getPosition();
        Vector3f vel = obj.getVelocity();
        obj.setPosition(new Vector3f((position.x+(vel.x*time)),position.y+(vel.y*time),(position.z+(vel.z*time))));
    }

    public static void setNewPosition(GolfObject obj, float time, Terrain terrain){
        Vector3f position = obj.getPosition();
        Vector3f vel = obj.getVelocity();
        Vector3f newPos = new Vector3f((position.x+(vel.x*time)),Math.max(position.y+(vel.y*time),0),(position.z+(vel.z*time)));

        float l1 = terrain.getX();
        float l2 = terrain.getZ();
        float l3 = terrain.getX()+terrain.width;
        float l4 = terrain.getZ()+terrain.height;


        if(!(newPos.x-0.35578898<=l1 || newPos.x+0.35578898>=l3 || newPos.z-0.35578898<=l2 || newPos.z+0.35578898>=l4)){
            obj.setPosition(newPos);
            System.out.println(newPos);
        }


    }

    public static void applyCollision(GolfObject obj, Vector3f normals, float time){
        Vector3f vel = obj.getVelocity();
        float dot = Maths.dot(vel,normals);
        Vector3f normalProc = Maths.scalarProduct(Maths.scalarProduct(normals,(1+obj.getCor())), dot);
        Vector3f newVel = Maths.vector3SUB(vel, normalProc);
        obj.setVelocity(newVel);

    }

    public static void applyFriction(GolfObject obj, float time){

        if(!obj.isFlying()) {
            float u = 1.5f * time;
            Vector3f vel = obj.getVelocity();
            Vector3f newVel = new Vector3f(vel.x - (vel.x * u), vel.y - (vel.y * u), vel.z - (vel.z * u));
            if (Math.abs(newVel.x) < 0.2 && Math.abs(newVel.z) < 0.2 && obj.getPosition().y <= 0 && Math.abs(newVel.y) < 0.2) {
                newVel.x = 0;
                newVel.y = 0;
                newVel.z = 0;
            }

            obj.setVelocity(newVel);
        }

    }


    public static void applyCollisionBall(GolfObject obj, GolfObject obj2, Vector3f normals, float time){
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
        float max1 = en1.getModel().getHighestPoint();
        float max2 = en2.getModel().getHighestPoint();
        float min1 = en1.getModel().getLowestPoint();
        float min2 = en2.getModel().getLowestPoint();

        if(!(max2<max1 && max2>min1 || max1<max2 && max1>min2)){

            return false;
        }



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

    public static void terrainCollision(GolfObject obj, Terrain terrain, float time){
        if(obj.isMoving()) {
            float l1 = terrain.getX();
            float l2 = terrain.getZ();
            float l3 = terrain.getX() + terrain.width;
            float l4 = terrain.getZ() + terrain.height;


            Vector3f vel = obj.getVelocity();
            Vector3f normalVel = new Vector3f(vel.x, vel.y, vel.z);
            normalVel.normalise();
            if (obj.getPosition().x - 0.35578898 <= l1 && Vector3f.dot(normalVel, new Vector3f(-1, 0, 0)) > 0) {
                applyCollision(obj, new Vector3f(-1, 0, 0), time);
                setNewPosition(obj, time, terrain);
            }
            if (obj.getPosition().x + 0.35578898 >= l3 && Vector3f.dot(normalVel, new Vector3f(1, 0, 0)) > 0) {
                applyCollision(obj, new Vector3f(1, 0, 0), time);
                setNewPosition(obj, time, terrain);
            }
            if (obj.getPosition().z - 0.35578898 <= l2 && Vector3f.dot(normalVel, new Vector3f(0, 0, -1)) > 0) {
                applyCollision(obj, new Vector3f(0, 0, -1), time);
                setNewPosition(obj, time, terrain);
            }
            if (obj.getPosition().z + 0.35578898 >= l4 && Vector3f.dot(normalVel, new Vector3f(0, 0, 1)) > 0) {
                applyCollision(obj, new Vector3f(0, 0, 1), time);
                setNewPosition(obj, time, terrain);
            }

            if (obj.getPosition().y <= 0) {
                applyCollision(obj, new Vector3f(0, 1, 0), time);
            }
        }



    }

    public static void collision(GolfObject obj1, GolfObject obj2, float time){
        obj1.setColliding(false);
        boolean collision = checkBroadCollision(obj1,obj2);
        if(collision){
            Vector3f vel = obj1.getVelocity();
            Vector3f normalVel = new Vector3f(vel.x,vel.y,vel.z);
            normalVel.normalise();
            TrianglePlane[] trianglePlanes = obj2.getModel().getTriangles();

            float closer = 1000;
            Vector3f normal = null;
            for(int i=0;i<trianglePlanes.length;i++){
                if(trianglePlanes[i].normal.y>=0 && trianglePlanes[i].isFrontFacingTo(normalVel)){
                    float d = checkDistanceTriangle(obj1,trianglePlanes[i]);
                    d = Math.abs(d);
                    if(d<closer && d>=0 && d<=0.35578898){
                        closer = d;
                        normal = trianglePlanes[i].normal;
                    }
                }
            }


            if(normal!=null) {
                obj1.setColliding(true);
                applyCollision(obj1, normal, time);
            }
        }


    }



    public static float checkDistanceTriangle(GolfObject obj,TrianglePlane triangle) {

        Vector3f pos = obj.getPosition();
        Vector3f newV = new Vector3f(pos.x,(float) (pos.y+0.35578898),pos.z);

        return Vector3f.dot(Maths.vector3SUB(newV,triangle.p1),triangle.normal);

    }



}
