package GameEngine;

import GraphicsEngine.Guis.GuiButton;
import GraphicsEngine.Guis.GuiGame;
import GraphicsEngine.fontMeshCreator.GUIText;
import Toolbox.MousePicker;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector2f;

/**
 * Created by giogio on 19/06/16.
 */
public class GuiControlGame {
    GuiGame guiGame;
    PlayerControl playerControl;
    GuiButton pause, play, menu;
    GUIText text;
    long time,timeUpdate;
    MousePicker picker;

    public GuiControlGame(GuiGame guiGame, PlayerControl playerControl, MousePicker picker) {
        this.guiGame = guiGame;
        this.playerControl = playerControl;
        pause = guiGame.getPause();
        play = guiGame.getResume();
        menu = guiGame.getMenu();
        text = guiGame.getText();
        this.picker = picker;
    }

    public void checkButtonsClick(){
        if(System.currentTimeMillis()-time>100){

            Vector2f mouseC = picker.getNormalCoord();

            if (!playerControl.pause && (mouseC.x >= 0.867) && (mouseC.x <= 0.924) && (mouseC.y <= 0.939) && (mouseC.y >= 0.855)) {
                pause.select();
                playerControl.wait =true;
                play.deselect();
                menu.deselect();
                if(Mouse.isButtonDown(0)){
                    checkButtons();
                }

                time = System.currentTimeMillis();


            }else if (playerControl.pause && (mouseC.x >= -0.268) && (mouseC.x <= 0.27) && (mouseC.y <= 0.25) && (mouseC.y >= 0.04)) {
                play.select();
                pause.deselect();
                menu.deselect();
                if(Mouse.isButtonDown(0)){
                    checkButtons();
                }

                time = System.currentTimeMillis();

            }else if (playerControl.pause && (mouseC.x >= -0.268) && (mouseC.x <= 0.27) && (mouseC.y <= -0.04) && (mouseC.y >= -0.25)) {
                menu.select();
                pause.deselect();
                play.deselect();
                if(Mouse.isButtonDown(0)){
                    checkButtons();
                }
                time = System.currentTimeMillis();

            }else {
                menu.deselect();
                pause.deselect();
                play.deselect();
                playerControl.wait =false;

            }



            updateText();
        }
    }

    public void updateText(){
        if(System.currentTimeMillis()-timeUpdate>1000) {
            if (playerControl.disabledShot) {
                text.setTextString("");
            } else {
                text.setTextString("Player " + (playerControl.nPlayer + 1) + " it s your turn, do your best shot! Time left: " + playerControl.timeLeft);
            }
            timeUpdate = System.currentTimeMillis();
        }
    }

    public void checkButtons(){
        if(pause.isSelected()){
            guiGame.getGuis().add(guiGame.getBackground());
            guiGame.getGuis().add(play.getGuiTexture());
            guiGame.getGuis().add(menu.getGuiTexture());
            playerControl.setPause(true);
            pause.swap();
        }else if(play.isSelected()){
            guiGame.getGuis().remove(guiGame.getBackground());
            guiGame.getGuis().remove(play.getGuiTexture());
            guiGame.getGuis().remove(menu.getGuiTexture());
            playerControl.setPause(false);
            play.swap();
            pause.swap();

        }

    }
}
