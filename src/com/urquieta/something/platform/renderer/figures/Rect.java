package com.urquieta.something.platform.renderer.figures;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

import android.opengl.GLES20;

import com.urquieta.something.platform.android.AndroidRenderer;
import com.urquieta.something.game.util.Color;

public class Rect {
    private final String vertex_shader =
        "uniform mat4 mvp_matrix; attribute vec4 vertex_position;" +
        "void main() { gl_Position  = mvp_matrix * vertex_position; }";
    private final String fragment_shader =
        "uniform vec4 fragment_color;"+
        " void main() { gl_FragColor = fragment_color; }";

    private int program;
    ByteBuffer byte_buffer_positions;
    ByteBuffer byte_buffer_order;
    FloatBuffer vertex_buffer;

    Color color;
    float position[];
    short draw_order[] = {
        0, 1, 2, 0, 2, 3
    };

    public Rect(float x, float y, float width, float height, float z, Color color) {
        float position[] = {
            x,     y,      z,
            x,     height, z,
            width, height, z,
            width, y,      z
        };
        this.position = position;
        this.color = color;
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
    }

    public boolean HasBeenCreated() {
        return (program != 0);
    }

    public void CreateProgram() {
        int vertex_shader_id   = AndroidRenderer.LoadShader(GLES20.GL_VERTEX_SHADER, vertex_shader);
        int fragment_shader_id = AndroidRenderer.LoadShader(GLES20.GL_FRAGMENT_SHADER, fragment_shader);
        program = GLES20.glCreateProgram();
        GLES20.glAttachShader(program, vertex_shader_id);
        GLES20.glAttachShader(program, fragment_shader_id);
        GLES20.glLinkProgram(program);
        GLES20.glDeleteShader(vertex_shader_id);
        GLES20.glDeleteShader(fragment_shader_id);
    }

    public void UpdatePosition(float x, float y, float width, float height, float z) {
        float position[] = {
            x,     y,      z,
            x,     height, z,
            width, height, z,
            width, y,      z
        };
        byte_buffer_positions = ByteBuffer.allocateDirect(position.length * 4);
        byte_buffer_positions.order(ByteOrder.nativeOrder());
        vertex_buffer = byte_buffer_positions.asFloatBuffer();
        vertex_buffer.put(position);
        vertex_buffer.position(0);
        this.position = position;
    }

    public void Draw(float[] mvp_matrix) {
        int error;
        GLES20.glUseProgram(program);
        int mvp_matrix_handle    = GLES20.glGetUniformLocation(program, "mvp_matrix");
        GLES20.glUniformMatrix4fv(mvp_matrix_handle, 1, false, mvp_matrix, 0);

        int position_handle = GLES20.glGetAttribLocation(program, "vertex_position");
        GLES20.glEnableVertexAttribArray(position_handle);
        GLES20.glVertexAttribPointer(position_handle, 3, GLES20.GL_FLOAT, false, 3*4, vertex_buffer);

        int uniform_color_handle = GLES20.glGetUniformLocation(program, "fragment_color");
        GLES20.glUniform4fv(uniform_color_handle, 1, color.GetNormalizeArray(), 0);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, 6,  GLES20.GL_UNSIGNED_SHORT, byte_buffer_order);
        GLES20.glDisableVertexAttribArray(position_handle);
    }

    public void Delete() {
        GLES20.glDeleteProgram(program);
        program = 0;
    }

    public String toString() {
        return String.format("RECT { program: %d, positions { "+Arrays.toString(position)+"}}", program);
    }
}
