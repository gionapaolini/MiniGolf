package Tests;

import GameEngine.MenuControl;
import GraphicsEngine.Guis.GuiMenu;
import GraphicsEngine.RenderEngine.DisplayManager;
import GraphicsEngine.RenderEngine.Loader;
import GraphicsEngine.fontRendering.TextMaster;
import Toolbox.MousePicker;
import org.lwjgl.opengl.Display;

/**
 * Created by giogio on 19/06/16.
 */
public class RandomTes {
    public static void main(String[] args){
        DisplayManager.createDisplay("Prova");
        Loader loader = new Loader();
        GuiMenu menu = new GuiMenu(loader);
        TextMaster.init(loader);
        MenuControl menuControl = new MenuControl(menu,new MousePicker());
        while (!Display.isCloseRequested()){
            DisplayManager.updateDisplay();
            menu.render();
            menuControl.checkButtonClick();

        }
        loader.cleanUp();
        menu.cleanUp();
        DisplayManager.closeDisplay();

    }
}
