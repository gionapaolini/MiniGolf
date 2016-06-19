package GolfObjects;

import GraphicsEngine.Entities.Entity;

/**
 * Created by giogio on 16/06/16.
 */
public class PutHole extends GolfObject{
    Entity fakeHole;
    public PutHole(Entity model, Entity fakeHole) {
        super(model);
        this.fakeHole = fakeHole;
    }

    public Entity getFakeHole(){
        return fakeHole;
    }
}
