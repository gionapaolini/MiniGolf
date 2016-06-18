package GraphicsEngine.Entities;

import GameEngine.Player;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 05/06/16.
 */
public class Camera {
    private Vector3f position = new Vector3f(0,10,12);
    private float lx =0;
    private float ly =0;
    private float lz =-1;
    private float pitch=40;
    private float yaw;
    private float increment = 0.4f;

    private float distanceFromPlayer;
    private float angleAroundPlayer;
    private Player currentPlayer;
    public boolean playing;

    public Camera(Player player){
        currentPlayer = player;
        distanceFromPlayer = 10;
        playing = true;
    }
    public Camera(){
        playing = false;
    }
    private void calculateZoom(){
        float zoomLevel = Mouse.getDWheel() * 0.01f;
        if(distanceFromPlayer-zoomLevel>3f)
             distanceFromPlayer -= zoomLevel;

    }
    private void calculateAngles(){
        if(Mouse.isButtonDown(1)){
            float pitchChange = Mouse.getDY()*0.1f;
            pitch -= pitchChange;
            float angleChange = Mouse.getDX()*0.3f;
            angleAroundPlayer-=angleChange;


        }
    }

    private float calculateHorizontalDistance(){
        return (float) (distanceFromPlayer*Math.cos(Math.toRadians(pitch)));
    }
    private float calculateVerticalDistance(){
        return (float) (distanceFromPlayer*Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizontal,float vertical){
        position.y = currentPlayer.getBall().getPosition().y +vertical;
        float theta = currentPlayer.getBall().getModel().getRy() + angleAroundPlayer;
        float offsetX = (float) (horizontal * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontal * Math.cos(Math.toRadians(theta)));

        position.x = currentPlayer.getBall().getPosition().x - offsetX;
        position.z = currentPlayer.getBall().getPosition().z - offsetZ;
    }

    public void move(){
        if(playing)
            moveMethod();
        else
            moveOnSight();
    }

    public void moveMethod(){
        calculateZoom();
        calculateAngles();
        float horizontal = calculateHorizontalDistance();
        float vertical =  calculateVerticalDistance();
        calculateCameraPosition(horizontal,vertical);
        this.yaw = 180 - (currentPlayer.getBall().getModel().getRy()+angleAroundPlayer);
    }

    public void setCurrentPlayer(Player player){
        this.currentPlayer = player;
    }

    public void moveOnSight(){

        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.x +=increment * lx;
            position.y +=increment* ly;
            position.z +=increment * lz;

        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.x -=increment * lx;
            position.y -=increment * ly;
            position.z -=increment * lz;
        }
        if(Mouse.isButtonDown(1)){
            yaw-=Mouse.getDX()/10;
            pitch+=Mouse.getDY()/10;
            lx =(float)Math.sin(Math.toRadians(yaw));
            ly = (float)-Math.sin(Math.toRadians(pitch));
            lz =(float)-Math.cos(Math.toRadians(yaw));
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x+=increment*(-lz);
            position.z+=increment*(lx);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x-=increment*(-lz);
            position.z-=increment*(lx);
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            position.y +=increment;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            position.y -=increment;
        }

    }

    public void movePositions(){
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            position.z -=increment;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            position.z +=increment;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            position.x -=increment;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            position.x +=increment;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            position.y +=increment;
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            position.y -=increment;
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

}
