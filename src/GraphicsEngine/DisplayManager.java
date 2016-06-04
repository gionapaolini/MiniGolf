package GraphicsEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

/**
 * Created by giogio on 04/06/16.
 */
public class DisplayManager {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;

    private static final int FPS = 120;

    public static void createDisplay(String title){


        ContextAttribs attribs = new ContextAttribs(3,2).withForwardCompatible(true).withProfileCore(true);

        try {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create(new PixelFormat(), attribs);
            Display.setTitle(title);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }

        GL11.glViewport(0,0,WIDTH,HEIGHT);
    }

    public static void updateDisplay(){
        Display.sync(FPS);
        Display.update();
    }

    public static void closeDisplay(){
        Display.destroy();
    }

}
