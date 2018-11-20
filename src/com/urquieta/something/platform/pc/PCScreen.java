package com.urquieta.something.platform.pc;

import com.urquieta.something.game.Game;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

// NOTE(Misael): Not necesary for the PC build.
import com.urquieta.something.platform.pc.Context;

public class PCScreen extends Canvas {
    protected int width  = 300;
    protected int height = 450;
    private Game game;
    private JFrame frame;
    BufferStrategy buffer;

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
        createBufferStrategy(3);
    }

    public Canvas getCanvas() {
        return this;
    }

    public void beginDraw() {
        // NOTE(Misael): Do we need to do something here?
    }

    public void endDraw() {
        if (getBufferStrategy().getDrawGraphics() != null) {
            getBufferStrategy().getDrawGraphics().dispose();
            getBufferStrategy().show();
        }
    }
}
