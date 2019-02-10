package com.urquieta.something.platform.android;

// import android.graphics.Paint;
// import android.graphics.RectF;
// import android.graphics.Canvas;
// import android.graphics.Bitmap;

import android.opengl.GLSurfaceView;
import android.opengl.GLES20;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.urquieta.something.platform.Screen;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import java.util.Stack;

public class AndroidRenderer implements GLSurfaceView.Renderer {
    public Screen screen;
    public class Rect {
        private final String vertex_shader = "attribute vec4 vertex_position; void main() { gl_Position  = vertex_position; }";
        private final String fragment_shader = "uniform vec4 fragment_color;  void main() { gl_FragColor = fragment_color; }";

        private final int program;
        ByteBuffer byte_buffer_positions;
        ByteBuffer byte_buffer_order;
        FloatBuffer vertex_buffer;

        float color[] = {.6f, 0.7f, 0.3f, 1.0f };
        float position[] = {
            -0.5f,  0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f,  0.5f, 0.0f
        };
        short draw_order[] = {
            0, 1, 2, 0, 2, 3
        };

        public Rect(int x, int y, int width, int height) {
            byte_buffer_positions = ByteBuffer.allocateDirect(position.length * 4);
            byte_buffer_positions.order(ByteOrder.nativeOrder());
            vertex_buffer = byte_buffer_positions.asFloatBuffer();
            vertex_buffer.put(position);
            vertex_buffer.position(0);

            byte_buffer_order = ByteBuffer.allocateDirect(draw_order.length * 2);
            byte_buffer_order.order(ByteOrder.nativeOrder());
            ShortBuffer draw_buffer = byte_buffer_order.asShortBuffer();
            draw_buffer.put(draw_order);
            draw_buffer.position(0);

            int vertex_shader_id   = AndroidRenderer.LoadShader(GLES20.GL_VERTEX_SHADER, vertex_shader);
            int fragment_shader_id = AndroidRenderer.LoadShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader);
            {
                int vertex_id   = AndroidRenderer.LoadShader(GLES20.GL_VERTEX_SHADER, vertex_shader);
                int fragment_id = AndroidRenderer.LoadShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader);
                int other_program = GLES20.glCreateProgram();
                System.out.println("SOMETHING PROGRAM: " + other_program + " - " + vertex_id + " - " + fragment_id);
            }
            program = GLES20.glCreateProgram();
            System.out.println("SOMETHING PROGRAM: " + program + " - " + vertex_shader_id + " - " + fragment_shader_id);
            GLES20.glAttachShader(program, vertex_shader_id);
            GLES20.glAttachShader(program, fragment_shader_id);
            GLES20.glLinkProgram(program);
            GLES20.glDeleteShader(vertex_shader_id);
            GLES20.glDeleteShader(fragment_shader_id);
        }

        public void Draw() {
            int error;
            GLES20.glUseProgram(program);
            int position_handle = GLES20.glGetAttribLocation(program, "vertex_position");
            GLES20.glEnableVertexAttribArray(position_handle);
            GLES20.glVertexAttribPointer(position_handle, 3, GLES20.GL_FLOAT, false, 3*4, vertex_buffer);
            int uniform_color_handle = GLES20.glGetUniformLocation(program, "fragment_color");
            GLES20.glUniform4fv(uniform_color_handle, 1, color, 0);
            GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6,  GLES20.GL_UNSIGNED_SHORT, byte_buffer_order);
            GLES20.glDisableVertexAttribArray(position_handle);
        }
    }

    Rect test_rect;

    public AndroidRenderer(Screen screen) {
        this.screen = screen;
    }

    public AndroidRenderer() {
        super();
        this.screen = null;
    }

    @Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        GLES20.glClearColor(0.6f, 0.5f, 1.0f, 1.0f);
        this.test_rect = new Rect(0, 0, 0, 0);
    }

    @Override
    public void onDrawFrame(GL10 unused) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        test_rect.Draw();
    }

    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
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

    public void DrawRect(int x, int y, int width, int height, int color) {
        // if (this.screen.getCanvas() != null)
        // {
        //     Paint paint = new Paint();
        //     paint.setColor(color);
        //     this.screen.getCanvas().drawRect(x, y, width, height, paint);
        // }
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
