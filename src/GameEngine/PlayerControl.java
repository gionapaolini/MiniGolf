package GameEngine;
import GolfObjects.Ball;
import GolfObjects.Obstacle;
import GolfObjects.PutHole;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Terrain;
import PhysicsEngine.Physics;
import Toolbox.Maths;
import Toolbox.MousePicker;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import java.util.List;


/**
 * Created by giogio on 17/06/16.
 */
public class PlayerControl {
    List<Player> players;
    Player currentPlayer;
    Camera camera;
    Entity arrow;
    public int nPlayer, timeLeft, maxTimeTurn;
    public boolean disabledShot, pause, wait;
    long time = 0;
    long timeLeftShot = 0;


    public PlayerControl(List<Player> players, Camera camera, Entity arrow, int maxTimeTurn) {
        this.players = players;
        this.camera = camera;
        nPlayer = 0;
        currentPlayer = players.get(0);
        this.arrow = arrow;
        arrow.setPosition(currentPlayer.getBall().getPosition());
        arrow.position.y=0.001f;
        disabledShot = false;
        this.maxTimeTurn = maxTimeTurn;

    }

    private void selectPlayer(){
        camera.setCurrentPlayer(currentPlayer);
        arrow.setPosition(currentPlayer.getBall().getPosition());
        arrow.position.y=0.001f;


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
            System.out.println("FFW");
        }

    }

    public void game(MousePicker picker, List<Obstacle> obstacles,Terrain terrain,PutHole putHole,float time){
        if(!pause && System.currentTimeMillis()-time >1000) {
            moveArrow(arrow, picker.getCurrentTerrainPoint());
            decrementTimeLeft();
            shot(picker.getCurrentTerrainPoint());
            nextPlayer();
            applyPhysics(obstacles,terrain,putHole,time);
        }
    }
    public void nextPlayer(){
        if((!currentPlayer.getBall().isMoving() && disabledShot)||(timeLeft<0)) {
            nPlayer += 1;
            nPlayer %= players.size();
            currentPlayer = players.get(nPlayer);
            selectPlayer();
            disabledShot = false;
            timeLeft = maxTimeTurn;
        }
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

    public void applyPhysics(List<Obstacle> obstacles, Terrain terrain, PutHole putHole
                             ,float time){
        for(Player player: players){
            Ball ball = player.getBall();
            if(ball.isMoving()){
                if(!Physics.checkBroadCollision(ball, putHole)){
                    Physics.applyGravity(ball,time,false);
                    Physics.applyFriction(ball,time);
                    Physics.terrainCollision(ball,terrain,time);
                    for(Obstacle obstacle:obstacles){
                        Physics.collision(ball,obstacle,time);
                    }
                }else {
                    Physics.applyGravity(ball,time,true);
                    Physics.collision(ball,putHole,time);
                }

                Physics.setNewPosition(ball,time);
            }
        }
    }

    public void setPause(boolean value){
        if(value){
            pause=value;
        }else {
            pause = false;
            time = System.currentTimeMillis();
        }
    }



    public Player getCurrentPlayer(){
        return currentPlayer;
    }

}
