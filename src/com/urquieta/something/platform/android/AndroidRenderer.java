package com.urquieta.something.platform.android;

// import android.graphics.Paint;
// import android.graphics.RectF;
// import android.graphics.Canvas;
// import android.graphics.Bitmap;

import android.opengl.GLSurfaceView;
import static android.opengl.GLES20.*;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.renderer.figures.Rect;
import com.urquieta.something.platform.renderer.figures.Circle;
import com.urquieta.something.game.util.Vec4;
import com.urquieta.something.game.util.Color;

import com.urquieta.something.game.GameModes;
import com.urquieta.something.game.GameState;

import java.util.ArrayDeque;
import java.util.Vector;

public class AndroidRenderer implements GLSurfaceView.Renderer {
    protected Screen screen;
    private float projection_matrix[];
    private float view_matrix[];
    private float mvp_matrix[];
    private float ratio;
    private static ArrayDeque<Rect> rects_to_draw_queue;
    private static Vector<Rect>     rects_to_draw;

    private GameModes prev_game_mode;

    public AndroidRenderer(Screen screen) {
        this.screen = screen;
        this.projection_matrix = null;
        this.mvp_matrix        = null;
        this.view_matrix       = null;
        this.rects_to_draw_queue = new ArrayDeque<Rect>();
        this.rects_to_draw       = new Vector<Rect>();
        this.ratio = 0;
    }

    public AndroidRenderer() {
        super();
        this.projection_matrix = new float[4*4];
        this.view_matrix       = new float[4*4];
        this.mvp_matrix        = new float[4*4];
        this.rects_to_draw_queue = new ArrayDeque<Rect>();
        this.rects_to_draw       = new Vector<Rect>();
        this.screen = null;
        this.ratio = 0;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        glClearColor(0.9f, 0.9f, 1.0f, 1.0f);
        glEnable(GL_DEPTH_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glDepthFunc(GL_LEQUAL);
        glDepthMask(true);
        Matrix.setLookAtM(view_matrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        if (prev_game_mode != GameState.current_mode) {
            prev_game_mode = GameState.current_mode;
            for (Rect rect: AndroidRenderer.rects_to_draw) {
                rect.Delete();
            }
            rects_to_draw.clear();
            rects_to_draw_queue.clear();
        }
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Matrix.multiplyMM(mvp_matrix, 0, projection_matrix, 0, view_matrix, 0);

        synchronized(this) {
            for (Rect rect: AndroidRenderer.rects_to_draw) {
                if (rect.HasBeenCreated() == false) {
                    rect.Create();
                }
                rect.Draw(mvp_matrix);
            }

            Rect temp = AndroidRenderer.rects_to_draw_queue.poll();
            while (temp != null) {
                temp.Create();
                temp.Draw(mvp_matrix);
                temp.Delete();
                temp = AndroidRenderer.rects_to_draw_queue.poll();
            }
        }

        glFlush();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        glViewport(0, 0, width, height);
        ratio = (float)width/(float)height;
        Matrix.frustumM(projection_matrix, 0, ratio, -ratio, -1, 1, 3, 7);
    }

    public static int LoadShader(int type, String source) {
        int shader = glCreateShader(type);
        glShaderSource(shader, source);
        glCompileShader(shader);
        return shader;
    }

    public void DrawText(String str, int x, int y, int color) {
        /* if (this.screen.getCanvas() != null) {
           Paint paint = new Paint();
           paint.setColor(color);
           this.screen.getCanvas().drawText(str, x, y, paint);
           } */
    }

    public void RemoveFigure(Rect rect) {
        synchronized(this) {
            int index = AndroidRenderer.rects_to_draw.indexOf(rect);
            if (index >= 0) {
                Rect temp = rects_to_draw.get(index);
                temp.Delete();
                rects_to_draw.remove(index);
            }
        }
    }

    public void DrawFigure(Rect rect) {
        synchronized(this) {
            if (AndroidRenderer.rects_to_draw.contains(rect) == false) {
                AndroidRenderer.rects_to_draw.add(rect);
            }
        }
    }

    public void DrawRect(float x, float y, float width, float height, float z, int color_raw) {
        Rect temp = new Rect(x, y, width, height, z, new Color(color_raw));
        synchronized(this) {
            AndroidRenderer.rects_to_draw_queue.push(temp);
        }
    }

    public void DrawCircle(int x, int y, int radius, int color) {
        // if (this.screen.getCanvas() != null) {
        //     Paint paint = new Paint();
        //     paint.setColor(color);
        //     float pos_x = x-radius;
        //     float pos_y = y-radius;
        //     radius = 2 * radius;
        //     RectF rect = new RectF(pos_x, pos_y,
        //                            radius+pos_x,
        //                            radius+pos_y);
        //     this.screen.getCanvas().drawOval(rect, paint);
        // }
    }

    public void DrawImage(AndroidImage image, int x, int y) {
        // if (this.screen.getCanvas() != null && image.GetBuffer() != null) {
        //     Bitmap bitmap = image.GetBuffer();
        //         this.screen.getCanvas().drawBitmap(bitmap,
        //                                            x - (bitmap.getWidth()/2), y - (bitmap.getHeight()/2),
        //                                            null);
        // }
    }

    public void DrawLine(int x1, int y1, int x2, int y2,
                         int thickness, int color) {
        // if (this.screen.getCanvas() != null) {
        //     Paint paint = new Paint();
        //     float width = paint.getStrokeWidth();
        //     paint.setColor(color);
        //     paint.setStrokeWidth(thickness);
        //     this.screen.getCanvas().drawLine(x1, y1, x2, y2, paint);
        //     paint.setStrokeWidth(width);
        // }
    }

    public void DrawLine(int x1, int y1, int x2, int y2, int color) {

        // if (this.screen.getCanvas() != null) {
        //     Paint paint = new Paint();
        //     paint.setColor(color);
        //     this.screen.getCanvas().drawLine(x1, y1, x2, y2, paint);
        // }
    }
}
