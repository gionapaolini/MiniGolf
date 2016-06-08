package PhysicsEngine;

import GolfObjects.GolfObject;
import Toolbox.Maths;
import org.lwjgl.util.vector.Vector3f;

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

    public static boolean narrowCollision(GolfObject obj1, GolfObject obj2){
        return false;
    }

    public static Vector3f pointCollision(GolfObject obj1, GolfObject obj2){
        //Discard points checking the direction
        return new Vector3f(0,0,0);
    }

    public static void applyCollision(GolfObject obj, Vector3f normals, float time){
        Vector3f vel = obj.getVelocity();
        System.out.println(vel);
        System.out.println(obj.getPosition());
        float dot = Maths.dot(vel,normals);
        Vector3f normalProc = Maths.scalarProduct(Maths.scalarProduct(normals,(1+obj.getCor())), dot);
        Vector3f newVel = Maths.vector3SUB(vel, normalProc);
        System.out.println(newVel);
        System.out.println("------------");


        obj.setVelocity(newVel);

    }

}
