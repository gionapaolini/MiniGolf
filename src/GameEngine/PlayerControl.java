package GameEngine;

import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import org.lwjgl.util.vector.Vector3f;

import java.util.List;

import static java.lang.Math.PI;

/**
 * Created by giogio on 17/06/16.
 */
public class PlayerControl {
    List<Player> players;
    Player currentPlayer;
    Camera camera;
    int nPlayer;

    public PlayerControl(List<Player> players, Camera camera) {
        this.players = players;
        this.camera = camera;
        nPlayer = 0;
        currentPlayer = players.get(0);
    }

    private void moveCamOnPlayer(Player player){
        camera.setCurrentPlayer(player);
    }
    public void shot(){

    }
    public void nextPlayer(){
        nPlayer+=1;
        nPlayer%=players.size();
        currentPlayer = players.get(nPlayer);
        moveCamOnPlayer(currentPlayer);
    }

    public void moveArrow(Entity arrow, Vector3f point){
        if(point!=null) {

            float angleInDegrees = GetAngleOfLineBetweenTwoPoints(arrow.getPosition(),point);
            System.out.println(angleInDegrees);
            arrow.setRy(-angleInDegrees);
        }


    }

    public static float GetAngleOfLineBetweenTwoPoints(Vector3f p1, Vector3f p2)
    {
        double xDiff = p2.x - p1.x;
        double yDiff = p2.z - p1.z;
        return (float) Math.toDegrees(Math.atan2(yDiff, xDiff));

    }

}
