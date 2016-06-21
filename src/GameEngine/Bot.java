package GameEngine;

import GameEngine.AI.Ray;
import GameEngine.AI.Triangulation;
import GolfObjects.Ball;
import GolfObjects.PutHole;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 17/06/16.
 */
public class Bot extends Player {
    PutHole hole;
    int level;
    int shots = 0;
    public Bot(Ball ball, PutHole putHole, int level){
        super(ball);
        this.hole = putHole;
        this.level = level;
    }
    public Vector3f shot(){
        return calc();
    }


    private Vector3f calc(){
        Vector3f h = hole.getPosition();
        System.out.println("hole: " + hole.getPosition().toString());
        System.out.println("ball: " + ball.getPosition().toString());

        if(level == 1){
            if(shots == 3){
                shots =0;
                return new Triangulation(ball.getPosition(), new Vector3f
                        (h.x, 0, h.z), 10f, 10f).shot();}
            else{
                shots++;
                return new Ray(ball.getPosition(), new Vector3f(h.x, 0, h.z)).shot();
            }

        }
        if (level == 2)
            return new Triangulation(ball.getPosition(), h, 10f, 10f).shot();
        else
            return null;
    }
}
