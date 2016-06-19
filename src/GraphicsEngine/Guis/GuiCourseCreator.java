package GraphicsEngine.Guis;

import GraphicsEngine.RenderEngine.Loader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 16/05/16.
 */
public class GuiCourseCreator{
    private List<GuiTexture> guis;
    private Loader loader;
    private GuiRenderer guiRenderer;
    private GuiButton ballButton, cubeButton,barButton,slopeButton,selectButton, saveButton;


    public GuiCourseCreator(Loader loader){
        this.loader = loader;
        guis = new ArrayList<GuiTexture>();
        ballButton = new GuiButton("ballButton_on","ballButton_off",loader,-0.75f,0.85f,0.15f);
        cubeButton = new GuiButton("cubeButton_on","cubeButton_off",loader,-0.45f,0.85f,0.15f);
        barButton = new GuiButton("barButton_on","barButton_off",loader,-0.15f,0.85f,0.15f);
        slopeButton = new GuiButton("slopeButton_on","slopeButton_off",loader,0.15f,0.85f,0.15f);
        selectButton = new GuiButton("selectButton_on","selectButton_off",loader,0.45f,0.85f,0.15f);
        saveButton = new GuiButton("saveButton","obstacle1Button_off",loader,0.75f,0.85f,0.15f);

        guis.add(ballButton.getGuiTexture());
        guis.add(saveButton.getGuiTexture());
        guis.add(cubeButton.getGuiTexture());
        guis.add(barButton.getGuiTexture());
        guis.add(slopeButton.getGuiTexture());
        guis.add(selectButton.getGuiTexture());
        guiRenderer = new GuiRenderer(loader);
    }

    public void render(){
        guiRenderer.render(guis);
    }
    public void cleanUp(){
        guiRenderer.cleanUp();
    }

    public GuiButton getBallButton() {
        return ballButton;
    }

    public GuiButton getSaveButtonButton() {
        return saveButton;
    }

    public GuiButton getCubeButton(){
        return cubeButton;
    }
    public GuiButton getBarButton(){
        return barButton;
    }
    public GuiButton getSlopeButton(){
        return slopeButton;
    }
    public GuiButton getSelectButton(){
        return selectButton;
    }

}
