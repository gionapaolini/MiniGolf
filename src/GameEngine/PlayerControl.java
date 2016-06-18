package GameEngine;
import GolfObjects.Ball;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import Toolbox.Maths;
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
    int nPlayer;

    public PlayerControl(List<Player> players, Camera camera, Entity arrow) {
        this.players = players;
        this.camera = camera;
        nPlayer = 0;
        currentPlayer = players.get(0);
        this.arrow = arrow;
        arrow.setPosition(currentPlayer.getBall().getPosition());
        arrow.position.y=0.001f;
    }

    private void selectPlayer(){
        camera.setCurrentPlayer(currentPlayer);
        arrow.setPosition(currentPlayer.getBall().getPosition());
        arrow.position.y=0.001f;


    }

    public void shot(Vector3f point){
        Vector3f diff = Maths.vector3SUB(point,currentPlayer.getBall().getPosition());
        float distance = Maths.distancePoints(arrow.getPosition(),point);
        distance = Math.min(distance,4);
        diff.normalise();
        diff = Maths.scalarProduct(diff,2*distance);
        currentPlayer.getBall().setVelocity(diff);

    }
    public void nextPlayer(){
        nPlayer+=1;
        nPlayer%=players.size();
        currentPlayer = players.get(nPlayer);
        selectPlayer();
    }

    public void moveArrow(Entity arrow, Vector3f point){
        if(point!=null) {
            float angleInDegrees = GetAngleOfLineBetweenTwoPoints(arrow.getPosition(),point);
            arrow.setRy(-angleInDegrees);
            float distance = Maths.distancePoints(arrow.getPosition(),point);
            if(distance<4)
                arrow.setScaleX(distance/1.3f);

        }


    }

    public static float GetAngleOfLineBetweenTwoPoints(Vector3f p1, Vector3f p2)
    {
        double xDiff = p2.x - p1.x;
        double yDiff = p2.z - p1.z;
        return (float) Math.toDegrees(Math.atan2(yDiff, xDiff));

    }

}
