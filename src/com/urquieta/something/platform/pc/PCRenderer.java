package com.urquieta.something.platform.pc;

import com.urquieta.something.platform.Screen;

import java.awt.Color;
import java.awt.Graphics;

public class PCRenderer {
    protected Screen screen;

    public PCRenderer(Screen screen) {
        this.screen = screen;
    }

    public void DrawRect(int x, int y, int width, int height, int color) {
        if (this.screen.getCanvas() != null) {
            Graphics graphics = this.screen.getCanvas().getBufferStrategy().getDrawGraphics();
            if (graphics != null) {
                graphics.setColor(new Color(color, true));
                graphics.fillRect(x, y, width - x, height - y);
            }
        }
    }

    public void DrawCircle(int x, int y, int radius, int color) {
        if (this.screen.getCanvas() != null) {
            Graphics graphics = this.screen.getCanvas().getBufferStrategy().getDrawGraphics();
            if (graphics != null) {
                graphics.setColor(new Color(color, true));
                graphics.fillOval(x-radius, y-radius, radius*2, radius*2);
            }
        }
    }
}
