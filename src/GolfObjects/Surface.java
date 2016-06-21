package GolfObjects;

import GraphicsEngine.Entities.Entity;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 20/06/16.
 */
public class Surface extends GolfObject{

    float coefficientFriction;
    public Vector3f[] points;
    String name;

    public Surface(Entity entity, float coefficientFriction, Vector3f[] points, String name){
        super(entity);
        this.coefficientFriction = coefficientFriction;
        this.points = new Vector3f[3];
        this.name = name;
        for(int i = 0;i<3;i++) {
            this.points[i] = new Vector3f(points[i].x, points[i].y, points[i].z);
        }
    }



    public float getCoefficientFriction() {
        return coefficientFriction;
    }
    public boolean ballIsOver(GolfObject obj){
        Vector3f[] pont = new Vector3f[3];

        for (int i=0;i<3;i++){
            pont[i] = new Vector3f(points[i].x+getModel().getPosition().x,0,points[i].z+getModel().getPosition().z);
        }
        float as_x = obj.getPosition().x-pont[0].x;
        float as_y = obj.getPosition().z-pont[0].z;


        boolean s_ab = (pont[1].x-pont[0].x)*as_y-(pont[1].z-pont[0].z)*as_x > 0;

        if((pont[2].x-pont[0].x)*as_y-(pont[2].z-pont[0].z)*as_x > 0 == s_ab) return false;

        if((pont[2].x-pont[1].x)*(obj.getPosition().z-pont[1].z)-(pont[2].z-pont[1].z)*(obj.getPosition().x-pont[1].x) > 0 != s_ab) return false;

        return true;
    }
    public boolean mouseOver(Vector3f point){
        Vector3f[] pont = new Vector3f[3];

        for (int i=0;i<3;i++){
            pont[i] = new Vector3f(points[i].x+getModel().getPosition().x,0,points[i].z+getModel().getPosition().z);
        }
        float as_x = point.x-pont[0].x;
        float as_y = point.z-pont[0].z;


        boolean s_ab = (pont[1].x-pont[0].x)*as_y-(pont[1].z-pont[0].z)*as_x > 0;

        if((pont[2].x-pont[0].x)*as_y-(pont[2].z-pont[0].z)*as_x > 0 == s_ab) return false;

        if((pont[2].x-pont[1].x)*(point.z-pont[1].z)-(pont[2].z-pont[1].z)*(point.x-pont[1].x) > 0 != s_ab) return false;

        return true;
    }
    public String name (){return name;}
}
