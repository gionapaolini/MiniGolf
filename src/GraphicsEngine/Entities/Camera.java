package GraphicsEngine.Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 05/06/16.
 */
public class Camera {
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;


    public void move2(){

    }

    public void move(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z -=0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z +=0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x -=0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x +=0.02f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            yaw -=1f;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            yaw +=1f;
        }
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }
}
