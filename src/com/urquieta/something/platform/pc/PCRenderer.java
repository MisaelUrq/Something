package com.urquieta.something.platform.pc;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.pc.PCImage;


// TODO(Misael): Remove this.
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.Font;
import java.awt.image.BufferedImage;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL11.*;

import com.urquieta.something.game.GameModes;
import com.urquieta.something.game.GameState;
import com.urquieta.something.game.util.Color;
import com.urquieta.something.platform.renderer.figures.Rect;
import com.urquieta.something.utils.Buffers;

import java.util.ArrayDeque;
import java.util.Vector;



import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;


public class PCRenderer {
    protected Screen screen;

    // TODO(Misael): Probably will be depracated
    private   Graphics graphics;
    private   Font font;

    private float projection_matrix[];
    private float view_matrix[];
    private float mvp_matrix[];
    private float ratio;
    private ArrayDeque<Rect> rects_to_draw_queue;
    private Vector<Rect>     rects_to_draw;
    private GameModes prev_game_mode;

    public PCRenderer(Screen screen) {
        this();
        this.screen = screen;
        if (this.screen.getCanvas() != null) {

        }
    }

    public int[] GetBuffer(int width, int height) {
        int buffer[] = new int[width*height*4];
        glReadPixels(0, 0, width, height, GL_RGBA, GL_INT, Buffers.ArrayToBuffer(buffer));
        return buffer;
    }

    public PCRenderer() {
        this.projection_matrix = new float[4*4];
        this.view_matrix       = new float[4*4];
        this.mvp_matrix        = new float[4*4];
        this.rects_to_draw_queue = new ArrayDeque<Rect>();
        this.rects_to_draw       = new Vector<Rect>();
        this.screen = null;
        this.ratio = 0;
    }

    public void RenderScene() {

        // if (prev_game_mode != GameState.current_mode) {
        //     prev_game_mode = GameState.current_mode;
        //     for (Rect rect: rects_to_draw) {
        //         rect.Delete();
        //     }
        //     rects_to_draw.clear();
        //     rects_to_draw_queue.clear();
        // }

        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        // Matrix.multiplyMM(mvp_matrix, 0, projection_matrix, 0, view_matrix, 0);

        // synchronized(this) {
        //     for (Rect rect: rects_to_draw) {
        //         if (rect.HasBeenCreated() == false) {
        //             rect.Create();
        //         }
        //         rect.Draw(mvp_matrix);
        //     }

        //     Rect temp = rects_to_draw_queue.poll();
        //     while (temp != null) {
        //         temp.Create();
        //         temp.Draw(mvp_matrix);
        //         temp.Delete();
        //         temp = rects_to_draw_queue.poll();
        //     }
        // }

        glFlush();
    }

    public void DrawText(String str, float x, float y, Color color) {
        // if (this.graphics != null) {
        //     this.graphics.setColor(new Color(color, true));
        //     this.graphics.drawString(str, x, y);
        // }
    }

    public void Draw(BufferedImage buffer) {
        if (this.graphics != null) {
            this.graphics.drawImage(buffer, 0, 0,null);
        }

    }

    public void DrawImage(PCImage image, float x, float y) {
        // if (this.graphics != null) {
        //     BufferedImage bitmap = image.GetBuffer();
        //     this.graphics.drawImage(bitmap,
        //                             x - (bitmap.getWidth()/2), y - (bitmap.getHeight()/2),
        //                             null);
        // }
    }

    public void DrawRect(float x, float y, float width, float height, Color color) {
        // if (this.graphics != null) {
        //     this.graphics.setColor(new Color(color, true));
        //     this.graphics.fillRect(x, y, width - x, height - y);
        // }
    }

    public void DrawCircle(float x, float y, float radius, Color color) {
        // if (this.graphics != null) {
        //     this.graphics.setColor(new Color(color, true));
        //     this.graphics.fillOval(x-radius, y-radius, radius*2, radius*2);
        // }
    }

    public void DrawLine(float x1, float y1, float x2, float y2, float thickness, Color color) {
        // if (this.graphics != null) {
        //     Graphics2D graphics_2d = (Graphics2D)graphics;
        //     graphics_2d.setColor(new Color(color, true));
        //     Stroke stroke = graphics_2d.getStroke();
        //     graphics_2d.setStroke(new BasicStroke(thickness));
        //     graphics_2d.draw(new Line2D.Float(x1, y1,
        //                                       x2, y2));
        //     graphics_2d.setStroke(stroke);
        // }
    }

    public void DrawLine(float x1, float y1, float x2, float y2, Color color) {
        // if (this.graphics != null) {
        //     Graphics2D graphics_2d = (Graphics2D)graphics;
        //     graphics_2d.setColor(new Color(color, true));
        //     graphics_2d.draw(new Line2D.Float(x1, y1,
        //                                       x2, y2));
        // }
    }

    public void DrawFigure(Rect rect) {
        if (rects_to_draw.contains(rect) == false) {
            rects_to_draw.add(rect);
        }
    }

    public void RemoveFigure(Rect rect) {
        int index = rects_to_draw.indexOf(rect);
        if (index >= 0) {
            Rect temp = rects_to_draw.get(index);
            temp.Delete();
            rects_to_draw.remove(index);
        }
    }

    public static int LoadShader(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);
        return shader;
    }

    public void DrawRect(float x, float y, float width, float height, float z, int color_raw) {
        Rect temp = new Rect(x, y, width, height, z, new Color(color_raw));
        rects_to_draw_queue.push(temp);
    }
}
