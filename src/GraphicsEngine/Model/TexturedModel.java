package GraphicsEngine.Model;

import GraphicsEngine.Textures.ModelTexture;

/**
 * Created by giogio on 05/06/16.
 */
public class TexturedModel {

    private RawModel rawModel;
    private ModelTexture texture;

    public TexturedModel(RawModel rawModel, ModelTexture texture) {
        this.rawModel = rawModel;
        this.texture = texture;
    }

    public RawModel getRawModel() {
        return rawModel;
    }

    public ModelTexture getTexture() {
        return texture;
    }

    public void setTexture(ModelTexture text){
        this.texture = text;
    }
}
