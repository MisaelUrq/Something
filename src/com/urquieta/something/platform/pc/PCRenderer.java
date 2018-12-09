package com.urquieta.something.platform.pc;

import com.urquieta.something.platform.Screen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.geom.Line2D;


public class PCRenderer {
    protected Screen screen;
    private Graphics graphics;

    public PCRenderer(Screen screen) {
        this.screen = screen;
        if (this.screen.getCanvas() != null) {
            graphics = this.screen.getCanvas().getBufferStrategy().getDrawGraphics();
        }
    }

    public void DrawRect(int x, int y, int width, int height, int color) {
        if (this.graphics != null) {
            this.graphics.setColor(new Color(color, true));
            this.graphics.fillRect(x, y, width - x, height - y);
        }
    }

    public void DrawCircle(int x, int y, int radius, int color) {
        if (this.graphics != null) {
            this.graphics.setColor(new Color(color, true));
            this.graphics.fillOval(x-radius, y-radius, radius*2, radius*2);
        }
    }

    public void DrawLine(int x1, int y1, int x2, int y2, int thickness, int color) {
        if (this.graphics != null) {
            Graphics2D graphics_2d = (Graphics2D)graphics;
            graphics_2d.setColor(new Color(color, true));
            Stroke stroke = graphics_2d.getStroke();
            graphics_2d.setStroke(new BasicStroke(thickness));
            graphics_2d.draw(new Line2D.Float(x1, y1,
                                              x2, y2));
            graphics_2d.setStroke(stroke);
        }
    }

    public void DrawLine(int x1, int y1, int x2, int y2, int color) {
        if (this.graphics != null) {
            Graphics2D graphics_2d = (Graphics2D)graphics;
            graphics_2d.setColor(new Color(color, true));
            graphics_2d.draw(new Line2D.Float(x1, y1,
                                              x2, y2));
        }
    }
}
