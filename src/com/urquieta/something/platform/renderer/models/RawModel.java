package com.urquieta.something.platform.renderer.models;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;

// TODO(Misael): Make sure all of this dependencies are platform free.
import static android.opengl.GLES20.*;

// import static org.lwjgl.opengl.GL20.*;
// import static org.lwjgl.opengl.GL13.*;

import com.urquieta.something.platform.Renderer;
import com.urquieta.something.game.util.Color;
import com.urquieta.something.utils.Buffers;


public class RawModel {
    // TODO(Misael): Do we need more shaders right now?

    // TODO(Misael): Find if the shaders work the same on OpenGL and OpenGL ES.
    private final String vertex_shader =
        "uniform mat4 mvp_matrix; attribute vec4 vertex_position;" +
        "void main() { gl_Position  = mvp_matrix * vertex_position; }";

    private final String fragment_shader =
        "uniform vec4 fragment_color;"+
        " void main() { gl_FragColor = fragment_color; }";

    private int program;
    FloatBuffer vertex_buffer;
    ShortBuffer  draw_buffer;
    ByteBuffer byte_buffer;

    Color color;
    float position[];
    short draw_order[];

    public RawModel(float positions[], short draw_order[], Color color) {
        this.draw_order = draw_order;
        this.position = positions;
        this.color = color;
        vertex_buffer = Buffers.ArrayToBuffer(position);
        draw_buffer   = Buffers.ArrayToBuffer(draw_order);
        byte_buffer   = Buffers.GenByteBuffer(draw_order);
    }

    public boolean HasBeenCreated() {
        return (program != 0);
    }

    public void CreateProgram() {
        int vertex_shader_id   = Renderer.LoadShader(GL_VERTEX_SHADER, vertex_shader);
        int fragment_shader_id = Renderer.LoadShader(GL_FRAGMENT_SHADER, fragment_shader);
        program = glCreateProgram();
        glAttachShader(program, vertex_shader_id);
        glAttachShader(program, fragment_shader_id);
        glLinkProgram(program);
        glDeleteShader(vertex_shader_id);
        glDeleteShader(fragment_shader_id);
    }

    public void UpdatePosition(float positions[]) {
        this.position = positions;
        vertex_buffer = Buffers.ArrayToBuffer(position);
    }

    public void Draw(float[] mvp_matrix) {
        // TODO(Misael): Maybe uses VAOs VBOs? I don't know how they work on android.
        glUseProgram(program);
        int mvp_matrix_handle    = glGetUniformLocation(program, "mvp_matrix");
        FloatBuffer mvp_buffer = Buffers.ArrayToBuffer(mvp_matrix);
        glUniformMatrix4fv(mvp_matrix_handle, 1, false, mvp_matrix, 0);

        int position_handle = glGetAttribLocation(program, "vertex_position");
        glEnableVertexAttribArray(position_handle);
        glVertexAttribPointer(position_handle, 3, GL_FLOAT, false, 3*4, vertex_buffer);

        int uniform_color_handle = glGetUniformLocation(program, "fragment_color");
        FloatBuffer color_buffer = Buffers.ArrayToBuffer(color.GetNormalizeArray());
        glUniform4fv(uniform_color_handle, 1, color.GetNormalizeArray(), 0);
        glDrawElements(GL_TRIANGLES, 6,  GL_UNSIGNED_SHORT, byte_buffer);
        glDisableVertexAttribArray(position_handle);
    }

    public void Delete() {
        glDeleteProgram(program);
        program = 0;
    }

    public String toString() {
        return String.format("RawModel { program: %d, positions { "+Arrays.toString(position)+"}}", program);
    }
}
