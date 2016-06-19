package GameEngine;

import GraphicsEngine.Guis.*;
import GraphicsEngine.fontMeshCreator.FontType;
import GraphicsEngine.fontMeshCreator.GUIText;
import Toolbox.MousePicker;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by giogio on 19/06/16.
 */
public class MenuControl {
    GuiMenu menu;
    MousePicker picker;
    GuiButton courseDesigner;
    GuiButton settings;
    GuiButton play;
    GuiButton back;

    TripleStateButton nPlayer;
    TripleStateButton nBot;
    TripleStateButton lvlBot;
    TripleStateButton timeTurn;

    FontType font;
    GUIText text,text1,text2,text3,n,n1,n2,n3;
    public boolean settingState;
    List<GuiTexture> guis;

    public MenuControl(GuiMenu menu, MousePicker picker){
        this.menu = menu;
        this.picker = picker;
        courseDesigner = menu.getCourseDesigner();
        settings = menu.getSettings();
        play = menu.getPlay();
        nPlayer = menu.getnPlayer();
        nBot = menu.getnBot();
        lvlBot = menu.getLvlBot();
        timeTurn = menu.getTimeTurn();
        text = menu.getText();
        text1 = menu.getText1();
        text2 = menu.getText2();
        text3 = menu.getText3();
        n = menu.getN();
        n1 = menu.getN1();
        n2 = menu.getN2();
        n3 = menu.getN3();
        guis = menu.getGuis();
        back = menu.getBackToMenu();
        guis.add(courseDesigner.getGuiTexture());
        guis.add(settings.getGuiTexture());
        guis.add(play.getGuiTexture());



    }

    public void checkButtonClick(){
        Vector2f mouseC = picker.getNormalCoord();
        if(Mouse.isButtonDown(0)){
            System.out.println(mouseC);
        }
        if (!settingState && (mouseC.x >= -0.396) && (mouseC.x <= 0.398) && (mouseC.y <= 0.638) && (mouseC.y >= 0.333)) {
            courseDesigner.select();
            settings.deselect();
            play.deselect();
        }else if (!settingState && (mouseC.x >= -0.396) && (mouseC.x <= 0.398) && (mouseC.y <= 0.241) && (mouseC.y >= -0.069)) {
            settings.select();
            courseDesigner.deselect();
            play.deselect();
            if(Mouse.isButtonDown(0)){
                checkButtonState();
            }

        }else if (!settingState && (mouseC.x >= -0.396) && (mouseC.x <= 0.398) && (mouseC.y <= -0.161) && (mouseC.y >= -0.469)) {
            play.select();
            settings.deselect();
            courseDesigner.deselect();
        }else if (settingState && (mouseC.x >= 0.129) && (mouseC.x <= 0.206) && (mouseC.y <= 0.769) && (mouseC.y >= 0.702)) {
            nPlayer.setTexturePlus();

        }else if (settingState && (mouseC.x >= 0.129) && (mouseC.x <= 0.207) && (mouseC.y <= 0.697) && (mouseC.y >= 0.627)) {
            nPlayer.setTextureMinus();

        }else if (settingState && (mouseC.x >= 0.129) && (mouseC.x <= 0.207) && (mouseC.y <= 0.369) && (mouseC.y >= 0.302)) {
            nBot.setTexturePlus();

        }else if (settingState && (mouseC.x >= 0.129) && (mouseC.x <= 0.207) && (mouseC.y <= 0.297) && (mouseC.y >= 0.227)) {
            nBot.setTextureMinus();

        }else if (settingState && (mouseC.x >= 0.129) && (mouseC.x <= 0.207) && (mouseC.y <= -0.03) && (mouseC.y >= -0.094)) {
            lvlBot.setTexturePlus();

        }else if (settingState && (mouseC.x >= 0.129) && (mouseC.x <= 0.207) && (mouseC.y <= -0.102) && (mouseC.y >= -0.172)) {
            lvlBot.setTextureMinus();

        }else if (settingState && (mouseC.x >= 0.129) && (mouseC.x <= 0.207) && (mouseC.y <= -0.433) && (mouseC.y >= -0.497)) {
            timeTurn.setTexturePlus();

        }else if (settingState && (mouseC.x >= 0.129) && (mouseC.x <= 0.207) && (mouseC.y <= -0.5) && (mouseC.y >= -0.572)) {
            timeTurn.setTextureMinus();

        }else if (settingState && (mouseC.x >= -0.198) && (mouseC.x <= 0.20) && (mouseC.y <= -0.697) && (mouseC.y >= -0.894)) {
            back.select();

        }else {
            play.deselect();
            settings.deselect();
            courseDesigner.deselect();
            nPlayer.setTextureOff();
            nBot.setTextureOff();
            lvlBot.setTextureOff();
            timeTurn.setTextureOff();
            back.deselect();
        }
    }

    private void checkButtonState(){
        if(settings.isSelected()){
            settingState = true;
            menu.getGuis().remove(courseDesigner.getGuiTexture());
            menu.getGuis().remove(settings.getGuiTexture());
            menu.getGuis().remove(play.getGuiTexture());
            guis.add(nPlayer.getGuiTexture());
            guis.add(nBot.getGuiTexture());
            guis.add(lvlBot.getGuiTexture());
            guis.add(timeTurn.getGuiTexture());
            guis.add(back.getGuiTexture());
            text.load();
            text1.load();
            text2.load();
            text3.load();
            n.load();
            n1.load();
            n2.load();
            n3.load();

        }
    }
}
