package PhysicsEngine;

import Toolbox.Maths;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 11/06/16.
 */
public class TrianglePlane {
    public Vector3f normal;
    Vector3f origin;
    float[] equation;
    Vector3f p1,p2,p3;

    public TrianglePlane(Vector3f p1, Vector3f p2, Vector3f p3){
        Vector3f p2p1sub = Maths.vector3SUB(p2,p1);
        Vector3f p3p1sub = Maths.vector3SUB(p3,p1);
        normal = Maths.crossProduct(p2p1sub,p3p1sub);
        normal.normalise();
        if(Math.abs(normal.x)<0.00001)
            normal.x = 0;
        if(Math.abs(normal.y)<0.00001)
            normal.y = 0;
        if(Math.abs(normal.z)<0.00001)
            normal.z = 0;

        System.out.println(p1);
        System.out.println(p2);
        System.out.println(p3);
        System.out.println("Normal: "+normal);

        origin = p1;
        equation = new float[4];
        equation[0] = normal.x;
        equation[1] = normal.y;
        equation[2] = normal.z;
        equation[3] = -(normal.x*origin.x+normal.y*origin.y
                +normal.z*origin.z);

        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;

    }

    public boolean isFrontFacingTo(Vector3f direction){
        double dot = Vector3f.dot(normal,direction);
        return (dot<0);
    }

    public double signedDistanceTo(Vector3f point){
        return Maths.dot(point,normal)+equation[3];
    }

}
