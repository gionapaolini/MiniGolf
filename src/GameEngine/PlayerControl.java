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


    public PlayerControl(List<Player> players, Camera camera, Entity arrow, Entity arrow3D, int maxTimeTurn, MousePicker picker) {
        this.players = players;
        this.camera = camera;
        nPlayer = 0;
        this.arrow = arrow;
        arrow.position.y=0.1f;
        disabledShot = true;
        this.maxTimeTurn = maxTimeTurn;
        this.picker = picker;
        this.arrow3D = arrow3D;

    }

    private void selectPlayer(){
        camera.setCurrentPlayer(currentPlayer);
        Vector3f pos = currentPlayer.getBall().getPosition();
        arrow.setPosition(new Vector3f(pos.x,pos.y,pos.z));
        arrow.position.y=0.001f;
        arrow3D.setPosition(new Vector3f(pos.x,pos.y,pos.z));
        arrow3D.position.y=4;


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
            currentPlayer.getBall().setVelocity(diff);
            disabledShot = true;
        }

    }

    public void game(List<Obstacle> obstacles, Terrain terrain, PutHole putHole, float time,List<Surface> surfaces){
        if(!pause && System.currentTimeMillis()-this.time>1000) {
            moveArrow(arrow, picker.getCurrentTerrainPoint());
            decrementTimeLeft();
            shot(picker.getCurrentTerrainPoint());
            nextPlayer();
            applyPhysics(obstacles,terrain,putHole,time,surfaces);
        }
    }
    public void nextPlayer(){
        if((!isMotion() && disabledShot)||(timeLeft<0)) {
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

    public void moveArrow(Entity arrow, Vector3f point){

        if(point!=null) {
            float angleInDegrees = Maths.GetAngleOfLineBetweenTwoPoints(arrow.getPosition(),point);
            arrow.setRy(-angleInDegrees);
            float distance = Maths.distancePoints(arrow.getPosition(),point);
            if(distance<4)
                arrow.setScaleX(distance/1.3f);

        }
    }

    public void applySurfaceFriction(GolfObject obj, float time,List<Surface> surfaces){
        for(Surface surface: surfaces){
            if(surface.ballIsOver(obj)){
                Physics.applyFriction(obj,time,surface.getCoefficientFriction());
                return;
            }
        }
        Physics.applyFriction(obj,time,1.5f);

    }

    public void applyPhysics(List<Obstacle> obstacles,Terrain terrain,PutHole putHole,float time,List<Surface> surfaces){
        for(Player player: players){
            Ball ball = player.getBall();
            if(ball.isMoving()){
                if(!Physics.checkBroadCollision(ball.getModel(),putHole.getFakeHole()) && ball.getPosition().y>-0.1){
                    Physics.applyGravity(ball,time,false);

                    applySurfaceFriction(ball,time,surfaces);
                    Physics.terrainCollision(ball,terrain,time);
                    for(Obstacle obstacle:obstacles){
                        Physics.collision(ball,obstacle,time);
                    }
                    for(Player player2: players){
                        if(player2 != player){
                            Physics.collisionBall(player.getBall(),player2.getBall(),time);
                        }
                    }
                    Physics.setNewPosition(ball,time,false);
                }else {
                    Physics.applyGravity(ball,time,true);
                    Physics.collision(ball,putHole,time);
                    Physics.setNewPosition(ball,time,true);
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
