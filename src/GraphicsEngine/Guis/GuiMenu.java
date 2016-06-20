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
public class GuiMenu {

    GuiButton courseDesigner;
    GuiButton settings;
    GuiButton play;
    GuiButton backToMenu;

    TripleStateButton nPlayer;
    TripleStateButton nBot;
    TripleStateButton lvlBot;
    TripleStateButton timeTurn;

    GuiTexture background;

    List<GuiTexture> guis;

    GuiRenderer guiRenderer;

    FontType font;
    GUIText text,text1,text2,text3,n,n1,n2,n3;

    public GuiMenu(Loader loader){
        guis = new ArrayList<GuiTexture>();

        courseDesigner = new GuiButton("designerButton_on","designerButton_off",loader,0,0.5f,0.4f);
        settings = new GuiButton("settingButton_on","settingButton_off",loader,0,0.1f,0.4f);
        play = new GuiButton("startButton_on","startButton_off",loader,0,-0.3f,0.4f);
        backToMenu = new GuiButton("backButton_on","backButton_off",loader,0,-0.8f,0.2f);


        font = new FontType(loader.loadTexture("arial"), new File("res/arial.fnt"));
        text = new GUIText("Number of Players", 2.05f, font, new Vector2f(-0.065f,0.115f),1f,true);
        text1 = new GUIText("Number of Bot", 2.05f, font, new Vector2f(-0.065f,0.315f),1f,true);
        text2 = new GUIText("Bot Difficulty Level", 2.05f, font, new Vector2f(-0.065f,0.515f),1f,true);
        text3 = new GUIText("Turn Duration (sec)", 2.05f, font, new Vector2f(-0.065f,0.715f),1f,true);
        n = new GUIText("1", 2.05f, font, new Vector2f(0.14f,0.115f),1f,true);
        n1 = new GUIText("0", 2.05f, font, new Vector2f(0.14f,0.315f),1f,true);
        n2 = new GUIText("1", 2.05f, font, new Vector2f(0.14f,0.515f),1f,true);
        n3 = new GUIText("30", 2.05f, font, new Vector2f(0.14f,0.715f),1f,true);


        nPlayer = new TripleStateButton("optionButton_off","optionButton_onPlus","optionButton_onMinus",loader,0,0.7f,0.4f);
        nBot = new TripleStateButton("optionButton_off","optionButton_onPlus","optionButton_onMinus",loader,0,0.3f,0.4f);
        lvlBot = new TripleStateButton("optionButton_off","optionButton_onPlus","optionButton_onMinus",loader,0,-0.1f,0.4f);
        timeTurn = new TripleStateButton("optionButton_off","optionButton_onPlus","optionButton_onMinus",loader,0,-0.5f,0.4f);

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

    public GuiButton getCourseDesigner() {
        return courseDesigner;
    }

    public GuiButton getSettings() {
        return settings;
    }

    public GuiButton getPlay() {
        return play;
    }

    public TripleStateButton getnPlayer() {
        return nPlayer;
    }

    public TripleStateButton getnBot() {
        return nBot;
    }

    public TripleStateButton getLvlBot() {
        return lvlBot;
    }

    public TripleStateButton getTimeTurn() {
        return timeTurn;
    }

    public List<GuiTexture> getGuis() {
        return guis;
    }

    public GUIText getText() {
        return text;
    }

    public GUIText getText1() {
        return text1;
    }

    public GUIText getText2() {
        return text2;
    }

    public GUIText getText3() {
        return text3;
    }

    public GUIText getN() {
        return n;
    }

    public GUIText getN1() {
        return n1;
    }

    public GUIText getN2() {
        return n2;
    }

    public GUIText getN3() {
        return n3;
    }
    public GuiButton getBackToMenu(){
        return backToMenu;
    }
}
