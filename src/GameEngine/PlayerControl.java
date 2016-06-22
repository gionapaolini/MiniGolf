package GameEngine;
import GolfObjects.*;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Terrain;
import PhysicsEngine.Physics;
import Toolbox.Maths;
import Toolbox.MousePicker;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by giogio on 17/06/16.
 */
public class PlayerControl {
    List<Player> players;
    Player currentPlayer;
    Camera camera;
    Entity arrow, arrow3D;
    public int nPlayer, timeLeft, maxTimeTurn;
    public boolean disabledShot, pause, wait;
    long time;
    long timeLeftShot = 0;
    MousePicker picker;
    long lastShot;
    Vector3f pointToReach,wind;
    Simulator simulator;


    public PlayerControl(List<Player> players, Camera camera, Entity arrow, Entity arrow3D, int maxTimeTurn, MousePicker picker, Simulator simulator) {
        this.players = players;
        this.camera = camera;
        nPlayer = 0;
        this.arrow = arrow;
        arrow.position.y=0.1f;
        disabledShot = true;
        this.maxTimeTurn = maxTimeTurn;
        this.picker = picker;
        this.arrow3D = arrow3D;
        wind = new Vector3f(0,0,0);
        pointToReach = new Vector3f(0,0,0);
        this.simulator=simulator;

    }

    private void selectPlayer(){
        camera.setCurrentPlayer(currentPlayer);
        Vector3f pos = currentPlayer.getBall().getPosition();
        arrow.setPosition(new Vector3f(pos.x,pos.y,pos.z));
        arrow.position.y=0.001f;


    }

    public void initialize(int timeLeft){
        currentPlayer = players.get(0);
        nPlayer = 0;
        disabledShot = false;
        selectPlayer();
        this.timeLeft = timeLeft;
        setPause(false);
    }
    public void destroy(){
        currentPlayer = null;
        disabledShot = true;
        setPause(true);
        for(Player player:players){
            player.getBall().setVelocity(new Vector3f(0,0,0));
        }
        players.clear();
    }

    private void decrementTimeLeft(){
        if(System.currentTimeMillis() - timeLeftShot >1000){
            timeLeft--;
            timeLeftShot = System.currentTimeMillis();
        }
    }

    public void shot(Vector3f point){
        if(point!= null && !disabledShot && Mouse.isButtonDown(0) && !wait) {
            Vector3f diff = Maths.vector3SUB(point, currentPlayer.getBall().getPosition());
            float distance = Maths.distancePoints(arrow.getPosition(), point);
            distance = Math.min(distance, 4);
            diff.normalise();
            diff = Maths.scalarProduct(diff, 5 * distance);
            simulator.simulate(currentPlayer.getBall().getPosition(),diff);
            currentPlayer.getBall().setVelocity(diff);

            disabledShot = true;
            lastShot = System.currentTimeMillis();
        }

    }

    public void game(List<Obstacle> obstacles, Terrain terrain, PutHole putHole,List<Surface> surfaces){
        if(!pause && System.currentTimeMillis()-this.time>1000) {
            moveArrow(picker.getCurrentTerrainPoint());
            moveArrowWind();
            decrementTimeLeft();
            shot(picker.getCurrentTerrainPoint());
            nextPlayer();
            applyPhysics(obstacles,terrain,putHole,surfaces);
        }
    }
    public void nextPlayer(){
        if(((!isMotion() || (System.currentTimeMillis()-lastShot>10000 && !players.get((nPlayer+1)%players.size()).getBall().isMoving())) && disabledShot)||(timeLeft<0)) {
            nPlayer += 1;
            nPlayer %= players.size();
            currentPlayer = players.get(nPlayer);
            selectPlayer();
            disabledShot = false;
            timeLeft = maxTimeTurn;
        }
    }

    public boolean isMotion(){
        for(Player player: players){
            if(player.getBall().isMoving()){
                return true;
            }
        }
        return false;
    }

    public void moveArrowWind(){
        double rand = Math.random()*10000;
        if(rand<50){
           pointToReach = new Vector3f((float) Math.random()*10-5,0,(float) Math.random()*10-5);
            System.out.println("Change wind!"+ rand);
        }
        if(wind.x<pointToReach.x){
            wind.x+=0.01;
        }
        if(wind.z<pointToReach.z){
            wind.z+=0.01;
        }
        if(wind.x>pointToReach.x){
            wind.x-=0.01;
        }
        if(wind.z>pointToReach.z){
            wind.z-=0.01;
        }
        float angleInDegrees = Maths.GetAngleOfLineBetweenTwoPoints(new Vector3f(0,0,0),wind);
        arrow3D.setRy(-angleInDegrees);
        float distance = Maths.distancePoints(new Vector3f(0,0,0),wind);
        arrow3D.setScaleX(distance/3);
        Vector3f pos = currentPlayer.getBall().getPosition();
        arrow3D.setPosition(new Vector3f(pos.x,pos.y+2,pos.z));

    }

    public void moveArrow(Vector3f point){

        if(point!=null) {
            float angleInDegrees = Maths.GetAngleOfLineBetweenTwoPoints(arrow.getPosition(),point);
            arrow.setRy(-angleInDegrees);
            float distance = Maths.distancePoints(arrow.getPosition(),point);
            if(distance<4)
                arrow.setScaleX(distance/1.3f);

        }
    }



    public void applyPhysics(List<Obstacle> obstacles,Terrain terrain,PutHole putHole,List<Surface> surfaces){
        for(Player player: players){
            Ball ball = player.getBall();
            if(ball.isMoving()){
                if(!Physics.checkBroadCollision(ball.getModel(),putHole.getFakeHole()) && ball.getPosition().y>-0.1){
                    Physics.applyGravity(ball,false);
                  //  Physics.applyWind(ball,time,wind,false);
                    Physics.applySurfaceFriction(ball,surfaces);
                    Physics.terrainCollision(ball,terrain);
                    for(Obstacle obstacle:obstacles){
                        Physics.collision(ball,obstacle,false);
                    }
                    /*
                    for(Player player2: players){
                        if(player2 != player){
                            Physics.collisionBall(player.getBall(),player2.getBall());
                        }
                    }
                    */
                }else {
                    Physics.applyGravity(ball,true);
                    Physics.collision(ball,putHole,true);
                    Physics.setNewPosition(ball,true);
                    if(ball.getPosition().y<-1){
                        pause=true;

                    }
                }

            }
        }
    }

    public void setPause(boolean value){
        if(value){
            pause=value;
        }else {
            pause = false;
            time = System.currentTimeMillis();
            System.out.println(time);

        }
    }



    public Player getCurrentPlayer(){
        return currentPlayer;
    }

}
