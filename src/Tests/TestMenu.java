package Tests;

import GameEngine.*;
import GolfObjects.Ball;
import GolfObjects.Obstacle;
import GolfObjects.PutHole;
import GraphicsEngine.Entities.Camera;
import GraphicsEngine.Entities.Entity;
import GraphicsEngine.Entities.Light;
import GraphicsEngine.Entities.Terrain;
import GraphicsEngine.Guis.GuiGame;
import GraphicsEngine.Guis.GuiMenu;
import GraphicsEngine.Model.RawModel;
import GraphicsEngine.Model.TexturedModel;
import GraphicsEngine.RenderEngine.DisplayManager;
import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.RenderEngine.MasterRenderer;
import GraphicsEngine.RenderEngine.OBJLoader;
import GraphicsEngine.Textures.ModelTexture;
import GraphicsEngine.fontRendering.TextMaster;
import Toolbox.MousePicker;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 19/06/16.
 */
public class TestMenu {

    public static void main(String[] args){

        DisplayManager.createDisplay("CrazyGolf Game");

        Loader loader = new Loader();
        Light light = new Light(new Vector3f(0,20,0),new Vector3f(1,1,1));
        Camera camera = new Camera();
        MasterRenderer masterRenderer = new MasterRenderer();
        Settings settings = new Settings();

        TextMaster.init(loader);
        MousePicker mousePicker = new MousePicker();
        GuiMenu menu = new GuiMenu(loader);
        MenuControl menuControl = new MenuControl(menu,mousePicker,settings);
        MasterRenderer renderer = new MasterRenderer();
        int choose = 2;
        while (!Display.isCloseRequested()){
            masterRenderer.render(light,camera);
            switch(choose){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;

            }

            menuControl.checkButtonClick();
            menu.render();
            DisplayManager.updateDisplay();


        }
        menu.cleanUp();
        loader.cleanUp();
        masterRenderer.cleanUp();

        DisplayManager.closeDisplay();


    }
}
