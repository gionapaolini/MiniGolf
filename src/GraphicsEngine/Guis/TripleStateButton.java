package GraphicsEngine.Guis;

import GraphicsEngine.RenderEngine.Loader;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by giogio on 19/06/16.
 */
public class TripleStateButton {

    private int textureOff;
    private int texturePlus;
    private int textureMinus;
    private GuiTexture currentText;
    private Vector2f position;
    private Vector2f scale;
    private boolean selected;

    public TripleStateButton(String off, String plus, String minus, Loader loader, float x, float y, float scale){
        position = new Vector2f(x,y);
        this.scale = new Vector2f(scale,scale);
        textureOff = loader.loadTexture(off);
        texturePlus = loader.loadTexture(plus);
        textureMinus = loader.loadTexture(minus);
        currentText = new GuiTexture(textureOff,position,this.scale);
        this.selected=false;
    }

    public boolean isPlus(){
        return currentText.getTexture() == texturePlus;
    }
    public boolean isMinus(){
        return currentText.getTexture() == textureMinus;
    }
    public boolean isOff(){
        return currentText.getTexture() == textureOff;
    }

    public GuiTexture getGuiTexture(){
        return currentText;
    }

    public void setTextureOff(){
        currentText.setTexture(textureOff);
    }
    public void setTexturePlus(){
        currentText.setTexture(texturePlus);
    }
    public void setTextureMinus(){
        currentText.setTexture(textureMinus);
    }
}
