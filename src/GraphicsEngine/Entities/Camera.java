package GraphicsEngine.Entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 05/06/16.
 */
public class Camera {
    private Vector3f position = new Vector3f(2.5f,5,8);
    private float lx =0;
    private float ly =0;
    private float lz =-1;
    private float pitch=40;
    private float yaw;
    private float roll;



    public void moveOnSight(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.x +=0.02f * lx;
            position.y +=0.02f * ly;
            position.z +=0.02f * lz;

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.x -=0.02f * lx;
            position.y -=0.02f * ly;
            position.z -=0.02f * lz;
        }
        if(Mouse.isButtonDown(0)){
            yaw-=Mouse.getDX()/10;
            pitch+=Mouse.getDY()/10;
            lx =(float)Math.sin(Math.toRadians(yaw));
            ly = (float)-Math.sin(Math.toRadians(pitch));
            lz =(float)-Math.cos(Math.toRadians(yaw));
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x+=0.02f*(-lz);
            position.z+=0.02f*(lx);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x-=0.02f*(-lz);
            position.z-=0.02f*(lx);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            position.y +=0.02;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            position.y -=0.02;
        }

    }

    public void movePositions(){
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
            position.y +=0.02;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            position.y -=0.02;
        }
        if(Mouse.isButtonDown(0)){
            yaw-=Mouse.getDX()/10;
            pitch+=Mouse.getDY()/10;
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
