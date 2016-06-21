package GolfObjects;

import GraphicsEngine.Entities.Entity;

/**
 * Created by giogio on 06/06/16.
 */
public class Obstacle extends GolfObject {
    String name;
    public Obstacle(Entity model,String name) {
        super(model);
        this.name = name;
    }
    public String getName(){return name;}
}
