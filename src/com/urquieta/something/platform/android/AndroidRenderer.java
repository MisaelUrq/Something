package com.urquieta.something.platform.android;

// import android.graphics.Paint;
// import android.graphics.RectF;
// import android.graphics.Canvas;
// import android.graphics.Bitmap;

import android.opengl.GLSurfaceView;
import android.opengl.GLES20;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.urquieta.something.platform.Screen;
import com.urquieta.something.platform.renderer.figures.Rect;
import com.urquieta.something.game.util.Vec4;
import com.urquieta.something.game.util.Color;

import java.util.ArrayDeque;
import java.util.Vector;

public class AndroidRenderer implements GLSurfaceView.Renderer {
    public Screen screen;
    private float projection_matrix[];
    private float view_matrix[];
    private float mvp_matrix[];
    private float ratio;
    private static ArrayDeque<Rect> rects_to_draw_queue;
    private static Vector<Rect>     rects_to_draw;

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
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glClearColor(0.9f, 0.9f, 1.0f, 1.0f);
        Matrix.setLookAtM(view_matrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        Matrix.multiplyMM(mvp_matrix, 0, projection_matrix, 0, view_matrix, 0);

        synchronized(this) {
            for (Rect rect: AndroidRenderer.rects_to_draw) {
                if (rect.HasBeenCreated() == false) {
                    rect.CreateProgram();
                }
                rect.Draw(mvp_matrix);
            }

            Rect temp = AndroidRenderer.rects_to_draw_queue.poll();
            while (temp != null) {
                temp.CreateProgram();
                temp.Draw(mvp_matrix);
                temp.Delete();
                temp = AndroidRenderer.rects_to_draw_queue.poll();
            }
        }
        GLES20.glFlush();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        ratio = (float)width/(float)height;
        Matrix.frustumM(projection_matrix, 0, ratio, -ratio, -1, 1, 3, 7);

    }

    public static int LoadShader(int type, String source) {
        int shader = GLES20.glCreateShader(type);
        GLES20.glShaderSource(shader, source);
        GLES20.glCompileShader(shader);
        return shader;
    }

    public void DrawText(String str, int x, int y, int color) {
        /* if (this.screen.getCanvas() != null) {
           Paint paint = new Paint();
           paint.setColor(color);
           this.screen.getCanvas().drawText(str, x, y, paint);
           } */
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
