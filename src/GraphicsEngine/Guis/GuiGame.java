package GraphicsEngine.Guis;

import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.fontMeshCreator.FontType;
import GraphicsEngine.fontMeshCreator.GUIText;
import GraphicsEngine.fontRendering.TextMaster;
import org.lwjgl.util.vector.Vector2f;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 19/06/16.
 */
public class GuiGame {
    GUIText text;
    GuiButton pause, resume, menu;
    FontType font;
    private List<GuiTexture> guis;
    private Loader loader;
    private GuiRenderer guiRenderer;
    private GuiTexture background;
    public GuiGame(Loader loader){
        this.loader = loader;
        font = new FontType(loader.loadTexture("arial"), new File("res/arial.fnt"));
        text = new GUIText("Player 1 is your turn, do your shot!", 1, font, new Vector2f(0,0),1f,true);
        text.setColour(127,255,0);
        guis = new ArrayList<GuiTexture>();
        pause = new GuiButton("pauseButton_on","pauseButton_off",loader,0.90f,0.90f,0.08f);
        resume = new GuiButton("playButton_on","playButton_off",loader,0,0.15f,0.27f);
        menu = new GuiButton("menuButton_on","menuButton_off",loader,0,-0.15f,0.27f);
        background = new GuiTexture(loader.loadTexture("background"),new Vector2f(0.0f,0f),new Vector2f(0.30f,0.30f));
        guis.add(pause.getGuiTexture());
        guiRenderer = new GuiRenderer(loader);

    }

    public void render(){
        guiRenderer.render(guis);
        TextMaster.render();
    }
    public void cleanUp(){
        guiRenderer.cleanUp();
        TextMaster.cleanUp();
    }

    public GUIText getText() {
        return text;
    }

    public GuiButton getPause() {
        return pause;
    }

    public GuiButton getResume() {
        return resume;
    }

    public GuiButton getMenu() {
        return menu;
    }

    public FontType getFont() {
        return font;
    }

    public List<GuiTexture> getGuis() {
        return guis;
    }

    public GuiTexture getBackground() {
        return background;
    }
}
