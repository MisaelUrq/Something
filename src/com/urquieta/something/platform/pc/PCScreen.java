package com.urquieta.something.platform.pc;

import com.urquieta.something.game.Game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
import static com.urquieta.something.platform.pc.PCOpenGL.*;
import com.urquieta.something.platform.Renderer;
import org.lwjgl.opengl.Display;

// NOTE(Misael): Not necesary for the PC build.
import com.urquieta.something.platform.pc.Context;

public class PCScreen extends Canvas {
    private static final long serialVersionUID = 1L;
    public static int width  = 400;
    public static int height = 650;
    private Game game;
    private JFrame frame;

    public PCScreen(Game game, Context context) {
        Dimension screenSize = new Dimension(width, height);
        setPreferredSize(screenSize);
        this.frame = new JFrame("Game");
        this.frame.setResizable(false);
        this.frame.add(this);
        this.frame.pack();
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setVisible(true);
        this.game = game;
    }

    public Graphics DrawToGGraphics(Graphics g, int buffer[]) {
        for (int y = 0; y < width; y++) {
            for (int x = 0; x < height; x++) {
                g.drawLine(x, y, x, y);
            }
        }
        return g;
    }

    public void InitOpenGLContext() {
        try {
            Display.setParent(this);
            Display.create();
        } catch (Exception e) {
            e.printStackTrace();
        }
        GLInit(width, height);
    }

    public Canvas getCanvas() {
        return this;
    }

    public void beginDraw() {
        // NOTE(Misael): Do we need to do something here?
    }

    public void endDraw() {
        game.GetRenderer().RenderScene();
        Display.update();
        int buffer[] = game.GetRenderer().GetBuffer(width, height);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        image.setRGB(0, 0, width, height, buffer, 0, width);
        game.GetRenderer().Draw(image);
    }
}
