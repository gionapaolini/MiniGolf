package GameEngine;

import GolfObjects.Ball;
import GolfObjects.Map;
import GolfObjects.Obstacle;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.Textures.ModelTexture;
import PhysicsEngine.Physics;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 22/06/16.
 */
public class Simulator {
    Map map;

    public Simulator(Map map){
        this.map = map;
    }

    public void simulate(Vector3f start, Vector3f shot){
        Vector3f startPosition = new Vector3f(start.x,start.y,start.z);
        Vector3f shots = new Vector3f(shot.x,shot.y,shot.z);
        RawModel model = map.getBallList().get(0).getModel().getModel().getRawModel();
        ModelTexture texture = map.getBallList().get(0).getModel().getTexture();
        TexturedModel ballModel = new TexturedModel(model,texture);
        Entity ballEnt = new Entity(ballModel,startPosition,0,0,0,1);

        Ball ball = new Ball(ballEnt);

        ball.setVelocity(shots);
        float time =0.017f;
        do {
            //if (!Physics.checkBroadCollision(ball.getModel(), map.getPutHole().getFakeHole()) && ball.getPosition().y > -0.1) {
                Physics.applyGravity(ball, false, time);

                //  Physics.applyWind(ball,time,wind,false);
                Physics.applySurfaceFriction(ball, map.getSurfaceList(),time);
                Physics.terrainCollision(ball, map.getTerrain(),time);

                for (Obstacle obstacle : map.getObstacleList()) {
                    Physics.collision(ball, obstacle, false,time);
                }
            /*
            } else {
                Physics.applyGravity(ball, true,time);
                Physics.collision(ball, map.getPutHole(), true);
                Physics.setNewPosition(ball, true,time);
                if (ball.getPosition().y < -1) {
                    break;

                }
            }
            */
        }while (ball.isMoving());

        map.getBallList().add(ball);


    }
}
