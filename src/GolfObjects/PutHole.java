package GolfObjects;

import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Textures.ModelTexture;
import org.lwjgl.util.vector.Vector3f;

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

    public void setCollideColor(ModelTexture texture){
        fakeHole.setTexture(texture);
    }
    public void unsetCollideColor(){
        fakeHole.setTexture(realTexture);
    }
    public void setPosition(Vector3f position){
        model.setPosition(position);
        fakeHole.setPosition(position);
    }

}
