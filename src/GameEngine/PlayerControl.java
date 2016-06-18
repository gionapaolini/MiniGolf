package GameEngine;

import GraphicsEngine.Entities.Camera;

import java.util.List;

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

}
