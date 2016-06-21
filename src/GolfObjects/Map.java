package GolfObjects;

import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.RenderEngine.MasterRenderer;

import java.util.List;

/**
 * Created by giogio on 21/06/16.
 */
public class Map {
    static int nMaps=1;
    int idMap;
    Terrain terrain;
    List<Obstacle> obstacleList;
    List<Ball> ballList;
    List<Surface> surfaceList;
    PutHole putHole;

    public Map(Terrain terrain, List<Obstacle> obstacleList, List<Ball> ballList, List<Surface> surfaceList, PutHole putHole) {
        this.terrain = terrain;
        this.obstacleList = obstacleList;
        this.ballList = ballList;
        this.surfaceList = surfaceList;
        this.putHole = putHole;
        idMap = nMaps;
        nMaps++;
    }

    public int getIdMap() {
        return idMap;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    public List<Obstacle> getObstacleList() {
        return obstacleList;
    }

    public List<Ball> getBallList() {
        return ballList;
    }

    public List<Surface> getSurfaceList() {
        return surfaceList;
    }

    public PutHole getPutHole() {
        return putHole;
    }

    public void renderMap(MasterRenderer renderer){
        renderer.processTerrain(terrain);
        for(Ball ball: ballList){
            renderer.processEntity(ball.getModel());
        }
        for(Obstacle obstacle: obstacleList){
            renderer.processEntity(obstacle.getModel());
        }
        for(Surface surface: surfaceList){
            renderer.processEntity(surface.getModel());
        }
        renderer.processEntity(putHole.getFakeHole());
    }
}
