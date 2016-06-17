package GameEngine;

import GolfObjects.Ball;

/**
 * Created by giogio on 17/06/16.
 */
public abstract class Player {
    Ball ball;

    public Player(Ball ball){
        this.ball = ball;
    }
}
