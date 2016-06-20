package GolfObjects;

import GraphicsEngine.Entities.Entity;

import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 20/06/16.
 */
public class Surface extends GolfObject{

    float coefficientFriction;
    Vector3f[] points;

    public Surface(Entity entity, float coefficientFriction, Vector3f[] points){
        super(entity);
        this.coefficientFriction = coefficientFriction;
        this.points = points;
    }

    public float getCoefficientFriction() {
        return coefficientFriction;
    }
    public boolean ballIsOver(GolfObject obj){
        float as_x = obj.getPosition().x-points[0].x;
        float as_y = obj.getPosition().z-points[0].z;

        boolean s_ab = (points[1].x-points[0].x)*as_y-(points[1].z-points[0].z)*as_x > 0;

        if((points[2].x-points[0].x)*as_y-(points[2].z-points[0].z)*as_x > 0 == s_ab) return false;

        if((points[2].x-points[1].x)*(obj.getPosition().z-points[1].z)-(points[2].z-points[1].z)*(obj.getPosition().x-points[1].x) > 0 != s_ab) return false;

        return true;
    }
}
