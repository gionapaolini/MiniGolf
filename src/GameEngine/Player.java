package GameEngine;

import GolfObjects.Ball;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by giogio on 17/06/16.
 */
public abstract class Player {
    Ball ball;
    public Vector3f shot(){return null;}

    public Player(Ball ball){
        this.ball = ball;
    }

    public Ball getBall(){
        return ball;
    }


}
