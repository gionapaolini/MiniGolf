package GolfObjects;

import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Textures.ModelTexture;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by giogio on 04/06/16.
 */
public abstract class GolfObject {

    protected Vector3f velocity;
    protected ModelTexture realTexture;
    protected Entity model;
    protected float mass;
    protected float cor;
    protected boolean isColliding;
    public GolfObject(Entity model) {
        this.model = model;
        velocity = new Vector3f(0,0,0);
        mass = 1;
        cor = 0.68f;
        setRealTexture(model.getTexture());
    }

    public Vector3f[] getWorldProjectionPoint(){
        return model.getWorldProjectionPoints();
    }
    public Vector3f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector3f velocity) {
        this.velocity = velocity;
    }

    public void setPosition(Vector3f position){
        model.setPosition(position);
    }

    public Vector3f getPosition(){
        return model.getPosition();
    }

    public void setCollideColor(ModelTexture texture){
        model.setTexture(texture);
    }
    public void unsetCollideColor(){
        model.setTexture(realTexture);
    }

    public void setRealTexture(ModelTexture realTexture){
        this.realTexture = realTexture;
    }

    public Entity getModel() {
        return model;
    }

    public void setModel(Entity model) {
        this.model = model;
    }

    public float getMass() {
        return mass;
    }

    public void setMass(float mass) {
        this.mass = mass;
    }

    public float getCor(){
        return cor;
    }

    public Vector4f[] getProjectionPoints(){
        return model.getProjectionPoints();
    }

    public boolean isMoving(){
        if(velocity.x!=0 || velocity.z!=0){
            return true;
        }
        return false;
    }

    public void setColliding(boolean value){
        isColliding = value;
    }
    public boolean isColliding(){
        return isColliding;
    }
    public boolean isFlying(){
        if(!isColliding && getPosition().y>0){
            return true;
        }
        return false;
    }


}
